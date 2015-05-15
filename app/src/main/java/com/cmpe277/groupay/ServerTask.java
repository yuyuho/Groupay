package com.cmpe277.groupay;

import android.content.Context;
import android.content.Entity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by ellismit on 5/12/2015.
 */
public class ServerTask extends AsyncTask <String, Void, String>
{
    private static final String TAG = "ServerTask";
    private static final String CONNECTION_FAIL = "Connection Failed";
    // Declare AsyncResponse interface
    public AsyncResponse delegate = null;
    public static ServerTask task;
    private Context mContext;

    public ServerTask(Context context, AsyncResponse asyncResponse){
        delegate = asyncResponse;
        mContext = context;
    }
    @Override
    public String doInBackground(String... request) {
        if (request[0].toString().compareTo("login") == 0) {
            String result = login(request[1], request[2]);
            if (result.compareTo("Password is incorrect") == 0 ||
                result.compareTo("Could not complete query. Missing parameter") == 0) {
                return "login failed";
            }else {
                return result;
            }
        }else if(request[0].toString().compareTo("addevent") == 0){
            return "event added";
        }else {
            //request not handled
            return "request not handled";
        }
    }


    public String login(String user, String pass){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://50.152.186.29/android/login.php");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("username", user));
            nameValuePairs.add(new BasicNameValuePair("pass", pass));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);


            Log.d("%s", result);

            return result;
        } catch (ClientProtocolException e) {
            return CONNECTION_FAIL;
        } catch (IOException e) {
            return CONNECTION_FAIL;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // send result to interface fn. // causes crash //
        if (result == CONNECTION_FAIL){
            Toast.makeText(mContext, R.string.connection_fail, Toast.LENGTH_LONG);
        }
        else {
            delegate.taskFinish(result);
        }
    }

}



