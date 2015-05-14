package com.cmpe277.groupay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Cynthia on 4/27/2015.
 */
public class ItemInfo {
    private double mItemPrice;
    private Bitmap mRecipe;
    private String mStore;
    private int mNumOfVote;
    private String mMemberName;
    private String mDescription;

    public ItemInfo (){ //TODO need to figure out how to get this info

        mItemPrice = 0.0;
        mRecipe = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_receipt);
        mStore = "";
        mNumOfVote = 1;
        mMemberName = "Gigi";
        mDescription = "";
    }

    public ItemInfo (
            double price,
            String store,
            String desc,
            String member
    ){ //TODO need to figure out how to get this info

        mItemPrice = price;
        mRecipe = null;
        mStore = store;
        mNumOfVote = 0;
        mMemberName = member;
        mDescription = desc;
    }

    public double getItemPrice() {
        return mItemPrice;
    }

    public void setItemPrice(double mItemPrice) {
        this.mItemPrice = mItemPrice;
    }

    public Bitmap getRecipe() {
        return mRecipe;
    }

    public void setRecipe(Bitmap mRecipe) {
        this.mRecipe = mRecipe;
    }

    public String getStore() {
        return mStore;
    }

    public void setStore(String mStore) {
        this.mStore = mStore;
    }

    public int getNumOfVote() {
        return mNumOfVote;
    }

    public void setNumOfVote(int numOfVote) {
        this.mNumOfVote = numOfVote;
    }

    public String getMemberName() {
        return mMemberName;
    }
    public void setMemberName(String mMemberName) {
        this.mMemberName = mMemberName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}
