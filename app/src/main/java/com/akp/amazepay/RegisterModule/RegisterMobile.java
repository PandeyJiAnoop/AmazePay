package com.akp.amazepay.RegisterModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.akp.amazepay.R;

public class RegisterMobile extends AppCompatActivity {
LinearLayout otp_ll;
AppCompatButton get_otp_btn,login_btn;
EditText mob_et,otp_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile);
        otp_ll=findViewById(R.id.otp_ll);
        get_otp_btn=findViewById(R.id.get_otp_btn);
        login_btn=findViewById(R.id.login_btn);
        mob_et=findViewById(R.id.mob_et);
        otp_et=findViewById(R.id.otp_et);
        get_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp_ll.setVisibility(View.VISIBLE);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus(); }
                else if (otp_et.getText().toString().equalsIgnoreCase("")){
                    otp_et.setError("Fields can't be blank!");
                    otp_et.requestFocus(); }
                else {
                    Intent intent=new Intent(getApplicationContext(),SignupForm.class);
                    startActivity(intent);
                }
            }
        });
    }
}