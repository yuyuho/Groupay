package com.cmpe277.groupay;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class MemberTabFragment extends Fragment {
    private static final String TAG = "EventListFragment";
    private Event mEvent;
    private int mEventIdx;

    public static MemberTabFragment newInstance ( int eventNum ){
        Bundle args = new Bundle();
        MemberTabFragment fragment = new MemberTabFragment();
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
        ListView memberListView = (ListView) v.findViewById(R.id.event_list);

        Log.d(TAG, mEvent.getmEventName());

        MemberListAdapter memberArrayAdapter=
                new MemberListAdapter( getActivity(),
                        android.R.layout.simple_list_item_1,
                        mEvent.getMemberList(),
                        BitmapFactory.decodeResource(getResources(),R.drawable.ic_member));

        memberListView.setAdapter(memberArrayAdapter);

        Data.get().getEvent(mEventIdx).setMemberListAdaptor(memberArrayAdapter);


        return v;
    }
}
