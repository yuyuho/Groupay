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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cynthia on 4/26/15.
 */
public class ItemInfoListAdapter extends ArrayAdapter<ItemInfo> {
    private static final String TAG = "ItemInfoListAdapter";
    private Context mContext;
    private int mLayoutResId;
    private ArrayList<ItemInfo> mListItemInfos;
    private Item mItem;

    public ItemInfoListAdapter(Context context,
                               int layoutResId,
                               ArrayList<ItemInfo> listItems,
                               Item item){

        super(context, layoutResId, listItems);
        mContext = context;
        mLayoutResId = layoutResId;
        mListItemInfos = listItems;
        mItem = item;
    }

    @Override
    public int getCount() {
        return mListItemInfos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View convertView, ViewGroup parent){
        if (convertView == null){
            LayoutInflater mInflator = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflator.inflate(R.layout.item_info_list_item, null);
        }

        TextView itemName = (TextView) convertView.findViewById(R.id.item_store);
        TextView itemPrice = (TextView) convertView.findViewById(R.id.item_price);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
        TextView progressText = (TextView) convertView.findViewById(R.id.progress_bar_text);

        progressBar.setMax(mItem.getTotalNumOfVote());
        progressBar.setProgress(mListItemInfos.get(position).getNumOfVote());

        double progress = (double)mListItemInfos.get(position).getNumOfVote() /
                (double)mItem.getTotalNumOfVote();
        Log.d(TAG, "Progres "+ progress);
        progressText.setText(String.format("%.2f%%",progress*100));
        itemName.setText("Option "+ (position+1));
        itemPrice.setText("$ " + Double.toString(mListItemInfos.get(position).getItemPrice()));

        Log.d(TAG, "option" + (position +1));

        return convertView;
    }


}