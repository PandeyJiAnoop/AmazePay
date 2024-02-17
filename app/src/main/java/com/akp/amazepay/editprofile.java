package com.akp.amazepay;
/**
 * Created by Anoop Pandey on 9696381023.
 */
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.akp.amazepay.Util.Api_Urls;
import com.akp.amazepay.Util.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class editprofile extends AppCompatActivity {

    EditText spname,et_sponsername,et_address,et_ifsccode,et_bankname,et_acctype,et_accno,et_nomiage,et_nomrelation,et_nominee,et_adharcard,et_email,et_pancard,et_mobile,et_state,et_city,et_dob,et_fname,et_gender,agentname,gender,qualif,father,dob,add,pancard,city,mobile,et_Name;
    TextView tv_mob_No,tv_qualification,txt_userName,txt_userId,txt_email;
    Button btn_update;
    Context context;
    Preferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        context=this.getApplicationContext();
        pref=new Preferences(context);
        findView();
        getData();
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findView(){
        et_sponsername=findViewById(R.id.et_sponsername);
        et_gender=findViewById(R.id.et_gender);
        et_fname=findViewById(R.id.et_fname);
        et_dob=findViewById(R.id.et_dob);
        et_address=findViewById(R.id.et_address);
        et_city=findViewById(R.id.et_city);
        et_state=findViewById(R.id.et_state);
        et_mobile=findViewById(R.id.et_mobile);
        et_email=findViewById(R.id.et_email);
        et_pancard=findViewById(R.id.et_pancard);
        et_adharcard=findViewById(R.id.et_adharcard);
        et_nominee=findViewById(R.id.et_nominee);
        et_nomrelation=findViewById(R.id.et_nomrelation);
        et_nomiage=findViewById(R.id.et_nomiage);
        et_accno=findViewById(R.id.et_accno);
        et_bankname=findViewById(R.id.et_bankname);
        et_ifsccode=findViewById(R.id.et_ifsccode);
        txt_userName=findViewById(R.id.txt_userName);
        txt_userId=findViewById(R.id.txt_userId);
        txt_email=findViewById(R.id.txt_email);
        btn_update=findViewById(R.id.btn_update);
    }

    public void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"GetProfile", new  Response.Listener<String>() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                Log.d("test",jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
//                    JSONArray Jarray  = object.getJSONArray("Response");

                    // JSONArray jsonarray = new JSONArray(jsonString);
//                    if (object.getString("agentname").equalsIgnoreCase("") || object.getString("agentname") == null)
//                        et_Name.setText("N/A");
//                    else
//                        et_Name.setText(object.getString("agentname"));
//                    if (object.getString("mobile").equalsIgnoreCase("") || object.getString("mobile") == null)
//                        tv_mob_No.setText("N/A");
//                    else
//                        tv_mob_No.setText(object.getString("mobile"));
                    et_sponsername.setText(object.getString("sponsorname"));
                    et_gender.setText(object.getString("gender"));
                    et_fname.setText(object.getString("fathername"));
                    et_dob.setText(object.getString("dob"));
                    et_city.setText(object.getString("city"));
                    et_state.setText(object.getString("state"));
                    et_mobile.setText(object.getString("mobile"));
                    et_email.setText(object.getString("email"));
                    et_pancard.setText(object.getString("pancardno"));
                    et_adharcard.setText(object.getString("AadhaarNo"));
                    et_nominee.setText(object.getString("NomineeName"));
                    et_nomiage.setText(object.getString("NomineeAge"));
                    et_nomrelation.setText(object.getString("NomineeRelation"));
                    et_accno.setText(object.getString("accountno"));
                    et_bankname.setText(object.getString("bankname"));
                    et_ifsccode.setText(object.getString("ifsccode"));
                    txt_userName.setText(object.getString("agentname"));
                    txt_userId.setText(object.getString("mobile"));
                    txt_email.setText(object.getString("email"));
//
                    //                    TextView.setText(jsonObject1.getString("username"));
//                    JSONObject jsonObj = jsonObject.getJSONObject("data");
//                    JSONArray jsonArray = jsonObj.getJSONArray("bussiness_income_list");
//                    TextView.setText(jsonArray.getString(1));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                Log.d("myTag", "message:"+error);
                Toast.makeText(editprofile.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Accept","application/json");
                params.put("Content-Type","application/json");
                params.put("userid","2021321");
//                 params.put("user_id","107");
//                 params.put("maembertype", "Level");
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(editprofile.this);
        requestQueue.add(stringRequest);

    }


    public void update() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"EditProfile", new  Response.Listener<String>() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                Log.d("test",jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
//                    JSONArray Jarray  = object.getJSONArray("Response");
                    // JSONArray jsonarray = new JSONArray(jsonString);
//                    if (object.getString("agentname").equalsIgnoreCase("") || object.getString("agentname") == null)
//                        et_Name.setText("N/A");
//                    else
//                        et_Name.setText(object.getString("agentname"));
//                    if (object.getString("mobile").equalsIgnoreCase("") || object.getString("mobile") == null)
//                        tv_mob_No.setText("N/A");
//                    else
//                        tv_mob_No.setText(object.getString("mobile"));

                    Toast.makeText(editprofile.this,object.getString("Msg") , Toast.LENGTH_SHORT).show();
//                    et_sponsername.setText(c);
//                    et_gender.setText(object.getString("gender"));
//                    et_fname.setText(object.getString("fathername"));
//                    et_dob.setText(object.getString("dob"));
//                    et_city.setText(object.getString("city"));
//                    et_state.setText(object.getString("state"));
//                    et_mobile.setText(object.getString("mobile"));
//                    et_email.setText(object.getString("email"));
//                    et_pancard.setText(object.getString("pancardno"));
//                    et_adharcard.setText(object.getString("AadhaarNo"));
//                    et_nominee.setText(object.getString("NomineeName"));
//                    et_nomrelation.setText(object.getString("NomineeRelation"));
//                    et_accno.setText(object.getString("accountno"));
//                    et_bankname.setText(object.getString("bankname"));
//                    et_ifsccode.setText(object.getString("ifsccode"));
//                    txt_userName.setText(object.getString("agentname"));
//                    txt_userId.setText(object.getString("mobile"));
//                    txt_email.setText(object.getString("email"));

//                    TextView.setText(jsonObject1.getString("username"));
//                    JSONObject jsonObj = jsonObject.getJSONObject("data");
//                    JSONArray jsonArray = jsonObj.getJSONArray("bussiness_income_list");
//                    TextView.setText(jsonArray.getString(1));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                Log.d("myTag", "message:"+error);
                Toast.makeText(editprofile.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Accept","application/json");
                params.put("Content-Type","application/json");
                params.put("SponsorId","");
                params.put("agentid","123");
                params.put("gender",et_email.getText().toString());
                params.put("agentname","");
                params.put("gender",et_gender.getText().toString());
                params.put("fathername",et_fname.getText().toString());
                params.put("Relation","");
                params.put("DOB",et_dob.getText().toString());
                params.put("address",et_city.getText().toString());
                params.put("mobile",et_mobile.getText().toString());
                params.put("email",et_email.getText().toString());
                params.put("pancardno",et_pancard.getText().toString());
                params.put("bankname",et_bankname.getText().toString());
                params.put("accountno",et_accno.getText().toString());
                params.put("city",et_city.getText().toString());
                params.put("state",et_state.getText().toString());
                params.put("ifsccode",et_ifsccode.getText().toString());
                params.put("adate","");
                params.put("password","");
                params.put("Passbook","");
                params.put("IdPrrof","");
                params.put("PanCard","");
                params.put("NomName",et_nominee.getText().toString());
                params.put("NomAge",et_nomiage.getText().toString());
                params.put("NomRelation",et_nominee.getText().toString());
                params.put("AadhaarNo",et_adharcard.getText().toString());
                params.put("AccHolderName","");
                params.put("Photo","");
                params.put("rankpost","");
                params.put("CommPer","");
//                 params.put("user_id","107");
//                 params.put("maembertype", "Level");
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(editprofile.this);
        requestQueue.add(stringRequest);

    }


}