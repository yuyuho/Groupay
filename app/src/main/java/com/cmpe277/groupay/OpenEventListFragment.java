package com.cmpe277.groupay;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class OpenEventListFragment extends Fragment {
    private static final String TAG = "OpenEventListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_list, container, false);
        ListView eventListView = (ListView) v.findViewById(R.id.event_list);

        EventListAdapter eventArrayAdapter=
                new EventListAdapter( getActivity(),
                        android.R.layout.simple_list_item_1,
                        Data.get().getmEventList()); //TODO the list should be obtain from server

        eventListView.setAdapter(eventArrayAdapter);
        eventListView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View newItemView = layoutInflater.inflate(R.layout.dialog_yes_no, null);
                TextView textView = (TextView) newItemView.findViewById(R.id.dialog_yesno_text_view);
                textView.setText(getResources().getText(R.string.would_like_to_join));

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                dialogBuilder.setView(newItemView);
                dialogBuilder
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //TODO send join request to the event
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                dialogBuilder.create().show();
            }
        });

        Data.get().setmEventListAdaptor(eventArrayAdapter);

        return v;
    }

}
