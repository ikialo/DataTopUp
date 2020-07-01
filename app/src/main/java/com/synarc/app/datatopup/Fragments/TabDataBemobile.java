package com.synarc.app.datatopup.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.synarc.app.datatopup.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TabDataBemobile extends Fragment {


    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE= 458;
    Button k3,k6,k12, k55, k110, k150, k30;
    String preSelect, ussd_code;
    Intent dial;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_bmobile, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        button_options();
    }

    //press button to start bundle selection
    private void button_options()  {
        k30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = (String) getString(R.string.bundle_30b);
                areYouSure(k30.getText().toString(), preSelect);            }
        });
        k3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = (String) getString(R.string.bundle_k3b);
                areYouSure(k3.getText().toString(), preSelect);            }
        });
        k6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = (String) getString(R.string.bundle_k6b);
                areYouSure(k6.getText().toString(), preSelect);
            }
        });
        k12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = (String) getString(R.string.bundle_k12b);
                areYouSure(k12.getText().toString(), preSelect);
            }
        });
        k55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = (String) getString(R.string.bundle_55b);
                areYouSure(k55.getText().toString(), preSelect);
            }
        });
        k110.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = (String) getString(R.string.bundle_110b);
                areYouSure(k110.getText().toString(), preSelect);
            }
        });
        k150.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preSelect = (String) getString(R.string.bundle_k150b);
                areYouSure(k150.getText().toString(), preSelect);

            }
        });

    }

    //one time initialized variable
    private void initialize(View view) {
        k3 = getView().findViewById(R.id.k3_be);
        k30 = getView().findViewById(R.id.k30_be);
        k6 = getView().findViewById(R.id.k6_be);
        k12 = getView().findViewById(R.id.k12_be);
        k55 = getView().findViewById(R.id.k55_be);
        k110 = getView().findViewById(R.id.k110_be);
        k150 = getView().findViewById(R.id.k150_be);

    }

    // dialog option to confirm button press
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Confirm Purchase of "+bundle).setPositiveButton("Confirm", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    //connects to Telikom system to purchase bundle
    private void call_ussd(String code) {

        try {
            ussd_code = URLEncoder.encode(code, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd_code));

        //check if permission is granted
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            //Request Permission if permission wasnt granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);

        }else {
            startActivity(dial);
        }

    }

    // on button telikom item clicked it brings up a menu options
//    // to choose other services such as esi pay and tok combo
//    public void angleMenu(Context context){
//        AllAngleExpandableButton button = (AllAngleExpandableButton)findViewById(R.id.button_expandable);
//        final List<ButtonData> buttonDatas = new ArrayList<>();
//
//        int[] draw = {R.drawable.ic_phone_android_black_24dp, R.drawable.ic_power_black_24dp,
//                R.drawable.ic_camera_alt_black_24dp, R.drawable.download};
//
//        for (int i = 0; i < draw.length; i++) {
//            ButtonData buttonData = (ButtonData) ButtonData.buildIconButton(this,draw[i], 5);
//            buttonDatas.add(buttonData);
//        }
//        button.setButtonDatas(buttonDatas);
//
//        button.setButtonEventListener(new ButtonEventListener() {
//            @Override
//            public void onButtonClicked(int index) {
//                //do whatever you want,the param index is counted from startAngle to endAngle,
//                //the value is from 1 to buttonCount - 1(buttonCount if aebIsSelectionMode=true)
//                if (index == 1){
//
//                        startActivity(new Intent(MainActivity.this, EsiPayActivity.class));
//                        Toast.makeText(MainActivity.this, "New", Toast.LENGTH_SHORT).show();
//                }
//                if (index == 2){
//
//                    startActivity(new Intent(MainActivity.this, ScanCreditActivity.class));
//
//
//                    Toast.makeText(MainActivity.this, "Requested", Toast.LENGTH_SHORT).show();
//                }
//
//
//                if (index == 3){
//                        Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(MainActivity.this, BemobileDataActivity.class));
//
//
//
//                }
//            }
//
//            @Override
//            public void onExpand() {
//
//            }
//
//            @Override
//            public void onCollapse() {
//
//            }
//        });
//    }
//
//




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

