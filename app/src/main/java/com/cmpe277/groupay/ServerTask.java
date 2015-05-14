package com.cmpe277.groupay;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;

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
    // Declare AsyncResponse interface
    public AsyncResponse delegate = null;
    public static ServerTask task;

    public ServerTask(AsyncResponse asyncResponse){
        delegate = asyncResponse;
    }
    @Override
    public String doInBackground(String... request) {
        if (request[0].toString() == "login") {
            boolean success = login(request[1], request[2]);
            if (success) return "login success";
            else return "login failed";
        }else if(request[0].toString() == "addevent"){
            return "event added";
        }else {
            //request not handled
            return "request not handled";
        }
    }
//        URL url;
//        HttpURLConnection connect;
//        String received = "";
//        //String test = "hello pi";
//        int i = 0;
//
//        try {
//            // External IP = 50.152.186.29
//            // Internal IP = 10.0.0.139
//            url = new URL("http://50.152.186.29");
//
//            //Open a connection to that URL.
//            connect = (HttpURLConnection) url.openConnection();
//            connect.setConnectTimeout(5000);
//
//            // State we are uploading to server
//            connect.setDoOutput(true);
//
//            //Send POST  request to server
//            PrintWriter output = new PrintWriter(connect.getOutputStream());
//            output.print(request);
//
//            // Listen for response from server // crashes...
//             Scanner inStream = new Scanner(connect.getInputStream());
//
//            // Store the string as it is recieved
//              while(inStream.hasNextLine())
//              received += (inStream.nextLine());
//
//            // release the connection so its resources can be reused/closed
//              connect.disconnect();

    /* Define InputStreams to read from the URLConnection. */
            //InputStream aInputStream = connect.getInputStream();
           // BufferedInputStream aBufferedInputStream = new BufferedInputStream(aInputStream);

    /* Read bytes to the Buffer until there is nothing more to read(-1) */
           /* ByteArrayBuffer aByteArrayBuffer = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = aBufferedInputStream.read()) != -1) {
                aByteArrayBuffer.append((byte) current);
            }
*/
    /* Convert the Bytes read to a String. */
  //          received = new String(aByteArrayBuffer.toByteArray());

//        catch (SocketException e){
//            Log.e(TAG, e.toString());
//            delegate.connectionFail();
//        }
//        catch (IOException e) {
//            Log.e(TAG, e.toString());
//            delegate.connectionFail();
//        }
//        return received;

    public boolean login(String user, String pass){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://50.152.186.29/android/login.php");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("username", user));
            nameValuePairs.add(new BasicNameValuePair("pass", pass));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            Log.d("%s", response.toString());
            if(response.toString() == "success"){
                return true;
            }
            else {
                return false;
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }

    @Override
    protected void onPostExecute(String result) {
        // send result to interface fn. // causes crash //
        delegate.taskFinish(result);

    }

}



