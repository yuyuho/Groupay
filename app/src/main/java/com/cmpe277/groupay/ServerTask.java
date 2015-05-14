package com.cmpe277.groupay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
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
    private static final String CONNECTION_FAIL = "Connection Fail";
    // Declare AsyncResponse interface
    public AsyncResponse delegate = null;
    public static ServerTask task;
    private Context mContext;

    public ServerTask(Context context, AsyncResponse asyncResponse){
        delegate = asyncResponse;
        mContext = context;
    }
    @Override
    public String doInBackground(String... request)
    {
        URL url;
        HttpURLConnection connect;
        String received = "";
        //String test = "hello pi";
        int i = 0;

        try {
            // External IP = 50.152.186.29
            // Internal IP = 10.0.0.139
            url = new URL("http://50.152.186.29/android/login.php");

            //Open a connection to that URL.
            connect = (HttpURLConnection) url.openConnection();
            connect.setConnectTimeout(5000);

            // State we are uploading to server
            connect.setDoOutput(true);  // Allows outputs/ POST
            connect.setDoInput(true);   // Allows inputs
            connect.setRequestMethod("POST");

            // Increase performance/ prevent exhaust res, stops Android from buffering the complete request body in memory before it is transmitted
                connect.setChunkedStreamingMode(0);  // Used when string length unknown
                // setFixedLengthStreamingMode(int) // Can use this when string length known


            //Things we need to pass with the POST request ////////// NEED TO GET VALUES FROM LOGIN AND .......
                //BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair("paramUsername", paramUsername);
                //BasicNameValuePair passwordBasicNameValuePAir = new BasicNameValuePair("paramPassword", paramPassword);

            // Add the content that we want to pass with the POST request to as name-value pairs
            //Put sending details to an ArrayList with type safe of NameValuePair
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            //nameValuePair.add(usernameBasicNameValuePair);
            //nameValuePair.add(passwordBasicNameValuePAir);


            //Send POST  request to server
            PrintWriter output = new PrintWriter(connect.getOutputStream());
            output.print(request);

            // Listen for response from server // crashes...
             Scanner inStream = new Scanner(connect.getInputStream());

            // Store the string as it is recieved
              while(inStream.hasNextLine())
              received += (inStream.nextLine());

            // release the connection so its resources can be reused/closed
              connect.disconnect();

            // Close instream
            inStream.close();

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
        }
        catch (SocketException e){
            Log.e(TAG, e.toString());
            return CONNECTION_FAIL;
        }
        catch (IOException e) {
            Log.e(TAG, e.toString());
            return CONNECTION_FAIL;
        }
        return received;
    }


    @Override
    protected void onPostExecute(String result) {
        // send result to interface fn. // causes crash //
        if (result ==  CONNECTION_FAIL){

            Log.e(TAG, "Connection Fail");
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Connection Fail")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();
        }
        else {
            delegate.taskFinish(result);
        }

    }

}



