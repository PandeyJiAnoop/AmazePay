package com.akp.amazepay.Das_KYCBlock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.amazepay.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.netpaisa.aepsriseinlib.MyDialog;
import com.netpaisa.aepsriseinlib.adapter.SearchableSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class KYC_AEPS extends AppCompatActivity {
    LinearLayout money_ll;
    TextInputEditText mob_et,adhar_et,amount_et,tpin_et;
    SearchableSpinner stateet;
    RadioGroup radiodevice;
    CheckBox checkbox,checkbox1;
    Button btn_submit;
    String UserId,U_name;

    ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();
    private String stateid;
    private String ip;
    private GpsTracker gpsTracker;
    String tvLatitude,tvLongitude;
    String SelectedButton="";
    private AlertDialog alertDialog2;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private FriendsListAdapter pdfAdapTer;
    RadioButton radioFemale,radioMale;
    String SelectDevice="";
    private int requestCodeMantra = 1;
   String mBiometricData = "";
    TextView text_mob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_y_c__a_e_p_s);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        U_name= sharedPreferences.getString("U_name", "");
        findViewId();
        onclickEvent();
        GetBankList();
        statusCheck();

        Context context = getApplicationContext();

//***************IP Address Get***********
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d("ipaddress",ip+tvLatitude+tvLongitude);


//***************Bank List Dropdown***********
        stateet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= StateId.size(); j++) {
                        if (stateet.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
}
    //***************Location Status Check***********
public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        else {
            getLocation();
        }
    }
//***************Location Get Service***********
    public void getLocation(){
        gpsTracker = new GpsTracker(KYC_AEPS.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            tvLatitude=(String.valueOf(latitude));
            tvLongitude=(String.valueOf(longitude));
//            Toast.makeText(getApplicationContext(),"Lat:-"+tvLatitude+"\n\nLong:- "+tvLongitude,Toast.LENGTH_LONG).show();
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    //***************Submit Button OnClick***********
    private void onclickEvent() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }
                else if (adhar_et.getText().toString().equalsIgnoreCase("")){
                    adhar_et.setError("Fields can't be blank!");
                    adhar_et.requestFocus();
                }
                else if (tpin_et.getText().toString().equalsIgnoreCase("")){
                    tpin_et.setError("Fields can't be blank!");
                    tpin_et.requestFocus();
                }
                else if(!checkbox.isChecked()){
                    checkbox.requestFocus();
                    Toast.makeText(getApplicationContext(),"Accept our instructions!",Toast.LENGTH_LONG).show();
                }
                else if(!checkbox1.isChecked()){
                    checkbox1.requestFocus();
                    Toast.makeText(getApplicationContext(),"Accept our instructions!",Toast.LENGTH_LONG).show();
                }
                else {

//***************Morpho Service***********
                    if (SelectDevice.equalsIgnoreCase("Morpho")){
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
                    else if (SelectDevice.equalsIgnoreCase("Mantra")){

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
                                    return; } }
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
                            } }
                    }

