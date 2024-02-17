package com.akp.amazepay.Fundfolder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import java.util.List;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class Fund_Request_Report extends AppCompatActivity {
    ImageView menuImg;
    String userId;
    TextView title_tv;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Fund_Request_Report.FriendsListAdapter pdfAdapTer;
    ImageView norecord_tv;
    private String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund__request__report);
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
        Fund_Request_ReportAPI(UserId);
    }


    public void Fund_Request_ReportAPI(String MemberId) {
        String otp1 = new GlobalAppApis().FundRequestReport(MemberId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_FundRequestReport(otp1);
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
                            title_tv.setText("Fund Request Report("+Response.length()+")");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("PKID", jsonObject.getString("PKID"));
                            hashlist.put("MemberId", jsonObject.getString("MemberId"));
                            hashlist.put("MemberName", jsonObject.getString("MemberName"));
                            hashlist.put("Amount", jsonObject.getString("Amount"));
                            hashlist.put("TransactionNo", jsonObject.getString("TransactionNo"));
                            hashlist.put("RecpFile", jsonObject.getString("RecpFile"));
                            hashlist.put("PaymentDateTime", jsonObject.getString("PaymentDateTime"));
                            hashlist.put("AdminStatus", jsonObject.getString("AdminStatus"));
                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new Fund_Request_Report.FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(Fund_Request_Report.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Fund_Request_Report.this, call1, "", true);
    }


    public class FriendsListAdapter extends RecyclerView.Adapter<Fund_Request_Report.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public Fund_Request_Report.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Fund_Request_Report.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_fund_request_report, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Fund_Request_Report.FriendsList holder, final int position) {
            holder.tv.setText(String.valueOf(" "+(position+1)));
            holder.tv1.setText(data.get(position).get("Amount"));
            holder.tv2.setText(data.get(position).get("TransactionNo"));
            holder.tv4.setText(data.get(position).get("PaymentDateTime"));

            if (data.get(position).get("AdminStatus").equalsIgnoreCase("Approve")){
                holder.tv5.setText(data.get(position).get("AdminStatus"));
                holder.tv5.setBackgroundResource(R.color.green);
                holder.tv5.setTextColor(Color.WHITE);
            }
            else {
                holder.tv5.setText(data.get(position).get("AdminStatus"));
                holder.tv5.setText("Pending");
            }
            holder.tv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = data.get(position).get("RecpFile");
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }});
        }
        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView tv,tv1,tv2,tv3,tv4,tv5;

        public FriendsList(View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);

            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);
            tv5=itemView.findViewById(R.id.tv5);
            tv=itemView.findViewById(R.id.tv);
        }
    }



}