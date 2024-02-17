package com.akp.amazepay.Das_KYCBlock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.amazepay.R;
import com.netpaisa.aepsriseinlib.MyDialog;
import com.paysprint.onboardinglib.activities.HostActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class KYC_NSDL extends AppCompatActivity {
//    JWT jwt = new JWT();
//    Algorithm algorithm = Algorithm.HMAC256("UFMwMDQzNWFlMWFiNTYwZjg0OWZjMDZkNTdjNzljZDM1ZWNjOWU=");
//    private static final String SECRET = "UFMwMDQzNWFlMWFiNTYwZjg0OWZjMDZkNTdjNzljZDM1ZWNjOWU="; // replace with your secret key
//    private static final String ISSUER = "PS0043"; // replace with your issuer name
//    String deviceId = "device123";
//    String serialNumber = "serial456";
//    String txnId = "txn789";
    String UserId,U_name,U_mob;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GpsTracker gpsTracker;
    String tvLatitude,tvLongitude,EmailAddress,MobileNo,FirmName,AadharCard;
    LinearLayout after_aeps_ll;
    RadioGroup radiodevice;
    String SelectDevice="";
    private int requestCodeMantra = 1;
    String mBiometricData = "";
    RadioButton radioFemale,radioMale;

    Button one,two,three;
    String ip;
    RelativeLayout header;
    String onboard_callbackUrl,onboard_apiUrl,onboard_MemberId,onboard_MemberName,
            onboard_MobileNo,onboard_AadharCard,onboard_EmailAddress,onboard_FirmName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_y_c__n_s_d_l);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        U_name= sharedPreferences.getString("U_name", "");
        U_mob= sharedPreferences.getString("U_mob", "");

        FindId();
        onclickEvent();
        OnboardKycAPI(UserId);

        GetProfile(UserId);
        //***************IP Address Get***********
        Context context = getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d("ipaddress",ip+tvLatitude+tvLongitude);


        if (ContextCompat.checkSelfPermission(KYC_NSDL.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            getLocation();
        } else {
            // Permission is not granted
            requestLocationPermission();
        }
//        Algorithm algorithm = Algorithm.HMAC256(SECRET);
//        String token = JWT.create()
//                .withIssuer(ISSUER)
//                .withSubject("Amazepay")
//                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
//                .sign(algorithm);
//        Log.d("restoken",token);


        Button btn_submit=findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KYC_NSDL.this, HostActivity.class);
                intent.putExtra("pId", "PS0043"); // partner Id provided in credential
                intent.putExtra("pApiKey", "UFMwMDQzNWFlMWFiNTYwZjg0OWZjMDZkNTdjNzljZDM1ZWNjOWU="); // JWT API Key provided in credential
                intent.putExtra("mCode", UserId); // Merchant Code
                intent.putExtra("mobile", U_mob); // merchant mobile number
                intent.putExtra("lat", tvLatitude);
                intent.putExtra("lng", tvLongitude);
                intent.putExtra("firm", FirmName);
                intent.putExtra("email", EmailAddress);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 999);
                Log.d("ressssss",""+intent);
