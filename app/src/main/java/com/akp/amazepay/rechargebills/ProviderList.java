package com.akp.amazepay.rechargebills;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.akp.amazepay.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProviderList extends AppCompatActivity {
    ImageView back_img;
    LinearLayout dynamic_ll;
    String Url = "GetProjectList";
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    RecyclerView wallet_histroy;
    TextView txt_nodata;
    String Id;
    SwipeRefreshLayout srl_refresh;
    private SharedPreferences sharedPreferences;
    String UserId;
    private String get_type;
    //    LinearLayout view_details_ll,show_view_details_ll,show_history_ll,dynamic_ll;
//    TextView amount_tv;
//    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_list);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("userid", "");
        get_type=getIntent().getStringExtra("type");
        back_img=findViewById(R.id.back_img);
//        dynamic_ll=findViewById(R.id.dynamic_ll);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wallet_histroy = findViewById(R.id.wallet_histroy);
        srl_refresh = findViewById(R.id.srl_refresh);

        getHistory();

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(ProviderList.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(ProviderList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });

      /*  for (int i=0;i<4;i++){
            GetProjectList();
        }*/
    }



    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(ProviderList.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://amazepay.net/api/Amazepay/GetOperator", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("REsponse_Data", response);
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("Name", jsonObject1.getString("Name"));
                        hm.put("Code", jsonObject1.getString("Code"));
                        arrayList.add(hm);

                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ProviderList.this, 1);
                    ProviderListAdapter customerListAdapter = new ProviderListAdapter(ProviderList.this, arrayList);
                    wallet_histroy.setLayoutManager(gridLayoutManager);
                    wallet_histroy.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong!", Toast.LENGTH_LONG).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ProviderList.this);
        requestQueue.add(stringRequest);
    }
}