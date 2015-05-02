package com.cmpe277.groupay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by cynthia on 4/26/15.
 */
public class ItemInfoDisplayFragment extends DialogFragment {
    private static final String TAG = "ItemBoughtFragment";
    private TextView mItemName;
    private TextView mItemPrice;
    private TextView mItemStore;
    private TextView mMemberText;
    private TextView mItemMember;
    private TextView mItemDescription;
    private LinearLayout mReceiptView;
    private ImageView mItemReceipt;
    private Item mItem;
    private Boolean mIsBought;
    private int mDisplayOption;

    static ItemInfoDisplayFragment newInstance(int eventIndex, int itemIndex,
                                               int displayOption,
                                               boolean isBought) {
        Bundle args = new Bundle();

        ItemInfoDisplayFragment fragment = new ItemInfoDisplayFragment();

        args.putInt(ItemInfoActivity.EVENT_INDEX, eventIndex);
        args.putInt(ItemInfoActivity.ITEM_INDEX, itemIndex);
        args.putInt(ItemInfoActivity.DISPLAY_OPTION, displayOption);
        args.putBoolean(ItemInfoActivity.ITEM_IS_BOUGHT, isBought);
        fragment.setArguments(args);

        return fragment;
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        int eventIdx = getArguments().getInt(ItemInfoActivity.EVENT_INDEX);
        int itemIdx = getArguments().getInt(ItemInfoActivity.ITEM_INDEX);
        mDisplayOption = getArguments().getInt(ItemInfoActivity.DISPLAY_OPTION);
        mIsBought = getArguments().getBoolean(ItemInfoActivity.ITEM_IS_BOUGHT);

        mItem = Data.get().getEvent(eventIdx).getItemAtIndex(itemIdx);
    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View newItemView = layoutInflater.inflate(R.layout.dialog_item_info_display, null);
        mItemName = (TextView) newItemView.findViewById(R.id.name_text);
        mItemPrice = (TextView) newItemView.findViewById(R.id.price_text);
        mItemStore = (TextView) newItemView.findViewById(R.id.store_text);
        mMemberText = (TextView) newItemView.findViewById(R.id.bought_by_text);
        mItemMember = (TextView) newItemView.findViewById(R.id.member_text);
        mItemDescription = (TextView) newItemView.findViewById(R.id.describe_text);
        mItemReceipt = (ImageView) newItemView.findViewById(R.id.receipt_image);
        mReceiptView = (LinearLayout) newItemView.findViewById(R.id.reciptLinearLayout);

        if (mIsBought) {
            mReceiptView.setVisibility(View.VISIBLE);
        }
        else{
            mReceiptView.setVisibility(View.GONE);
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setView(newItemView);

        mItemName.setText(mItem.getItemName());


        ItemInfo itemInfo;
        if ((mIsBought && mDisplayOption == -1)  || mItem.getItemInfoList().size() == 0) {
            itemInfo = mItem.getItemFinalInfo();
            mMemberText.setText("Bought by");
        }
        else if (mDisplayOption == -1 || mIsBought){ // This should not happen
            itemInfo = mItem.getItemInfoList().get(0);
            mMemberText.setText("Bought by");
        }
        else{
            itemInfo = mItem.getItemInfoList().get(mDisplayOption);
            mMemberText.setText("Suggested by");
        }
        if (mItem.getItemInfoList().size() > 0 || (mIsBought) ||( mDisplayOption == -1)) {
            mItemPrice.setText("$ " + Double.toString(itemInfo.getItemPrice()));
            mItemStore.setText(itemInfo.getStore());
            mItemMember.setText(itemInfo.getMemberName());
            mItemDescription.setText(itemInfo.getDescription());
            mItemReceipt.setImageBitmap(itemInfo.getRecipe());
        }
        dialogBuilder
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        return  dialogBuilder.create();
    }


}
