package com.akp.amazepay.Das_BillPaymnet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.amazepay.R;
import com.akp.amazepay.dashboard;
import com.akp.amazepay.rechargebills.InsuranceBill;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost1;

public class MobileRecharge extends AppCompatActivity {
    ImageView ivBack;
    LinearLayout provider_ll;
    TextView provoider_et;
    String service_name,UserId;
    String getProvider_id,roletype;
    Button btnSubmit;
    private SharedPreferences sharedPreferences;
    EditText mob_et,amount_et;
    AlertDialog alertDialog;
    String get_operator_ref,get_payid,getonlyservice;
    TextView title_tv,mob_code_tv;
    TextView txtMarquee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge);
        title_tv=findViewById(R.id.title_tv);
        mob_code_tv=findViewById(R.id.mob_code_tv);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("userid", "");
        roletype=getIntent().getStringExtra("RoleType");

        service_name=getIntent().getStringExtra("servicename");
        getProvider_id=getIntent().getStringExtra("provider_id");
        getonlyservice=getIntent().getStringExtra("onlyservice");


        btnSubmit=findViewById(R.id.btnSubmit);

        // casting of textview
        txtMarquee = (TextView) findViewById(R.id.marqueeText);
        // Now we will call setSelected() method
        // and pass boolean value as true
        txtMarquee.setSelected(true);

//        Toast.makeText(getApplicationContext(),getonlyservice,Toast.LENGTH_LONG).show();

        ivBack=findViewById(R.id.ivBack);
        provider_ll=findViewById(R.id.provider_ll);
        provoider_et=findViewById(R.id.provoider_et);
        mob_et=findViewById(R.id.mob_et);
        amount_et=findViewById(R.id.amount_et);
        title_tv.setText(getonlyservice+" Service");


        if (getonlyservice.equalsIgnoreCase("MOBILE RECHARGE")){
            mob_et.setHint("Enter your mobile number");
            mob_code_tv.setVisibility(View.VISIBLE);
            mob_et.setInputType(InputType.TYPE_CLASS_NUMBER);
            mob_et.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(10) });

        }
        else {
            mob_et.setHint("Enter your Unique Id Number");
            mob_code_tv.setVisibility(View.GONE);
        }

        provoider_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getonlyservice.equalsIgnoreCase("MOBILE RECHARGE")){
                    Intent intent=new Intent(getApplicationContext(),Mobile.class);
                    startActivity(intent);
                }
                else if (getonlyservice.equalsIgnoreCase("DTH")){
                    Intent intent=new Intent(getApplicationContext(),DTH.class);
                    startActivity(intent);
                }
               else if (getonlyservice.equalsIgnoreCase("ELECTRICITY")){
                    Intent intent=new Intent(getApplicationContext(),Electricity.class);
                    startActivity(intent);
                }
                else if (getonlyservice.equalsIgnoreCase("Insurance")){
                    Intent intent=new Intent(getApplicationContext(), InsuranceBill.class);
                    startActivity(intent);
                }
               else  if (getonlyservice.equalsIgnoreCase("WATER")){
                    Intent intent=new Intent(getApplicationContext(),Water.class);
                    startActivity(intent);
                }




            }
        });


        if(service_name ==null){

        }
        else {
            provoider_et.setText(service_name);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getProvider_id == null){
                }
                else {

                    Log.d("sdghsfg",""+UserId+amount_et.getText().toString()+mob_et.getText().toString()+getProvider_id);
                    Payment(UserId,amount_et.getText().toString(),mob_et.getText().toString(),getProvider_id);
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });


    }

    public void PAYMENTAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(MobileRecharge.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://msc.mscpaycash.com/api/telecom/v1/payment?api_token=98bUAIRRHAH1klt3Nr5hk2hRSQQqAv60uiezDnXOw980C9vTTzVWjSAvdPZk&number="+mob_et.getText().toString()+"&amount="+amount_et.getText().toString()+"&provider_id=9"+"&client_id="+UserId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")){
                        get_operator_ref=jsonObject.getString("operator_ref");
                        get_payid=jsonObject.getString("payid");
                        showpopupwindow();
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MobileRecharge.this);
        requestQueue.add(stringRequest);
    }

    private void showpopupwindow() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(MobileRecharge.this).inflate(R.layout.successfullycreated_popup, viewGroup, false);
        Button ok = (Button) dialogView.findViewById(R.id.btnDialog);
        TextView txt_msg=dialogView.findViewById(R.id.txt_msg);
        TextView pass_tv= dialogView.findViewById(R.id.pass_tv);
        txt_msg.setText("Operator_ref- "+get_operator_ref+" ("+UserId+")");
        pass_tv.setText("Payid- "+get_payid);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //Now we need an AlertDialog.Builder object
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }





    private void Payment(String UserId,String amount,String number, String provider_id) {
        String otp1 = new GlobalAppApis().PostRechargeAPI(UserId,amount,number,provider_id);
        ApiService client1 = getApiClient_ByPost1();
        Call<String> call1 = client1.PostRecharge(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("Status").equalsIgnoreCase("true")){
                        get_operator_ref=jsonObject.getString("operator_ref");
                        get_payid=jsonObject.getString("payid");
                        showpopupwindow();
                        Toast.makeText(getApplicationContext(),jsonObject.getString("Message"),Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, MobileRecharge.this, call1, "", true);

    }


}