package com.example.theon.expensetracker;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class read_sms extends AppCompatActivity {


    ListView smsListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    private static final int REQUEST_DISPLAY_SMS=4;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_sms);

//        animate the dollar sign
       ImageView dollar=(ImageView)findViewById(R.id.loadDollar);
        Animation rotate = AnimationUtils.loadAnimation(read_sms.this, R.anim.rotate_move_down);
        dollar.startAnimation(rotate);


//SMS READING CODE
        smsListView = (ListView) findViewById(R.id.SMSList);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, smsMessagesList);
        smsListView.setAdapter(arrayAdapter);

        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            refreshSmsInbox();
        } else {
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(read_sms.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);

//           move to new intent
            Intent intent1 = new Intent(getApplicationContext(), display_sms_details.class);
           startActivityForResult(intent1,REQUEST_DISPLAY_SMS);

        }
    }



    private void refreshSmsInbox() {

            Uri uri = Uri.parse("content://sms/inbox");

            Cursor cursor = getContentResolver().query(uri, new String[]{"_id", "address", "date", "body"}, null, null, null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String address = cursor.getString(1);
                String date = cursor.getString(2);
                String body = cursor.getString(3);

                String[] sms_each = body.split("\\s");
                for (int i = 0; i < sms_each.length; i++) {

//                    credit card transaction

                    if (sms_each[i].contains("Transaction")) {
                        String merchantName="";
                        smsMessagesList.add("Type"+"CREDIT CARD");
//                        amount
                        smsMessagesList.add("amount "+sms_each[i+2]);
//                        date
                        smsMessagesList.add("date "+sms_each[i + 9] + sms_each[i + 10] + sms_each[i + 11]);
//                        merchant name
                        for (int y = 4; y < 7; y++) {

                            if (sms_each[i + y].contains("was")) {
                                break;
                            }
                            merchantName+=sms_each[i + y] + " ";
                        }
                        smsMessagesList.add("Merchant Name "+merchantName);

                    }


//				check debit details
                    else if (sms_each[i].contains("charge") && sms_each[i + 1].contains("made")) {
                        smsMessagesList.add("Type"+"DEBIT");
                        smsMessagesList.add("date " + sms_each[i + 8]);
                        smsMessagesList.add("amount " + sms_each[i + 10]);
                    }
//					check credit details
                    else if (sms_each[i].contains("deposit")) {
                        smsMessagesList.add("Type"+ "Credit");
                        smsMessagesList.add("date " + sms_each[i + 4]);
                        smsMessagesList.add("amount " + sms_each[i + 2]);
                    }
                }
                arrayAdapter.add(smsMessagesList);
            }
        }


        public void updateList(final String smsMessage) {
            arrayAdapter.insert(smsMessage, 0);
            arrayAdapter.notifyDataSetChanged();
        }
//end of sms parsing code



    }




