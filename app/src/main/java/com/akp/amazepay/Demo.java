package com.akp.amazepay;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import im.delight.android.webview.AdvancedWebView;

public class Demo extends AppCompatActivity {
    private String PID_DATA;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        tv=findViewById(R.id.tv);
       launchRdService();
    }

    private void launchRdService() {
        try {
            String pidOption = getPIDOptions();
            Intent intent2 = new Intent();
            intent2.setPackage("com.mantra.rdservice");
            intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
            intent2.putExtra("PID_OPTIONS", pidOption);
            //startActivityForResult(intent2, 1);
            startActivityForScanDevice.launch(intent2);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Please install Rd Service first.",Toast.LENGTH_LONG).show();
//            ErrorDialog.createErrorDialog(Demo.class, "Please install " + deviceName + " Rd Service first.");
        }
    }
    ActivityResultLauncher<Intent> startActivityForScanDevice = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    PID_DATA = intent.getStringExtra("PID_DATA");
                    Log.d("PID_DATA", PID_DATA);
                    String xmlString = XMLUtils.convertStringToXmlString(PID_DATA);
                    Log.d("XML String", xmlString);
                    tv.setText(xmlString);
                    Toast.makeText(getApplicationContext(),PID_DATA,Toast.LENGTH_LONG).show();
                    if (PID_DATA.contains("Device not ready") || PID_DATA.contains("Please connect") || PID_DATA.contains("Not connected")) {
                        Toast.makeText(getApplicationContext(),"Device Not Connected",Toast.LENGTH_LONG).show();
//                        ErrorDialog.createErrorDialog(activity, "Device Not Connected");
                    } else if (PID_DATA.contains("Bluetooth Connection Failed") || PID_DATA.contains("Bluetooth connection failed")) {
//                        ErrorDialog.createErrorDialog(activity, "Please pair your device");
                        Toast.makeText(getApplicationContext(),"Please pair your device",Toast.LENGTH_LONG).show();

                    } else {
//                        checkAePSService();
                    } } });

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                Log.d("res_data",result);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
                    }
                }
                break;
        }
    }*/

    private String getPIDOptions() {
        try {
            return "<?xml version=\"1.0\"?> <PidOptions ver=\"1.0\"> <Opts fCount=\"1\" fType=\"2\" iCount=\"0\" pCount=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"10000\" posh=\"UNKNOWN\" env=\"P\" /> '+DString+'<CustOpts><Param name=\"mantrakey\" value=\"\" /></CustOpts> </PidOptions>";
        } catch (Exception e) {
            return "";
        } }

    public static class XMLUtils {
        public static String convertStringToXmlString(String inputString) {
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                // Set output properties to preserve indentation and formatting
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                // Create the input source from the modified input string
                String modifiedInputString = inputString.replaceAll("</br>", "<br/>");
                Source inputSource = new StreamSource(new StringReader(modifiedInputString));
                // Create a string writer to store the XML string
                StringWriter stringWriter = new StringWriter();
                // Transform the input source to the string writer
                transformer.transform(inputSource, new StreamResult(stringWriter));
                // Retrieve the transformed XML string from the string writer
                return stringWriter.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }
    }
}