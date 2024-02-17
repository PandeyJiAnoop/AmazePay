package com.akp.amazepay.Das_FindLostDocuments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.amazepay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class FL_FindPanByAdhar extends AppCompatActivity {
Spinner stateet;
    ArrayList<String> ProductSizeValue = new ArrayList<>();

    Spinner spinner;
    String colors[] = {"PMJAYId","Mobile","HHID"};
    ArrayAdapter<String> spinnerArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_l__find_pan_by_adhar);
        EditText et_firstname=findViewById(R.id.et_firstname);
        stateet=findViewById(R.id.stateet);
        Button btn_submit=findViewById(R.id.btn_submit);
        RelativeLayout header=findViewById(R.id.header);

        spinner = findViewById(R.id.spinner);
        spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, colors);
        spinner.setAdapter(spinnerArrayAdapter);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetStateList();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_firstname.getText().toString().equalsIgnoreCase("")){
                    et_firstname.setError("Fields can't be blank!");
                    et_firstname.requestFocus();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Submit Successfully!",Toast.LENGTH_LONG).show();
                }
            }
        });

        stateet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String imc_met=stateet.getSelectedItem().toString();
//                Toast.makeText(Oder_Summery_Details.this, imc_met + " selected size", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void GetStateList() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_getStateList();
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JSONArray jsonArray = object.getJSONArray("state_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String value = jsonArray.getString(i);
                            ProductSizeValue.add(value);
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                            String productSizeValue = jsonObject1.getString("ProductSizeValue");
//                            ProductSizeValue.add(productSizeValue);
                        }
                        stateet.setAdapter(new ArrayAdapter<String>(FL_FindPanByAdhar.this, android.R.layout.simple_spinner_dropdown_item, ProductSizeValue));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, FL_FindPanByAdhar.this, call1, "", true);
    }


}