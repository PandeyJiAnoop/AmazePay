package com.akp.amazepay.rechargebills;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.akp.amazepay.R;
import com.android.volley.AuthFailureError;
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
import java.util.List;
import java.util.Map;

public class WithdrawHistory extends AppCompatActivity {
    ImageView back_img;
    LinearLayout dynamic_ll;
    String Url = "GetProjectList";
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    RecyclerView wallet_histroy;
//    TextView txt_nodata;
    String Id;
    SwipeRefreshLayout srl_refresh;
    private SharedPreferences sharedPreferences;
    String UserId;
    TextView title_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_history);
       GetId();
        getHistory();

//        swipe to refresh function call here
        srl_refresh = findViewById(R.id.srl_refresh);
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(WithdrawHistory.this)) {
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
                    Toast.makeText(WithdrawHistory.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                } }});

    }

    private void GetId() {
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("userid", "");
        back_img=findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wallet_histroy = findViewById(R.id.wallet_histroy);
//        txt_nodata = findViewById(R.id.txt_nodata);
        title_tv=findViewById(R.id.title_tv);

    }

    //    Report API implemented history
    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(WithdrawHistory.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://amazepay.net/api/Amazepay/ViewRechargeHistory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ViewRechargeHistory",response);
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    title_tv.setText("Recharge History("+jsonArrayr.length()+")");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("MemberId", jsonObject1.getString("MemberId"));
                        hm.put("MemberName", jsonObject1.getString("MemberName"));
                        hm.put("MobileNo", jsonObject1.getString("MobileNo"));
                        hm.put("OrderId", jsonObject1.getString("OrderId"));
                        hm.put("RechargeDate", jsonObject1.getString("RechargeDate"));
                        hm.put("Amount", jsonObject1.getString("Amount"));
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(WithdrawHistory.this, 1);
                    WithdrawHistoryAdapter customerListAdapter = new WithdrawHistoryAdapter(WithdrawHistory.this, arrayList);
                    wallet_histroy.setLayoutManager(gridLayoutManager);
                    wallet_histroy.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show(); }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MemberId", UserId);
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(WithdrawHistory.this);
        requestQueue.add(stringRequest);
    }


//    Adapter class here added
    public class WithdrawHistoryAdapter extends RecyclerView.Adapter<WithdrawHistoryAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public WithdrawHistoryAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.withdraw_transactions, viewGroup, false);
            VH viewHolder = new VH(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull VH vh, int i) {
            vh.tv1.setText("#ODR"+arrayList.get(i).get("OrderId"));
            vh.tv.setText("Mobile Recharge on +91-"+arrayList.get(i).get("MobileNo")+" \nDate :- "+arrayList.get(i).get("RechargeDate"));
            vh.tv2.setText("Rs. "+arrayList.get(i).get("Amount"));

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView tv,tv1,tv2;
            public VH(@NonNull View itemView) {
                super(itemView);
                tv=itemView.findViewById(R.id.tv);
                tv1=itemView.findViewById(R.id.tv1);
                tv2=itemView.findViewById(R.id.tv2); }
        }}
}