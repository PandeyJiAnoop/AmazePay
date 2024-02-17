package com.akp.amazepay.SliderMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.amazepay.Helper.AnimationHelper;
import com.akp.amazepay.Helper.NetworkConnectionHelper;
import com.akp.amazepay.R;
import com.akp.amazepay.Util.Api_Urls;
import com.akp.amazepay.Util.Constants;
import com.akp.amazepay.Util.Preferences;
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

public class CustomerVisit extends AppCompatActivity {
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String UserId;
    SwipeRefreshLayout srl_refresh;
    ImageView sliding_menu;
    ImageView norecord_tv;
    Context context;
    Preferences pref;
    NotificationListAdapter customerListAdapter;
    SearchView et_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_visit);
        context=this.getApplicationContext();
        pref=new Preferences(context);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        srl_refresh=findViewById(R.id.srl_refresh);
        norecord_tv=findViewById(R.id.norecord_tv);


        et_search=findViewById(R.id.search);
        sliding_menu=findViewById(R.id.menuImg);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);

        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(CustomerVisit.this)) {
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
                    Toast.makeText(CustomerVisit.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        getHistory("");

        et_search.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 2) {
                    arrayList1.clear();
                    customerListAdapter.notifyDataSetChanged();
                    getHistory(newText);
                }
                else {
                    arrayList1.clear();
                    customerListAdapter.notifyDataSetChanged();
                    getHistory("");
                }
                return true;
            }
        });


//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                arrayList1.clear();
//                customerListAdapter.notifyDataSetChanged();
////reset adapter with empty array list (it did the trick animation)
//                getHistory(search_et.getText().toString());
//            }
//        });

    }
    public void getHistory(String ser) {
        final ProgressDialog progressDialog = new ProgressDialog(CustomerVisit.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"ViewFollowupHistory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resssss",response);
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("flag");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        cust_recyclerView.setVisibility(View.VISIBLE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("entryby", jsonObject.getString("entryby"));
                            hashlist.put("Description", jsonObject.getString("Description"));
                            hashlist.put("CustomerName", jsonObject.getString("CustomerName"));
                            hashlist.put("FollowupDate", jsonObject.getString("FollowupDate"));
                            hashlist.put("NextFollowupDate", jsonObject.getString("NextFollowupDate"));
                            hashlist.put("LeadFollowBy", jsonObject.getString("LeadFollowBy"));
                            hashlist.put("VisitStatus", jsonObject.getString("VisitStatus"));
                            hashlist.put("CustMobile", jsonObject.getString("CustMobile"));
                            arrayList1.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(CustomerVisit .this, 1);
                        customerListAdapter = new NotificationListAdapter(CustomerVisit.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        cust_recyclerView.setVisibility(View.GONE);
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(CustomerVisit.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
//                Toast.makeText(CustomerVisit.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("Followedby",pref.get(Constants.USERID));
            params.put("fromdate","");
            params.put("Todate","");
            params.put("Search",ser);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(CustomerVisit.this);
        requestQueue.add(stringRequest);

    }




    public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public NotificationListAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public NotificationListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_customervisit, viewGroup, false);
            NotificationListAdapter.VH viewHolder = new NotificationListAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationListAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.tv.setText("Customer:-"+arrayList.get(i).get("CustomerName"));
            vh.tv1.setText(arrayList.get(i).get("Description"));
            vh.tv2.setText(arrayList.get(i).get("FollowupDate"));
            vh.tv3.setText(arrayList.get(i).get("NextFollowupDate"));
            vh.tv4.setText(arrayList.get(i).get("LeadFollowBy"));
            vh.tv5.setText(arrayList.get(i).get("VisitStatus"));
            vh.tv9.setText(arrayList.get(i).get("CustMobile"));


        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView tv,tv1,tv2,tv3,tv4,tv5,tv9;

            public VH(@NonNull View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
                tv1 = itemView.findViewById(R.id.tv1);
                tv2 = itemView.findViewById(R.id.tv2);

                tv3 = itemView.findViewById(R.id.tv3);
                tv4 = itemView.findViewById(R.id.tv4);
                tv5 = itemView.findViewById(R.id.tv8);
                tv9 = itemView.findViewById(R.id.tv9);

            }
        }
    }


}