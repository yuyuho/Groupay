package com.cmpe277.groupay;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by cynthia on 4/26/15.
 */
public class CreateEventFragment extends DialogFragment {
    private static final String TAG = "CreateEventFragment";
    private static final int ACTIVITY_SELECT_IMAGE = 0x1010;
    private ImageButton mEventImageButton;
    private Bitmap mEventIcon;
    private EditText mEventName;
    private EditText mEventDate;

    public int mDay;
    public int mMonth;
    public int mYear;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View newItemView = layoutInflater.inflate(R.layout.dialog_create_event, null);

        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        mEventImageButton = (ImageButton) newItemView.findViewById(R.id.event_icon_selector);
        mEventName = (EditText) newItemView.findViewById(R.id.new_event_edit_text);
        mEventDate = (EditText) newItemView.findViewById(R.id.new_event_edit_date);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setView(newItemView);

        mEventImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });

        mEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment() {

                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        return new DatePickerDialog(
                                getActivity(), this, mYear, mMonth, mDay);
                    }

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(mYear, mMonth, mDay);
                        mYear = year;
                        mMonth = month + 1;
                        mDay = day;
                        mEventDate.setText(mMonth + "/" + mDay + "/" + mYear);
                    }
                };
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

            }
        });

        dialogBuilder.setView(newItemView);
        dialogBuilder
                .setPositiveButton("Enter",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Event event = new Event(
                                        Data.get().getMyName(),
                                        mEventIcon,
                                        mEventName.getText().toString(),
                                        mYear, mMonth, mDay,
                                        Data.get().getmEventList().size());

                                Data.get().getMe().addNewEvent(event.getEventID());
                                Data.get().addEvent(event);

                                ServerTask serverTask =
                                        new ServerTask(getActivity(), new AsyncResponse() {
                                            @Override
                                            public void taskFinish(String output) {
                                                Log.d(TAG, "Response from task: " + output);
                                                if (output.equalsIgnoreCase("Success")){
                                                    Log.d(TAG, "Create event");
                                                }
                                            }
                                        });
                                serverTask.execute(serverTask.CREATE_EVENT,
                                        String.valueOf(Data.get().getMyID()),
                                        mEventName.getText().toString(),
                                        String.valueOf(mMonth), String.valueOf(mDay), String.valueOf(mYear));
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .setCancelable(true);
        setDefault();

        return  dialogBuilder.create();
    }

    public void setDefault(){

        mEventImageButton.setImageResource(R.drawable.ic_logo_b);
        mEventIcon = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_logo2);
        mEventName.setText("");
        mEventDate.setText(mMonth + "/" + mDay + "/" + mYear);
    }
    public void onActivityResult (int requestCode, int resultCode, Intent imageReturnIntent){
        super.onActivityResult(requestCode, resultCode, imageReturnIntent);
        switch (requestCode){
            case ACTIVITY_SELECT_IMAGE:
                if (resultCode == getActivity().RESULT_OK){
                    Bitmap icon = null;
                    try {
                        Uri imageUri = imageReturnIntent.getData();

                        String filePath;
                        String[] imageColumn = { MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver()
                                .query(imageUri, null, null, null, null);
                        if (cursor == null){
                            filePath = imageUri.getPath();
                        }
                        else{
                            cursor.moveToFirst();
                            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            filePath = cursor.getString(idx);
                        }
                        ExifInterface exif = new ExifInterface(filePath);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                        InputStream imageStream = getActivity().getContentResolver()
                                .openInputStream(imageUri);
                        icon = BitmapFactory.decodeStream(imageStream);
                        int newHeight = mEventImageButton.getHeight();
                        int newWidth = mEventImageButton.getWidth();

                        float scaleWidth = ((float) newHeight)/ icon.getWidth();
                        float scaleHeight = ((float) newWidth)/ icon.getWidth();

                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth, scaleHeight);
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }

                        icon = Bitmap.createBitmap(icon, 0, 0,
                                icon.getWidth(), icon.getHeight(),
                                matrix, true);

                        mEventIcon = icon;
                        mEventImageButton.setImageBitmap(icon);
                        Log.d(TAG, "Done decoding the image");
                    }
                    catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                    if (icon == null){
                        Toast.makeText(getActivity(), "Cannot find icon", Toast.LENGTH_SHORT);
                    }
                }
                break;
        }
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(
                    getActivity(), this, year, month, day);
        }
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day){

        }
    }

}
