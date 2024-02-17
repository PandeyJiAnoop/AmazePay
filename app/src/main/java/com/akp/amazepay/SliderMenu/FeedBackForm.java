package com.akp.amazepay.SliderMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.akp.amazepay.R;

public class FeedBackForm extends AppCompatActivity {
    ImageView back_img;
    Button btn_sendotp;
    EditText comment_et,service_et,mob_et,email_et,subject_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_form);
        subject_et=findViewById(R.id.subject_et);
        service_et=findViewById(R.id.service_et);
        mob_et=findViewById(R.id.mob_et);
        email_et=findViewById(R.id.email_et);
        comment_et=findViewById(R.id.comment_et);
        btn_sendotp=findViewById(R.id.btn_sendotp);


        back_img=findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subject_et.getText().toString().equalsIgnoreCase("")){
                    subject_et.setError("Fields can't be blank!");
                    subject_et.requestFocus();
                }
                else if (service_et.getText().toString().equalsIgnoreCase("")){
                    service_et.setError("Fields can't be blank!");
                    service_et.requestFocus();
                }
                else if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }
                else if (email_et.getText().toString().equalsIgnoreCase("")){
                    email_et.setError("Fields can't be blank!");
                    email_et.requestFocus();
                }
                else if (comment_et.getText().toString().equalsIgnoreCase("")){
                    comment_et.setError("Fields can't be blank!");
                    comment_et.requestFocus();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Request Successfully sent  to Admin\nPlease wait for response......",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

    }
}