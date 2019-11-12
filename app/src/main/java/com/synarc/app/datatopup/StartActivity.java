package com.synarc.app.datatopup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StartActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 565;
    private String ussd_code;
    private Intent dial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void Telikom_Click(View view) {

        startActivity(new Intent(StartActivity.this, MainActivity.class));
    }

    public void EsiPay_Click(View view) {
        startActivity(new Intent(StartActivity.this, EsiPayActivity.class));
    }

    public void Scan_Click(View view) {
        startActivity(new Intent(StartActivity.this, ScanCreditActivity.class));
    }

    public void bmob_Click(View view) {
        startActivity(new Intent(StartActivity.this, Bemobile.class));

    }

    public void checkBal(View view) {


        call_ussd("*120#");

    }


    public void call_ussd(String code) {
        try {

            ussd_code = URLEncoder.encode(code, "UTF-8");
            dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd_code));
            //check if permission is granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                //Request Permission if permission wasnt granted
                ActivityCompat.requestPermissions(StartActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);

            } else {
                startActivity(dial);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
