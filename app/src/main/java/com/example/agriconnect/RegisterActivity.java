package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
  EditText edttxtemail;
    EditText edttxtfname;
    EditText edttxtpassword ;
  Button cregister;
  String fname,email,password;
  TextView txtviewerror;
  ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edttxtemail = findViewById(R.id.edtcemail);
        edttxtfname = findViewById(R.id.edtcfname);
        edttxtpassword = findViewById(R.id.edtcpasswd);
        cregister = findViewById(R.id.Cregisterbtn);
        txtviewerror = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);

        cregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                txtviewerror.setVisibility(View.GONE);

                fname = String.valueOf(edttxtfname.getText());
                email = String.valueOf(edttxtemail.getText());
                password = String.valueOf(edttxtpassword.getText());


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.11/agriconnect/php/register.php"; //host ip and phpfile

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
                        paramV.put("name",fname);
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