package com.cmpe277.groupay;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by cynthia on 4/26/15.
 */
public class Event {
    public enum EVENT_TYPE {open_event, close_event};
    public enum EVENT_STATUS {on_going, close};

    private UUID mEventID;
    private Bitmap mEventIcon;
    private String mEventName;
    private int mEventDay;
    private int mEventMonth;
    private int mEventYear;
    private EVENT_TYPE mEventType;
    private double mEventExpense; //TODO need to record the calculate the total
    private EVENT_STATUS mEventStatus;

    private ArrayList<String> mMemberList;
    private MemberListAdapter mMemberListAdaptor;

    private ArrayList<Item> mItemList;
    private ItemListAdapter mItemListAdaptor;
    private String mEventModifiedTimeStamp;
    private String mItemModifiedTimeStamp;
    private String mMemberModifiedTimeStamp;

    private ItemTabFragment mItemTabFragment;
    private MemberTabFragment mMemberTabFragment;

    public Event(Bitmap icon,
                 String name,
                 int year,
                 int month,
                 int day,
                 EVENT_TYPE eventType,
                 int eventNum){

        mEventName = name;
        mEventID = UUID.randomUUID();
        mEventIcon = icon;
        mEventYear = year;
        mEventMonth = month;
        mEventDay = day;
        mEventType = eventType;
        mEventExpense = 0;
        mEventStatus = EVENT_STATUS.on_going;
        mEventModifiedTimeStamp = getCurrentTimeStamp();
        mItemList = new ArrayList<Item>();
        mMemberList = new ArrayList<String>();
        mItemTabFragment = ItemTabFragment.newInstance(eventNum);
        mMemberTabFragment = MemberTabFragment.newInstance(eventNum);
    }

    public UUID getEventID() {
        return mEventID;
    }

    public void setEventID(UUID mEventID) {
        this.mEventID = mEventID;
    }
    public Bitmap getmEventIcon() {
        return mEventIcon;
    }

    public void setmEventIcon(Bitmap mEventIcon) {
        this.mEventIcon = mEventIcon;
        mEventModifiedTimeStamp = getCurrentTimeStamp();
    }

    public String getmEventName() {
        return mEventName;
    }

    public void setmEventName(String mEventName) {
        this.mEventName = mEventName;
        mEventModifiedTimeStamp = getCurrentTimeStamp();
    }

    public int getmEventDay() {
        return mEventDay;
    }

    public void setmEventDay(int mEventDate) {
        this.mEventDay = mEventDate;
        mEventModifiedTimeStamp = getCurrentTimeStamp();
    }

    public int getmEventMonth() {
        return mEventMonth;
    }

    public void setmEventMonth(int mEventMonth) {
        this.mEventMonth = mEventMonth;
        mEventModifiedTimeStamp = getCurrentTimeStamp();
    }

    public int getmEventYear() {
        return mEventYear;
    }

    public void setmEventYear(int mEventYear) {
        this.mEventYear = mEventYear;
        mEventModifiedTimeStamp = getCurrentTimeStamp();
    }

    public double getEventExpense() {
        return mEventExpense;
    }

    public void setEventExpense(double mEventExpense) {
        this.mEventExpense = mEventExpense;
    }

    public void addEventExpense(double eventExpense){
        this.mEventExpense += eventExpense;
    }

    public EVENT_STATUS getEventStatus() {
        return mEventStatus;
    }

    public void setEventStatus(EVENT_STATUS mEventStatus) {
        this.mEventStatus = mEventStatus;
    }


    //Functions to take of itemList
    public ArrayList<Item> getItemList(){
        return mItemList;
    }
    public Item getItemAtIndex(int i){
        return mItemList.get(i);
    }
    public void addItem(Item item ){
        mItemList.add(item);
        mItemModifiedTimeStamp = getCurrentTimeStamp();
        if (mItemListAdaptor != null) {
            mItemListAdaptor.notifyDataSetChanged();
        }
    }
    public void deleteItem(String name){
        mItemList.remove(name);
        mItemModifiedTimeStamp = getCurrentTimeStamp();
        if (mItemListAdaptor != null) {
            mItemListAdaptor.notifyDataSetChanged();
        }
    }

    public void setItemList(ArrayList<Item> mItemList) {
        this.mItemList = mItemList;
    }

    public ItemListAdapter getItemListAdaptor() {
        return mItemListAdaptor;
    }

    public void setItemListAdaptor(ItemListAdapter mItemListAdaptor) {
        this.mItemListAdaptor = mItemListAdaptor;
    }
    // Functions to take care of Member List
    public ArrayList<String> getMemberList(){
        return mMemberList;
    }
    public void addMember(String name){
        mMemberList.add(name);
        mMemberModifiedTimeStamp = getCurrentTimeStamp();
        if (mMemberListAdaptor != null) {
            mMemberListAdaptor.notifyDataSetChanged();
        }
    }
    public void deleteMember(String name){
        mMemberList.remove(name);
        mMemberModifiedTimeStamp = getCurrentTimeStamp();
        if (mMemberListAdaptor != null) {
            mMemberListAdaptor.notifyDataSetChanged();
        }
    }

    public MemberListAdapter getMemberListAdaptor() {
        return mMemberListAdaptor;
    }

    public void setMemberListAdaptor(MemberListAdapter mListAdaptor) {
        this.mMemberListAdaptor = mListAdaptor;
    }

    public ItemTabFragment getmItemTabFragment() {
        return mItemTabFragment;
    }

    public void setmItemTabFragment(ItemTabFragment mItemTabFragment) {
        this.mItemTabFragment = mItemTabFragment;
    }
    public MemberTabFragment getmMemberTabFragment() {
        return mMemberTabFragment;
    }

    public void setmMemberTabFragment(MemberTabFragment mMemberTabFragment) {
        this.mMemberTabFragment = mMemberTabFragment;
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
