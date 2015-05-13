package com.cmpe277.groupay;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;

public class BuyItemFragment extends DialogFragment {
    private static final String TAG = "BuyItemFragment";
    private Item mItem;
    private int mEventIdx;
    private int mItemIdx;

    static BuyItemFragment newInstance(int eventIndex, int itemIndex) {
        Bundle args = new Bundle();

        BuyItemFragment fragment = new BuyItemFragment();

        args.putInt(ItemInfoActivity.EVENT_INDEX, eventIndex);
        args.putInt(ItemInfoActivity.ITEM_INDEX, itemIndex);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mEventIdx = getArguments().getInt(ItemInfoActivity.EVENT_INDEX);
        mItemIdx = getArguments().getInt(ItemInfoActivity.ITEM_INDEX);

        mItem = Data.get().getEvent(mEventIdx).getItemAtIndex(mItemIdx);

    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View newItemView = layoutInflater.inflate(R.layout.dialog_buy_item, null);
        final EditText priceEditText = (EditText) newItemView.findViewById(R.id.price_edit_text);
        final EditText storeEditText = (EditText) newItemView.findViewById(R.id.store_edit_text);
        final EditText descEditText = (EditText) newItemView.findViewById(R.id.describe_edit_text);
        final ImageButton receiptImageButton = (ImageButton) newItemView.findViewById(R.id.item_receipt_image);

        final ItemInfo itemInfo = mItem.getItemFinalInfo();
        final Bitmap recipeImage;
        if (itemInfo.getRecipe()!= null){
            recipeImage = itemInfo.getRecipe();
        }
        else {
            recipeImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_receipt);
        }
        receiptImageButton.setImageBitmap(recipeImage);
        priceEditText.setText(Double.toString(itemInfo.getItemPrice()));
        storeEditText.setText(itemInfo.getStore());
        descEditText.setText(itemInfo.getDescription());

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(newItemView);

        dialogBuilder
                .setTitle(mItem.getItemName())
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                                double price = Double.parseDouble(priceEditText.getText().toString());
                                String store = storeEditText.getText().toString();

                                itemInfo.setItemPrice(price);
                                itemInfo.setStore(store);
                                itemInfo.setRecipe(recipeImage);

                                mItem.setItemFinalInfo(itemInfo);
                                mItem.setItemStatus(Item.itemStatusEnum.bought);
                                Data.get().getEvent(mEventIdx).getItemListAdaptor().notifyDataSetChanged();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        receiptImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return dialogBuilder.create();
    }
}