//***************If Device Noting Selected Then ByDefault Morpho Service call***********
                    else if (SelectDevice == null){
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
                    else {
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
            }
        });
    }

    private void findViewId() {
        text_mob=findViewById(R.id.text_mob);
        tpin_et=findViewById(R.id.tpin_et);
        mob_et=findViewById(R.id.mob_et);
        adhar_et=findViewById(R.id.adhar_et);
        stateet=findViewById(R.id.stateet);
        amount_et=findViewById(R.id.amount_et);
        radiodevice=findViewById(R.id.radiodevice);
        checkbox=findViewById(R.id.checkbox);
        checkbox1=findViewById(R.id.checkbox1);
        btn_submit=findViewById(R.id.btn_submit);
        AppCompatButton cw_btn=findViewById(R.id.cw_btn);
        AppCompatButton be_btn=findViewById(R.id.be_btn);
        AppCompatButton ms_btn=findViewById(R.id.ms_btn);
        money_ll=findViewById(R.id.money_ll);
        RelativeLayout header=findViewById(R.id.header);
        radioMale=findViewById(R.id.radioMale);
        radioFemale=findViewById(R.id.radioFemale);

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

        cw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedButton="1";
                cw_btn.setBackgroundResource(R.color.red);
                be_btn.setBackgroundResource(R.color.bluee);
                ms_btn.setBackgroundResource(R.color.bluee);
                money_ll.setVisibility(View.VISIBLE);
            }
        });

        be_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedButton="2";
                cw_btn.setBackgroundResource(R.color.bluee);
                be_btn.setBackgroundResource(R.color.red);
                ms_btn.setBackgroundResource(R.color.bluee);
                money_ll.setVisibility(View.GONE);
            }
        });

        ms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedButton="3";
                cw_btn.setBackgroundResource(R.color.bluee);
                be_btn.setBackgroundResource(R.color.bluee);
                ms_btn.setBackgroundResource(R.color.red);
                money_ll.setVisibility(View.GONE);
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
                                String xmlString = XMLUtils.convertStringToXmlString(resultPidData);
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
                                        Toast.makeText(getApplicationContext(),"Capture Success",Toast.LENGTH_LONG).show();

                                        if (SelectedButton.equalsIgnoreCase("1")){
                                            AEPSWithdrawAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),amount_et.getText().toString(),stateid,xmlString,tpin_et.getText().toString());
                                        }
                                        else if (SelectedButton.equalsIgnoreCase("2")){
                                            AEPSBankBalanceAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),stateid,xmlString,tpin_et.getText().toString());
                                        }
                                        else if (SelectedButton.equalsIgnoreCase("3")){
                                            AEPSMiniStatementAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),stateid,xmlString,tpin_et.getText().toString());
                                        }
                                        else {
                                            AEPSWithdrawAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),amount_et.getText().toString(),stateid,xmlString,tpin_et.getText().toString());
                                        }
                                    } else if (!mErrInfo.isEmpty() && mErrInfo.equalsIgnoreCase("SafetyNet Integrity not passed so please refresh RD Service manually.")) {
                                        Toast.makeText(getApplicationContext(),"Device not ready.",Toast.LENGTH_LONG).show();
                                    } else if (!mErrInfo.isEmpty() && mErrInfo.equalsIgnoreCase("Device not ready")) {
                                        Toast.makeText(getApplicationContext(),mErrInfo +"Device not ready.",Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Device not ready.",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                } catch (JSONException var10) {
                                    MyDialog.errorDialog(KYC_AEPS.this, var10.getMessage());
                                }
                            } }
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
                                if (SelectedButton.equalsIgnoreCase("1")){
                                    AEPSWithdrawAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),amount_et.getText().toString(),stateid,result,tpin_et.getText().toString());
                                }
                                else if (SelectedButton.equalsIgnoreCase("2")){
                                    AEPSBankBalanceAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),stateid,result,tpin_et.getText().toString());
                                }
                                else if (SelectedButton.equalsIgnoreCase("3")){
                                    AEPSMiniStatementAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),stateid,result,tpin_et.getText().toString());
                                }
                                else {
                                    AEPSWithdrawAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),amount_et.getText().toString(),stateid,result,tpin_et.getText().toString());
                                }
//                                if (U_Pin == null){
//                                    Toast.makeText(KYC_AEPS.this,"T-Pin Not Found!!",Toast.LENGTH_LONG).show();//
//                                }
//                                else {
//                                    if (U_Pin.equalsIgnoreCase(tpin_et.getText().toString())){
//                                        Log.d("responseresult",result);
//                                        if (SelectedButton.equalsIgnoreCase("1")){
//                                            AEPSWithdrawAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),amount_et.getText().toString(),stateid,result,tpin_et.getText().toString());
//                                        }
//                                        else if (SelectedButton.equalsIgnoreCase("2")){
//                                            AEPSBankBalanceAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),stateid,result,tpin_et.getText().toString());
//                                        }
//                                        else if (SelectedButton.equalsIgnoreCase("3")){
//                                            AEPSMiniStatementAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),stateid,result,tpin_et.getText().toString());
//                                        }
//                                        else {
//                                            AEPSWithdrawAPI(UserId,ip,tvLatitude,tvLongitude,mob_et.getText().toString(),adhar_et.getText().toString(),amount_et.getText().toString(),stateid,result,tpin_et.getText().toString());
//                                        }
//                                    }
//                                }
//                                Toast.makeText(getContext(),"XML Result:----->     \n"+result,Toast.LENGTH_LONG).show();//                                pidData = serializer.read(PidData.class, result);
//                                setText(result);
                            } }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
                    }}
                break;
        } }


