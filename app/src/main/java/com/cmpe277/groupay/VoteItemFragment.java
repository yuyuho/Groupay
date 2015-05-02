package com.cmpe277.groupay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cynthia on 4/27/15.
 */
public class VoteItemFragment extends DialogFragment {
    private static final String TAG = "VoteItemFragment";
    private int mSelectedItem;
    private int mEventIdx;
    private int mItemIdx;
    private Item mItem;

    static VoteItemFragment newInstance( int eventIdx, int itemIdx){
        Bundle args = new Bundle();
        VoteItemFragment fragment = new VoteItemFragment();

        args.putInt(ItemInfoActivity.EVENT_INDEX, eventIdx);
        args.putInt(ItemInfoActivity.ITEM_INDEX, itemIdx);
        fragment.setArguments(args);

        return fragment;
    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_delete, null);
        List<String> itemList = new ArrayList<String>();
        final CharSequence[] itemName;
        mSelectedItem = 0;

        mEventIdx = getArguments().getInt(ItemInfoActivity.EVENT_INDEX);
        mItemIdx = getArguments().getInt(ItemInfoActivity.ITEM_INDEX);
        mItem = Data.get().getEvent(mEventIdx).getItemAtIndex(mItemIdx);
        ArrayList<ItemInfo> itemInfoList = mItem.getItemInfoList();

        for (int i = 0; i < itemInfoList.size(); i++) {
            itemList.add("Option "+ (i+1)+ " ( $"+itemInfoList.get(i).getItemPrice()+" )");
        }
        itemName = itemList.toArray(new CharSequence[itemList.size()]);

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());

        builder.setView(v)
                .setTitle(R.string.vote_for_option)
                .setSingleChoiceItems(itemName,0, new DialogSelectionClickHandler())
                .setPositiveButton("OK", new positionClickListener())
                .setNeutralButton(android.R.string.cancel, null);

        return builder.create();
    }

    public class DialogSelectionClickHandler implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int clicked) {
            mSelectedItem = clicked;
        }
    }
    public class positionClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int id) {
            ItemInfo itemInfo = mItem.getItemInfoList().get(mSelectedItem);
            itemInfo.setNumOfVote(itemInfo.getNumOfVote() + 1);
            mItem.addVote();
        }
    }
}