//                Intent intent = new Intent(getApplicationContext(), Hostnew.class);
//                intent.putExtra("partnerId", "PS0043");
//                intent.putExtra("apiKey", "UFMwMDQzNWFlMWFiNTYwZjg0OWZjMDZkNTdjNzljZDM1ZWNjOWU=");
//                intent.putExtra("transactionType", txntype);
//                intent.putExtra("amount", amount);
//                intent.putExtra("merchantCode", "R010");
//                intent.putExtra("remarks", "Test Transaction");
//                intent.putExtra("mobileNumber", "9695100980");
//                intent.putExtra("referenceNumber", getRandomString(5, chars));
//                intent.putExtra("latitude", "22.572646");
//                intent.putExtra("longitude", "88.363895");
//                intent.putExtra("subMerchantId", "R010");
//                intent.putExtra("deviceManufacturerId", "3");
//                startActivityForResult(intent, 999);
            }
        });
    }

    private void FindId() {
        header=findViewById(R.id.header);
        after_aeps_ll=findViewById(R.id.after_aeps_ll);
        radiodevice=findViewById(R.id.radiodevice);
        one=findViewById(R.id.one);
        two=findViewById(R.id.two);
        three=findViewById(R.id.three);
        radioMale=findViewById(R.id.radioMale);
        radioFemale=findViewById(R.id.radioFemale);
    }

    private void OnboardKycAPI(String userid) {
        String otp1 = new GlobalAppApis().OnboardKyc(userid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_OnboardKyc(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("Message").equalsIgnoreCase("Success")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            onboard_callbackUrl = jsonObject.getString("callbackUrl");
                            onboard_apiUrl = jsonObject.getString("apiUrl");
                            onboard_MemberId = jsonObject.getString("MemberId");
                            onboard_MemberName = jsonObject.getString("MemberName");
                            onboard_MobileNo = jsonObject.getString("MobileNo");
                            onboard_AadharCard = jsonObject.getString("AadharCard");
                            onboard_EmailAddress = jsonObject.getString("EmailAddress");
                            onboard_FirmName = jsonObject.getString("FirmName");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } }, KYC_NSDL.this, call1, "", true);
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Boolean status = data.getBooleanExtra("status", false);
            Integer response = data.getIntExtra("response", 0);
            String message = data.getStringExtra("message");
            String detailedResponse = "Status: " + status + ", Response: " + response + ", Message: " + message;
            Log.d("resoutput",detailedResponse);
            AlertDialog.Builder builder = new AlertDialog.Builder(KYC_NSDL.this);
            builder.setTitle("Info");
            builder.setMessage(message);
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }*/

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user
            new AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Request the permission
                            ActivityCompat.requestPermissions(KYC_NSDL.this,
                                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                                    REQUEST_LOCATION_PERMISSION);
                        }
                    })
                    .create()
                    .show();
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_LOCATION_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getLocation();
            } else {
                // Permission is not granted
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            } }
    }
    private void GetPermission() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
            else {
                getLocation();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getLocation(){
        gpsTracker = new GpsTracker(KYC_NSDL.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            tvLatitude=(String.valueOf(latitude));
            tvLongitude=(String.valueOf(longitude));
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    public void GetProfile(String MemberId) {
        String otp1 = new GlobalAppApis().GetProfile(MemberId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_getProfile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("sfafgadf","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("Message").equalsIgnoreCase("Success")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            String MemberId = jsonObject.getString("MemberId");
                            String MemberName = jsonObject.getString("MemberName");
                            EmailAddress = jsonObject.getString("EmailAddress");
                            MobileNo = jsonObject.getString("MobileNo");
                            String Pancard = jsonObject.getString("Pancard");
                            AadharCard = jsonObject.getString("AadharCard");

                            String FullAddress = jsonObject.getString("FullAddress");
                            FirmName = jsonObject.getString("FirmName");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, KYC_NSDL.this, call1, "", true);
    }

    private void onclickEvent() {
        radioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDevice="Morpho";
            }
        });
        radioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDevice="Mantra";
            }
        });
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//***************Morpho Service***********
                if (SelectDevice.equalsIgnoreCase("Morpho")) {
                    try {
                        String pidOption = getPIDOptions();
                        if (pidOption != null) {
                            Log.e("PidOptions", pidOption);
                            Intent intent2 = new Intent();
                            intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                            intent2.putExtra("PID_OPTIONS", pidOption);
                            startActivityForResult(intent2, 2);
                        }
                    } catch (Exception e) {
                        boolean isAppInstall = appInstalledOrNot("com.scl.rdservice");
                        if (isAppInstall) {
                            Log.i("Application is ", "already installed.");
                        } else {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.scl.rdservice&hl=en_IN")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.scl.rdservice&hl=en_IN")));
                            }
                            return;
                        }
                        Log.e("Error", e.toString());
                    }
                }

