package com.synarc.app.datatopup;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    Button k3,k6,k12, k55, k110, k150;
    String preSelect, ussd_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        k3 = findViewById(R.id.k3_);
        k6 = findViewById(R.id.k6_);
        k12 = findViewById(R.id.k12_);
        k55 = findViewById(R.id.k55_);
        k110 = findViewById(R.id.k110_);
        k150 = findViewById(R.id.k150_);


        k3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = "*777*1*1*1#";
                areYouSure(k3.getText().toString(), preSelect);            }
        });
        k6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = "*777*1*2*1#";
                areYouSure(k6.getText().toString(), preSelect);
            }
        });
        k12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = "*777*1*3*1#";
                areYouSure(k12.getText().toString(), preSelect);
            }
        });
        k55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = "*777*1*4*1#";
                areYouSure(k55.getText().toString(), preSelect);
            }
        });
        k110.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = "*777*1*5*1#";
                areYouSure(k110.getText().toString(), preSelect);
            }
        });
        k150.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preSelect = "*777*1*6*1#";
                areYouSure(k150.getText().toString(), preSelect);

            }
        });



    }

    private void areYouSure(String bundle, final String preSelect){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        call_ussd(preSelect);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm Purchase of "+bundle).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void call_ussd(String code) {
        try {

            ussd_code = URLEncoder.encode(code, "UTF-8");


            Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd_code));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(dial);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()){
//            case R.id.k3_:
//                preSelect = "*777*1*1#";
//
//            case R.id.k6_:
//                preSelect = "*777*1*2#";
//            case R.id.k12_:
//                preSelect = "*777*1*3#";
//            case R.id.k55_:
//                preSelect = "*777*1*4#";
//            case R.id.k110_:
//                preSelect = "*777*1*5#";
//            case R.id.k150_:
//                preSelect = "*777*1*6#";
//
//            default:
//                preSelect = "null";
//        }
//
//        try {
//            if (!preSelect.equals("null")) {
//                ussd_code = URLEncoder.encode(preSelect, "UTF-8");
//            }
//
//            Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd_code));
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            startActivity(dial);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }



}
