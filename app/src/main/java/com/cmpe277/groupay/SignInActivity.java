package com.cmpe277.groupay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SignInActivity extends ActionBarActivity {
    private static final String TAG = "SignInActivity";
    private Toolbar mToolbar;
    private Button mSignInButton;
    private Button mCancelButton;
    private EditText mUserName;
    private EditText mPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSignInButton = (Button) findViewById(R.id.signin_button);
        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mUserName = (EditText) findViewById(R.id.user_name_view);
        mPassword = (EditText) findViewById(R.id.password_view);

        if(mToolbar != null) {
            mToolbar.setLogo(R.mipmap.ic_logo);
            mToolbar.setTitle(R.string.sign_in);
            setSupportActionBar(mToolbar);
        }


        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check User identity here
                String serverMsg = mUserName.getText().toString() + " " +mPassword.getText().toString();
                ServerTask serverTask = new ServerTask(SignInActivity.this, new AsyncResponse() {
                    @Override
                    public void taskFinish(String output) {
                        Log.d(TAG, "Response from task: " + output);
                        signInSuccessful(true);
                    }

                });
                serverTask.execute(serverMsg);

            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName.setText("");
                mPassword.setText("");
            }
        });
    }


    private void signInSuccessful(boolean isUser){
        if (isUser) {
            Data.get().getMe().setMyName(mUserName.getText().toString());
            Intent i = new Intent(SignInActivity.this, EventListActivity.class);
            startActivity(i);
        }
        else {
            LayoutInflater layoutInflater = LayoutInflater.from(SignInActivity.this);
            View newItemView = layoutInflater.inflate(R.layout.dialog_yes_no, null);
            TextView textView = (TextView) newItemView.findViewById(R.id.dialog_yesno_text_view);
            textView.setText(getResources().getText(R.string.identity_is_not_being_recognized));

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignInActivity.this);

            dialogBuilder.setView(newItemView);
            dialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            dialogBuilder.create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

}
