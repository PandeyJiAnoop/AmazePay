package com.akp.amazepay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class OTPVerify extends AppCompatActivity {
    TextView tvMobileNo, tvResend;
    //EditText
    EditText etCode1, etCode2, etCode3, etCode4,etCode5,etCode6;
    int check = 0;
    TextView tvTimerText,tvOtp;
    //Button
    Button btnSubmit;
    String Checksum,GetMobile,GetOtp;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    RelativeLayout main_ll;
    TextView tvResendOtp;
    CountDownTimer startTimer;
    String otp;
    ImageView fb,telegram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verify);

        FindId();
        OnClick();

    }

    private void OnClick() {
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp("7068236202","Welcome To AmazePay");
            }
        });
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.google.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        etCode1.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) etCode2.requestFocus();
            }
        } );

        etCode2.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) etCode3.requestFocus();
            }
        } );

        etCode3.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) etCode4.requestFocus();
            }
        } );

        etCode4.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() != 0) etCode5.requestFocus();
            }
        } );
//        etCode5.addTextChangedListener( new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() != 0) etCode6.requestFocus();
//            }
//        } );
//
//        etCode6.addTextChangedListener( new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//        } );

        etCode1.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode1.getText().toString().trim().isEmpty()) {
                        etCode1.requestFocus();
                    } }
                return false;
            }
        } );

        etCode2.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode2.getText().toString().trim().isEmpty()) {
                        etCode1.requestFocus();
                    } }
                return false;
            }
        } );
        etCode3.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode3.getText().toString().trim().isEmpty()) {
                        etCode2.requestFocus();
                    } }
                return false;
            }
        } );

        etCode4.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode4.getText().toString().trim().isEmpty()) {
                        etCode3.requestFocus();
                    } }
                return false;
            }
        } );
//        etCode5.setOnKeyListener( new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                // You can identify which key pressed buy checking keyCode value
//                // with KeyEvent.KEYCODE_
//                if (keyCode == KeyEvent.KEYCODE_DEL) {
//                    // this is for backspace
//                    if (etCode5.getText().toString().trim().isEmpty()) {
//                        etCode4.requestFocus();
//                    }
//                }
//                return false;
//            }
//        } );
//
//        etCode6.setOnKeyListener( new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                // You can identify which key pressed buy checking keyCode value
//                // with KeyEvent.KEYCODE_
//                if (keyCode == KeyEvent.KEYCODE_DEL) {
//                    // this is for backspace
//                    if (etCode6.getText().toString().trim().isEmpty()) {
//                        etCode5.requestFocus();
//                    }
//                }
//                return false;
//            }
//        } );

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = etCode1.getText().toString().trim() +
                        etCode2.getText().toString().trim() +
                        etCode3.getText().toString().trim() +
                        etCode4.getText().toString().trim();
                if (otp.length() != 4) {
                    AppUtils.showToastSort( getApplicationContext(),"Enter Valid OTP");
                } else {
                    OtpverifyAPI(GetMobile,GetOtp);
                }} });
    }

    private void FindId() {
        tvResendOtp=findViewById(R.id.tvResendOtp);
        Checksum = getIntent().getStringExtra("checksum");
        GetMobile = getIntent().getStringExtra("send_mob");
        GetOtp = getIntent().getStringExtra("send_otp");
//        ReferralCode = getIntent().getStringExtra("ref");
//            Toast.makeText(getApplicationContext(),GetOtp,Toast.LENGTH_LONG).show();
        main_ll=findViewById(R.id.main_rl);

        //Button
        btnSubmit = findViewById( R.id.btnSubmit );
        fb = findViewById( R.id.fb );
        telegram = findViewById( R.id.telegram );

        //EditText
        etCode1 = findViewById(R.id.etCode1);
        etCode2 = findViewById(R.id.etCode2);
        etCode3 = findViewById(R.id.etCode3);
        etCode4 = findViewById(R.id.etCode4);
//        etCode5 = findViewById( R.id.etCode5 );
//        etCode6 = findViewById( R.id.etCode6 );
        tvOtp = findViewById( R.id.tvOtp );
        tvMobileNo = findViewById( R.id.tvMobileNo );
        tvOtp.setText("Your Otp " + Checksum);
        tvMobileNo.setText("User Id:-  " +GetMobile );

        startTimer();
    }


    public void OtpverifyAPI(String MemberId, String Otp) {
        String otp1 = new GlobalAppApis().VerifyOtp(MemberId,Otp);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_verifyOtp(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("resotp","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            Intent intent = new Intent(getApplicationContext(), dashboard.class);
                            login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                            login_editor = login_preference.edit();
                            login_editor.putString("U_id",jsonObject.getString("UserName"));
                            login_editor.putString("U_name",jsonObject.getString("Name"));
                            login_editor.putString("U_mob",jsonObject.getString("MobileNo"));
                            login_editor.putString("MasterPassword",jsonObject.getString("MasterPassword"));

                            login_editor.commit();
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, OTPVerify.this, call1, "", true);
    }

    private void startTimer() {
        startTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                tvResendOtp.setText(String.format("( %02d SEC LEFT)", sec));
                if(sec == 1)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvResendOtp.setText("( 00 SEC LEFT)");
                        }
                    }, 1000);
                }
            }
            public void onFinish() {
                tvResendOtp.setText("Resend OTP");
                tvResendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String input = Userid+":ip6wgncgt6:nxe7j2f6c0";
//                        String strup = input.toUpperCase();
//                        String checksum = generateMD5Checksum(strup);
//                        Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);
//                        ResendAPI(Userid,checksum);

//                        if (ReferralCode == null){
//                            ResendOtp("");
//                        }
//                        else {
//                            ResendOtp(ReferralCode);
//                        }
                    }
                });
            }
        }.start();
    }

    public void ResendAPI(String user_id,String checksum) {
        String otp1 = new GlobalAppApis().ResendOtp(user_id,checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_resendOtp(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg    = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String msg   = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        Snackbar.make(main_ll, object.getString("message"), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, OTPVerify.this, call1, "", true);
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