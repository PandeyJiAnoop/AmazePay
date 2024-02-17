package com.akp.amazepay.SliderMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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
import com.akp.amazepay.Adapter.FullImagePage;
import com.akp.amazepay.R;
import com.akp.amazepay.dashboard;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class ExternalLink extends AppCompatActivity {
    ImageView menuImg;
    String userId;
    TextView title_tv;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    ImageView norecord_tv;
    private String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_link);
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
        ExternalLinkAPI();
    }


    public void ExternalLinkAPI() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_ExternalLinkList();
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
                            title_tv.setText("External Link Report("+Response.length()+")");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("BannerImg", jsonObject.getString("BannerImg"));
                            hashlist.put("Url", jsonObject.getString("Url"));

                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(ExternalLink.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, ExternalLink.this, call1, "", true);
    }


    public class FriendsListAdapter extends RecyclerView.Adapter<ExternalLink.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public ExternalLink.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ExternalLink.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_externallink, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final ExternalLink.FriendsList holder, final int position) {
            holder.tv.setText(data.get(position).get("Url"));
            if (data.get(position).get("BannerImg").equalsIgnoreCase("")){
                Toast.makeText(getApplicationContext(),"Image not found!", Toast.LENGTH_LONG).show();
            }
            else {
                Glide.with(ExternalLink.this).load(data.get(position).get("BannerImg")).into(holder.ext_img);
            }

            holder.ext_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), FullImagePage.class);
                    intent.putExtra("path",data.get(position).get("BannerImg"));
                    startActivity(intent);
                }
            });

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView ext_img;


        public FriendsList(View itemView) {
            super(itemView);
            ext_img=itemView.findViewById(R.id.ext_img);
            tv=itemView.findViewById(R.id.tv);
        }
    }

}