//***************Morpho Mantra Bank List API Response***********
    void GetBankList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://amazepay.net/api/Amazepay/GetAEPSBankList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
//                    StateName.add("Select Bank");
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        StateId.add(jsonObject1.getString("iinno"));
                        String statename = jsonObject1.getString("ABankName");
                        StateName.add(statename);
                    }

                    stateet.setAdapter(new ArrayAdapter<String>(KYC_AEPS.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(KYC_AEPS.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }



//***************Morpho Mantra Check Balance API***********
    public void AEPSBankBalanceAPI(String MemberId,String ip,String lat,String lag,String mob,String adhar,String innon,String xmldata,String tp) {
        String otp1 = new GlobalAppApis().AEPSBankBalancedata(MemberId,ip,lat,lag,mob,adhar,innon,xmldata,tp);
        Log.d("reswallwetotp1","cxc"+otp1);
//        text_mob.setText(otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_AEPSBankBalance(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("AEPSBankBalancedata","cxc"+result);
                try {
                    JSONObject aobject = new JSONObject(result);
                    if (aobject.getString("msg").equalsIgnoreCase("Success")) {
                        JSONObject jsonObject1 = aobject.getJSONObject("Response");
                        if (jsonObject1.getBoolean("status") == false) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(KYC_AEPS.this);
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
                        }
                        else {
//                        for (int i = 0; i < Response.length(); i++) {
//                            JSONObject jsonObject = Response.getJSONObject(i);
                            AlertDialog.Builder builder = new AlertDialog.Builder(KYC_AEPS.this);
                            builder.setTitle("API Response:-")
                                    .setMessage(jsonObject1.getString("message")+"\n\nBalance Amount:- Rs."+jsonObject1.getString("balanceamount"))
                                    .setCancelable(false)
                                    .setIcon(R.drawable.logo)
                                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.create().show();
//                        }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(KYC_AEPS.this);
                        builder.setTitle("Error!!  Response:-")
                                .setMessage(aobject.getString("msg"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }});
                        builder.create().show();
                        Toast.makeText(KYC_AEPS.this,aobject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        }, KYC_AEPS.this, call1, "", true);
    }



//***************Morpho Mantra Withdraw API***********
    public void AEPSWithdrawAPI(String MemberId,String ip,String lat,String lag,String mob,String adhar,String amt,String innon,String xmldata,String tp) {
        String otp1 = new GlobalAppApis().AEPSWithdraw(MemberId,ip,lat,lag,mob,adhar,amt,innon,xmldata,tp);
        Log.d("AEPSWithdraw","cxc"+otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_AEPSWithdraw(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.v("AEPSWithdraw11","cxc"+result);
                try {
                    JSONObject aobject = new JSONObject(result);
                    if (aobject.getString("msg").equalsIgnoreCase("Success")) {
                        JSONObject jsonObject1 = aobject.getJSONObject("Response");
//                        for (int i = 0; i < Response.length(); i++) {
//                            JSONObject jsonObject = Response.getJSONObject(i);
                        AlertDialog.Builder builder = new AlertDialog.Builder(KYC_AEPS.this);
                        builder.setTitle("API Response:-")
                                .setMessage(jsonObject1.getString("message"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }});
                        builder.create().show();
//                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(KYC_AEPS.this);
                        builder.setTitle("Error!!  Response:-")
                                .setMessage(aobject.getString("msg"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }});
                        builder.create().show();
                        Toast.makeText(KYC_AEPS.this,aobject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, KYC_AEPS.this, call1, "", true);
    }



 //***************Morpho Mantra MINI Statement Response***********
    public void AEPSMiniStatementAPI(String MemberId,String ip,String lat,String lag,String mob,String adhar,String innon,String xmldata,String tp) {
        String otp1 = new GlobalAppApis().AEPSMiniStatement(MemberId,ip,lat,lag,mob,adhar,innon,xmldata,tp);
        Log.d("reswallwetotp1mini","cxc"+otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_AEPSMiniStatement(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("AEPSMiniStatement","cxc"+result);
                try {
                    JSONObject aobject = new JSONObject(result);
                    if (aobject.getString("msg").equalsIgnoreCase("Success")) {
                        JSONObject jsonObject1 = aobject.getJSONObject("Response");
                        AlertDialog.Builder builder11 = new AlertDialog.Builder(KYC_AEPS.this);
                        builder11.setTitle("Response:-")
                                .setMessage(jsonObject1.getString("message"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }});
                        builder11.create().show();
                        JSONArray jsonArray = jsonObject1.getJSONArray("ministatement");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjecta = jsonArray.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("date", jsonObjecta.getString("date"));
                            hashlist.put("amount", jsonObjecta.getString("amount"));
                            hashlist.put("txnType", jsonObjecta.getString("txnType"));
                            hashlist.put("narration", jsonObjecta.getString("narration"));
                            arrFriendsList.add(hashlist);
                        }
                        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        //then we will inflate the custom alert dialog xml that we created
                        View dialogView = LayoutInflater.from(KYC_AEPS.this).inflate(R.layout.ministatement, viewGroup, false);
                        Button exit = (Button) dialogView.findViewById(R.id.exit);
                        rcvList = dialogView.findViewById(R.id.rcvList);
                        TextView mess_tv = dialogView.findViewById(R.id.mess_tv);
                        TextView bal_tv = dialogView.findViewById(R.id.bal_tv);
                        TextView dat_tv = dialogView.findViewById(R.id.dat_tv);

                        mess_tv.setText(jsonObject1.getString("message"));
                        bal_tv.setText(jsonObject1.getString("balanceamount"));
                        dat_tv.setText(jsonObject1.getString("datetime"));

                        exit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog2.dismiss();
                            }
                        });
                        //Now we need an AlertDialog.Builder object
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(KYC_AEPS.this);
                        //setting the view of the builder to our custom view that we already inflated
                        builder.setView(dialogView);
                        //finally creating the alert dialog and displaying it
                        alertDialog2 = builder.create();
                        alertDialog2.show();
//                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(KYC_AEPS.this);
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
                        Toast.makeText(KYC_AEPS.this,aobject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, KYC_AEPS.this, call1, "", true);
    }

//***************Morpho Mantra MiniStatement Adapter***********
    public class FriendsListAdapter extends RecyclerView.Adapter<FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_ministatement, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final FriendsList holder, final int position) {
            holder.tv.setText(String.valueOf(" "+(position+1)));
            holder.tv1.setText(data.get(position).get("amount"));
            holder.tv2.setText(data.get(position).get("txnType"));
            holder.tv4.setText(data.get(position).get("date"));
            holder.tv5.setText(data.get(position).get("narration"));
        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView tv,tv1,tv2,tv4,tv5;
        public FriendsList(View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
            tv4=itemView.findViewById(R.id.tv4);
            tv5=itemView.findViewById(R.id.tv5);
            tv=itemView.findViewById(R.id.tv);
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

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(KYC_AEPS.this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }


//*************** Mantra fingerprint Response Convert XML***********
    public static class XMLUtils {
        public static String convertStringToXmlString(String inputString) {
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                // Set output properties to preserve indentation and formatting
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                // Create the input source from the modified input string
                String modifiedInputString = inputString.replace("<br />", "");
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

