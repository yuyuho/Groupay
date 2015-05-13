package com.cmpe277.groupay;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;

import java.lang.reflect.Member;
import java.util.UUID;


public class EventActivity extends ActionBarActivity implements TabHost.OnTabChangeListener {
    private static final String TAG = "EventActivity";
    public static final String EVENT_INDEX ="event Index";
    public static final String EVENT_UUID ="event UUID";
    private Toolbar mToolbar;
    private TabHost mTabHost;

    private int mEventIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mEventIndex = (int) getIntent().getSerializableExtra(EVENT_INDEX);
        Log.d(TAG,"Event Index" + mEventIndex);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(Data.get().getEvent(mEventIndex).getmEventName());
            mToolbar.setLogo(R.mipmap.ic_logo);
            setSupportActionBar(mToolbar);
        }
        mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup();

        TabHost.TabSpec tabSpec =
                mTabHost.newTabSpec(getResources().getString(R.string.item_tab_name));
        tabSpec.setIndicator(getResources().getString(R.string.item_tab_name));
        tabSpec.setContent(R.id.item_tab);
        mTabHost.addTab(tabSpec);

        tabSpec = mTabHost.newTabSpec(getResources().getString(R.string.member_tab_name));
        tabSpec.setIndicator(getResources().getString(R.string.member_tab_name));
        tabSpec.setContent(R.id.member_tab);
        mTabHost.addTab(tabSpec);


        tabSpec = mTabHost.newTabSpec(getResources().getString(R.string.notification_tab_name));
        tabSpec.setIndicator(getResources().getString(R.string.notification_tab_name));
        tabSpec.setContent(R.id.notification_tab);
        mTabHost.addTab(tabSpec);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.propose_new_item:
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View newItemView = layoutInflater.inflate(R.layout.dialog_propose_item, null);
                final EditText newItemET =
                        (EditText) newItemView.findViewById(R.id.new_item_edit_text);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

                dialogBuilder.setView(newItemView);
                dialogBuilder
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Data.get().getEvent(mEventIndex).
                                                addItem(new Item(newItemET.getText().toString()));
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                dialogBuilder.create().show();

                return true;
            case R.id.action_settings:
                return true;

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabChanged(String tabId) {

        if (mTabHost.getCurrentTab() == 0) {
            ItemTabFragment itemTabFragment
                    = Data.get().getEvent(mEventIndex).getmItemTabFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.tabcontent, itemTabFragment)
                    .commit();
        } else if (mTabHost.getCurrentTab() == 1) {
            MemberTabFragment memberTabFragment
                    = Data.get().getEvent(mEventIndex).getmMemberTabFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.tabcontent, memberTabFragment)
                    .commit();
        } else if (mTabHost.getCurrentTab() == 2) {
            /*
            NotificationTabFragment notificationTabFragment
                    = Data.get().getEvent(mEventIndex).getmNotificationTabFragment();;
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.tabcontent, notificationTabFragment)
                    .commit();
                    */
            NotificationTabFragment notificationTabFragment
                    = new NotificationTabFragment().newInstance(Data.get().getEvent(mEventIndex).getEventID());
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.tabcontent, notificationTabFragment)
                    .commit();
        }
    }
    public int getCurrentEventIndex(){
        return mEventIndex;
    }
    public UUID getCurrentEventUUID(){
        return Data.get().getEvent(mEventIndex).getEventID();
    }
}
