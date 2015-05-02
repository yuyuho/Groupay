package com.cmpe277.groupay;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by cynthia on 4/26/15.
 */
public class SuggestItemInfoFragment extends DialogFragment {
    private static final String TAG = "SuggestItemInfoFragment";
    private EditText mItemPrice;
    private EditText mItemStore;
    private EditText mItemDescibe;
    private int mEventIdx;
    private int mItemIdx;
    private Item mItem;

    static SuggestItemInfoFragment newInstance( int eventIdx, int itemIdx){
        Bundle args = new Bundle();
        SuggestItemInfoFragment fragment = new SuggestItemInfoFragment();

        args.putInt(ItemInfoActivity.EVENT_INDEX, eventIdx);
        args.putInt(ItemInfoActivity.ITEM_INDEX, itemIdx);
        fragment.setArguments(args);

        return fragment;
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mEventIdx = getArguments().getInt(ItemInfoActivity.EVENT_INDEX);
        mItemIdx = getArguments().getInt(ItemInfoActivity.ITEM_INDEX);
        mItem = Data.get().getEvent(mEventIdx).getItemAtIndex(mItemIdx);
    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View newItemView = layoutInflater.inflate(R.layout.dialog_suggest_iteminfo, null);


        mItemPrice = (EditText) newItemView.findViewById(R.id.suggest_price_edit_text);
        mItemStore = (EditText) newItemView.findViewById(R.id.suggest_store_edit_text);
        mItemDescibe = (EditText) newItemView.findViewById(R.id.suggest_descibe_edit_text);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setView(newItemView);
        mItemPrice.setText("0.0");
        dialogBuilder
                .setTitle(mItem.getItemName())
                .setPositiveButton("Suggest",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ItemInfo itemInfo = new ItemInfo(
                                        Double.parseDouble(mItemPrice.getText().toString()),
                                        mItemStore.getText().toString(),
                                        mItemDescibe.getText().toString(),
                                        Data.get().getMyName()
                                );
                                mItem.addItemInfo(itemInfo);
                                if (mItem.getItemStatus() != Item.itemStatusEnum.waitForVote) {
                                    mItem.setItemStatus(Item.itemStatusEnum.waitForVote);
                                    Data.get().getEvent(mEventIdx).getItemListAdaptor().notifyDataSetChanged();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .setCancelable(true);

        return  dialogBuilder.create();
    }


}
