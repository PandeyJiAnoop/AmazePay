package com.akp.amazepay.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.amazepay.AEPSWebview;
import com.akp.amazepay.Das_AccountOpening.AO_AxisBank;
import com.akp.amazepay.Das_AccountOpening.AO_HDFCBank;
import com.akp.amazepay.Das_AccountOpening.AO_JupitarBank;
import com.akp.amazepay.Das_AccountOpening.AO_kotakBank;
import com.akp.amazepay.Das_BillPaymnet.MobileRecharge;
import com.akp.amazepay.Das_FindLostDocuments.FL_AadharPrint;
import com.akp.amazepay.Das_FindLostDocuments.FL_AyushmanPrint;
import com.akp.amazepay.Das_FindLostDocuments.FL_DLPrint;
import com.akp.amazepay.Das_FindLostDocuments.FL_FindPanByAdhar;
import com.akp.amazepay.Das_GovermentServices.GS_UDTDCard;
import com.akp.amazepay.Das_GovermentServices.GS_ViklankPension;
import com.akp.amazepay.Das_GovermentServices.GS_VridhaPension;
import com.akp.amazepay.Das_GovermentServices.GS_WidowPension;
import com.akp.amazepay.Das_KYCBlock.KYC_AEPS;
import com.akp.amazepay.Das_KYCBlock.KYC_NSDL;
import com.akp.amazepay.Das_KYCBlock.KYC_UTI;
import com.akp.amazepay.Fundfolder.Fund_Request;
import com.akp.amazepay.Helper.NetworkConnectionHelper;
import com.akp.amazepay.MoneyTransfer.DMTService;
import com.akp.amazepay.SliderMenu.AddFundReport;
import com.akp.amazepay.SliderMenu.MyWallet;
import com.akp.amazepay.Webpage.Web_BillPayment;
import com.akp.amazepay.Webpage.Web_DTHPayment;
import com.akp.amazepay.Webpage.Web_GasPayment;
import com.akp.amazepay.Webpage.Web_InsurancePayment;
import com.akp.amazepay.Webpage.Web_Recharge;
import com.akp.amazepay.dashboard;
import com.akp.amazepay.rechargebills.DTHBill;
import com.akp.amazepay.rechargebills.ElectricityBill;
import com.akp.amazepay.rechargebills.GasBill;
import com.akp.amazepay.rechargebills.InsuranceBill;
import com.akp.amazepay.rechargebills.InsuranceBillNew;
import com.akp.amazepay.rechargebills.New_MobileRecharge;
import com.akp.amazepay.user_profile;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.akp.amazepay.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.netpaisa.aepsriseinlib.AepsRiseinActivity;
import com.netpaisa.aepsriseinlib.location.GetLocation;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;
/**
 * Created by Anoop Pandey on 9696381023.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    Context context;
    SwipeRefreshLayout srl_refresh;
    Activity activity;
    private String mParam1,mParam2;
    private SliderLayout sliderLayout;
    String UserId;
    TextView apis_bal_tv,wallet_bal_tv;

    LinearLayout nsdl_kyc_ll,nsdl_ll,aeps_ll,uti_ll,gaspay_ll;
    LinearLayout mobile_ll,dth_ll,electricity_ll,water_ll;
    LinearLayout aadhar_print_ll,ayushman_print_ll,findpanbyaadhar_ll,dl_print_ll;
    LinearLayout axis_bank_ll,hdfc_bank_ll,jupitar_bank_ll,kotak_bank_ll;
    LinearLayout vridha_pension_ll,viklank_pension_ll,widow_pension_ll,utdt_card_ll;
    private TextView mob_tv;
    private String U_name;
    LinearLayout dmt_ll;  int AEPS_REQUEST_CODE = 10923;
    private Dialog alertDialog2;

    LinearLayout insurance_ll;



String XMLDATA="<?xml version=&quot;1.0&quot;?><PidData><Resp errCode=&quot;0&quot; errInfo=&quot;&quot; fCount=&quot;1&quot; fType=&quot;2&quot; iCount=&quot;&quot; pCount=&quot;&quot; pgCount=&quot;&quot; pTimeout=&quot;&quot; nmPoints=&quot;19&quot; qScore=&quot;27\n" +
        "&quot;/><DeviceInfo dpId=&quot;Morpho.SmartChip&quot; rdsId=&quot;SCPL.WIN.001&quot; rdsVer=&quot;1.0.10&quot; dc=&quot;1548810c-2c46-4160-88ca-f92bacc57863&quot; mi=&quot;MSO1300E3L0SW&quot; mc=&quot;MIIEGzCCAwOgAwIBAgIGAYfVnQfaMA0GCSqGSIb3DQEBCwUAMIG4MSEwHwYDVQQDDBhEUyBTTUFSVCBDSElQIFBWVCBMVEQgMTkxHjAcBgNVBDMTFVBMT1QgTk8gMSBBIFNFQ1RPUiA3MzEOMAwGA1UECQwFTk9JREExFjAUBgNVBAgMDVVUVEFSIFBSQURFU0gxITAfBgNVBAsMGERJR0lUQUwgU0VSVklDRSBERUxJVkVSWTEbMBkGA1UECgwSU01BUlQgQ0hJUCBQVlQgTFREMQswCQYDVQQGEwJJTjAeFw0yMzA1MDEwNDQwNTBaFw0yMzA1MzEwNDQwNTBaMIHFMRQwEgYDVQQKDAtNQVJQSE9SRFBPQzEMMAoGA1UECwwDRFNBMTEwLwYJKoZIhvcNAQkBFiJwYW5rYWouYWdhcndhbEBzbWFydGNoaXBvbmxpbmUuY29tMQ4wDAYDVQQHDAVOb2lkYTEWMBQGA1UECAwNVXR0YXIgUHJhZGVzaDELMAkGA1UEBhMCaW4xNzA1BgNVBAMMLnJkX2RldmljZV8xNTQ4ODEwYy0yYzQ2LTQxNjAtODhjYS1mOTJiYWNjNTc4NjMwggEhMA0GCSqGSIb3DQEBAQUAA4IBDgAwggEJAoIBANHIEnpMRt+Sijv+lv+lx6/eixOikLfuXPxoSYjFR0xpDcO3piVV1PEOOA1Z7W6IRLD9HX5EhJ8AqV538tDiRgAU7CV52CfYbeB2XJpjFfCz2Fzdh2Nn9woHmXPgzk+oKd2to+DEcVZcBhzqEIrciFHbzMpNa6g4cjM4qn8l5JcoSDmdGAEedJeJed7PfF9l8xrBd48Wm4MP5Tu5vbyqeMwVQ0bavp8STHOwHtjC66Oz/I2d9C2ZCzQOaU2svMWQShfZjJpstoCiZtLej330XKP+y+Dg8DuZXT3XU3gptl8o44bZh3Xcq1Vj4VI5MKgGfaJnlqwik3kf4TxkhVzCruECAwEAAaMdMBswDAYDVR0TBAUwAwEB/zALBgNVHQ8EBAMCAYYwDQYJKoZIhvcNAQELBQADggEBALOeHJV0yczuwccatcqF7EhD0NhrLaGlcFj4rmKM1d13AIq6YbGwkaEApdmdt9vxqKWvGeTqKZ6Po9FFUnZEVO2en7Ty/a3UY6XRpd9r5uM7vI5XYF5X18PD4saqg1fI12iT/e8Wnn5zYZ+vEPRTnDQ0CTepcncUr4WRN5An8YqvX3Ja8qiI4kpcMvhmUuoHKYpOJzVFlTagm+Zs77B8GxUMP4a6nLJi7FB/ZeXJMJS8CZgLU3SNnLiuHaZuXYGLWdH4dwYxmG0Rf+FbNgBN9xh+QbxgamAdVzzT2PElqFaXAno/S1Y4XckYfh8EZa6pFze1ztGaiLvbYVc1YHBboVY=&quot;><additional_info><Param name=&quot;srno&quot; value=&quot;1804I013245&quot;/></additional_info></DeviceInfo>\n" +
        "<Skey ci=&quot;20221021&quot;>FacNY07TbdE0k84aewZHl04YNJiTgFG7S2Db7oRpRI3X8l+P1dlPg0o5VpKJo4AF84gOO7jQ5rKqsAdRhrkmVHewSFDtyawgSKm87/Cg66M++aetkNE0IkjwjhOJCNWfvosS4DpvhJlx7hZ0xMTJHKgndBByluFCYL0hKPj2tyeYWEab0BmlBtKQW2cCgXcn8jOFdkxn25jpeQL6iR0Nu9mG+5CRkqa20sFrgfd9QQLIm9jTZPOv6WGOSfKeHuZnj7ujfauVnILYY/EwkX6TqWOx7PxQIryg1PqQD0wCVwqp0rA2eNhqlWJb2VzpLvxrYEGe6c6+2rOC0c2xVhZ31w==</Skey><Hmac>R31ER/Vru6Nz6+0Q1GWSPzDpScjrSqdtN54eOkuQBmKu4VN0NBwWjXrbeqVZynDz</Hmac><Data type=&quot;X&quot;>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</Data></PidData>";
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context=this.getActivity();
        sliderLayout = view.findViewById(R.id.imageSlider);
        BottomNavigationView navigation = (BottomNavigationView)view.findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        findViewByIds(view);
        onClickListeners();

        return view;
    }
    ///
    private void findViewByIds(View view) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        U_name= sharedPreferences.getString("U_name", "");
        wallet_bal_tv = view.findViewById(R.id.wallet_bal_tv);
        apis_bal_tv = view.findViewById(R.id.apis_bal_tv);
        srl_refresh = view.findViewById(R.id.srl_refresh);
        //forBanner();
        setSliderViews();
        String input = UserId+":ip6wgncgt6:nxe7j2f6c0";
        String strup = input.toUpperCase();
        String checksum = generateMD5Checksum(strup);
        Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);


        mob_tv=view.findViewById(R.id.mob_tv);
        nsdl_kyc_ll=view.findViewById(R.id.nsdl_kyc_ll);
        nsdl_ll=view.findViewById(R.id.nsdl_ll);
        aeps_ll=view.findViewById(R.id.aeps_ll);
        uti_ll=view.findViewById(R.id.uti_ll);
        dmt_ll=view.findViewById(R.id.dmt_ll);

        insurance_ll=view.findViewById(R.id.insurance_ll);


        if (U_name.equalsIgnoreCase("")){
        }
        else {
            mob_tv.setText(U_name+"("+UserId+")");
        }


        dmt_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), DMTService.class); startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        nsdl_kyc_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(context).inflate(R.layout.commingsoon_popup, viewGroup, false);
                Button exit = (Button) dialogView.findViewById(R.id.exit);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog2.dismiss();
                    }
                });
                //Now we need an AlertDialog.Builder object
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);
                //finally creating the alert dialog and displaying it
                alertDialog2 = builder.create();
                alertDialog2.show();



//                String packageName = "protean.assisted.ekyc";
//                String className = "protean.assisted.ekyc.MainActivity";
//                Intent intent = new Intent();
//                intent.setClassName(packageName, className);
//                startActivity(intent);


//                launch("protean.assisted.ekyc","protean.assisted.ekyc.MainActivity");
            }});
        nsdl_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), KYC_NSDL.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});

        insurance_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), InsuranceBillNew.class);
                intent.putExtra("onlyservice", "Insurance");
                startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});
        aeps_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusCheck();
//             yha se live code anoop
//
//                try {
//                    String pidOption = getPIDOptions();
//                    /*String pidOption = "<PidOptions ver=\"2.0\">" +
//                            "   <Opts env=\"S\" fCount=\"1\" fType=\"2\" format=\"0\" iCount=\"0\" iType=\"0\" otp=\"1234\" wadh=\"Hello\" pCount=\"0\" pType=\"0\" pidVer=\"2.0\" posh=\"UNKNOWN\" timeout=\"10000\"/>" +
//                            "   <Demo lang=\"05\">" +
//                            "   <Pi ms=\"P\" mv=\"Jigar Shekh\" name=\"Jigar\" lname=\"Shekh\" lmv=\"\" gender=\"M\" dob=\"\" dobt=\"V\" age=\"24\" phone=\"\" email=\"\"/>" +
//                            "   <Pa ms=\"E\" co=\"\" house=\"\" street=\"\" lm=\"\" loc=\"\"" +
//                            "  vtc=\"\" subdist=\"\" dist=\"\" state=\"\" country=\"\" pc=\"\" po=\"\"/>" +
//                            "   <Pfa ms=\"E|P\" mv=\"\" av=\"\" lav=\"\" lmv=\"\"/>" +
//                            "   </Demo>" +
//                            "</PidOptions>";*/
//
//                    /*String pidOption = "<PidOptions ver=\"2.0\">" +
//                            "   <Opts env=\"S\" fCount=\"1\" fType=\"2\" format=\"0\" iCount=\"0\" iType=\"0\" pCount=\"0\" pType=\"0\" pidVer=\"2.0\" posh=\"UNKNOWN\" timeout=\"10000\"/>" +
//                            "   <Demo lang=\"05\">" +
//                            "   <Pi ms=\"P\" mv=\"100\" name=\"Mahesh\" gender=\"M\"/>" +
//                            "   </Demo>" +
//                            "</PidOptions>";*/
//                    if (pidOption != null) {
//                        Log.e("PidOptions", pidOption);
//                        Intent intent2 = new Intent();
//                        intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
//
//                        //pass arg value as device rd servie Package name
//			/*if(arg.equals("com.scl.rdservice")){
//				intent2.setPackage("com.scl.rdservice");
//			}else if(arg.equals("com.mantra.rdservice")){
//				intent2.setPackage("com.mantra.rdservice");
//			}else if(arg.equals("com.precision.pb510.rdservice")){
//				intent2.setPackage("com.precision.pb510.rdservice");
//			}else if(arg.equals("com.secugen.rdservice")){
//				intent2.setPackage("com.secugen.rdservice");
//			}else if(arg.equals("com.nextbiometrics.rdservice")){
//				intent2.setPackage("com.nextbiometrics.rdservice");
//			}else if(arg.equals("com.acpl.registersdk")){
//			  intent2.setPackage("com.acpl.registersdk");
//			}else if(arg.equals("co.aratek.asix_gms.rdservice")){
//			  	intent2.setPackage("co.aratek.asix_gms.rdservice");
//			}
//			*/
//                        intent2.putExtra("PID_OPTIONS", pidOption);
//                        startActivityForResult(intent2, 2);
//                    }
//                } catch (Exception e) {
//                    Log.e("Error", e.toString());
//                }
//
                //             yha tak bs live code anoop


                /*     Intent intentTrans = new Intent(context, AepsRiseinActivity.class);
                intentTrans.putExtra("api_access_key", "UFMwMDQzNWFlMWFiNTYwZjg0OWZjMDZkNTdjNzljZDM1ZWNjOWU=");
                intentTrans.putExtra("outletid", UserId);
                intentTrans.putExtra("authorized_key", "YzdhMWQyYjkxZDgwNjg5ZmEwMzI3ZTlhMWY0N2I1YjU=");
                intentTrans.putExtra("pan_no", "BHBPT1041H");
                intentTrans.putExtra("app_label", "Aeps One");
                intentTrans.putExtra("primary_color", R.color.colorPrimary);
                intentTrans.putExtra("accent_color", R.color.colorAccent);
                intentTrans.putExtra("primary_dark_color", R.color.colorPrimaryDark);
                intentTrans.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentTrans);*/
