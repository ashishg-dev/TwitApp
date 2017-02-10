package com.rlite.tweet.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.rlite.tweet.R;

import java.util.List;

/**
 * Created by le no vo on 07-02-2017.
 */

public class ImageAdapter extends ArrayAdapter<Bitmap> {

    private Activity activity;
    private List<Bitmap> imageList;


    public ImageAdapter(Activity context, int resource, List<Bitmap> objects) {
        super(context,resource,objects);
        this.activity = context;
        this.imageList = objects;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Bitmap getItem(int position) {
        return imageList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.lsv_item_image, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.imageView.setImageURI(getItem(position));

        holder.imageView.setImageBitmap(getItem(position));

        return convertView;
    }


    private class ViewHolder
    {
        private ImageView imageView;

        public ViewHolder(View v)
        {
            imageView = (ImageView) v.findViewById(R.id.display_image);
        }

    }

}
