//package com.akp.amazepay;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import com.morpho.morphosmart.sdk.CallbackMask;
//import com.morpho.morphosmart.sdk.MorphoDevice;
//import com.morpho.morphosmart.sdk.MorphoDevice.CaptureError;
//import com.morpho.morphosmart.sdk.MorphoDevice.CaptureEvent;
//import com.morpho.morphosmart.sdk.MorphoDevice.CaptureFeedback;
//import com.morpho.morphosmart.sdk.MorphoDevice.CaptureResult;
//import com.morpho.morphosmart.sdk.MorphoImage;
//import com.morpho.morphosmart.sdk.MorphoImageWriter;
//import com.morpho.morphosmart.sdk.MorphoSmartSDK;
//import com.morpho.morphosmart.sdk.MorphoTemplateType;
//
//public class FingerPrintDemo extends AppCompatActivity {
//
//    private MorphoDevice morphoDevice;
//    private MorphoImageWriter imageWriter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_finger_print_demo);
//        // Initialize MorphoDevice and ImageWriter
//        morphoDevice = MorphoDevice.getDeviceInstance();
//        imageWriter = new MorphoImageWriter();
//
//        // Open the MorphoDevice
//        int ret = morphoDevice.openDevice();
//        if (ret != MorphoSmartSDK.MORPHO_OK) {
//            // Handle error
//            Toast.makeText(this, "Failed to open MorphoDevice", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Start fingerprint capture
//        morphoDevice.capture(CallbackMask.DEVICE_SERIAL_INTERNAL | CallbackMask.CAPTURE_CALLBACK, new MorphoDevice.CaptureCallback() {
//            @Override
//            public void onCaptureEvent(CaptureEvent captureEvent) {
//                // Handle capture event
//                if (captureEvent == CaptureEvent.FINGER_DOWN) {
//                    // Finger placed on the sensor
//                    // You can display a message or update UI as needed
//                } else if (captureEvent == CaptureEvent.FINGER_UP) {
//                    // Finger lifted from the sensor
//                    // You can display a message or update UI as needed
//                }
//            }
//
//            @Override
//            public void onCaptureResult(CaptureResult captureResult) {
//                // Handle capture result
//                if (captureResult.errorCode == CaptureError.MORPHO_OK) {
//                    // Fingerprint capture successful
//                    // Process the captured fingerprint image or template
//                    MorphoImage fingerprintImage = captureResult.image;
//                    // You can save the image or extract template from it
//                    // Example: imageWriter.encodeImage(MorphoImageWriter.EncodeFormat.ISO_FMC_NS, fingerprintImage, "path_to_save_template");
//
//                    // You can also convert the image to a bitmap and display it
//                    Bitmap fingerprintBitmap = fingerprintImage.toBitmap();
//                    ImageView fingerprintImageView = findViewById(R.id.fingerprintImageView);
//                    fingerprintImageView.setImageBitmap(fingerprintBitmap);
//                } else {
//                    // Fingerprint capture failed
//                    // Handle error
//                    Toast.makeText(FingerPrintDemo.this, "Fingerprint capture failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCaptureFeedback(CaptureFeedback captureFeedback) {
//                // Handle capture feedback
//                // This is optional and can be used to provide visual feedback during fingerprint capture
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        // Close the MorphoDevice
//        if (morphoDevice != null) {
//            morphoDevice.closeDevice();
//        }
//    }
//}