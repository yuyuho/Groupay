package com.cmpe277.groupay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
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
            url = new URL("http://50.152.186.29");

            //Open a connection to that URL.
            connect = (HttpURLConnection) url.openConnection();
            connect.setConnectTimeout(5000);

            // State we are uploading to server
            connect.setDoOutput(true);

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



