package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText edttxtusername,edttxtlname,edttxtdob,edttxtaddressstreet,edttxtaddresscity,edttxtaddressregion,edttxtcontactnumber,edttxtemail,edttxtfname,edttxtpassword;
    RadioGroup gender;

    Button cregister;
    String username,password,name_last,name_first,dob,email,address_street,address_city,address_region,contactnumber;
    TextView txtviewerror;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        edttxtusername =findViewById(R.id.edtcusername);
        edttxtpassword = findViewById(R.id.edtcpasswd);
        edttxtlname = findViewById(R.id.edtclname);
        edttxtfname = findViewById(R.id.edtcfname);
        gender = findViewById(R.id.gender);
        edttxtdob = findViewById(R.id.cdob);
        edttxtemail = findViewById(R.id.edtcemail);
        edttxtaddressstreet = findViewById(R.id.edtcaddressstreet);
        edttxtaddresscity = findViewById(R.id.edtcaddresscity);
        edttxtaddressregion = findViewById(R.id.edtcaddressregion);
        edttxtcontactnumber =findViewById(R.id.edtccnumber);

        cregister = findViewById(R.id.Cregisterbtn);
        txtviewerror = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);

        cregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                txtviewerror.setVisibility(View.GONE);

                username = String.valueOf(edttxtusername.getText());
                name_last = String.valueOf(edttxtlname.getText());
                name_first = String.valueOf(edttxtfname.getText());
                dob = String.valueOf(edttxtdob.getText());
                address_street = String.valueOf(edttxtaddressstreet.getText());
                address_city = String.valueOf(edttxtaddresscity.getText());
                address_region = String.valueOf(edttxtaddressregion.getText());
                contactnumber = String.valueOf(edttxtcontactnumber.getText());
                email = String.valueOf(edttxtemail.getText());
                password = String.valueOf(edttxtpassword.getText());
                RadioButton checkedBtn = findViewById(gender.getCheckedRadioButtonId());
                String gender = checkedBtn.getText().toString();


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.4/agriconnect/php/register.php"; //host ip and phpfile

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                if(response.equals("success")){
                                    Toast.makeText(getApplicationContext(),"Registration Successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{txtviewerror.setText(response);
                                    txtviewerror.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        txtviewerror.setVisibility(View.VISIBLE);
                        txtviewerror.setText(error.getLocalizedMessage());
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("username",username);
                        paramV.put("name_last",name_last);
                        paramV.put("name_first",name_first);
                        paramV.put("gender",gender);
                        paramV.put("dob",dob);
                        paramV.put("address_street",address_street);
                        paramV.put("address_city",address_city);
                        paramV.put("address_region",address_region);
                        paramV.put("contact_number",contactnumber);
                        paramV.put("email",email);
                        paramV.put("password",password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });

    }
}