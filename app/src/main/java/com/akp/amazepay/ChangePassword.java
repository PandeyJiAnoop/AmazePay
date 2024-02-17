package com.akp.amazepay;
/**
 * Created by Anoop Pandey on 9696381023.
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.amazepay.Das_KYCBlock.KYC_AEPS;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.akp.amazepay.Util.Api_Urls;
import com.akp.amazepay.Util.Preferences;

import org.androidannotations.annotations.App;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class ChangePassword extends AppCompatActivity {
    EditText edt_new_pass;
    private EditText edt_old_pass,edt_conf_pass;
    private AppCompatButton btn_sendotp,t_btn_sendotp;
    String UserId;
    private EditText t_edt_new_pass,t_edt_old_pass,t_edt_conf_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");

        t_edt_new_pass=(EditText)findViewById(R.id.t_edt_new_pass);
        t_edt_old_pass=(EditText)findViewById(R.id.t_edt_old_pass);
        t_edt_conf_pass=(EditText)findViewById(R.id.t_edt_conf_pass);

        edt_new_pass=(EditText)findViewById(R.id.edt_new_pass);
        edt_old_pass=(EditText)findViewById(R.id.edt_old_pass);
        edt_conf_pass=(EditText)findViewById(R.id.edt_conf_pass);
        btn_sendotp=(AppCompatButton) findViewById(R.id.btn_sendotp);
        t_btn_sendotp=(AppCompatButton) findViewById(R.id.t_btn_sendotp);
        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_old_pass.getText().toString().equalsIgnoreCase("")){
                    edt_old_pass.setError("Fields can't be blank!");
                    edt_old_pass.requestFocus();
                }
                else if (edt_new_pass.getText().toString().equalsIgnoreCase("")){
                    edt_new_pass.setError("Fields can't be blank!");
                    edt_new_pass.requestFocus();
                }
                else if (edt_conf_pass.getText().toString().equalsIgnoreCase("")){
                    edt_conf_pass.setError("Fields can't be blank!");
                    edt_conf_pass.requestFocus();
                }
               else if(!edt_new_pass.getText().toString().equals(edt_conf_pass.getText().toString())){
                    //Toast is the pop up message
                    Toast.makeText(getApplicationContext(), "Password Not matched!", Toast.LENGTH_LONG).show();
                }
                else {
                    changePassword(UserId,t_edt_old_pass.getText().toString(),t_edt_new_pass.getText().toString());
                    // Toast.makeText(getApplicationContext(),"Password Changed Successfully!",Toast.LENGTH_LONG).show();
                }
            }
        });

        t_btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t_edt_old_pass.getText().toString().equalsIgnoreCase("")){
                    t_edt_old_pass.setError("Fields can't be blank!");
                    t_edt_old_pass.requestFocus();
                }
                else if (t_edt_new_pass.getText().toString().equalsIgnoreCase("")){
                    t_edt_new_pass.setError("Fields can't be blank!");
                    t_edt_new_pass.requestFocus();
                }
                else if (t_edt_conf_pass.getText().toString().equalsIgnoreCase("")){
                    t_edt_conf_pass.setError("Fields can't be blank!");
                    t_edt_conf_pass.requestFocus();
                }
                else if(!t_edt_new_pass.getText().toString().equals(t_edt_conf_pass.getText().toString())){
                    //Toast is the pop up message
                    Toast.makeText(getApplicationContext(), "Password Not matched!", Toast.LENGTH_LONG).show();
                }
                else {
                    T_changePassword(UserId,t_edt_old_pass.getText().toString(),t_edt_new_pass.getText().toString());
                    // Toast.makeText(getApplicationContext(),"Password Changed Successfully!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public void changePassword(String MemberId,String oldp,String newp) {
        String otp1 = new GlobalAppApis().ChangePass(MemberId,oldp,newp);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_ChangePassword(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("API_ChangePassword","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Message");
                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder11 = new AlertDialog.Builder(ChangePassword.this);
                            builder11.setTitle("Response:-")
                                    .setMessage(jsonObject.getString("Msg"))
                                    .setCancelable(false)
                                    .setIcon(R.drawable.logo)
                                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                            overridePendingTransition(0, 0);
                                            startActivity(getIntent());
                                            overridePendingTransition(0, 0);
                                            dialog.cancel();
                                        }
                                    });
                            builder11.create().show();
                        }
                    }
                    else {
                        AlertDialog.Builder builder11 = new AlertDialog.Builder(ChangePassword.this);
                        builder11.setTitle("Response:-")
                                .setMessage(object.getString("Message"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        dialog.cancel();
                                    }
                                });
                        builder11.create().show();
                        Toast.makeText(getApplicationContext(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, ChangePassword.this, call1, "", true);
    }


    public void T_changePassword(String t_MemberId,String t_oldp,String t_newp) {
        String otp1 = new GlobalAppApis().T_ChangePass(t_MemberId,t_oldp,t_newp);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_ChangeTpin(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("API_ChangeTpin","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Message");
                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder11 = new AlertDialog.Builder(ChangePassword.this);
                            builder11.setTitle("Response:-")
                                    .setMessage(jsonObject.getString("Msg"))
                                    .setCancelable(false)
                                    .setIcon(R.drawable.logo)
                                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                            overridePendingTransition(0, 0);
                                            startActivity(getIntent());
                                            overridePendingTransition(0, 0);
                                            dialog.cancel();
                                        }
                                    });
                            builder11.create().show();
                        }
                    }
                    else {
                        AlertDialog.Builder builder11 = new AlertDialog.Builder(ChangePassword.this);
                        builder11.setTitle("Response:-")
                                .setMessage(object.getString("Message"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        dialog.cancel();
                                    }
                                });
                        builder11.create().show();
                        Toast.makeText(getApplicationContext(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, ChangePassword.this, call1, "", true);
    }
}