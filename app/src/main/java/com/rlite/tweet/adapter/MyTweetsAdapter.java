package com.rlite.tweet.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rlite.tweet.R;
import com.rlite.tweet.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by le no vo on 10-02-2017.
 */

public class MyTweetsAdapter extends ArrayAdapter<Tweet> {


    private Activity activity;
    ArrayList<Tweet> arraylist;
    public List<Tweet> favList;

    public MyTweetsAdapter(Activity context, int resource, List<Tweet> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.favList = objects;
        arraylist = new ArrayList<Tweet>();
        arraylist.addAll(favList);
    }

    @Override
    public int getCount() {
        return favList.size();
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
            convertView = inflater.inflate(R.layout.lsv_item_mytweets, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tweetText.setText(favList.get(position).getText());

        return convertView;
    }


    private class ViewHolder
    {
        private TextView tweetText;
        public ViewHolder(View v)
        {
            tweetText = (TextView)v.findViewById(R.id.tv_mytweets);
        }
    }

}
