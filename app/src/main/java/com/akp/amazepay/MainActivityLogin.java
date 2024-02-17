package com.akp.amazepay;
/**
 * Created by Anoop Pandey on 9696381023.
 */
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.amazepay.RegisterModule.RegisterMobile;
import com.akp.amazepay.RegisterModule.SignupForm;
import com.android.volley.RequestQueue;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class MainActivityLogin extends AppCompatActivity  {
    AppCompatButton login_btn,reg_btn;
    EditText mob_et,pass_et;
    RelativeLayout main_rl;
    String checksum;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    private RequestQueue requestQueue1;// Declare
    ImageView fb,telegram;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findID();
        OnClickEvent();
        fb = findViewById( R.id.fb );
        telegram = findViewById( R.id.telegram );
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp("7068236202","Welcome To AmazePay");
            }
        });
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.eazypan.in/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });



    }

    private void OnClickEvent() {
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }
                if (pass_et.getText().toString().equalsIgnoreCase("")){
                    pass_et.setError("Fields can't be blank!");
                    pass_et.requestFocus();
                }
                else {
                    String input = mob_et.getText().toString()+":ip6wgncgt6:nxe7j2f6c0";
                    String strup = input.toUpperCase();
                    checksum = generateMD5Checksum(strup);
                    Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);

//                    Toast.makeText(LoginScreen.this, "Enter passcode-  12345 ", Toast.LENGTH_SHORT).show();



                    LoginScreenAPI(mob_et.getText().toString().trim(),pass_et.getText().toString());
                }
            }
        });


        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SignupForm.class);
                startActivity(intent);
            }
        });
    }

    private void findID() {
        login_btn=findViewById(R.id.login_btn);
        reg_btn=findViewById(R.id.reg_btn);
        mob_et=findViewById(R.id.mob_et);
        pass_et=findViewById(R.id.pass_et);
        main_rl=findViewById(R.id.main_rl);
    }


    public void LoginScreenAPI(String MemberId, String Password) {
        String otp1 = new GlobalAppApis().Login(MemberId,Password);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_login(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("dfhfdhdhdlogin","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            String msg=jsonObject.getString("msg");
                            if (msg.equalsIgnoreCase("Otp send successfully.")){
                                Toast.makeText(MainActivityLogin.this, "Verify Your OTP!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), OTPVerify.class);
                                intent.putExtra("send_otp",jsonObject.getString("OTP"));
                                intent.putExtra("send_mob",mob_et.getText().toString());
                                startActivity(intent);
                            }
                            if (msg.equalsIgnoreCase("Login Success")){
                                login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                                login_editor = login_preference.edit();
                                login_editor.putString("U_id",jsonObject.getString("UserName"));
                                login_editor.putString("U_name",jsonObject.getString("Name"));
                                login_editor.putString("U_mob",jsonObject.getString("MobileNo"));
                                login_editor.commit();
                                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, MainActivityLogin.this, call1, "", true);
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

}