package com.akp.amazepay;
/**
 * Created by Anoop Pandey on 9696381023.
 */
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.amazepay.Util.Preferences;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class user_profile extends AppCompatActivity {
    EditText fullnameet,aadharet,panet,addresset;
    TextView tv_mob_No,et_Name;
    Context context;
    Preferences pref;
    Button save;

    ArrayList<String> ProductSizeValue = new ArrayList<>();
String UserId;
    String checksum;
    CircleImageView profile_image;
    private SharedPreferences sharedPreferences;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;
    ImageButton img_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        context=this.getApplicationContext();
        pref=new Preferences(context);

        findViewById(R.id.menuImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findView();
        GetStateList();
        String input = UserId+":ip6wgncgt6:nxe7j2f6c0";
        String strup = input.toUpperCase();
        checksum = generateMD5Checksum(strup);
        Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);

        GetProfile(UserId);

        findViewById(R.id.img_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(user_profile.this);
                builder.setMessage(Html.fromHtml("<font color='#000'>Are you sure want to Logout?</font>"));
                builder.setCancelable(false);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LogoutAPI();

                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setBackgroundColor(Color.BLACK);
                nbutton.setTextColor(Color.WHITE);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setBackgroundColor(Color.RED);
                pbutton.setTextColor(Color.WHITE);
            }
        });

    }
    public void findView(){
        et_Name=findViewById(R.id.et_Name);
        tv_mob_No=findViewById(R.id.tv_mob_No);
        fullnameet=findViewById(R.id.fullnameet);
        aadharet=findViewById(R.id.aadharet);
        panet=findViewById(R.id.panet);
        addresset=findViewById(R.id.addresset);
        save=findViewById(R.id.save);

        profile_image=findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullnameet.getText().toString().equalsIgnoreCase("")){
                    fullnameet.setError("Fields can't be blank!");
                    fullnameet.requestFocus();
                }
                else if (aadharet.getText().toString().equalsIgnoreCase("")){
                    aadharet.setError("Fields can't be blank!");
                    aadharet.requestFocus();
                }
                else if (panet.getText().toString().equalsIgnoreCase("")){
                    panet.setError("Fields can't be blank!");
                    panet.requestFocus();
                }
                else  if (addresset.getText().toString().equalsIgnoreCase("")){
                    addresset.setError("Fields can't be blank!");
                    addresset.requestFocus();
                }
                else {

                    UpdateProfile(temp);

//                    if (profile_image.getDrawable() == null){
//                        UpdateProfile(UserId,fullnameet.getText().toString(),emailet.getText().toString(),aadharet.getText().toString(),
//                                panet.getText().toString(),firm_nameet.getText().toString(),
//                                pincodeet.getText().toString(),stateet.getSelectedItem().toString(),
//                                addresset.getText().toString(),"",checksum);
//                    }
//                    else {
//                        UpdateProfile(UserId,fullnameet.getText().toString(),emailet.getText().toString(),aadharet.getText().toString(),
//                                panet.getText().toString(),firm_nameet.getText().toString(),
//                                pincodeet.getText().toString(),stateet.getSelectedItem().toString(),
//                                addresset.getText().toString(),temp,checksum);
//                    }


                }

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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, user_profile.this, call1, "", true);
    }




    public void UpdateProfile(String img) {
        String otp1 = new GlobalAppApis().UpdateProfile(img);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_updateProfile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("resupdate","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);

                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, user_profile.this, call1, "", true);
    }





    public void GetProfile(String MemberId) {
        String otp1 = new GlobalAppApis().GetProfile(MemberId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_getProfile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("sfafgadf","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("Message").equalsIgnoreCase("Success")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            String MemberId = jsonObject.getString("MemberId");
                            String MemberName = jsonObject.getString("MemberName");
                            String EmailAddress = jsonObject.getString("EmailAddress");

                            String MobileNo = jsonObject.getString("MobileNo");
                            String Pancard = jsonObject.getString("Pancard");
                            String AadharCard = jsonObject.getString("AadharCard");

                            String FullAddress = jsonObject.getString("FullAddress");

//
//                        if (object.getString("profile_image").equalsIgnoreCase("")){
//                        }
//                        else {
//                            Glide.with(user_profile.this).load(object.getString("profile_image")).into(profile_image);
//                        }


                            et_Name.setText("Member Id-(" + MemberId + ")");
                            tv_mob_No.setText("+91- " + MobileNo);
                            fullnameet.setText(MemberName);
                            aadharet.setText(AadharCard);
                            panet.setText(Pancard);
                            addresset.setText(FullAddress);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, user_profile.this, call1, "", true);
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



    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(user_profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(user_profile.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Toast.makeText(getApplicationContext(),""+bm,Toast.LENGTH_LONG).show();
        profile_image.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }
    private void LogoutAPI() {
        String otp1 = new GlobalAppApis().Logout(UserId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_Logout(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Message");
                    if (status.equalsIgnoreCase("Success")) {
                        SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), splashscrn.class);
                        startActivity(intent);
                    } else {
                        SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), splashscrn.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            } }, user_profile.this, call1, "", true);
    }
}