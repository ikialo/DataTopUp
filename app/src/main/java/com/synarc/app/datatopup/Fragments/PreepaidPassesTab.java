package com.synarc.app.datatopup.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.synarc.app.datatopup.R;

public class PreepaidPassesTab extends Fragment {

    Button k3_prepaid_info_btn;
    TextView k3_prepaid_info_tv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prepaid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        k3_prepaid_info_btn = getView().findViewById(R.id.k3_prepaid_info_btn);
        k3_prepaid_info_tv = getView().findViewById(R.id.prepaid_3_info_tv);

        k3_prepaid_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (k3_prepaid_info_tv.getVisibility() == View.GONE) {

                    k3_prepaid_info_tv.setVisibility(View.VISIBLE);
                }
                else {
                    k3_prepaid_info_tv.setVisibility(View.GONE);
                }
            }
        });

    }
}
