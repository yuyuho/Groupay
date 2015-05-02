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
public class DeleteEventFragment extends DialogFragment {
    private static final String TAG = "DeleteEventFragment";
    private ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();

    public Dialog onCreateDialog (Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_delete, null);
        List<String> eventList = new ArrayList<String>();
        final CharSequence[] eventName;

        ArrayList<Event> rooms = Data.get().getmEventList();
        for (int i = 0; i < rooms.size(); i++) {
            eventList.add(rooms.get(i).getmEventName());
        }
        eventName = eventList.toArray(new CharSequence[eventList.size()]);

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());

        builder.setView(v)
                .setTitle(R.string.select_delete_event)
                .setMultiChoiceItems(eventName, null, new DialogSelectionClickHandler())
                .setPositiveButton("OK", new positionClickListener() )
                .setNeutralButton(android.R.string.cancel, null);

        return builder.create();
    }

    public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener{
        @Override
        public void onClick(DialogInterface dialog, int clicked, boolean isChecked) {

            if (isChecked) {
                mSelectedItems.add(clicked);
            } else if (mSelectedItems.contains(clicked)) {
                mSelectedItems.remove(Integer.valueOf(clicked));
            }
        }
    }
    public class positionClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int id) {

            ArrayList<Event> eventDeleteList = new ArrayList<Event>();

            for (Integer i : mSelectedItems) {
                eventDeleteList.add(Data.get().getmEventList().get(i));
            }

            for (Event e: eventDeleteList) {
                Toast.makeText(getActivity(), "Delete " + e.getmEventName(), Toast.LENGTH_SHORT)
                        .show();
                Log.d(TAG, "Delete " + e.getmEventName());
                Data.get().removeEvent(e);
            }
        }
    }
}
