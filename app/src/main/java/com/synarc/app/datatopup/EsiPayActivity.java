package com.synarc.app.datatopup;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class EsiPayActivity extends AppCompatActivity implements AdapterEsiPayRecycler.OnItemClickListener {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE =987 ;
    private List<EsiPayMeterDetail> mList;
    private List<String> listDriver;
    RecyclerView recyclerView;
    SQLiteDatabase mydatabase;
    AdapterEsiPayRecycler adapter;
    Cursor resultSet;
EditText etAmount;
    private String ussd_code;
    private Intent dial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esi_pay);
       // Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);



        mList = new ArrayList<>();

        etAmount = findViewById(R.id.amount_et);

        mydatabase = openOrCreateDatabase("save_esipay",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS EsiPayDetails(MeterNumber VARCHAR,Name VARCHAR);");

        resultSet = mydatabase.rawQuery("Select * from EsiPayDetails",null);

        long sizeOfEntry = getProfilesCount(mydatabase, "EsiPayDetails");

        resultSet.moveToFirst();

        for (int q = 0; q<sizeOfEntry; q++){

            resultSet.moveToPosition(q);

            String esipaynumber = resultSet.getString(0);

            String esipayname = resultSet.getString(1);

            mList.add(new EsiPayMeterDetail(esipaynumber,esipayname));


        }

        recyclerView = findViewById(R.id.recyclerview);

        adapter  = new AdapterEsiPayRecycler(EsiPayActivity.this,mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(EsiPayActivity.this));

        adapter.setOnItemClickListener(EsiPayActivity.this);

        recyclerView.setAdapter(adapter);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Input your Esi Pay Meter Number to save", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                save_meter_dialog();
            }
        });

       // angleMenu(this);

    }

    private void save_meter_dialog() {



        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Set Esi Pay Number & Name/Description");


        Context context = this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add a TextView here for the "Title" label, as noted in the comments
        final EditText titleBox = new EditText(context);
        titleBox.setHint("Meter Number");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            titleBox.setTextColor(getColor(R.color.colorPrimary));
        }
        titleBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(titleBox); // Notice this is an add method

        // Add another TextView here for the "Description" label
        final EditText descriptionBox = new EditText(context);
        descriptionBox.setHint("Name/Description");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            descriptionBox.setTextColor(getColor(R.color.colorPrimary));
        }
        layout.addView(descriptionBox); // Another add method

        dialog.setView(layout); // Again this is a set method, not add

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked

                        save_meter_number(titleBox.getText().toString(), descriptionBox.getText().toString());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        dialog.setPositiveButton("Save", dialogClickListener);
        dialog.setNegativeButton("Cancel", dialogClickListener);

        dialog.show();



    }

    private void save_meter_number(String meterNumber, String name) {
        EsiPayMeterDetail esiPayMeterDetail = new EsiPayMeterDetail(meterNumber, name);

        mydatabase.execSQL("INSERT INTO EsiPayDetails VALUES('" + esiPayMeterDetail.getMeterNumber() + "','" + esiPayMeterDetail.getName() + "');");

        mList.add(esiPayMeterDetail);

        adapter.notifyDataSetChanged();
    }

    public long getProfilesCount( SQLiteDatabase db, String TABLE_NAME ) {
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);

        return count;
    }

    @Override
    public void onItemClick(int position) {

        String amount = etAmount.getText().toString();

        if (!amount.isEmpty() && (Integer.parseInt(amount) >= 15 ||Integer.parseInt(amount) <=50) ){

            String code = "*775*"+mList.get(position).getMeterNumber()+"*"+ etAmount.getText().toString()+ "#";

            call_ussd(code);

        }
        else {
            Toast.makeText(this, "Enter an Amount to purchase", Toast.LENGTH_SHORT).show();

        }

    }

    public void call_ussd(String code) {
        try {

            ussd_code = URLEncoder.encode(code, "UTF-8");


            dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd_code));

            //check if permission is granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                //Request Permission if permission wasnt granted
                ActivityCompat.requestPermissions(EsiPayActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);

            }else {
                startActivity(dial);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



    public void angleMenu(Context context){
        AllAngleExpandableButton button = (AllAngleExpandableButton)findViewById(R.id.button_expandable);
        final List<ButtonData> buttonDatas = new ArrayList<>();

        int[] draw = {R.drawable.ic_phone_android_black_24dp, R.drawable.box,
                R.drawable.ic_camera_alt_black_24dp, R.drawable.download};

        for (int i = 0; i < draw.length; i++) {
            ButtonData buttonData = (ButtonData) ButtonData.buildIconButton(this,draw[i], 5);
            buttonDatas.add(buttonData);
        }
        button.setButtonDatas(buttonDatas);

        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                //do whatever you want,the param index is counted from startAngle to endAngle,
                //the value is from 1 to buttonCount - 1(buttonCount if aebIsSelectionMode=true)
                if (index == 1){

                    startActivity(new Intent(EsiPayActivity.this, MainActivity.class));
                    Toast.makeText(EsiPayActivity.this, "New", Toast.LENGTH_SHORT).show();
                }
                if (index == 2){

                    startActivity(new Intent(EsiPayActivity.this, ScanCreditActivity.class));


                    Toast.makeText(EsiPayActivity.this, "Requested", Toast.LENGTH_SHORT).show();
                }


                if (index == 3){
                    Toast.makeText(EsiPayActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EsiPayActivity.this, BemobileDataActivity.class));



                }
            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onCollapse() {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startActivity(dial);


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