//                Intent intentTrans = new Intent(context, AepsRiseinActivity.class);
//                intentTrans.putExtra("api_access_key", "YzdhMWQyYjkxZDgwNjg5ZmEwMzI3ZTlhMWY0N2I1YjU=");
//                intentTrans.putExtra("outletid", "PS0043");
//                intentTrans.putExtra("pan_no", "EBYPS5209E");
//                intentTrans.putExtra("app_label", "AmazePay");
//                intentTrans.putExtra("primary_color", R.color.colorPrimary);
//                intentTrans.putExtra("accent_color", R.color.colorAccent);
//                intentTrans.putExtra("primary_dark_color", R.color.colorPrimaryDark);
//                intentTrans.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intentTrans);
//                Intent intent=new Intent(getActivity(), AEPSWebview.class); startActivity(intent);
            }});
        uti_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(context).inflate(R.layout.commingsoon_popup, viewGroup, false);
                Button exit = (Button) dialogView.findViewById(R.id.exit);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog2.dismiss();
                    }
                });
                //Now we need an AlertDialog.Builder object
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);
                //finally creating the alert dialog and displaying it
                alertDialog2 = builder.create();
                alertDialog2.show();
//                Intent intent=new Intent(getActivity(), KYC_UTI.class); startActivity(intent);
//                Intent intent=new Intent(getActivity(), KYC_UTI.class); startActivity(intent);
            }});

        mobile_ll=view.findViewById(R.id.mobile_ll);
        dth_ll=view.findViewById(R.id.dth_ll);
        electricity_ll=view.findViewById(R.id.electricity_ll);
        water_ll=view.findViewById(R.id.water_ll);
        gaspay_ll=view.findViewById(R.id.gaspay_ll);

        mobile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), New_MobileRecharge.class);
                intent.putExtra("onlyservice", "Mobile");
                startActivity(intent);
            /*    Intent intent=new Intent(getActivity(), Web_Recharge.class);
//                intent.putExtra("onlyservice","MOBILE RECHARGE");
                startActivity(intent); */
                getActivity().overridePendingTransition(0, 0);
            }});
        dth_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Intent intent=new Intent(getActivity(), Web_DTHPayment.class);
