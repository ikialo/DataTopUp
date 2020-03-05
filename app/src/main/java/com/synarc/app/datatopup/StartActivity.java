package com.synarc.app.datatopup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 565;
    private String ussd_code;
    private Intent dial;

    List<String> list_menu = new ArrayList();
    List<Integer> list_menu_image = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        list_menu.add("Share");
        list_menu.add("Contact Us");
        list_menu.add("Rate The App");

        list_menu_image.add(R.drawable.ic_share_black_24dp);
        list_menu_image.add(R.drawable.ic_email_black_24dp);
        list_menu_image.add(R.drawable.ic_rate_review_black_24dp);

        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);


        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(list_menu_image.get(i)).normalText(list_menu.get(i))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {

                            if (index == 0){
                                shareMe();
                            }
                            if (index == 1){
                                startActivity(new Intent(StartActivity.this, ContactUs.class));

                            }
                            if (index == 2){
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.synarc.app.datatopup&hl=en"));
                                startActivity(browserIntent);
                            }
                        }
                    });
            bmb.addBuilder(builder);
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Check balance", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                checkBal(view);
            }
        });
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
     public  void  shareMe(){
         Intent intent = new Intent();
         intent.setAction(Intent.ACTION_SEND);

         String URL_TO_SHARE = "https://play.google.com/store/apps/details?id=com.synarc.app.datatopup&hl=en";

         intent.setType("text/plain");
         intent.putExtra(Intent.EXTRA_TEXT, URL_TO_SHARE);
         startActivity(Intent.createChooser(intent, "Share"));
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

    public void share(View view) {
        shareMe();
    }
}
