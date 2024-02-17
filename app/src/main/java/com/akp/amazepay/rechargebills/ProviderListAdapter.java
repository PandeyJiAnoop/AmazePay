package com.akp.amazepay.rechargebills;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.amazepay.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProviderListAdapter extends RecyclerView.Adapter<ProviderListAdapter.VH> {
    Context context;
    List<HashMap<String, String>> arrayList;
    int j=0;
    RecyclerView wallet_histroy;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    double aresult;
    private String provider;

    public ProviderListAdapter(Context context, List<HashMap<String, String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.provider_list, viewGroup, false);
        VH viewHolder = new VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        vh.name_tv.setText(arrayList.get(i).get("Name"));
        vh.provider_tv.setText(arrayList.get(i).get("Code"));

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, New_MobileRecharge.class);
                intent.putExtra("servicename",arrayList.get(i).get("Name"));
                intent.putExtra("providerid",arrayList.get(i).get("Code"));
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView name_tv,provider_tv;

ImageView imv_transaction_type;



        public VH(@NonNull View itemView) {
            super(itemView);
            name_tv=itemView.findViewById(R.id.name_tv);
            imv_transaction_type=itemView.findViewById(R.id.imv_transaction_type);
            provider_tv=itemView.findViewById(R.id.provider_tv);

        }
    }



}