//***************Mantra Service***********
                else if (SelectDevice.equalsIgnoreCase("Mantra")) {

                    try {
                        String pidOptionMantra = getPIDOptions();
                        if (pidOptionMantra != null) {
                            Intent intentMantra = new Intent();
                            intentMantra.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                            intentMantra.setPackage("com.mantra.rdservice");
                            intentMantra.putExtra("PID_OPTIONS", pidOptionMantra);
//                                startActivityForScanDevice.launch(intentMantra);
                            startActivityForResult(intentMantra, requestCodeMantra);
                        } else {
                            boolean isAppInstall = appInstalledOrNot("com.mantra.rdservice");
                            if (isAppInstall) {
                                Log.i("Application is ", "already installed.");
                            } else {
                                // Redirect to play store
                                final String appPackageName = "com.mantra.rdservice";
                                try {
                                    // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mantra.rdservice&hl=en_IN")));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mantra.rdservice&hl=en_IN")));
                                }
                                return;
                            }
                        }
                    } catch (Exception ex) {
                        //Log.e("Error", e.toString());
                        boolean isAppInstall = appInstalledOrNot("com.mantra.rdservice");
                        if (isAppInstall) {
                            Log.i("Application is ", "already installed.");
                        } else {
                            // Redirect to play store
                            final String appPackageName = "com.mantra.rdservice";
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mantra.rdservice&hl=en_IN")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mantra.rdservice&hl=en_IN")));
                            }
                            return;
                        }
                    }
                }

