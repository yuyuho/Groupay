package com.cmpe277.groupay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class ItemInfoListFragment extends Fragment {
    private static final String TAG = "ItemInfoListFragment";
    private static final String SHOW_ITEM_INFO_DIALOG_TAG = "Show item info";
    private int mEventIdx;
    private int mItemIdx;
    private Item mItem;

    static ItemInfoListFragment newInstance( int eventIdx, int itemIdx){
        Bundle args = new Bundle();
        ItemInfoListFragment fragment = new ItemInfoListFragment();

        args.putInt(ItemInfoActivity.EVENT_INDEX, eventIdx);
        args.putInt(ItemInfoActivity.ITEM_INDEX, itemIdx);
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_list, container, false);
        ListView itemInfoListView = (ListView) v.findViewById(R.id.event_list);

        ItemInfoListAdapter itemInfoArrayAdapter=
                new ItemInfoListAdapter( getActivity(),
                        android.R.layout.simple_list_item_1,
                        mItem.getItemInfoList(),
                        mItem);

        itemInfoListView.setAdapter(itemInfoArrayAdapter);
        itemInfoListView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                FragmentManager fm = getFragmentManager();
                ItemInfoDisplayFragment fragment
                        = ItemInfoDisplayFragment.newInstance(mEventIdx, mItemIdx, position, false);
                fragment.show(fm, SHOW_ITEM_INFO_DIALOG_TAG);
            }
        });

        mItem.setItemInfoListAdaptor(itemInfoArrayAdapter);

        return v;
    }
}
