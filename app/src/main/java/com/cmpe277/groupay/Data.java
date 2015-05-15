package com.cmpe277.groupay;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cynthia on 4/27/15.
 */
public class Data {
    private static final String TAG = "Data";
    private static Data sData;

    private Member mMe;
    private ArrayList<Event> mEventList;
    private EventListAdapter mEventListAdaptor;
    private String mEventListModifiedTimeStamp;

    private Data(){
        sData = this;
        mEventList = new ArrayList<Event>();
        if(mMe == null){
            mMe = new Member();
        }

    }
    public static Data get (){
        if (sData == null){
            sData = new Data();
        }
        return sData;
    }
    public Member getMe(){
        return mMe;
    }
    public String getMyName() {
        return mMe.getMyName();
    }
    public int getMyID() {
        return mMe.getMemberId();
    }
    public void setMyName(String myName) {

        if(mMe == null){
            mMe = new Member();
        }
        else{
            mMe.setMyName(myName);
        }
    }

    public void addEvent(Event newEvent){
        mEventList.add(newEvent);
        mEventListModifiedTimeStamp = getCurrentTimeStamp();
        if(this.mEventListAdaptor != null ) {
            this.mEventListAdaptor.notifyDataSetChanged();
        }
    }
    public void removeEvent(Event e){
        mEventList.remove(e);
        mEventListModifiedTimeStamp = getCurrentTimeStamp();
        if(this.mEventListAdaptor != null ) {
            this.mEventListAdaptor.notifyDataSetChanged();
        }
    }
    public Event getEvent(int index){
        if (mEventList == null) return null;
        return mEventList.get(index);
    }
    public ArrayList<Event> getmEventList() {
        return mEventList;
    }
    public void setmEventListAdaptor(EventListAdapter mEventListAdaptor) {
        this.mEventListAdaptor = mEventListAdaptor;
    }
    public EventListAdapter getEventListAdaptor() {
        return this.mEventListAdaptor;
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date());

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
