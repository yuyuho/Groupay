package com.cmpe277.groupay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class NotificationTabFragment extends Fragment {
    private static final String TAG = "NotificationTabFragment";
    private Event mEvent;
    private int mEventIdx;

    public static NotificationTabFragment newInstance (int eventNum){
        Bundle args = new Bundle();
        NotificationTabFragment fragment = new NotificationTabFragment();
        args.putInt(EventActivity.EVENT_INDEX,eventNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if(getArguments() != null){
            mEventIdx = getArguments().getInt(EventActivity.EVENT_INDEX);
        }
        else{
            mEventIdx = ((EventActivity)getActivity()).getCurrentEventIndex();
        }
        Log.d(TAG,"current event index "+ mEventIdx);
        mEvent = Data.get().getEvent(mEventIdx);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_list, container, false);


        ListView notifyListView = (ListView) v.findViewById(R.id.event_list);

        NotificationListAdapter notifyArrayAdapter=
                new NotificationListAdapter( getActivity(),
                        android.R.layout.simple_list_item_1,
                        mEvent.getNotifyList(),
                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_note));

        notifyListView.setAdapter(notifyArrayAdapter);
        notifyListView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View view, final int position, long id) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View newItemView = layoutInflater.inflate(R.layout.dialog_yes_no, null);
                TextView textView = (TextView) newItemView.findViewById(R.id.dialog_yesno_text_view);
                textView.setText(getResources().getText(R.string.would_you_delete_notify));

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                dialogBuilder.setView(newItemView);
                dialogBuilder
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        mEvent.deleteNotify(position);
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialogBuilder.create().show();
            }
        });

        Data.get().getEvent(mEventIdx).setNotifyListAdaptor(notifyArrayAdapter);

        return v;
    }

}
