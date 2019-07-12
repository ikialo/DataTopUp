package com.synarc.app.datatopup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.synarc.app.datatopup.ML_Kit_Classes.CameraSource;
import com.synarc.app.datatopup.ML_Kit_Classes.CameraSourcePreview;
import com.synarc.app.datatopup.ML_Kit_Classes.GraphicOverlay;
import com.synarc.app.datatopup.ML_Kit_Classes.TextRecognitionProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScanCreditActivity extends AppCompatActivity {

    private static final String TAG = "LauncherActivity";
    private CameraSourcePreview preview; // To handle the camera
    private GraphicOverlay graphicOverlay; // To draw over the camera screen
    private CameraSource cameraSource = null; //To handle the camera
    private RecyclerView resultSpinner;// To display the results recieved from Firebase MLKit
    private static final int PERMISSION_REQUESTS = 1; // to handle the runtime permissions
    private List<String> displayList; // to manage the adapter of the results recieved
    private ResultAdapter displayAdapter; // adapter bound with the result recycler view ---> Contains a simple textview with background
    private TextView resultNumberTv;// to display the number of results
    private LinearLayout resultContainer;// just another layout to maintain the symmetry
    public Button capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_credit);

        xmlViews();

        initializeView();

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

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraSource();
            }
        });

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
                preview.start(cameraSource,graphicOverlay);
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


    @SuppressLint("StringFormatInvalid")
    private void initializeView() {

        // intializing views
        displayList = new ArrayList<>();
        resultSpinner.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        displayAdapter = new ResultAdapter(this, displayList);
        resultSpinner.setAdapter(displayAdapter);
        resultContainer.getLayoutParams().height = (int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.65);
        resultNumberTv.setText(getString(R.string.x_results_found, displayList.size()));
    }

    private void xmlViews() {
        // getting views from the xml
        resultNumberTv = findViewById(R.id.resultsMessageTv);
        resultContainer = findViewById(R.id.resultsContainer);
        resultSpinner = findViewById(R.id.results_spinner);
        preview =  findViewById(R.id.Preview);
        graphicOverlay = findViewById(R.id.Overlay);
        capture = findViewById(R.id.capture);
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
        for (FirebaseVisionText.TextBlock eachBlock : blocks) {
            for (FirebaseVisionText.Line eachLine : eachBlock.getLines()) {
                for (FirebaseVisionText.Element eachElement : eachLine.getElements()) {
                    if (!displayList.contains(eachElement.getText()) && displayList.size() <= 9) {
                        displayList.add(eachLine.getText());

                        Log.d(TAG, "updateSpinnerFromTextResults: "+ eachElement.getText());

                    }
                }
            }
        }
        resultNumberTv.setText(getString(R.string.x_results_found, displayList.size()));
        displayAdapter.notifyDataSetChanged();
    }


}
