package com.cmpe277.groupay;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuyItemFragment extends DialogFragment {
    private static final String TAG = "BuyItemFragment";
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
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


        ////////////////////////////////// Take Picture for Reciept button //////////////////////////////////////////////
        receiptImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        // handle error.......................................
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    };
                }
            }


        });

        return dialogBuilder.create();
    }




    ///////////////////////////////////// Creates a file to store the image   /////////////////////////////////////////////
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

}





