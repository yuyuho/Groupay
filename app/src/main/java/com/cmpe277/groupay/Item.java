package com.cmpe277.groupay;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Cynthia on 4/27/2015.
 */
public class Item{
    public enum itemStatusEnum { newItem, waitForVote, waitToBeBuy, bought, requestProof, approved };
    public enum itemDecideEnum { group, member };
    private String mItemName;
    private int mItemQuantity;
    private itemStatusEnum mItemStatus;
    private itemDecideEnum mItemPermission;
    private ItemInfo mItemFinalInfo;
    private ArrayList<ItemInfo> mItemInfoList;
    private ItemInfoListAdapter mItemInfoListAdaptor;
    private int mTotalNumOfVote;
    private UUID mItemID;

    public Item (String name){
        mItemName = name;
        mItemQuantity = 1;
        mItemStatus = itemStatusEnum.newItem;
        mItemPermission = itemDecideEnum.group;
        mItemInfoList = new ArrayList<ItemInfo>();
        mTotalNumOfVote = 0;
        mItemID = UUID.randomUUID();
        mItemFinalInfo = new ItemInfo();
    }

    public UUID getItemID() {
        return mItemID;
    }

    public void setItemID(UUID mItemID) {
        this.mItemID = mItemID;
    }
    public int getItemQuantity() {
        return mItemQuantity;
    }

    public void setItemQuantity(int mItemQuantity) {
        this.mItemQuantity = mItemQuantity;
    }

    public itemStatusEnum getItemStatus() {
        return mItemStatus;
    }

    public void setItemStatus(itemStatusEnum mItemStatus) {
        this.mItemStatus = mItemStatus;
        if(mItemInfoListAdaptor != null){
            mItemInfoListAdaptor.notifyDataSetChanged();
        }
    }

    public itemDecideEnum getItemPermission() {
        return mItemPermission;
    }

    public void setItemPermission(itemDecideEnum mItemPermission) {
        this.mItemPermission = mItemPermission;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public void addItemInfo(ItemInfo info){
        mItemInfoList.add(info);
        //TODO remove this
        mTotalNumOfVote += info.getNumOfVote();
        if (mItemInfoListAdaptor != null){
            mItemInfoListAdaptor.notifyDataSetChanged();
        }
    }

    public void setItemOwnership(String member){
        mItemFinalInfo.setMemberName(member);
    }
    public void setItemFinalInfo(ItemInfo finalInfo){
        mItemFinalInfo = finalInfo;
    }
    public ItemInfo getItemFinalInfo(){
        return mItemFinalInfo;
    }
    public ArrayList<ItemInfo> getItemInfoList(){
        return mItemInfoList;
    }


    public ItemInfoListAdapter getItemInfoListAdaptor() {
        return mItemInfoListAdaptor;
    }

    public void setItemInfoListAdaptor(ItemInfoListAdapter mItemInfoListAdaptor) {
        this.mItemInfoListAdaptor = mItemInfoListAdaptor;
    }

    public int getTotalNumOfVote() {
        return mTotalNumOfVote;
    }

    public void setTotalNumOfVote(int mTotalNumOfVote) {
        this.mTotalNumOfVote = mTotalNumOfVote;
    }

    public void addVote(){
        this.mTotalNumOfVote ++;
        mItemInfoListAdaptor.notifyDataSetChanged();
    }
}
