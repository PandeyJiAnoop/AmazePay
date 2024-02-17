package com.akp.amazepay;
/**
 * Created by Anoop Pandey on 9696381023.
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.amazepay.Das_KYCBlock.KYC_NSDL;
import com.akp.amazepay.Fundfolder.Fund_Request;
import com.akp.amazepay.Fundfolder.Fund_Request_Report;
import com.akp.amazepay.Fundfolder.Fund_Wallet_Report;
import com.akp.amazepay.SliderMenu.AddFundReport;
import com.akp.amazepay.SliderMenu.ExternalLink;
import com.akp.amazepay.SliderMenu.FAQ;
import com.akp.amazepay.SliderMenu.FeedBackForm;
import com.akp.amazepay.SliderMenu.MyWallet;
import com.akp.amazepay.SliderMenu.PrivacyPolicy;
import com.akp.amazepay.SliderMenu.ServiceWallet;
import com.akp.amazepay.TransationFolder.Transaction_AEPS;
import com.akp.amazepay.TransationFolder.Transaction_DMT;
import com.akp.amazepay.TransationFolder.Transaction_DMT1;
import com.akp.amazepay.Util.Preferences;
import com.akp.amazepay.Webpage.Web_RechargeReport;
import com.akp.amazepay.fragments.HomeFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;


public class dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener  {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    BottomNavigationView navigation;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Context context;
    Preferences pref;
    TextView profile_ll,logout_ll;
    TextView txt_username,txt_usergmail;
    private String UserId,U_name;
    CircleImageView civ_profile_image;
    ImageView logo_img;
    private DrawerLayout mDrawer;
    TextView add_fund_report_ll,my_wallet_ll,service_history_ll,faq_ll,privacy_policy_ll,refer_ll;
    ImageView fb,telegram,youtube,watsap;
    ImageView help_img;

    LinearLayout trans_report_ll,hide_trans_ll,hide_aeps_fund_ll;
    TextView aeps_tv,dmt_tv,dmt1_tv,fund_req_tv,fund_req_report_tv,wallet_report_tv,aeps_fund_ll,external_ll,recharge_report_ll,pass_ll;
    int i = 0;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        U_name= sharedPreferences.getString("U_name", "");

        findViewByIds();
        toolbar = findViewById(R.id.toolbar);
        context=this.getApplicationContext();
        pref =new Preferences(context);
        setSupportActionBar(toolbar);
        //toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawatv_MyOfferble.ic_menu));
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigation = (BottomNavigationView) findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(this);
        HomeFragment homeFragment = new HomeFragment();
        loadFragment(homeFragment);
        navigation.getItemIconTintList();
        navigation.setSelectedItemId(R.id.bnav_Home);

        verifyStoragePermission(dashboard.this);
//        on click event and permisson call here
//        contactUs();
        navigationItemClicks();
        //  isStoragePermissionGranted();
        checkAndRequestPermissions();

//        Code to get checksum code
        String input = UserId+":ip6wgncgt6:nxe7j2f6c0";
        String strup = input.toUpperCase();
        String checksum = generateMD5Checksum(strup);
        Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);

//        Profile dahsbord api call
        GetDashboard(UserId);
        if(UserId == null){
            txt_username.setText("Welcome Guest");
        }
        else {
            txt_username.setText("Welcome, "+U_name);
        }
        txt_usergmail.setText("UserId-"+UserId);
    }

    private void findViewByIds() {
        txt_username = findViewById(R.id.txt_username);
        txt_usergmail = findViewById(R.id.txt_usergmail);
        mDrawer=findViewById(R.id.drawer_layout);
        logout_ll=findViewById(R.id.logout_ll);
        profile_ll=findViewById(R.id.profile_ll);
        civ_profile_image=findViewById(R.id.civ_profile_image);
        logo_img=findViewById(R.id.logo_img);

        add_fund_report_ll=findViewById(R.id.add_fund_report_ll);
        my_wallet_ll=findViewById(R.id.my_wallet_ll);
        service_history_ll=findViewById(R.id.service_history_ll);
        faq_ll=findViewById(R.id.faq_ll);
        privacy_policy_ll=findViewById(R.id.privacy_policy_ll);
        refer_ll=findViewById(R.id.refer_ll);
        fb=findViewById(R.id.fb);
        telegram=findViewById(R.id.telegram);
        youtube=findViewById(R.id.youtube);
        watsap=findViewById(R.id.whatsap);
        help_img=findViewById(R.id.help_img);

        external_ll=findViewById(R.id.external_ll);
        recharge_report_ll=findViewById(R.id.recharge_report_ll);
        trans_report_ll=findViewById(R.id.trans_report_ll);
        aeps_fund_ll=findViewById(R.id.aeps_fund_ll);
        hide_trans_ll=findViewById(R.id.hide_trans_ll);
        hide_aeps_fund_ll=findViewById(R.id.hide_aeps_fund_ll);

        aeps_tv=findViewById(R.id.aeps_tv);
        dmt_tv=findViewById(R.id.dmt_tv);
        dmt1_tv=findViewById(R.id.dmt1_tv);
        fund_req_tv=findViewById(R.id.fund_req_tv);
        fund_req_report_tv=findViewById(R.id.fund_req_report_tv);
        wallet_report_tv=findViewById(R.id.wallet_report_tv);
        pass_ll=findViewById(R.id.pass_ll);
        service_history_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    hide_trans_ll.setVisibility(View.GONE);
                    i++;
                } else if (i == 1) {
                    hide_trans_ll.setVisibility(View.VISIBLE);
                    i = 0;
                }
            }
        });
        aeps_fund_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    hide_aeps_fund_ll.setVisibility(View.GONE);
                    i++;
                } else if (i == 1) {
                    hide_aeps_fund_ll.setVisibility(View.VISIBLE);
                    i = 0;
                }
            }
        });
        aeps_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Transaction_AEPS.class);
                startActivity(intent);
            }
        });
        dmt_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Transaction_DMT1.class);
                startActivity(intent);
            }
        });

        dmt1_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Transaction_DMT.class);
                startActivity(intent);
            }
        });

        fund_req_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AddFundReport.class);
                startActivity(intent);
            }
        });
        fund_req_report_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Fund_Request_Report.class);
                startActivity(intent);
            }
        });
        wallet_report_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Fund_Wallet_Report.class);
                startActivity(intent);
            }
        });
        external_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ExternalLink.class);
                startActivity(intent);
            }
        });
        if (ContextCompat.checkSelfPermission(dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            getLocation();
        } else {
            // Permission is not granted
            requestLocationPermission();
        }

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebookIntent(dashboard.this, ""); mDrawer.closeDrawer(Gravity.START);
            }});
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMessageTelegram("Welcome To AmazePay"); mDrawer.closeDrawer(Gravity.START);
            }});
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { mDrawer.closeDrawer(Gravity.START);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/"));
                intent.setPackage("com.google.android.youtube");
                PackageManager manager = getPackageManager();
                List<ResolveInfo> info = manager.queryIntentActivities(intent, 0);
                if (info.size() > 0) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"No Application Installed!",Toast.LENGTH_LONG).show();
                    //No Application can handle your intent
                } }});
        watsap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp("","Welcome To AmazePay"); mDrawer.closeDrawer(Gravity.START); }});


        help_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getApplicationContext(), FeedBackForm.class); startActivity(intent);
            }});
        profile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getApplicationContext(),user_profile.class); startActivity(intent);
                mDrawer.closeDrawer(Gravity.START);
            }});
        add_fund_report_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getApplicationContext(), AddFundReport.class); startActivity(intent);
                mDrawer.closeDrawer(Gravity.START);
            }});
        my_wallet_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getApplicationContext(), MyWallet.class); startActivity(intent);
                mDrawer.closeDrawer(Gravity.START);
            }});

        recharge_report_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getApplicationContext(), Web_RechargeReport.class); startActivity(intent);
                mDrawer.closeDrawer(Gravity.START);
            }});
        faq_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getApplicationContext(), FAQ.class); startActivity(intent);
                mDrawer.closeDrawer(Gravity.START);
            }});
        privacy_policy_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent=new Intent(getApplicationContext(), PrivacyPolicy.class); startActivity(intent);
                mDrawer.closeDrawer(Gravity.START);
            }});
        refer_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { takeScreenShot(logo_img); mDrawer.closeDrawer(Gravity.START);
            }});

        pass_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ChangePassword.class); startActivity(intent);

            mDrawer.closeDrawer(Gravity.START);
            }});


    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.bnav_Home);
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        //transaction.addToBackStack(null);
        fragmentManager.popBackStack();
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu_owner, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(dashboard.this);
        builder.setMessage(Html.fromHtml("<font color='#000'>Are you sure want to Exit From App?</font>"));
        builder.setCancelable(false);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
             finishAffinity();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setBackgroundColor(Color.BLACK);
        nbutton.setTextColor(Color.WHITE);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.RED);
        pbutton.setTextColor(Color.WHITE);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;
        switch (menuItem.getItemId()) {
            case R.id.bnav_Home:
                HomeFragment homeFragment = new HomeFragment();
                loadFragment(homeFragment);
                break;
            case R.id.bnav_History:
                startActivity(new Intent(dashboard.this, dashboard.class));
                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    void navigationItemClicks() {
        logout_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(dashboard.this);
                builder.setMessage(Html.fromHtml("<font color='#000'>Are you sure want to Logout?</font>"));
                builder.setCancelable(false);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LogoutAPI();

                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setBackgroundColor(Color.BLACK);
                nbutton.setTextColor(Color.WHITE);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setBackgroundColor(Color.RED);
                pbutton.setTextColor(Color.WHITE);
            }
        });
    }

    private void LogoutAPI() {
        String otp1 = new GlobalAppApis().Logout(UserId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_Logout(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Message");
                    if (status.equalsIgnoreCase("Success")) {
                        SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), splashscrn.class);
                        startActivity(intent);
                    } else {
                        SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), splashscrn.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            } }, dashboard.this, call1, "", true);
    }

    private boolean checkAndRequestPermissions() {
        int phone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (phone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

//    public boolean isStoragePermissionGranted() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v("TAG", "Permission is granted");
//                return true;
//            } else {
//                Log.v("TAG", "Permission is revoked");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                return false;
//            }
//        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v("TAG", "Permission is granted");
//            return true;
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0]);
//            //resume tasks needing this permission
//        }
//    }




    public void GetDashboard(String user_id) {
        String otp1 = new GlobalAppApis().Dashboard(user_id);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_dashboard(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String phone       = object.getString("phone");
                        String aeps_balance       = object.getString("aeps_balance");
                        String wallet_balance       = object.getString("wallet_balance");
                        if(UserId == null){
                            txt_username.setText("Welcome Guest");
                        }
                        else {
                            txt_username.setText("Welcome, "+UserId);
                        }
                        txt_usergmail.setText("+91- "+phone);
                        String msg       = object.getString("message");
                    } } catch (JSONException e) {
                    e.printStackTrace(); } } }, dashboard.this, call1, "", true); }



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





















    private void takeScreenShot(View view) {
        Date date = new Date();
        CharSequence format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date);

        try {
            File mainDir = new File(
                    this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FilShare");
            if (!mainDir.exists()) {
                boolean mkdir = mainDir.mkdir();
            }

            String path = mainDir + "/" + "TrendOceans" + "-" + format + ".jpeg";
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            shareScreenShot(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Share ScreenShot
    private void shareScreenShot(File imageFile) {
        Uri uri =  FileProvider.getUriForFile(getApplicationContext(), "com.akp.amazepay.provider", imageFile);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, "Download AmazePay From PlayStore \n https://play.google.com/store/apps/details?id=com.akp.amazepay" +"\n User MY REFERRAL CODE :- "+UserId);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            this.startActivity(Intent.createChooser(intent, "Share With"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    //Permissions Check
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } }

//Facebook Intent akp
    public static void openFacebookIntent(Context context, String facebookID) {
        Intent intent;
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/facebookID"));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/facebookID"));
        }context.startActivity(intent);
    }

//Whatsap intent akp
    private void openWhatsApp(String numero,String mensaje){
        try{
            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else {
                Toast.makeText(getApplicationContext(),"Whatsapp Not Installed!",Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            Log.e("ERROR WHATSAPP",e.toString());
            Toast.makeText(getApplicationContext(),"Whatsapp Not Installed!",Toast.LENGTH_LONG).show();
        } }

//Telegram Intent akp
    void intentMessageTelegram(String msg)
    {
        final String appName = "org.telegram.messenger";
        final boolean isAppInstalled = isAppAvailable(getApplicationContext(), appName);
        if (isAppInstalled)
        { Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            myIntent.setPackage(appName);
            myIntent.putExtra(Intent.EXTRA_TEXT, msg);//
            startActivity(Intent.createChooser(myIntent, "Share with"));
        }
        else { Toast.makeText(getApplicationContext(), "Telegram not Installed", Toast.LENGTH_SHORT).show(); }
    }
    public static boolean isAppAvailable(Context context, String appName)
    { PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES); return true;
        } catch (PackageManager.NameNotFoundException e)
        { return false; } }
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
                            ActivityCompat.requestPermissions(dashboard.this,
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
    private void getLocation() {
        // Code to get the location goes here
        Toast.makeText(this, "Location is being retrieved", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            } }}
}
