package com.akp.amazepay.RegisterModule;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.amazepay.Das_BillPaymnet.Mobile;
import com.akp.amazepay.MainActivityLogin;
import com.akp.amazepay.R;
import com.akp.amazepay.dashboard;
import com.akp.amazepay.user_profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class SignupForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    AppCompatButton reg_btn;
    EditText ref_et, ref_name_et, name_et, mob_et, email_et, add_et, shop_et, pan_et, adhar_et,firm_et,pass_et;
    CheckBox checkbox;
    // create array of Strings
    // and store name of courses
    String[] courses = {"State Partner", "Master Distributor","Distributor", "Merchant"};
    Spinner spino;
    String SelectType;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);
        findViewByIds();
    }

    private void findViewByIds() {
        ref_et = findViewById(R.id.ref_et);
        ref_name_et = findViewById(R.id.ref_name_et);
        name_et = findViewById(R.id.name_et);
        mob_et = findViewById(R.id.mob_et);
        email_et = findViewById(R.id.email_et);
        add_et = findViewById(R.id.add_et);
        shop_et = findViewById(R.id.shop_et);
        pan_et = findViewById(R.id.pan_et);
        firm_et = findViewById(R.id.firm_et);
        pass_et = findViewById(R.id.pass_et);
        adhar_et = findViewById(R.id.adhar_et);
        checkbox = findViewById(R.id.checkbox);
         spino = findViewById(R.id.coursesspinner);

        reg_btn = findViewById(R.id.reg_btn);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_et.getText().toString().equalsIgnoreCase("")) {
                    name_et.setError("Fields can't be blank!");
                    name_et.requestFocus();
                } else if (mob_et.getText().toString().equalsIgnoreCase("")) {
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                } else if (email_et.getText().toString().equalsIgnoreCase("")) {
                    email_et.setError("Fields can't be blank!");
                    email_et.requestFocus();
                } else if (add_et.getText().toString().equalsIgnoreCase("")) {
                    add_et.setError("Fields can't be blank!");
                    add_et.requestFocus();
                } else if (shop_et.getText().toString().equalsIgnoreCase("")) {
                    shop_et.setError("Fields can't be blank!");
                    shop_et.requestFocus();
                } else if (pan_et.getText().toString().equalsIgnoreCase("")) {
                    pan_et.setError("Fields can't be blank!");
                    pan_et.requestFocus();
                } else if (adhar_et.getText().toString().equalsIgnoreCase("")) {
                    adhar_et.setError("Fields can't be blank!");
                    adhar_et.requestFocus();
                }
                else if (firm_et.getText().toString().equalsIgnoreCase("")) {
                    firm_et.setError("Fields can't be blank!");
                    firm_et.requestFocus();
                }
                else if (pass_et.getText().toString().equalsIgnoreCase("")) {
                    pass_et.setError("Fields can't be blank!");
                    pass_et.requestFocus();
                }
                else if (!checkbox.isChecked()) {
                    checkbox.requestFocus();
                    Toast.makeText(getApplicationContext(), "Accept our terms and conditions!", Toast.LENGTH_LONG).show();
                    //do some validation
                } else {

                    if (SelectType == null){
                        RegisterAPI(ref_et.getText().toString(), name_et.getText().toString(), mob_et.getText().toString(), email_et.getText().toString(), add_et.getText().toString(), adhar_et.getText().toString(), pan_et.getText().toString(), firm_et.getText().toString(),pass_et.getText().toString(),"2");

                    }
                    else {
                        RegisterAPI(ref_et.getText().toString(), name_et.getText().toString(), mob_et.getText().toString(), email_et.getText().toString(), add_et.getText().toString(), adhar_et.getText().toString(), pan_et.getText().toString(),firm_et.getText().toString(),pass_et.getText().toString(), SelectType);
                    } } }
        });

        ref_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }});

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

    }

    // Performing action when ItemSelected
    // from spinner, Overriding onItemSelected method
    @Override
    public void onItemSelected(AdapterView arg0, View arg1, int position, long id) {
        // make toastof name of course
        // which is selected in spinner
        if (spino.getSelectedItem().toString().equalsIgnoreCase("State Partner")) {
            SelectType = "2";
        } else if (spino.getSelectedItem().toString().equalsIgnoreCase("Master Distributor")) {
            SelectType = "3";
        } else if (spino.getSelectedItem().toString().equalsIgnoreCase("Distributor")) {
            SelectType = "4";
        } else if (spino.getSelectedItem().toString().equalsIgnoreCase("Merchant")) {
            SelectType = "5";
        }
    }


    @Override
    public void onNothingSelected(AdapterView arg0) {
        // Auto-generated method stub
    }

    public void RegisterAPI(String RefrerralId,String MemberName,String MobileNo,String EmialId,String Address,String AadharNo,String PanNo,String FirmName,String Password,String RoleType) {
        String otp1 = new GlobalAppApis().Register(RefrerralId,MemberName,MobileNo,EmialId,Address,AadharNo,PanNo,FirmName,Password,RoleType);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_Registration(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("resupdate","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("Message").equalsIgnoreCase("Success")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            //then we will inflate the custom alert dialog xml that we created
                            View dialogView = LayoutInflater.from(SignupForm.this).inflate(R.layout.successfullycreated_popup, viewGroup, false);
                            Button ok = (Button) dialogView.findViewById(R.id.btnDialog);
                            TextView txt_msg=dialogView.findViewById(R.id.txt_msg);
                            txt_msg.setText(jsonObject.getString("msg")+"\n\nYour User Id -("+jsonObject.getString("MemberId")+")"+"\nPassword Is - ("+jsonObject.getString("Password")+")");
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getApplicationContext(), MainActivityLogin.class);
                                    startActivity(intent);
                                    alertDialog.dismiss();
                                }
                            });
                            //Now we need an AlertDialog.Builder object
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupForm.this);
                            //setting the view of the builder to our custom view that we already inflated
                            builder.setView(dialogView);
                            //finally creating the alert dialog and displaying it
                            alertDialog = builder.create();
                            alertDialog.show();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SignupForm.this, call1, "", true);
    }


    public void VerifyRef(String RefrerralId) {
        String otp1 = new GlobalAppApis().VerifyReferral(RefrerralId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_VerifyReferral(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("API_VerifyReferral","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("Message").equalsIgnoreCase("Success")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            ref_name_et.setText(jsonObject.getString("MemberName"));
                        } }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SignupForm.this, call1, "", true); }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        VerifyRef(ref_et.getText().toString());
    }
}