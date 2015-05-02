package com.cmpe277.groupay;

import java.util.UUID;

/**
 * Created by Cynthia on 4/28/2015.
 */
public class Vote {
    private UUID mVotedItemId;
    private int mOptions;

    public UUID getVotedItemId() {
        return mVotedItemId;
    }

    public void setVotedItemId(UUID mVotedItemId) {
        this.mVotedItemId = mVotedItemId;
    }

    public int getOptions() {
        return mOptions;
    }

    public void setOptions(int mOptions) {
        this.mOptions = mOptions;
    }
}
