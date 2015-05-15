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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MyEventListFragment extends Fragment {
    private static final String TAG = "MyEventListFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_list, container, false);
        ListView eventListView = (ListView) v.findViewById(R.id.event_list);

        ServerTask serverTask =
                new ServerTask(getActivity(), new AsyncResponse() {
                    @Override
                    public void taskFinish(String output) {
                        Log.d(TAG, "Response from task: " + output);
                        try{
                            int second_pos = 0;
                            int first_pos = 0;
                            String eventId;
                            String date;
                            String status;
                            String open;
                            String manager;
                            String totalSpent;
                            String eventName;

                            while(second_pos < output.length()-1){
                                second_pos = output.indexOf('}', second_pos) + 1;

                                String temp = output.substring(first_pos, second_pos);
                                first_pos = second_pos;

                                Log.d(TAG, "Temp "+ temp);
                                JSONObject jsonObject = new JSONObject(temp);
                                eventId = jsonObject.getString("eventid");
                                date = jsonObject.getString("date");
                                status = jsonObject.getString("status");
                                open = jsonObject.getString("open");
                                manager = jsonObject.getString("manager");
                                totalSpent = jsonObject.getString("totalspent");
                                eventName = jsonObject.getString("name");

                                Log.d(TAG, "eventid "+ eventId + "date "+ date +
                                        "open " + open + "status" + status + "manager " + manager + "totalspent "+ totalSpent + "name " + eventName);

                            }


                        }
                        catch (JSONException e ){
                            Log.e(TAG, "Return Response cannot be phased "+ output );
                        }
                    }
                });
        serverTask.execute(serverTask.GET_MY_EVENT, String.valueOf(Data.get().getMyID()));


        EventListAdapter eventArrayAdapter=
                new EventListAdapter( getActivity(),
                        android.R.layout.simple_list_item_1,
                        Data.get().getmEventList());

        eventListView.setAdapter(eventArrayAdapter);
        eventListView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),EventActivity.class);
                i.putExtra(EventActivity.EVENT_INDEX, position);
                startActivity(i);

            }
        });
        eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View newItemView = layoutInflater.inflate(R.layout.dialog_yes_no, null);
                TextView textView = (TextView) newItemView.findViewById(R.id.dialog_yesno_text_view);
                textView.setText(getResources().getText(R.string.mark_event_done));

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                dialogBuilder.setView(newItemView);
                dialogBuilder
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Data.get().getEvent(position).setEventStatus(Event.EVENT_STATUS.close);
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Data.get().getEvent(position).setEventStatus(Event.EVENT_STATUS.on_going);
                            }
                        });
                dialogBuilder.create().show();
                return true;
            }
        });

        Data.get().setmEventListAdaptor(eventArrayAdapter);

        return v;
    }

}
