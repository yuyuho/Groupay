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
import java.util.UUID;

/**
 * Created by cynthia on 4/27/15.
 */
public class VoteItemFragment extends DialogFragment {
    private static final String TAG = "VoteItemFragment";
    private int mSelectedItem;
    private int mEventIdx;
    private int mItemIdx;
    private Item mItem;
    private int mOption;

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

        mOption = Data.get().getMe()
                .getVote(Data.get().getEvent(mEventIdx).getEventID(),
                        Data.get().getEvent(mEventIdx).getItemAtIndex(mItemIdx).getItemID());

        mOption = (mOption > itemInfoList.size()) ? -1: mOption;
        builder.setView(v)
                .setTitle(R.string.vote_for_option)
                .setSingleChoiceItems(itemName,(mOption == -1 )? 0: mOption, new DialogSelectionClickHandler())
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
            UUID eventId = Data.get().getEvent(mEventIdx).getEventID();
            UUID itemId = Data.get().getEvent(mEventIdx).getItemAtIndex(mItemIdx).getItemID();
            if (mOption == -1) {
                itemInfo.setNumOfVote(itemInfo.getNumOfVote() + 1);
                mItem.addVote();
                Data.get().getMe().addVote(eventId, itemId, mSelectedItem);
            }
            else {
                int vote = Data.get().getMe().getVote(eventId, itemId);
                if (vote != mSelectedItem ) {
                    Log.d(TAG, " My VOTE WAS " + vote);
                    if (vote != -1) {
                        itemInfo = mItem.getItemInfoList().get(vote);
                        itemInfo.setNumOfVote(itemInfo.getNumOfVote() - 1);
                    }
                    itemInfo = mItem.getItemInfoList().get(mSelectedItem);
                    itemInfo.setNumOfVote(itemInfo.getNumOfVote() + 1);

                }
                Data.get().getMe().addVote(eventId, itemId, mSelectedItem);
            }
            Data.get().getEvent(mEventIdx).getItemAtIndex(mItemIdx).getItemInfoListAdaptor().notifyDataSetChanged();
        }
    }
}
