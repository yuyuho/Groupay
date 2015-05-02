package com.cmpe277.groupay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Member;
import java.util.ArrayList;

/**
 * Created by cynthia on 4/26/15.
 */
public class NotificationListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "GpListAdapter";
    private Context mContext;
    private int mLayoutResId;
    private ArrayList<String> mListItems;
    private Bitmap mIcon;

    public NotificationListAdapter(Context context,
                                   int layoutResId,
                                   ArrayList<String> listItems,
                                   Bitmap icon){

        super(context, layoutResId, listItems);
        mContext = context;
        mLayoutResId = layoutResId;
        mListItems = listItems;
        mIcon = icon;
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

        icon.setImageBitmap(mIcon);
        item.setText(mListItems.get(position));

        return convertView;
    }


}