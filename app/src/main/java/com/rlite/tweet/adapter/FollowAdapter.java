package com.rlite.tweet.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rlite.tweet.Follow;
import com.rlite.tweet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by le no vo on 04-02-2017.
 */

public class FollowAdapter extends ArrayAdapter<Follow> {

    private Activity activity;
    ArrayList<Follow> arraylist;
    public List<Follow> favList;

    public FollowAdapter(Activity context, int resource, List<Follow> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.favList = objects;
        arraylist = new ArrayList<Follow>();
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
            convertView = inflater.inflate(R.layout.lsv_item_follower, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.followerName.setText(favList.get(position).getFollowerName());

        return convertView;
    }


    private class ViewHolder
    {
        private TextView followerName;
        public ViewHolder(View v)
        {
            followerName = (TextView)v.findViewById(R.id.tv_foll_name);
        }
    }

}
