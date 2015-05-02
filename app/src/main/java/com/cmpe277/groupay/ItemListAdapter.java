package com.cmpe277.groupay;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
public class ItemListAdapter extends ArrayAdapter<Item> {
    private static final String TAG = "ItemListAdapter";
    private Context mContext;
    private int mLayoutResId;
    private ArrayList<Item> mListItems;
    private Bitmap mIcon;

    public ItemListAdapter(Context context,
                           int layoutResId,
                           ArrayList<Item> listItems,
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
            convertView = mInflator.inflate(R.layout.item_list_item, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.item_icon);
        TextView item = (TextView) convertView.findViewById(R.id.item_text);
        TextView status = (TextView) convertView.findViewById(R.id.item_status);

        Item i = mListItems.get(position);
        icon.setImageBitmap(mIcon);
        item.setText(i.getItemName());
        switch (i.getItemStatus()){
            case newItem:
                status.setText("New");
                break;
            case waitForVote:
                status.setText("Wait for votes");
                break;
            case waitToBeBuy:
                status.setText("Wait to be bought");
                break;
            case bought:
                status.setText("Bought");
                break;
            case requestProof:
                status.setText("Proof required");
                break;
            case approved:
                status.setText("Expense Approved");
                break;
            default:
                status.setText("");
                break;


        }

        return convertView;
    }


}