package com.akp.amazepay.Das_FindLostDocuments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.amazepay.R;

import java.util.Calendar;

public class FL_AyushmanPrint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_l__ayushman_print);
        EditText et_firstname=findViewById(R.id.et_firstname);
        EditText et_aadhar=findViewById(R.id.et_aadhar);
        TextView et_dob=findViewById(R.id.et_dob);

        Button btn_submit=findViewById(R.id.btn_submit);
        RelativeLayout header=findViewById(R.id.header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_firstname.getText().toString().equalsIgnoreCase("")){
                    et_firstname.setError("Fields can't be blank!");
                    et_firstname.requestFocus();
                }
                else  if (et_aadhar.getText().toString().equalsIgnoreCase("")){
                    et_aadhar.setError("Fields can't be blank!");
                    et_aadhar.requestFocus();
                }
                else   if (et_dob.getText().toString().equalsIgnoreCase("")){
                    et_dob.setError("Fields can't be blank!");
                    et_dob.requestFocus();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Submit Successfully!",Toast.LENGTH_LONG).show();
                }
            }
        });



        // on below line we are adding click listener for our pick date button
        et_dob.setOnClickListener(new View.OnClickListener() {
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
                        FL_AyushmanPrint.this,
//                        FL_AyushmanPrint.this,  R.style.MyTimePickerDialogTheme,

                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                et_dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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
    }
}