package com.akp.amazepay.rechargebills;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.amazepay.R;
import com.akp.amazepay.dashboard;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InsuranceBillNew extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView ivBack;
    LinearLayout provider_ll;
    TextView provoider_et;
    String service_name,UserId;
    String getProvider_id,roletype;
    Button btnSubmit;
    private SharedPreferences sharedPreferences;
    EditText mob_et,amount_et,email_et,dob_et;
    AlertDialog alertDialog;
    String get_operator_ref,get_payid,getonlyservice,GetMobile;
    TextView title_tv,mob_code_tv;
    TextView txtMarquee,view_plan_tv,plan_dis_tv;
    private Dialog alertDialog1;
    private final ArrayList<HashMap<String, String>> arrLegalList = new ArrayList<>();
    private InsuranceBillNew.LegalListAdapter pdfAdapTer;
    RecyclerView plan_rv;
    SearchableSpinner sp_state;
    String stateid="";
    ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();
    String amount;

    SearchableSpinner sp_state1;
    String stateid1;
    ArrayList<String> StateName1 = new ArrayList<>();
    ArrayList<String> StateId1 = new ArrayList<>();

    String Plan_name,Plan_billAmount,Plan_billdate,Plan_bill_fetch;

    private InsuranceBillNew.GpsTracker gpsTracker;
    String tvLatitude,tvLongitude;
    Spinner spinner;
    String[] courses = {"online", "ofline"};
    private String SelectedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_bill_new);
        FindId();
        OnClickListner();
        statusCheck();
    }
    private void OnClickListner() {
        // Now we will call setSelected() method
        // and pass boolean value as true
        txtMarquee.setSelected(true);
        sp_state.setTitle("");
//        Toast.makeText(getApplicationContext(),getonlyservice,Toast.LENGTH_LONG).show();

//        if (getonlyservice.equalsIgnoreCase("Mobile")){
//            mob_et.setHint("Enter your mobile number");
//            mob_code_tv.setVisibility(View.VISIBLE);
//            mob_et.setInputType(InputType.TYPE_CLASS_NUMBER);
//            view_plan_tv.setVisibility(View.VISIBLE);
//            mob_et.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(10) });
//        }
//        else {
//            mob_et.setHint("Enter your Unique Id Number");
//            mob_code_tv.setVisibility(View.GONE);
//            view_plan_tv.setVisibility(View.GONE);
//        }


        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= StateId.size(); j++) {
                        if (sp_state.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        sp_state1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= StateId1.size(); j++) {
                        if (sp_state1.getSelectedItem().toString().equalsIgnoreCase(StateName1.get(j))) {
                            // position = i;
                            stateid1 = StateId1.get(j);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // on below line we are adding click listener for our pick date button
        dob_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();
                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        InsuranceBillNew.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                dob_et.setText(year+ "/"+(monthOfYear + 1)+ "/"+dayOfMonth );
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });


        view_plan_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }
                else {
                    GetAppPlanAPI();
                }
                Log.d("stateid_st",stateid);
            }
       /*         if (sp_state.getSelectedItem().toString().equalsIgnoreCase("AIRTEL")){
                    arrLegalList.clear();
                    GetAppPlanAPI("A");
                }
                else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("VODAFONE")){
                    arrLegalList.clear();
                    GetAppPlanAPI("V");
                }
                else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("IDEA")){
                    arrLegalList.clear();
                    GetAppPlanAPI("I");
                }
                else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("BSNL")){
                    arrLegalList.clear();
                    GetAppPlanAPI("BT");
                }
                else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("RELIANCE - JIO")){
                    arrLegalList.clear();
                    GetAppPlanAPI("J");
                }}*/
        });
        provoider_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getonlyservice.equalsIgnoreCase("Mobile")){
                    Intent intent=new Intent(getApplicationContext(),New_MobileRecharge.class);
                    startActivity(intent);
                }
                else if (getonlyservice.equalsIgnoreCase("DTH")){
                    Intent intent=new Intent(getApplicationContext(), DTHBill.class);
                    startActivity(intent);
                }
                else if (getonlyservice.equalsIgnoreCase("Electricity")){
                    Intent intent=new Intent(getApplicationContext(),ElectricityBill.class);
                    startActivity(intent);
                }
                else if (getonlyservice.equalsIgnoreCase("Insurance")){
                    Intent intent=new Intent(getApplicationContext(),InsuranceBillNew.class);
                    startActivity(intent);
                }
            }
        });

        if(service_name ==null){
        }
        else {
            provoider_et.setText(service_name);
        }
        GetOperatorList(getonlyservice);
        GetRechargeCircleList();
        final Handler handler = new Handler();
        final long delayMillis = 5000; // Delay in milliseconds
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }
                else { amount = amount_et.getText().toString().replaceAll("\\.\\d+$", "");
                    btnSubmit.setEnabled(false);
                    // Enable the button after a delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnSubmit.setEnabled(true);
                        }
                    }, delayMillis);
                    Log.d("sdghsfg",""+UserId+amount_et.getText().toString()+mob_et.getText().toString()+getProvider_id);
                    Payment(UserId,mob_et.getText().toString(),amount_et.getText().toString());
                }
            } });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }});




    }

    private void FindId() {
        email_et=findViewById(R.id.email_et);
        dob_et=findViewById(R.id.dob_et);
        title_tv=findViewById(R.id.title_tv);
        mob_code_tv=findViewById(R.id.mob_code_tv);
        plan_rv = (RecyclerView) findViewById(R.id.plan_rv);
        roletype=getIntent().getStringExtra("RoleType");
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        GetMobile= sharedPreferences.getString("mobile", "");
        service_name=getIntent().getStringExtra("servicename");
        getProvider_id=getIntent().getStringExtra("provider_id");
        getonlyservice=getIntent().getStringExtra("onlyservice");
        plan_dis_tv=findViewById(R.id.plan_dis_tv);
        btnSubmit=findViewById(R.id.btnSubmit);
        sp_state=findViewById(R.id.stateet);
        sp_state1=findViewById(R.id.stateet1);
        // casting of textview
        txtMarquee = (TextView) findViewById(R.id.marqueeText);
//        Toast.makeText(getApplicationContext(),getonlyservice,Toast.LENGTH_LONG).show();

        ivBack=findViewById(R.id.ivBack);
        provider_ll=findViewById(R.id.provider_ll);
        provoider_et=findViewById(R.id.provoider_et);
        mob_et=findViewById(R.id.mob_et);
        amount_et=findViewById(R.id.amount_et);
        title_tv.setText(getonlyservice+" Service");
        view_plan_tv=findViewById(R.id.view_plan_tv);
        mob_et.setText(GetMobile);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courses);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
    }



    private void showpopupwindow() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(InsuranceBillNew.this).inflate(R.layout.successfullycreated_popup, viewGroup, false);
        Button ok = (Button) dialogView.findViewById(R.id.btnDialog);
        TextView txt_msg=dialogView.findViewById(R.id.txt_msg);
        TextView id_tv= dialogView.findViewById(R.id.id_tv);
        TextView pass_tv= dialogView.findViewById(R.id.pass_tv);
        id_tv.setText("Operator_ref- "+get_operator_ref+" ("+UserId+")");
