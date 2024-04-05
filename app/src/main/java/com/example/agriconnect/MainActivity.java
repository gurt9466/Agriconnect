package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView register;
    EditText edtusername,edtpassword;
    Button login;
    String name_first,username,password,apiKey;
    TextView txtviewerror;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtusername = findViewById(R.id.editTextlUsername);
        edtpassword = findViewById(R.id.editTextlPassword);

        txtviewerror = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);
        login = findViewById(R.id.loginbutton);
        register = findViewById(R.id.textViewclickhere);

        sharedPreferences = getSharedPreferences("Agriconnect",MODE_PRIVATE);

        if(sharedPreferences.getString("logged","false").equals("true")){
            Intent intent = new Intent(getApplicationContext(),home.class);
            startActivity(intent);
            finish();

        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                txtviewerror.setVisibility(View.GONE);

                username = String.valueOf(edtusername.getText());
                password = String.valueOf(edtpassword.getText());


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://agriconnect.me/register/login.php"; //host ip and phpfile
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if(status.equals("success")){
                                        name_first = jsonObject.getString("name_first");
                                        username = jsonObject.getString("username");
                                        apiKey = jsonObject.getString("apiKey");
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("logged","true");
                                        editor.putString("name_first",name_first);
                                        editor.putString("username",username);
                                        editor.putString("apiKey",apiKey);
                                        editor.apply();

                                        Intent intent = new Intent(getApplicationContext(),home.class);
                                        startActivity(intent);
                                        finish();

                                    }else {
                                        txtviewerror.setText(message);
                                        txtviewerror.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);

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
                        paramV.put("password",password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }
}