//***************If Device Noting Selected Then ByDefault Morpho Service call***********
                else if (SelectDevice == null) {
                    try {
                        String pidOption = getPIDOptions();
                        if (pidOption != null) {
                            Log.e("PidOptions", pidOption);
                            Intent intent2 = new Intent();
                            intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                            intent2.putExtra("PID_OPTIONS", pidOption);
                            startActivityForResult(intent2, 2);
                        }
                    } catch (Exception e) {
                        boolean isAppInstall = appInstalledOrNot("com.scl.rdservice");
                        if (isAppInstall) {
                            Log.i("Application is ", "already installed.");
                        } else {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.scl.rdservice&hl=en_IN")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.scl.rdservice&hl=en_IN")));
                            }
                            return;
                        }
                        Log.e("Error", e.toString());
                    }
                } else {
                    try {
                        String pidOption = getPIDOptions();
                        if (pidOption != null) {
                            Log.e("PidOptions", pidOption);
                            Intent intent2 = new Intent();
                            intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                            intent2.putExtra("PID_OPTIONS", pidOption);
                            startActivityForResult(intent2, 2);
                        }
                    } catch (Exception e) {
                        boolean isAppInstall = appInstalledOrNot("com.scl.rdservice");
                        if (isAppInstall) {
                            Log.i("Application is ", "already installed.");
                        } else {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.scl.rdservice&hl=en_IN")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.scl.rdservice&hl=en_IN")));
                            }
                            return;
                        }
                        Log.e("Error", e.toString());
                    }
                }
            }

        });



    }


    //***************Morpho Mantra OnActivityResult Response***********
    private String getPIDOptions() {
        try {
            return "<?xml version=\"1.0\"?><PidOptions ver=\"1.0\"><Opts fCount=\"1\" fType=\"2\" iCount=\"0\" pCount=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"10000\" wadh=\"\" posh=\"UNKNOWN\" env=\"P\" /><CustOpts><Param name=\"mantrakey\" value=\"\" /></CustOpts></PidOptions>";
        } catch (Exception e) {
            return "";
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //***************Morpho  OnActivityResult Response***********
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            Bundle mrBundle = data.getExtras();
                            if (mrBundle != null) {
                                String resultPidData = mrBundle.getString("PID_DATA"); // in this variable you will get Pid data in response
                                Log.e("BiometricData1", resultPidData);
                                String xmlString = KYC_AEPS.XMLUtils.convertStringToXmlString(resultPidData);
                                Log.d("XML String", xmlString);
//                                Toast.makeText(getApplicationContext(),"Mantra Response:-\n\n"+resultPidData,Toast.LENGTH_LONG).show();
                                mBiometricData = resultPidData;
                                JSONObject jsonObj = null;
                                try {
                                    try {
                                        // jsonObj = XML.toJSONObject(resultPidData);
                                        XmlToJson xmlToJson = new XmlToJson.Builder(resultPidData).build();
                                        // convert to a JSONObject
                                        jsonObj = xmlToJson.toJson();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    // Log.e("CapturedJSONMr", jsonObj.toString());
                                    JSONObject mrPidDataObject = jsonObj.getJSONObject("PidData");
                                    Log.e("PidData12", mrPidDataObject.toString());
//                                    {"Hmac":"acWx7W0gLCE4ju5FLvEEuHUXezPxQDaQTdVjl3ot2w126+bgTtDk\/wJ71ti3v6Tj","Resp":{"qScore":"74","errInfo":"Capture Success","fType":"0","errCode":"0","iCount":"0","pType":"0","fCount":"1","nmPoints":"35","iType":"0","pCount":"0"},"DeviceInfo":{"additional_info":{"Param":[{"name":"srno","value":"5476209"},{"name":"sysid","value":"5327ef1c97dd1604"},{"name":"ts","value":"2023-06-26T18:15:30+05:30"}]},
//                                    "mc":"MIIEGjCCAwKgAwIBAgIGAYj3Sg8wMA0GCSqGSIb3DQEBCwUAMIHqMSowKAYDVQQDEyFEUyBNQU5UUkEgU09GVEVDSCBJTkRJQSBQVlQgTFREIDMxVTBTBgNVBDMTTEItMjAzIFNoYXBhdGggSGV4YSBPcHBvc2l0ZSBHdWphcmF0IEhpZ2ggQ291cnQgUy5HIEhpZ2h3YXkgQWhtZWRhYmFkIC0zODAwNjAxEjAQBgNVBAkTCUFITUVEQUJBRDEQMA4GA1UECBMHR1VKQVJBVDELMAkGA1UECxMCSVQxJTAjBgNVBAoTHE1BTlRSQSBTT0ZURUNIIElORElBIFBWVCBMVEQxCzAJBgNVBAYTAklOMB4XDTIzMDYyNjEwMjUwNloXDTIzMDcyNjEwNDAwNVowgbAxJDAiBgkqhkiG9w0BCQEWFXN1cHBvcnRAbWFudHJhdGVjLmNvbTELMAkGA1UEBhMCSU4xEDAOBgNVBAgTB0dVSkFSQVQxEjAQBgNVBAcTCUFITUVEQUJBRDEOMAwGA1UEChMFTVNJUEwxHjAcBgNVBAsTFUJpb21ldHJpYyBNYW51ZmFjdHVyZTElMCMGA1UEAxMcTWFudHJhIFNvZnRlY2ggSW5kaWEgUHZ0IEx0ZDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALu9Pp\/iEMskRPwD4Ur35klsar\/5lehAwp6I8XfQRzAjAbeULGDyMKP6M0xfhOflKsJNg+dougPiYiYM+MNZHKs4nFonNnMJKm1gaq0eeTGBA9Mc4AdovB5ETCtX9vIbuvrCcGPRo+I570qPkBM+1UfIEFu1qjYNDEvCXeSPIG7CSraAj2k03LIigMtgF9qdiPNRNPYewMgRjnMxTADwsX26kZ0DUEvOQgG8EJrzA+IqpW1kDrClUrjGr+9\/u0T3rAWmXhj9jzMW0NVI1w312mTTYZaoXiBiW3IOKCYbT77yg\/BvufiFmJfiefRNYJuHDuFbMCDufJjE58QMsMZYcAsCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAd28Ye0cpW49UkJrxfS8eG3iPMTXlCr1QiDtJlV7xYtgbbBrnc7\/7pYy8BdP108iBZpSVTp+9jxD\/5RYZWVdV4PEYuCCf15Qk5U2fyQEWCQY\/tv19Hi68r+EJ6anGuk5ps2DaUgI6r7uYvLJPNaaiIUEukh5nXDJChEtBgB8TzAeI9q2hYYBlg7O9x3NYpKwnwQt5zfAIRrfaRZUFBWpe2x8ua139tYQEKeaIZ0jdg+zFBMEfz1qTQPjvvHv5+w2\/WfgrvLUfmi5j8qu2YtQbhQlo\/rxq+X85S6quablI8XGFRXB4IRXH5JyKKmNuRy6ZVt31FWtzfJGm1dRbKKoQPA==","dpId":"MANTRA.MSIPL","rdsId":"MANTRA.AND.001","mi":"MFS100","rdsVer":"1.0.8","dc":"32c96059-7bc7-4b1a-8f14-5a0535105158"},"Data":{"content":"MjAyMy0wNi0yNlQxODoxNToyN+eEM09v13XTASvB8wFej7b\/yOJkMNoBN3Ne6h\/w2l3U1hVHSFjm\/rH9P\/sdAhKMJ1AEDS8II0EhWhCoh+\/rjFFTD6aqp0DqMa14pCv142KPWw8VvezMO5LrXbl5YO\/KPbdJAy7+CgmRrMSs4kR15B1t2BtED2iYPiDVw2g\/hGvFcyfm80myjwRWgXbatlAIYIpQ0VVPceMx3QuT83zTvkxZD9v2YYUey8fivgjW84TFZnvlxutiqDhOJ3Ghmwy7MA7ia6VQHl6hKDxHAJ07eCK+t6gQBpEYYPQYuIGOraqbqwYC8bh\/KIDtPPiBm+BbDZ07bBCscmtjOENZPayiDTbnXxNh\/noyIbL1U\/Pa9qOAxcjtRvq3pJO6Hz7FSXLQsj10irlEdsJqsa5uCjfnUcYBnlXCz0jFQyByQdN8uy\/+JY+2W1YJ49QZdroFBhkoB0FbU2+WmQA12FtMjEkxcUMY4VwdcRLld05pg+TKB9sWiiwEA0kcJ5rnu5qAJvakVycYllTZsQsLK6oujy1CDS6jw\/kIcAHi0eU48sURyxhtrSBjeet3jG5GxRtwG1l0L1BPvNmJxR78c9viWeVqUIIZkI7PkYKkm\/Vr1DESf1F0NQVBLPYEIrXnpWsBQ5x4pc1XqiSYW+o0XJQTd6VaCoohuH56IgxtQZoziLwTNEmezdy14suzx7cp6vuV21iUUH8UX2FrLdUmMOhOGoUO2hZWS2diWte1Bh4x7i6Qt\/AyifuDyccvbpnqK8VCxCSCnWj4cxVthgRqSJuhLjpMee72H2YI2QdjNF0ySxOmUnR9tBpKr3epNkADF8NywSRhg8CkMionos2QuiCMsiz\/AVXKXctTcce563u5dxd0MjF3xBNLowxpEby9dwP2dfuLmB9Qu0dXs+H88WSchoJBLl\/ZgB\/h\/ej6l6fs3QAnbaGfKpPP27k3oeyFL7E\/Gf2YbxS3\/KbKR54AzzPuK3Za9WqprUfwDLa8D2bnLa7Y3vYUwUv2jcg0dBVBRMSPXqj7sL9nyusC6jO9yU9a4uXpZeUhl8iZT3HslUpx6H0CLRf6gmD11yGyJNU1YSEDwaZubECbdz+SywT0wXjcMSCcnQjxgTCYcch+Ams8pvJM","type":"X"},"Skey":{"content":"Yc5uUjtDGSpoHZLrC44TMprqLhyqpk1yBw7RLBCCR8TsSdFk7kbOLESUVYJCkvTJWswySGHD8+412sThwsVgmoNcX+slNFH+AJwGEfm8k4QIPsRplujypaxzoSZVqxk0cW5+TL93jwjzxkZLpQJrQNMHpXyu5F4ViwVVLgGp8WQWp+NsxGhbacrzdYDSWpA+B0RoLn0fs8w0E+zKEYbCwKcYeQANj2X0E3qmutwrijokit3jYOoXzpc0flgSpZaNH4gMfDyiLAlkUi58Owuq+qvfcSJsrfbMdM+zoUeuhZEKOjFnQqJraRa8tkvtxPZf6f67u8OzGpv+2cODp8QPmg==","ci":"20250923"}}
                                    JSONObject mrRespObject = mrPidDataObject.getJSONObject("Resp");
                                    Log.e("PidData11", mrRespObject.toString());
//                                    {"qScore":"74","errInfo":"Capture Success","fType":"0","errCode":"0",
//                                    "iCount":"0","pType":"0","fCount":"1","nmPoints":"35","iType":"0","pCount":"0"}

                                    String mErrCode = mrRespObject.getString("errCode");
                                    //sErrCode = mErrCode;

                                    String mErrInfo = "";
                                    if (mErrCode.equalsIgnoreCase("0")) {
                                        mErrInfo = "Capture Success";

                                    } else {
                                        mErrInfo = mrRespObject.getString("errInfo");
                                    }
                                    //sErrInfo = mErrInfo;

                                    if (mErrCode.equalsIgnoreCase("0")) {
                                        Toast.makeText(getApplicationContext(), "Capture Success", Toast.LENGTH_LONG).show();
                                        two.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                TwoFactorAuthenticationRegAPI(UserId,ip,tvLatitude,tvLongitude,MobileNo,AadharCard,
                                                        "",xmlString,"");
                                            }
                                        });
                                        three.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                TwoFactorAuthenticationAPI(UserId,ip,tvLatitude,tvLongitude,MobileNo,AadharCard,
                                                        "",xmlString,"");
                                            }
                                        });


                                    } else if (!mErrInfo.isEmpty() && mErrInfo.equalsIgnoreCase("SafetyNet Integrity not passed so please refresh RD Service manually.")) {
                                        Toast.makeText(getApplicationContext(), "Device not ready.", Toast.LENGTH_LONG).show();
                                    } else if (!mErrInfo.isEmpty() && mErrInfo.equalsIgnoreCase("Device not ready")) {
                                        Toast.makeText(getApplicationContext(), mErrInfo + "Device not ready.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Device not ready.", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                } catch (JSONException var10) {
                                    MyDialog.errorDialog(KYC_NSDL.this, var10.getMessage());
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while Desirable PID Data", e);
                    }
                }
                break;
            //*************** Mantra OnActivityResult Response***********
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                two.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TwoFactorAuthenticationRegAPI(UserId,ip,tvLatitude,tvLongitude,MobileNo,AadharCard,
                                                "",result,"");
                                    }
                                });
                                three.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TwoFactorAuthenticationAPI(UserId,ip,tvLatitude,tvLongitude,MobileNo,AadharCard,
                                                "",result,"");
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
                    }
                }
                break;

