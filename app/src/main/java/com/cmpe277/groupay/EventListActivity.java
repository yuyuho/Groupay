package com.cmpe277.groupay;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;


public class EventListActivity extends ActionBarActivity
        implements TabHost.OnTabChangeListener{
    private static final String TAG = "EventListActivity";
    private static final String CREATE_EVENT_DIALOG_TAG = "Create event";
    private static final String DELETE_EVENT_DIALOG_TAG = "Delete event";
    private Toolbar mToolbar;
    private TabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mToolbar != null) {
            mToolbar.setTitle(R.string.event_list);
            mToolbar.setLogo(R.mipmap.ic_logo);
            setSupportActionBar(mToolbar);
        }

        mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup();

        TabHost.TabSpec tabSpec =
                mTabHost.newTabSpec(getResources().getString(R.string.my_event));
        tabSpec.setIndicator(getResources().getString(R.string.my_event));
        tabSpec.setContent(R.id.my_event_tab);
        mTabHost.addTab(tabSpec);

        tabSpec = mTabHost.newTabSpec(getResources().getString(R.string.open_event));
        tabSpec.setIndicator(getResources().getString(R.string.open_event));
        tabSpec.setContent(R.id.open_event_tab);
        mTabHost.addTab(tabSpec);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_event_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        switch(item.getItemId()) {
            case R.id.create_event_item:
                CreateEventFragment fragment = new CreateEventFragment();
                fragment.show(fm, CREATE_EVENT_DIALOG_TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabChanged(String tabId) {

        if (mTabHost.getCurrentTab() == 0) {
            MyEventListFragment myEventListFragment =
                    new MyEventListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.tabcontent, myEventListFragment)
                    .commit();
        } else if (mTabHost.getCurrentTab() == 1) {
            OpenEventListFragment openEventListFragment
                    = new OpenEventListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.tabcontent, openEventListFragment)
                    .commit();
        }
    }

}
