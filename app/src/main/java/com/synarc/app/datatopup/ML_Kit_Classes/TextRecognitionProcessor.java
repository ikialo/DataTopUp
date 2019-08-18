package com.synarc.app.datatopup.ML_Kit_Classes;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.synarc.app.datatopup.ScanCreditActivity;

import java.io.IOException;
import java.util.List;

public class TextRecognitionProcessor extends VisionProcessorBase<FirebaseVisionText> {

    private static final String TAG = "TextRecProc";

    private final FirebaseVisionTextRecognizer detector;
    private final ScanCreditActivity activityInstance;

    public TextRecognitionProcessor(ScanCreditActivity scanCreditActivity) {
        detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        activityInstance = scanCreditActivity;
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Text Detector: " + e);
        }
    }

    @Override
    protected Task<FirebaseVisionText> detectInImage(FirebaseVisionImage image) {


        return detector.processImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull final FirebaseVisionText results,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {



        graphicOverlay.clear();

        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay,
                    originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }
        List<FirebaseVisionText.TextBlock> blocks = results.getTextBlocks();
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {




                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();



                for (int k = 0; k < elements.size(); k++) {



                    if (lines.get(j).getElements().size() == 3) {
                        if (elements.get(k).getText().length() == 4) {





                            GraphicOverlay.Graphic textGraphic = new TextGraphic(graphicOverlay,
                                    elements.get(k));
                            graphicOverlay.add(textGraphic);
                        }

                    }
                }
            }
        }
        graphicOverlay.postInvalidate();


        activityInstance.updateSpinnerFromTextResults(results);

//        activityInstance
//                .capture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activityInstance.displayList.clear();
//
//            }
//        });

        ///

    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.w(TAG, "Text detection failed." + e);
    }


//    Boolean IsDigitsOnly(String str)
//    {
//        for ()
//        {
//
//        }
//
//        return true;
//    }
}