//                intent.putExtra("onlyservice","DTH");
                startActivity(intent); */
                Intent intent = new Intent(getActivity(), DTHBill.class);
                intent.putExtra("onlyservice", "DTH");
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }});

        electricity_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent=new Intent(getActivity(), Web_BillPayment.class);
//                intent.putExtra("onlyservice","ELECTRICITY");
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);*/
                Intent intent = new Intent(getActivity(), ElectricityBill.class);
                intent.putExtra("onlyservice", "ELECTRICITY");
                startActivity(intent);
            }});
        water_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InsuranceBill.class);
                intent.putExtra("onlyservice", "LIC");
                startActivity(intent);
            }});

        gaspay_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GasBill.class);
                intent.putExtra("onlyservice", "Gas");
                startActivity(intent);
              /*  //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(context).inflate(R.layout.commingsoon_popup, viewGroup, false);
                Button exit = (Button) dialogView.findViewById(R.id.exit);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog2.dismiss();
                    }
                });
                //Now we need an AlertDialog.Builder object
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);
                //finally creating the alert dialog and displaying it
                alertDialog2 = builder.create();
                alertDialog2.show();
               *//* Intent intent=new Intent(getActivity(), Web_GasPayment.class);
//                intent.putExtra("onlyservice","WATER");
                startActivity(intent);*/
            }});
        aadhar_print_ll=view.findViewById(R.id.aadhar_print_ll);
        ayushman_print_ll=view.findViewById(R.id.ayushman_print_ll);
        findpanbyaadhar_ll=view.findViewById(R.id.findpanbyaadhar_ll);
        dl_print_ll=view.findViewById(R.id.dl_print_ll);
        aadhar_print_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), FL_AadharPrint.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});
        ayushman_print_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), FL_AyushmanPrint.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});
        findpanbyaadhar_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), FL_FindPanByAdhar.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});
        dl_print_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), FL_DLPrint.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});


        axis_bank_ll=view.findViewById(R.id.axis_bank_ll);
        hdfc_bank_ll=view.findViewById(R.id.hdfc_bank_ll);
        jupitar_bank_ll=view.findViewById(R.id.jupitar_bank_ll);
        kotak_bank_ll=view.findViewById(R.id.kotak_bank_ll);
        axis_bank_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), AO_AxisBank.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});
        hdfc_bank_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), AO_HDFCBank.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});
        jupitar_bank_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent = new Intent(getActivity(), AO_JupitarBank.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});
        kotak_bank_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent = new Intent(getActivity(), AO_kotakBank.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});


        vridha_pension_ll=view.findViewById(R.id.vridha_pension_ll);
        widow_pension_ll=view.findViewById(R.id.widow_pension_ll);
        viklank_pension_ll=view.findViewById(R.id.viklank_pension_ll);
        utdt_card_ll=view.findViewById(R.id.utdt_card_ll);
        vridha_pension_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), GS_VridhaPension.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});
        widow_pension_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), GS_WidowPension.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});
        viklank_pension_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), GS_ViklankPension.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});
        utdt_card_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getActivity(), GS_UDTDCard.class); startActivity(intent); getActivity().overridePendingTransition(0, 0);
            }});

        GetDashboard(UserId);
    }
    private void onClickListeners() {
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(getActivity())) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activity.finish();
                            activity.overridePendingTransition(0, 0);
                            startActivity(activity.getIntent());
                            activity.overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(getActivity(), "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }});


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_add:
//                    Toast.makeText(getActivity(),"Coming Soon!",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getContext(), AddFundReport.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_me:
                    Intent intent1=new Intent(getContext(), user_profile.class);
                    startActivity(intent1); getActivity().overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        }
    };
    private void setSliderViews() {
        for (int i = 0; i <= 4; i++) {
            SliderView sliderView = new SliderView(context);
            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.ban_1);
                    sliderView.setDescription("Welcome To\n" + "AmazePay");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.ban_2);
