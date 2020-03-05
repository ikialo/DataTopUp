package com.synarc.app.datatopup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.ParseException;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.synarc.app.datatopup.ML_Kit_Classes.CameraSource;
import com.synarc.app.datatopup.ML_Kit_Classes.CameraSourcePreview;
import com.synarc.app.datatopup.ML_Kit_Classes.GraphicOverlay;
import com.synarc.app.datatopup.ML_Kit_Classes.TextRecognitionProcessor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

public class ScanCreditActivity extends AppCompatActivity {

    private static final String TAG = "LauncherActivity";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 358;
    private CameraSourcePreview preview; // To handle the camera
    private GraphicOverlay graphicOverlay; // To draw over the camera screen
    private CameraSource cameraSource = null; //To handle the camera
    private RecyclerView resultSpinner;// To display the results recieved from Firebase MLKit
    private static final int PERMISSION_REQUESTS = 1; // to handle the runtime permissions
    public List<String> displayList; // to manage the adapter of the results recieved
    // private ResultAdapter displayAdapter; // adapter bound with the result recycler view ---> Contains a simple textview with background
    private LinearLayout resultContainer;// just another layout to maintain the symmetry
    public Button capture;
    private String ussd_code;
    private Intent dial;
    private char[] voucher;
    int voucher_index = 0;

    public boolean continue_scan = true;


    private android.hardware.Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private android.hardware.Camera.Parameters params;
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_credit);

        xmlViews();

        // initializeView();

        chechFlash();

        getCamera();

        /*
         * Switch button click event to toggle flash on/off
         */


        if (preview == null) {
            Log.d(TAG, " Preview is null ");
        }

        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null ");
        }
        if (allPermissionsGranted()) {
            createCameraSource();
        } else {
            getRuntimePermissions();
        }
        // angleMenu(this);
//        capture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startCameraSource();
//            }
//        });


    }

    private void chechFlash() {

        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(ScanCreditActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");

            alert.show();
            return;
        }
    }

    /*
     * Get the camera
     */
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();


            } catch (RuntimeException e) {
                Log.e("CameraError ", e.getMessage());
            }
        }
    }


    // Actual code to start the camera
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "startCameraSource resume: Preview is null ");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "startCameraSource resume: graphOverlay is null ");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.d(TAG, "startCameraSource : Unable to start camera source." + e.getMessage());
                cameraSource.release();
                cameraSource = null;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }


//    @SuppressLint("StringFormatInvalid")
//    private void initializeView() {
//
//        // intializing views
//        displayList = new ArrayList<>();
//        resultSpinner.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//      //  displayAdapter = new ResultAdapter(this, displayList);
//        Log.d(TAG, "initializeView: On click listener set");
//      //  displayAdapter.setOnItemClickListener(ScanCreditActivity.this);
//      //  resultSpinner.setAdapter(displayAdapter);
//        resultContainer.getLayoutParams().height = (int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.65);
//
//    }

    private void xmlViews() {
        // getting views from the xml

        //resultContainer = findViewById(R.id.resultsContainer);
        //  resultSpinner = findViewById(R.id.results_spinner);
        preview = findViewById(R.id.Preview);
        graphicOverlay = findViewById(R.id.Overlay);
        //  capture = findViewById(R.id.capture);
        // flash switch button
        // btnSwitch =  findViewById(R.id.flash);
    }

    // Function to check if all permissions given by the user
    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    // List of permissions required by the application to run.
    private String[] getRequiredPermissions() {
        return new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    // Checking a Runtime permission value
    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "isPermissionGranted Permission granted : " + permission);
            return true;
        }
        Log.d(TAG, "isPermissionGranted: Permission NOT granted -->" + permission);
        return false;
    }

    // getting runtime permissions
    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    // Function to create a camera source and retain it.
    private void createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        try {


            cameraSource.setMachineLearningFrameProcessor(new TextRecognitionProcessor(this));

        } catch (Exception e) {
            Log.d(TAG, "createCameraSource can not create camera source: " + e.getCause());
            e.printStackTrace();
        }
    }


    //  updating and displaying the results recieved from Firebase Text Processor Api
    @SuppressLint("StringFormatInvalid")
    public void updateSpinnerFromTextResults(FirebaseVisionText textresults) {
        List<FirebaseVisionText.TextBlock> blocks = textresults.getTextBlocks();

        outerloop:
        for (FirebaseVisionText.TextBlock eachBlock : blocks) {
            for (FirebaseVisionText.Line eachLine : eachBlock.getLines()) {
                for (FirebaseVisionText.Element eachElement : eachLine.getElements()) {
                    // if (!displayList.contains(eachElement.getText()) && displayList.size() <= 9) {

                    if (eachLine.getElements().size() == 3 && eachElement.getText().length() == 4) {
                        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
                        String voucherNo = eachLine.getText();//displayList.get(position);
                        // replace the space
                        voucherNo = voucherNo.replaceAll("\\s", "");


                        if (voucherNo.length() == 12) {


                            if (!hasLetter(voucherNo)) {

                                String code = "*121*" + voucherNo + "#";

                                Toast.makeText(this, code, Toast.LENGTH_LONG).show();
                                call_ussd(code);
                                // continue scan variable is used as a public variable so  setting it to
                                // false affects the read in TextRecognitionProcessor.java class
                                continue_scan = false;
                                finish();
                                break outerloop;
                            }
                        }
                    }
                    if ((eachLine.getElements().size() == 3 || eachLine.getElements().size() == 4 || eachLine.getElements().size() == 5) && (eachElement.getText().length() == 2 || eachElement.getText().length() == 3

                            || eachElement.getText().length() == 4)) {


                        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
                        String voucherNo = eachLine.getText();//displayList.get(position);


                        // replace the space
                        voucherNo = voucherNo.replaceAll("\\s", "");


                        if (voucherNo.length() == 12 || voucherNo.length() == 14 || voucherNo.length() == 13) {


                            if (!hasLetter(voucherNo)) {
                                String code = "*121*" + voucherNo + "#";

                                Toast.makeText(this, code, Toast.LENGTH_LONG).show();

                                call_ussd(code);
                                // continue scan variable is used as a public variable so  setting it to
                                // false affects the read in TextRecognitionProcessor.java class
                                continue_scan = false;
                                finish();
                                break outerloop;
                            }

                        }
                    }


                }
            }
        }

    }


    public boolean hasLetter(String input) {
        try {
            Long num = Long.parseLong(input);

            Toast.makeText(this, "Has Number Only: " + num, Toast.LENGTH_SHORT).show();


            return false;
        } catch (ParseException e) {
            Toast.makeText(this, "Has Letter: " + input, Toast.LENGTH_SHORT).show();

            return true;
        } catch (NumberFormatException e) {

            Toast.makeText(this, "Has Letter: " + input, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public void call_ussd(String code) {
        try {

            ussd_code = URLEncoder.encode(code, "UTF-8");
            dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd_code));
            //check if permission is granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                //Request Permission if permission wasnt granted
                ActivityCompat.requestPermissions(ScanCreditActivity.this,
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