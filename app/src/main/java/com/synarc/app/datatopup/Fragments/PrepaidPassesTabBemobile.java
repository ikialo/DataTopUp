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
import android.widget.TextView;

import com.synarc.app.datatopup.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PrepaidPassesTabBemobile extends Fragment {


    Button k3_prepaid_info_btn;
    TextView k3_prepaid_info_tv,k6_prepaid_info_tv,k9_prepaid_info_tv,k17_prepaid_info_tv,k32_prepaid_info_tv;

    Button k3_pre, k6_pre, k9_pre, k17_pre, k32_pre;

    String preSelect;
    private String ussd_code;
    private Intent dial;
    final  static int MY_PERMISSIONS_REQUEST_CALL_PHONE = 123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bemobile_prepaid_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        k3_pre = getView().findViewById(R.id.k3_prepaid_b);
        k6_pre = getView().findViewById(R.id.k6_prepaid_b);
        k9_pre = getView().findViewById(R.id.k9_prepaid_b);

        k17_pre = getView().findViewById(R.id.k17_prepaid_b);
        k32_pre = getView().findViewById(R.id.k32_prepaid_b);



        k3_prepaid_info_btn = getView().findViewById(R.id.k3_prepaid_info_btn_b);
        k3_prepaid_info_tv = getView().findViewById(R.id.prepaid_3_info_tv_b);
        k6_prepaid_info_tv = getView().findViewById(R.id.prepaid_6_info_tv_b);
        k9_prepaid_info_tv = getView().findViewById(R.id.prepaid_9_info_tv_b);
        k17_prepaid_info_tv = getView().findViewById(R.id.prepaid_17_info_tv_b);
        k32_prepaid_info_tv = getView().findViewById(R.id.prepaid_32_info_tv_b);


        k3_prepaid_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (k3_prepaid_info_tv.getVisibility() == View.GONE) {

                    k3_prepaid_info_tv.setVisibility(View.VISIBLE);
                    k6_prepaid_info_tv.setVisibility(View.VISIBLE);
                    k9_prepaid_info_tv.setVisibility(View.VISIBLE);
                    k17_prepaid_info_tv.setVisibility(View.VISIBLE);
                    k32_prepaid_info_tv.setVisibility(View.VISIBLE);
                }
                else {
                    k3_prepaid_info_tv.setVisibility(View.GONE);
                    k6_prepaid_info_tv.setVisibility(View.GONE);
                    k9_prepaid_info_tv.setVisibility(View.GONE);
                    k17_prepaid_info_tv.setVisibility(View.GONE);
                    k32_prepaid_info_tv.setVisibility(View.GONE);                }
            }
        });

        button_options();

    }

    private void button_options() {
        k3_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = (String) getString(R.string.bundle_k3b_prepaid);
                areYouSure(k3_pre.getText().toString(), preSelect);            }
        });
        k6_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = (String) getString(R.string.bundle_k6b_prepaid);
                areYouSure(k6_pre.getText().toString(), preSelect);
            }
        });
        k9_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = (String) getString(R.string.bundle_k9b_prepaid);
                areYouSure(k9_pre.getText().toString(), preSelect);
            }
        });
        k17_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = (String) getString(R.string.bundle_17b_prepaid);
                areYouSure(k17_pre.getText().toString(), preSelect);
            }
        });
        k32_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSelect = (String) getString(R.string.bundle_32b_prepaid);
                areYouSure(k32_pre.getText().toString(), preSelect);
            }
        });


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
}
