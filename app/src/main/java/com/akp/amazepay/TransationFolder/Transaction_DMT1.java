package com.akp.amazepay.TransationFolder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class Transaction_DMT1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView menuImg;
    String userId;
    TextView title_tv;


    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    ImageView norecord_tv;
    private String UserId;
    String[] courses = {"Aadhar Payment", "Balance Enquiry",
            "Cash Withdrawal", "Min Statement"};
    Spinner spino;
    String SelectType; EditText tran_et;
    AppCompatButton search_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction__d_m_t1);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        menuImg=findViewById(R.id.menuImg);
        spino = findViewById(R.id.coursesspinner);
        title_tv=findViewById(R.id.title_tv);
        tran_et=findViewById(R.id.tran_et);
        search_btn=findViewById(R.id.search_btn);

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });

        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);


        spino.setOnItemSelectedListener(this);
        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courses);
        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spino.setAdapter(ad);
        Transaction_DMT1API(UserId,"");
    }





    public void Transaction_DMT1API(String MemberId,String TransNumber) {
        String otp1 = new GlobalAppApis().DMT1Report(MemberId,TransNumber);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_DMT1Report(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("DMTReport","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Message");
                    if (status.equalsIgnoreCase("Success")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            title_tv.setText("DMT-2 Report("+Response.length()+")");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("DmtTransId", jsonObject.getString("DmtTransId"));
                            hashlist.put("MemberId", jsonObject.getString("MemberId"));
                            hashlist.put("FirstName", jsonObject.getString("FirstName"));
                            hashlist.put("BankId", jsonObject.getString("BankId"));
                            hashlist.put("ackno", jsonObject.getString("ackno"));
                            hashlist.put("utr", jsonObject.getString("utr"));
                            hashlist.put("refid", jsonObject.getString("refid"));
                            hashlist.put("beneName", jsonObject.getString("beneName"));
                            hashlist.put("txnAmount", jsonObject.getString("txnAmount"));
                            hashlist.put("BalanceAmt", jsonObject.getString("BalanceAmt"));

                            hashlist.put("txnType", jsonObject.getString("txnType"));
                            hashlist.put("txnMessage", jsonObject.getString("txnMessage"));
                            hashlist.put("account_number", jsonObject.getString("account_number"));
                            hashlist.put("EntryDate", jsonObject.getString("EntryDate"));
                            hashlist.put("txnStatus", jsonObject.getString("txnStatus"));




                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(Transaction_DMT1.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Transaction_DMT1.this, call1, "", true);
    }


    public class FriendsListAdapter extends RecyclerView.Adapter<FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_transaction_dmt1, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final FriendsList holder, final int position) {
            holder.tv.setText(String.valueOf(" "+(position+1)));
            holder.tv1.setText(data.get(position).get("MemberId"));
            holder.tv2.setText(data.get(position).get("FirstName"));
            holder.tv4.setText(data.get(position).get("BankId"));
            holder.tv3.setText(data.get(position).get("ackno"));
            holder.tv5.setText(data.get(position).get("utr"));
            holder.tv6.setText(data.get(position).get("refid"));
            holder.tv7.setText(data.get(position).get("beneName"));
            holder.tv8.setText(data.get(position).get("txnAmount"));
            holder.tv9.setText(data.get(position).get("BalanceAmt"));
            holder.tv10.setText(data.get(position).get("txnType"));
            holder.tv11.setText(data.get(position).get("txnMessage"));
            holder.tv12.setText(data.get(position).get("account_number"));
            holder.tv13.setText(data.get(position).get("EntryDate"));
            holder.tv14.setText(data.get(position).get("txnStatus"));

            holder.print_script_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),DMT_Report1_PrintInvoicePage.class);
                    intent.putExtra("DmtTransId",data.get(position).get("DmtTransId"));
                    startActivity(intent);

                }
            });

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView tv,tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12,tv13,tv14;
        AppCompatButton print_script_btn;


        public FriendsList(View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);

            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);
            tv5=itemView.findViewById(R.id.tv5);
            tv6=itemView.findViewById(R.id.tv6);
            tv7=itemView.findViewById(R.id.tv7);
            tv8=itemView.findViewById(R.id.tv8);
            tv=itemView.findViewById(R.id.tv);


            tv9=itemView.findViewById(R.id.tv9);
            tv10=itemView.findViewById(R.id.tv10);
            tv11=itemView.findViewById(R.id.tv11);
            tv12=itemView.findViewById(R.id.tv12);
            tv13=itemView.findViewById(R.id.tv13);
            tv14=itemView.findViewById(R.id.tv14);

            print_script_btn=itemView.findViewById(R.id.print_script_btn);
        }
    }
    // Performing action when ItemSelected
    // from spinner, Overriding onItemSelected method
    @Override
    public void onItemSelected(AdapterView arg0, View arg1, int position, long id) {
        ((TextView) arg0.getChildAt(0)).setTextSize(12);

        // make toastof name of course
        // which is selected in spinner
        if (spino.getSelectedItem().toString().equalsIgnoreCase("Aadhar Payment")) {
            SelectType = "AP";
        } else if (spino.getSelectedItem().toString().equalsIgnoreCase("Balance Enquiry")) {
            SelectType = "BE";
        } else if (spino.getSelectedItem().toString().equalsIgnoreCase("Cash Withdrawal")) {
            SelectType = "CW";
        } else if (spino.getSelectedItem().toString().equalsIgnoreCase("Min Statement")) {
            SelectType = "MS";
        }


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectType == null){
                    arrFriendsList.clear();
                    Transaction_DMT1API(UserId,tran_et.getText().toString());
                }
                else {
                    arrFriendsList.clear();
                    Transaction_DMT1API(UserId,tran_et.getText().toString());
                }

            }
        });




    }


    @Override
    public void onNothingSelected(AdapterView arg0) {
        // Auto-generated method stub
    }

}