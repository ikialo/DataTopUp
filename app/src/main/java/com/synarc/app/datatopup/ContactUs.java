package com.synarc.app.datatopup;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactUs extends AppCompatActivity {



    EditText name, email, message;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);


        submit = findViewById(R.id.submit_contact);
        name = findViewById(R.id.name_contact);
        email = findViewById(R.id.contact_email);
        message = findViewById(R.id.contact_message);


    }

    public void submit_message(View view) {





sendEmail(email.getText().toString(), name.getText().toString(), message.getText().toString());






    }

    protected void sendEmail(String email_, String name_, String message_) {
        Log.i("Send email", "");

        String[] TO = {"synarcsystems@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");



        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Top Up PNG - Message Review: "+ name_);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message_);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i(getString(R.string.test_1), "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactUs.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
