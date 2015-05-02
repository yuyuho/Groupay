package com.cmpe277.groupay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cynthia on 4/26/15.
 */
public class EventListAdapter extends ArrayAdapter<Event> {
    private static final String TAG = "EventListAdapter";
    private Context mContext;
    private int mLayoutResId;
    private ArrayList<Event> mListItems;

    public EventListAdapter(Context context,
                            int layoutResId,
                            ArrayList<Event> listItems){

        super(context, layoutResId, listItems);
        mContext = context;
        mLayoutResId = layoutResId;
        mListItems = listItems;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View convertView, ViewGroup parent){
        if (convertView == null){
            LayoutInflater mInflator = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflator.inflate(R.layout.event_list_item, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.event_icon);
        TextView item = (TextView) convertView.findViewById(R.id.event_text);

        icon.setImageBitmap(mListItems.get(position).getmEventIcon());
        item.setText(mListItems.get(position).getmEventName());

        return convertView;
    }


}