//                    sliderView.setDescription("सच होगा सपना");
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.ban_3);
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.ban_4);
                    break;
                case 4:
                    sliderView.setImageDrawable(R.drawable.ban_5);
                    break;
            }
            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(context, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });
            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }


    public void GetDashboard(String MemberId) {
        String otp1 = new GlobalAppApis().Dashboard(MemberId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_dashboard(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("reswallwet","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Message");
                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            String WalletBalance=jsonObject.getString("AepsWallet");
                            Log.d("WalletBalance","cxc"+WalletBalance);
                            wallet_bal_tv.setText("\u20B9 "+jsonObject.getString("AepsWallet"));
                        }
                    } else {
                        Toast.makeText(getActivity(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), call1, "", true);
    }

    public static String generateMD5Checksum(String input) {
        try {
            // Create an MD5 MessageDigest instance
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Convert the input string to bytes and feed it to the MessageDigest instance
            md.update(input.getBytes());
            // Generate the MD5 checksum
            byte[] digest = md.digest();
            // Convert the byte array to a hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void launch(String packageName, String className) {
        if (context == null || packageName == null || className == null) return;
        ComponentName component = new ComponentName(packageName, className);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(component);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Log.v(TAG, e.toString());
            Toast.makeText(getActivity(),"Package Not Found!!\n Install Apk Firstly",Toast.LENGTH_LONG).show();
            Log.d("dhfghfghfh", e.toString());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=protean.assisted.ekyc")));

        }

    }



/*    public void AEPSBankBalanceAPI(String MemberId,String xmldata) {
        String otp1 = new GlobalAppApis().AEPSBankBalancedata(MemberId,xmldata);
        Log.d("reswallwetotp1","cxc"+otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_AEPSBankBalance(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                try {
                    JSONObject aobject = new JSONObject(result);
                    if (aobject.getString("msg").equalsIgnoreCase("Success")) {
                        JSONObject jsonObject1 = aobject.getJSONObject("Response");
//                        for (int i = 0; i < Response.length(); i++) {
//                            JSONObject jsonObject = Response.getJSONObject(i);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("API Response:-")
                                    .setMessage(jsonObject1.getString("message")+"\n\nResponse:- "+jsonObject1)
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                        Toast.makeText(context,aobject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), call1, "", true);
    }*/

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
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("DEVICE_INFO");
                            String rdService = data.getStringExtra("RD_SERVICE_INFO");
                            String display = "";
                            if (rdService != null) {
                                display = "RD Service Info :\n" + rdService + "\n\n";
                            }
                            if (result != null) {
                                /*DeviceInfo info = serializer.read(DeviceInfo.class, result);
                                display = display + "Device Code: " + info.dc + "\n\n"
                                        + "Serial No: " + info.srno + "\n\n"
                                        + "dpId: " + info.dpId + "\n\n"
                                        + "MC: " + info.mc + "\n\n"
                                        + "MI: " + info.mi + "\n\n"
                                        + "rdsId: " + info.rdsId + "\n\n"
                                        + "rdsVer: " + info.rdsVer;*/
                                display += "Device Info :\n" + result;
//                                setText(display);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze device info", e);
                    }
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                Log.d("responseresult",result);
//                                AEPSBankBalanceAPI(UserId,result);
//                                Toast.makeText(getContext(),"XML Result:----->     \n"+result,Toast.LENGTH_LONG).show();//                                pidData = serializer.read(PidData.class, result);
//                                setText(result);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
                    }
                }
                break;
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
        else {
            Intent intent=new Intent(getActivity(), KYC_AEPS.class);
            startActivity(intent); getActivity().overridePendingTransition(0, 0);        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
}

