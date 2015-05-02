package com.cmpe277.groupay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class SignInActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private Button mSignInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSignInButton = (Button) findViewById(R.id.signin_button);

        if(mToolbar != null) {
            mToolbar.setLogo(R.mipmap.ic_logo);
            mToolbar.setTitle(R.string.sign_in);
            setSupportActionBar(mToolbar);
        }


        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check User identity here

                // If pass
                Intent i = new Intent(SignInActivity.this, EventListActivity.class);
                startActivity(i);
                
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
