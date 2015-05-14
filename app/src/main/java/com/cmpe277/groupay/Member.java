package com.cmpe277.groupay;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Cynthia on 4/28/2015.
 */
public class Member {
    private String mMyName;
    private ArrayList<myEvent> myEventList;

    public Member(){
        mMyName = "YuYu";
        myEventList = new ArrayList<myEvent>();
    }
    public Member(String myName){
        mMyName = myName;
        myEventList = new ArrayList<myEvent>();
    }

    public String getMyName(){
        return mMyName;
    }
    public void setMyName(String myName){
        mMyName = myName;
    }

    public void addNewEvent(UUID eventID){
        myEventList.add(new myEvent(eventID));
    }

    public void addExpense(UUID eventID, float expense){
        for (myEvent event: myEventList){
            if (event.getmEventID() == eventID){
                event.setmMoneySpent(expense);
            }
        }
    }

    public float getExpense(UUID eventID){
        for (myEvent event: myEventList){
            if (event.getmEventID() == eventID){
                return event.getmMoneySpent();
            }
        }
        return 0;
    }
    public int getVote(UUID eventID, UUID itemID){
        for (myEvent event: myEventList){
            if (event.getmEventID() == eventID){
                 return event.getmVote(itemID);

            }
        }
        return -1;
    }

    public void addVote(UUID eventID, UUID itemID, int option){
        for (myEvent event: myEventList){
            if (event.getmEventID() == eventID){
                event.setmVote(itemID, option);

            }
        }
    }
    public void setNotifyAdapter(UUID eventID, NotificationListAdapter notifyAdapter){
        for (myEvent event: myEventList){
            if (event.getmEventID() == eventID){
                event.setmNotificationListAdapter(notifyAdapter);

            }
        }
    }
    public void setNotify(UUID eventID, String notify){
        for (myEvent event: myEventList){
            if (event.getmEventID() == eventID){
                event.addmNotification(notify);

            }
        }
    }
    public void deleteNotify(UUID eventID, int notify){
        for (myEvent event: myEventList){
            if (event.getmEventID() == eventID){
                event.removeNotification(notify);

            }
        }
    }
    public ArrayList<String> getNotify(UUID eventID){
        for (myEvent event: myEventList){
            if (event.getmEventID() == eventID){
                return event.getmNotification();

            }
        }
        return null;
    }
    public class myEvent{
        private UUID mEventID;
        private float mMoneySpent;
        private ArrayList<Vote> mVoteList;
        private ArrayList<String> mNotificationList;
        private NotificationListAdapter mNotificationListAdapter;

        public myEvent(UUID eventId){
            mEventID = eventId;
            mMoneySpent = 0;
            mVoteList = new ArrayList<Vote>();
            mNotificationList = new ArrayList<String>();
        }
        public UUID getmEventID() {
            return mEventID;
        }

        public void setmEventID(UUID mEventID) {
            this.mEventID = mEventID;
        }

        public float getmMoneySpent() {
            return mMoneySpent;
        }

        public void setmMoneySpent(float mMoneySpent) {
            this.mMoneySpent += mMoneySpent;
        }

        public int getmVote(UUID itemID) {
            int option = -1;
            for (Vote v : mVoteList) {
                if (v.getItemID() == itemID){
                    return v.getOption();
                }
            }
            return option;
        }

        public void setmVote(UUID itemID, int option){
            for (Vote v : mVoteList) {
                if (v.getItemID() == itemID){
                    v.setOption(option);
                    return;
                }
            }
            mVoteList.add(new Vote(itemID,option));
        }

        public ArrayList<String> getmNotification() {
            return mNotificationList;
        }

        public void addmNotification(String notify) {
            this.mNotificationList.add(notify);
            if (mNotificationListAdapter != null){
                mNotificationListAdapter.notifyDataSetChanged();
            }
        }
        public void removeNotification(int notify) {
            this.mNotificationList.remove(notify);
            if (mNotificationListAdapter != null){
                mNotificationListAdapter.notifyDataSetChanged();
            }
        }

        public NotificationListAdapter getmNotificationListAdapter() {
            return mNotificationListAdapter;
        }

        public void setmNotificationListAdapter(NotificationListAdapter mNotificationListAdapter) {
            this.mNotificationListAdapter = mNotificationListAdapter;
        }

    }

    public class Vote {
        private UUID mItemID;
        private int mOption;

        public Vote(UUID itemID, int option){
            mItemID = itemID;
            mOption = option;
        }

        public UUID getItemID() {
            return mItemID;
        }

        public void setItemID(UUID itemID) {
            this.mItemID = itemID;
        }

        public int getOption() {
            return mOption;
        }

        public void setOption(int option) {
            this.mOption = option;
        }
    }
}