//                Onbording result
            case 999:
                if (resultCode == Activity.RESULT_OK) {
                    Boolean status = data.getBooleanExtra("status", false);
                    Integer response = data.getIntExtra("response", 0);
                    String message = data.getStringExtra("message");
                    String detailedResponse = "Status: " + status + ", Response: " + response + ", Message: " + message;
                    Log.d("resoutput", detailedResponse);
                    AlertDialog.Builder builder = new AlertDialog.Builder(KYC_NSDL.this);
                    builder.setTitle("Info");
                    builder.setMessage(message);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            after_aeps_ll.setVisibility(View.VISIBLE);
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
                break;

        }
    }


    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }



    private void TwoFactorAuthenticationAPI(String MemberId,String ip,String lat,String lag,String mob,String adhar,String innon,String xmldata,String tp) {
        String otp1 = new GlobalAppApis().TwoFactorAuthenticationReg(MemberId,ip,lat,lag,mob,adhar,innon,xmldata,tp);
        Log.d("AEPSWithdraw","cxc"+otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_TwoFactorAuthentication(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject aobject = new JSONObject(result);
                    if (aobject.getString("msg").equalsIgnoreCase("Success")) {
                        JSONObject jsonObject1 = aobject.getJSONObject("Response");
//                        for (int i = 0; i < Response.length(); i++) {
//                            JSONObject jsonObject = Response.getJSONObject(i);
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(KYC_NSDL.this);
                        builder.setTitle("API Response:-")
                                .setMessage(jsonObject1.getString("message"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();
//                        }

                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(KYC_NSDL.this);
                        builder.setTitle("Error!!  Response:-")
                                .setMessage(aobject.getString("msg"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        builder.create().show();
                        Toast.makeText(KYC_NSDL.this,aobject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } }, KYC_NSDL.this, call1, "", true);
    }

    private void TwoFactorAuthenticationRegAPI(String MemberId,String ip,String lat,String lag,String mob,String adhar,String innon,String xmldata,String tp) {
        String otp1 = new GlobalAppApis().TwoFactorAuthenticationReg(MemberId,ip,lat,lag,mob,adhar,innon,xmldata,tp);
        Log.d("AEPSWithdraw","cxc"+otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_TwoFactorAuthenticationReg(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject aobject = new JSONObject(result);
                    if (aobject.getString("msg").equalsIgnoreCase("Success")) {
                        JSONObject jsonObject1 = aobject.getJSONObject("Response");
//                        for (int i = 0; i < Response.length(); i++) {
//                            JSONObject jsonObject = Response.getJSONObject(i);
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(KYC_NSDL.this);
                        builder.setTitle("API Response:-")
                                .setMessage(jsonObject1.getString("message"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }); builder.create().show();
//                        }
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(KYC_NSDL.this);
                        builder.setTitle("Error!!  Response:-")
                                .setMessage(aobject.getString("msg"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel(); }});
                        builder.create().show();
                        Toast.makeText(KYC_NSDL.this,aobject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } }, KYC_NSDL.this, call1, "", true);
    }

}
