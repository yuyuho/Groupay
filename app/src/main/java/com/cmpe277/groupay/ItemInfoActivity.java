package com.cmpe277.groupay;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import java.util.ArrayList;


public class ItemInfoActivity extends ActionBarActivity {
    private static final String TAG = "ItemInfoActivity";
    public static final String CAST_YOUR_VOTE_DIALOG_TAG = "Cast your vote";
    public static final String SUGGEST_DIALOG_TAG = "add suggestion";
    public static final String ITEM_INDEX = "item Index";
    public static final String EVENT_INDEX = EventActivity.EVENT_INDEX;
    public static final String DISPLAY_OPTION = "Display option";
    public static final String ITEM_IS_BOUGHT = "item is bought";
    private Toolbar mToolbar;
    private int mEventIndex;
    private int mItemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        mEventIndex = (int) getIntent().getIntExtra(EVENT_INDEX, -1);
        mItemIndex = (int) getIntent().getIntExtra(ITEM_INDEX, -1);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(Data.get().getEvent(mEventIndex).getmEventName());
            mToolbar.setLogo(R.mipmap.ic_logo);
            setSupportActionBar(mToolbar);
        }
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.item_info_container);
        if (fragment == null){

            Item mItem = Data.get().getEvent(mEventIndex).getItemAtIndex(mItemIndex);
            ArrayList<ItemInfo> itemInfoList = mItem.getItemInfoList();

            fragment = ItemInfoListFragment.newInstance(mEventIndex, mItemIndex);
            fm.beginTransaction()
                    .add(R.id.item_info_container,fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        switch (item.getItemId()){

            case R.id.action_cast_vote:

                Item mItem = Data.get().getEvent(mEventIndex).getItemAtIndex(mItemIndex);
                ArrayList<ItemInfo> itemInfoList = mItem.getItemInfoList();

                VoteItemFragment fragment = VoteItemFragment.newInstance(mEventIndex,mItemIndex);
                fragment.show(fm,CAST_YOUR_VOTE_DIALOG_TAG);
                break;
            case R.id.action_add_suggestion:
                SuggestItemInfoFragment suggestItemInfoFragment
                        = SuggestItemInfoFragment.newInstance(mEventIndex,mItemIndex);
                suggestItemInfoFragment.show(fm, SUGGEST_DIALOG_TAG);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public Item getItem(){
        return Data.get().getEvent(mEventIndex).getItemAtIndex(mItemIndex);
    }
}
