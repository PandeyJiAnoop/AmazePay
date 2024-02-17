package com.akp.amazepay;
/**
 * Created by Anoop Pandey on 9696381023.
 */
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CompanyProfile extends AppCompatActivity {
TextView company_name,address,website;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        findViewById(R.id.menuImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findView();
        getData();

    }
    public void findView(){
        company_name=findViewById(R.id.company_name);
        address=findViewById(R.id.address);
        website=findViewById(R.id.website);
//        mobile=findViewById(R.id.mobile);
//        et_Name=findViewById(R.id.et_Name);
//        tv_mob_No=findViewById(R.id.tv_mob_No);
//        gender=findViewById(R.id.gender);
//        tv_qualification=findViewById(R.id.tv_qualification);
    }

    public void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"CompanyDetail", new  Response.Listener<String>() {
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
                    if (object.getString("company_name").equalsIgnoreCase("") || object.getString("company_name") == null)
                        company_name.setText("N/A");
                    else
                        company_name.setText(object.getString("company_name"));

                    if (object.getString("Address").equalsIgnoreCase("") || object.getString("Address") == null)
                        address.setText("N/A");
                    else
                        address.setText(object.getString("Address"));

                    if (object.getString("Website").equalsIgnoreCase("") || object.getString("Website") == null)
                        website.setText("N/A");
                    else
                        website.setText(object.getString("Website"));

//                    if (object.getString("mobile").equalsIgnoreCase("") || object.getString("mobile") == null)
//                        tv_mob_No.setText("N/A");
//                    else
//                        tv_mob_No.setText(object.getString("mobile"));


//                    spname.setText(object.getString("sponsorname"));
//                    agentname.setText(object.getString("agentname"));
//                    mobile.setText(object.getString("mobile"));
//                    gender.setText(object.getString("gender"));
//                    gender.setText(object.getString("gender"));
//                    tv_qualification.setText(object.getString("Qualification"));
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
                Toast.makeText(CompanyProfile.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Accept","application/json");
                params.put("Content-Type","application/json");
//                params.put("userid","2021321");

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

        RequestQueue requestQueue = Volley.newRequestQueue(CompanyProfile.this);
        requestQueue.add(stringRequest);

    }
}