package com.synarc.app.datatopup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EsiPayActivity extends AppCompatActivity {

    EditText meterNumber, amount;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esi_pay);


        initialize();
        button_pressed();
    }

    private void button_pressed() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String esipayFormat = "*775*"+ meterNumber.getText().toString()
                        +"*"+amount.getText().toString()+"#";

                Toast.makeText(EsiPayActivity.this, esipayFormat, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {

        confirm = findViewById(R.id.Confirm);
        meterNumber = findViewById(R.id.et_meterNumber);
        amount = findViewById(R.id.et_amount);
    }
}
