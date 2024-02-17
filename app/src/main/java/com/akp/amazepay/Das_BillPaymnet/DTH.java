package com.akp.amazepay.Das_BillPaymnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost1;

public class DTH extends AppCompatActivity {
    ImageView ivBack;
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_t_h);
        ivBack=findViewById(R.id.ivBack);
        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getHistory("DTH");
    }


    public void  getHistory(String service){
        String otp1 = new GlobalAppApis().Operator(service);
        ApiService client1 = getApiClient_ByPost1();
        Call<String> call1 = client1.OperatorService(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("ressss",result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("provider_Name", jsonObject1.getString("provider_Name"));
                        hm.put("provider_icon", jsonObject1.getString("provider_icon"));
                        hm.put("provider_id", jsonObject1.getString("provider_id"));
                        hm.put("service_id", jsonObject1.getString("service_id"));
                        hm.put("service_name", jsonObject1.getString("service_name"));
                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(DTH.this, 3);
                    CategoryServiceListAdapter customerListAdapter = new CategoryServiceListAdapter(DTH.this, arrayList1);
                    cust_recyclerView.setLayoutManager(gridLayoutManager);
                    cust_recyclerView.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, DTH.this, call1, "", true);
    }


    public class CategoryServiceListAdapter extends RecyclerView.Adapter<CategoryServiceListAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public CategoryServiceListAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public CategoryServiceListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_category, viewGroup, false);
            CategoryServiceListAdapter.VH viewHolder = new CategoryServiceListAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryServiceListAdapter.VH vh, int i) {
            vh.provider_name_tv.setText(arrayList.get(i).get("provider_Name"));
            if (arrayList.get(i).get("provider_icon").equalsIgnoreCase("")){
                Toast.makeText(context,"Image not found!", Toast.LENGTH_LONG).show();
            }
            else {
                Glide.with(context).load(arrayList.get(i).get("provider_icon")).into(vh.provider_img);
            }
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MobileRecharge.class);
                    intent.putExtra("servicename",arrayList.get(i).get("provider_Name")+"("+arrayList.get(i).get("service_name")+")");
                    intent.putExtra("provider_id",arrayList.get(i).get("provider_id"));
                    intent.putExtra("onlyservice",arrayList.get(i).get("service_name"));
                    context.startActivity(intent); }});
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView provider_name_tv;
            CircleImageView provider_img;

            public VH(@NonNull View itemView) {
                super(itemView);
                provider_name_tv = itemView.findViewById(R.id.provider_name_tv);
                provider_img = itemView.findViewById(R.id.provider_img);
            }
        }
    }


}