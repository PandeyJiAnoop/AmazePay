package com.akp.amazepay.Fundfolder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.amazepay.R;
import com.akp.amazepay.dashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class Fund_Wallet_Report extends AppCompatActivity {
    ImageView menuImg;
    String userId;
    TextView title_tv;


    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Fund_Wallet_Report.FriendsListAdapter pdfAdapTer;
    ImageView norecord_tv;
    private String UserId;
    TextView wallet_bal_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund__wallet__report);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        menuImg=findViewById(R.id.menuImg);
        title_tv=findViewById(R.id.title_tv);

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });

        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);

        wallet_bal_tv=findViewById(R.id.wallet_bal_tv);
        Fund_Wallet_ReportAPI(UserId);

        GetDashboard(UserId);
    }





    public void Fund_Wallet_ReportAPI(String MemberId) {
        String otp1 = new GlobalAppApis().WalletTransactionReport(MemberId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_WalletTransactionReport(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("sfafgadf","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Message");
                    if (status.equalsIgnoreCase("Success")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            title_tv.setText("Wallet Report("+Response.length()+")");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("mwId", jsonObject.getString("mwId"));
                            hashlist.put("Type", jsonObject.getString("Type"));
                            hashlist.put("memberId", jsonObject.getString("memberId"));
                            hashlist.put("Amount", jsonObject.getString("Amount"));
                            hashlist.put("Entry_Date", jsonObject.getString("Entry_Date"));
                            hashlist.put("dt", jsonObject.getString("dt"));
                            hashlist.put("Remark", jsonObject.getString("Remark"));
                            hashlist.put("TransferId", jsonObject.getString("TransferId"));
                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new Fund_Wallet_Report.FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(Fund_Wallet_Report.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        }, Fund_Wallet_Report.this, call1, "", true);
    }


    public class FriendsListAdapter extends RecyclerView.Adapter<Fund_Wallet_Report.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public Fund_Wallet_Report.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Fund_Wallet_Report.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_fund_wallet_report, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Fund_Wallet_Report.FriendsList holder, final int position) {
            holder.tv.setText(String.valueOf((position+1)));
            if (data.get(position).get("Type").equalsIgnoreCase("Debit")){
                holder.tv2.setText(data.get(position).get("Amount"));
            }
            else {
                holder.tv1.setText(data.get(position).get("Amount"));
            }

            holder.tv3.setText(data.get(position).get("Entry_Date"));
            holder.tv4.setText(data.get(position).get("Remark"));

        }
        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView tv,tv1,tv2,tv3,tv4;

        public FriendsList(View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);
        }
    }

    public void GetDashboard(String MemberId) {
        String otp1 = new GlobalAppApis().Dashboard(MemberId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_dashboard(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("reswallwet","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Message");
                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            String WalletBalance=jsonObject.getString("AepsWallet");
                            Log.d("WalletBalance","cxc"+WalletBalance);
                            wallet_bal_tv.setText("\u20B9 "+jsonObject.getString("AepsWallet"));
                        }
                    } else {
                        Toast.makeText(Fund_Wallet_Report.this,object.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Fund_Wallet_Report.this, call1, "", true);
    }



}