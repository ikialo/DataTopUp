package com.synarc.app.datatopup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    

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
}
