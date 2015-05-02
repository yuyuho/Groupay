package com.cmpe277.groupay;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Cynthia on 4/28/2015.
 */
public class Member {
    private String mMyName;
    private ArrayList<Vote> mVotes;

    public Member(){
        mMyName = "YUYU";
        mVotes = new ArrayList<Vote>();
    }
    public Member(String myName){
        mMyName = myName;
        mVotes = new ArrayList<Vote>();
    }
    public String getMyName(){
        return mMyName;
    }
    public ArrayList<Vote> getVotes() {
        return mVotes;
    }

    public void setVotes(ArrayList<Vote> mVotes) {
        this.mVotes = mVotes;
    }

    public int findVoteAndGetOption (UUID voteId){
        for (Vote v : mVotes){
            if (v.getVotedItemId() == voteId){
                return v.getOptions();
            }
        }
        return -1;
    }
}
