package com.rlite.tweet;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.rlite.tweet.adapter.ImageAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.graphics.BitmapFactory.decodeFile;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment implements View.OnClickListener {


    private GridView gridView;
    private Button btnAddPhots,btnUploadImage;
    private Button btnSaveImages;
    private ArrayList<String> imagesPathList;
    private ArrayList<Bitmap> imagesPathList1;
    private Bitmap yourbitmap;
    private Bitmap resized;
    private final int PICK_IMAGE_MULTIPLE =1;

    private ProgressDialog dialog;
    MultipartEntity entity;
    int count = 0;
    public ArrayList<String> map = new ArrayList<String>();


    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;


    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        gridView = (GridView) getActivity().findViewById(R.id.listView_set);
        btnAddPhots = (Button)getActivity().findViewById(R.id.btnAddPhots);
        btnUploadImage = (Button)getActivity().findViewById(R.id.btnUploadImages);
        btnAddPhots.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddPhots:

                Intent intent = new Intent(getActivity(),CustomPhotoGalleryActivity.class);

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Show Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Need Write Permission");
                        builder.setMessage("This app needs call permission.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else if (permissionStatus.getBoolean(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                        // Redirect to Settings after showing Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Need Call Permission");
                        builder.setMessage("This app needs storage permission.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                sentToSettings = true;
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                Toast.makeText(getActivity(), "Go to Permissions to Grant WRITE STORAGE", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else {
                        //just request the permission
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }

                    SharedPreferences.Editor editor = permissionStatus.edit();
                    editor.putBoolean(Manifest.permission.CALL_PHONE, true);
                    editor.commit();

                }
                else {
                    startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
                }
                break;
            case R.id.btnUploadImages:
                if(imagesPathList !=null){
                    new ImageUploadTask().execute(count + "", randomAlphaNumeric(10) + ".jpg");

                }else{
                    Toast.makeText(getActivity()," no images are selected", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == PICK_IMAGE_MULTIPLE){
                imagesPathList = new ArrayList<String>();
                imagesPathList1 = new ArrayList<Bitmap>();

                String[] imagesPath = data.getStringExtra("data").split("\\|");
                try{
                    //lnrImages.removeAllViews();
                }catch (Throwable e){
                    e.printStackTrace();
                }
                for (int i=0;i<imagesPath.length;i++){
                    imagesPathList.add(imagesPath[i]);
                    map.add(imagesPath[i]);
                    yourbitmap = decodeFile(imagesPath[i]);
                    imagesPathList1.add(yourbitmap);
                    //ImageView imageView = new ImageView(getActivity());
                    //imageView.setImageBitmap(yourbitmap);
                    //imageView.setAdjustViewBounds(true);
                    //lnrImages.addView(imageView);
                }
                ImageAdapter imageAdapter = new ImageAdapter(getActivity(),R.layout.lsv_item_image,imagesPathList1);
                gridView.setAdapter(imageAdapter);
            }
        }

    }


    class ImageUploadTask extends AsyncTask<String, Void, String> {

        String sResponse = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(), "Uploading",
                    "Please wait...", true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String url = "http://freerc.asia/findmedecor/fileupload.php";
                int i = Integer.parseInt(params[0]);
                Bitmap bitmap = decodeFile(map.get(i));
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);
                entity = new MultipartEntity();

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] data = bos.toByteArray();

                entity.addPart("image", new ByteArrayBody(data,
                        "image/*", params[1]));

                httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost,
                        localContext);
                //sResponse = EntityUtils.getContentCharSet(response.getEntity());
                sResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Log.e(e.getClass().getName(), e.getMessage(), e);

            }
            return sResponse;
        }

        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();

                if (sResponse != null) {
                    count++;
                    if (count < map.size()) {
                        new ImageUploadTask().execute(count + "", randomAlphaNumeric(10)
                                + ".jpg");
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }

        }
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

}
