package com.cmpe277.groupay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.UUID;


public class MainActivity extends Activity {
    private static final int oneSec = 1000;

    Handler timeHandler;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView)findViewById(R.id.icon_login);

        mImageView.setImageResource(R.drawable.logo);
        timeHandler = new Handler();
        //////////////////

        ///////////////////Hack
        Bitmap icon =  BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo2);

        Data.get().getmEventList().add(new Event( Data.get().getMyName(), icon, "Birthday", 2000, 10, 2, Data.get().getmEventList().size()));
        Data.get().getmEventList().add(new Event( "Ryan", icon, "ToyRus", 2015, 12, 1, Data.get().getmEventList().size()));


        Data.get().getmEventList().get(0).addItem( new Item("Cake"));
        Data.get().getmEventList().get(0).addItem( new Item("Candle"));
        Data.get().getmEventList().get(0).addItem( new Item("fork"));
        Data.get().getmEventList().get(0).addItem( new Item("plate"));
        Data.get().getmEventList().get(0).addItem( new Item("flower"));
        Data.get().getmEventList().get(0).addItem( new Item("Balloon"));

        ItemInfo itemInfo = new ItemInfo();
        itemInfo.setItemPrice(0.44);
        itemInfo.setStore("Amazon");
        Data.get().getmEventList().get(0).getItemAtIndex(1).addItemInfo(itemInfo);

        itemInfo = new ItemInfo();
        itemInfo.setItemPrice(0.99);
        itemInfo.setStore("ebay");
        Data.get().getmEventList().get(0).getItemAtIndex(1).addItemInfo(itemInfo);


        Data.get().getmEventList().get(0).getItemAtIndex(1).setItemStatus(Item.itemStatusEnum.waitForVote);
        Data.get().getmEventList().get(0).getItemAtIndex(2).setItemStatus(Item.itemStatusEnum.bought);
        Data.get().getmEventList().get(0).getItemAtIndex(3).setItemStatus(Item.itemStatusEnum.waitToBeBuy);
        Data.get().getmEventList().get(0).getItemAtIndex(4).setItemStatus(Item.itemStatusEnum.approved);
        Data.get().getmEventList().get(0).getItemAtIndex(5).setItemStatus(Item.itemStatusEnum.requestProof);

        itemInfo = new ItemInfo();
        itemInfo.setItemPrice(0.999);
        itemInfo.setStore("ebay");
        Data.get().getmEventList().get(0).getItemAtIndex(2).addItemInfo(itemInfo);

        itemInfo = new ItemInfo();
        itemInfo.setMemberName("John");
        itemInfo.setItemPrice(0.999);
        itemInfo.setStore("Target");
        itemInfo.setDescription("Plastic, White");
        Data.get().getmEventList().get(0).getItemAtIndex(3).setItemFinalInfo(itemInfo);

        ItemInfo finalInfo = new ItemInfo();
        finalInfo.setItemPrice(200);
        finalInfo.setMemberName("Carol");
        finalInfo.setDescription("White plastic plate");
        finalInfo.setStore("Michael's");
        Bitmap receipt = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_receipt);
        finalInfo.setRecipe(receipt);
        Data.get().getmEventList().get(0).getItemAtIndex(4).setItemFinalInfo(finalInfo);

        finalInfo = new ItemInfo();
        finalInfo.setItemPrice(300);
        finalInfo.setMemberName("Judy");
        finalInfo.setDescription("Red and paper");
        finalInfo.setStore("Michael's");
        receipt = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_receipt);
        finalInfo.setRecipe(receipt);
        Data.get().getmEventList().get(0).getItemAtIndex(5).setItemFinalInfo(finalInfo);

        Data.get().getEvent(1).addItem( new Item("Robot"));
        Data.get().getEvent(1).addItem( new Item("Kitchen Top"));

        Data.get().getmEventList().get(0).addMember("Cynthia");
        Data.get().getmEventList().get(0).addMember("Abraham");
        Data.get().getmEventList().get(1).addMember("Elliot");
        UUID eventID1 = Data.get().getEvent(0).getEventID();
        Data.get().getMe().addNewEvent(eventID1);
        UUID eventID2 = Data.get().getEvent(1).getEventID();
        Data.get().getMe().addNewEvent(eventID2);
        Data.get().getMe().setNotify(eventID1,"vote for an item");
        Data.get().getMe().setNotify(eventID2,"An item is waiting to be bought");
        Data.get().getMe().setNotify(eventID2,"request proof");

        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, oneSec);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
