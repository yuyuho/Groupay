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

import java.util.ArrayList;

/**
 * Created by cynthia on 4/26/15.
 */
public class MemberListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "GpListAdapter";
    private Context mContext;
    private int mLayoutResId;
    private ArrayList<String> mListItems;
    private Bitmap mIcon;

    public MemberListAdapter(Context context,
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflator = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflator.inflate(R.layout.item_list_item, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.item_icon);
        TextView member = (TextView) convertView.findViewById(R.id.item_text);
        TextView status = (TextView) convertView.findViewById(R.id.item_status);

        icon.setImageBitmap(mIcon);
        member.setText(mListItems.get(position));
        if (position != 0){
            status.setText("Member");
        }
        else {
            status.setText("Manager");
        }

        return convertView;
    }


}