//        pass_tv.setText("Payid- "+get_payid);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }




/*

    private void Payment(String user_id, String phone, String service_code, String amount,String customer_phone,String checksum) {
        String otp1 = new GlobalAppApis().PostRechargeAPI(user_id,phone,service_code,amount,customer_phone,checksum);
        Log.e("PostRecharge",otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.PostRecharge(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.e("PostRecharge",result);
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("0")){
                        String msg       = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(InsuranceBillNew.this);
                        builder.setTitle("Response!")
                                .setMessage(msg)
                                .setCancelable(false)
                                .setIcon(R.drawable.a_fulllogo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
//                        .setNeutralButton("Middle", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(MyActivity.this, "Middle button clicked!", Toast.LENGTH_SHORT).show();
//                            }
//                        });

                        builder.create().show();

                    }
                    else {
                        get_operator_ref=jsonObject.getString("message");
                        showpopupwindow();
                        Toast.makeText(getApplicationContext(),jsonObject.getString("Message"),Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, InsuranceBillNew.this, call1, "", true);

    }
*/




    public void GetAppPlanAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://amazepay.net/api/Amazepay/GetCustomerInfo_LicBill?EmailId="+email_et.getText().toString()+"&dob="+dob_et.getText().toString()+"&canumber="+mob_et.getText().toString()+"&mode="+SelectedValue, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("electricityinfo","https://amazepay.net/api/Amazepay/GetCustomerInfo_LicBill?EmailId="+email_et.getText().toString()+"&dob="+dob_et.getText().toString()+"&canumber="+mob_et.getText().toString());
                try {
                    JSONObject objectlist = new JSONObject(response);
                    JSONObject Responobj= objectlist.getJSONObject("Response");
                    Toast.makeText(InsuranceBillNew.this, Responobj.getString("message"), Toast.LENGTH_SHORT).show();
                    plan_dis_tv.setText("Name:- "+Responobj.getString("name")+"\nDue Date:- "+Responobj.getString("duedate")
                            +"\nBill Fetch:- "
                            +Responobj.getString("bill_fetch")+
                            "\nMessage:- "+Responobj.getString("message"));
                    Plan_name=Responobj.getString("name");
                    Plan_billAmount=Responobj.getString("amount");
                    Plan_billdate=Responobj.getString("billdate");
                    Plan_bill_fetch=Responobj.getString("bill_fetch");
                    mob_et.setText(Responobj.getString("amount"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(InsuranceBillNew.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(InsuranceBillNew.this);
        requestQueue.add(stringRequest);

    }


    public class LegalListAdapter extends RecyclerView.Adapter<InsuranceBillNew.LegalList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public LegalListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrLegalList) {
            data = arrLegalList;
        }

        public InsuranceBillNew.LegalList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InsuranceBillNew.LegalList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_viewplan, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final InsuranceBillNew.LegalList holder, final int position) {
            holder.head_rb.setText(data.get(position).get("name"));
            holder.des_tv.setText(data.get(position).get("bill_fetch")+"\nBillDate:- "+data.get(position).get("billdate"));
            holder.tv.setText("Plan Amount:- "+data.get(position).get("amount"));
            holder.tv1.setText("Validity:- "+data.get(position).get("duedate"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.head_rb.setChecked(true);
//                    alertDialog1.dismiss();
                    amount_et.setText(data.get(position).get("amount"));
                    plan_dis_tv.setText(data.get(position).get("name")+"\n"+data.get(position).get("bill_fetch")+"\nBillDate:- "+data.get(position).get("billdate"));

                }
            });
        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class LegalList extends RecyclerView.ViewHolder {
        TextView des_tv,tv1,tv;
        RadioButton head_rb;

        public LegalList(View itemView) {
            super(itemView);
            head_rb = itemView.findViewById(R.id.head_rb);
            des_tv = itemView.findViewById(R.id.des_tv);
            tv = itemView.findViewById(R.id.tv);
            tv1 = itemView.findViewById(R.id.tv1);
        }
    }

    public static String generateMD5Checksum(String input) {
        try {
            // Create an MD5 MessageDigest instance
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Convert the input string to bytes and feed it to the MessageDigest instance
            md.update(input.getBytes());
            // Generate the MD5 checksum
            byte[] digest = md.digest();
            // Convert the byte array to a hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void GetOperatorList(String op_code) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://amazepay.net/api/Amazepay/RechargeOperatorList?OperatorName="+op_code, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("res_p",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("false")){
                        String msg       = object.getString("msg");
                        Toast.makeText(getApplicationContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JSONArray jsonArray = object.getJSONArray("Response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            StateId.add(jsonObject1.getString("OperatorCode"));
                            String statename = jsonObject1.getString("OperatorName");
                            StateName.add(statename);
                        }
                        sp_state.setAdapter(new ArrayAdapter<String>(InsuranceBillNew.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(InsuranceBillNew.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(InsuranceBillNew.this);
        requestQueue.add(stringRequest);

    }





    public void GetRechargeCircleList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://amazepay.net/api/Amazepay/RechargeCircleList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("res_p",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("false")){
                        String msg       = object.getString("msg");
                        Toast.makeText(getApplicationContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JSONArray jsonArray = object.getJSONArray("Response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            StateId1.add(jsonObject1.getString("CircleId"));
                            String statename1 = jsonObject1.getString("CircleName");
                            StateName1.add(statename1);
                        }
                        sp_state1.setAdapter(new ArrayAdapter<String>(InsuranceBillNew.this, android.R.layout.simple_spinner_dropdown_item, StateName1));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(InsuranceBillNew.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(InsuranceBillNew.this);
        requestQueue.add(stringRequest);

    }



    public void Payment(String id,String mob,String amount) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://amazepay.net/api/Amazepay/DoInsurancePaymet", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("MobileDoRecharge",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Status").equalsIgnoreCase("false")){
                        String msg       = jsonObject.getString("msg");
                        Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(InsuranceBillNew.this);
                        builder.setTitle("Response!")
                                .setMessage(msg)
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();

                    }
                    else {
                        get_operator_ref=jsonObject.getString("msg");
                        showpopupwindow();
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(InsuranceBillNew.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MemberId", id);
                params.put("BillNo",mob);
                params.put("circlecode", stateid1);
                params.put("OperatorCode",stateid);
                params.put("amount",amount);
                params.put("userName", Plan_name);
                params.put("billAmount",Plan_billAmount);
                params.put("billdate", Plan_billdate);
                params.put("latitude",tvLatitude);
                params.put("longitude",tvLongitude);
                Log.d("dopayment_electricity",""+params);
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(InsuranceBillNew.this);
        requestQueue.add(stringRequest);
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        else {
            getLocation();
        }
    }
    //***************Location Get Service***********
    public void getLocation(){
        gpsTracker = new InsuranceBillNew.GpsTracker(InsuranceBillNew.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            tvLatitude=(String.valueOf(latitude));
            tvLongitude=(String.valueOf(longitude));
//            Toast.makeText(getApplicationContext(),"Lat:-"+tvLatitude+"\n\nLong:- "+tvLongitude,Toast.LENGTH_LONG).show();
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(InsuranceBillNew.this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }


    class GpsTracker extends Service implements LocationListener {
        private final Context mContext;
        // flag for GPS status
        boolean isGPSEnabled = false;
        // flag for network status
        boolean isNetworkEnabled = false;
        // flag for GPS status
        boolean canGetLocation = false;
        Location location; // location
        double latitude; // latitude
        double longitude; // longitude
        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
        // Declaring a Location Manager
        protected LocationManager locationManager;
        public GpsTracker(Context context) {
            this.mContext = context;
            getLocation();
        }
        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
                // getting GPS status
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        //check the network permission
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                        }
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            //check the network permission
                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                            }
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                } } } } }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return location;
        }

        /**
         * Stop using GPS listener
         * Calling this function will stop using GPS in your app
         * */

        public void stopUsingGPS(){
            if(locationManager != null){
                locationManager.removeUpdates(InsuranceBillNew.GpsTracker.this);
            }
        }

        /**
         * Function to get latitude
         * */
        public double getLatitude(){
            if(location != null){
                latitude = location.getLatitude();
            }
            // return latitude
            return latitude;
        }
        /**
         * Function to get longitude
         * */
        public double getLongitude(){
            if(location != null){
                longitude = location.getLongitude();
            }
            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/wifi enabled
         * @return boolean
         * */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }
        /**
         * Function to show settings alert dialog
         * On pressing Settings button will lauch Settings Options
         * */

        public void showSettingsAlert(){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");
            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });
            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
        @Override
        public void onLocationChanged(Location location) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }
    }
    // Performing action when ItemSelected
    // from spinner, Overriding onItemSelected method
    @Override
    public void onItemSelected(AdapterView arg0, View arg1, int position, long id)
    {
        SelectedValue=spinner.getSelectedItem().toString();
//        Toast.makeText(getApplicationContext(),spin.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView arg0)
    {
        // Auto-generated method stub
    }




}