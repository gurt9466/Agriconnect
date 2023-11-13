package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class home extends AppCompatActivity {
    TextView txtfname, txtviewfetchreslt;
    Button btnrequest, btnpurchase,btnstatus;
    ImageView btnlogout, btncart,btnprofile;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnrequest =findViewById(R.id.requestproduct);
        txtfname = findViewById(R.id.txtviewfname);
        sharedPreferences = getSharedPreferences("Agriconnect", MODE_PRIVATE);
        btnlogout = findViewById(R.id.logout13);
        txtviewfetchreslt = findViewById(R.id.ftechresult);
        btnpurchase =findViewById(R.id.purchase);
        btncart =findViewById(R.id.cart);
        btnprofile = findViewById(R.id.profileimg);
        btnstatus = findViewById(R.id.orderstatus);

        if (sharedPreferences.getString("logged", "false").equals("false")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }
        txtfname.setText(sharedPreferences.getString("name_first",""));



        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.9/agriconnect/php/register/logout.php"; //host ip and phpfile
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.equals("success")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "");
                                    editor.putString("username", "");
                                    editor.putString("apiKey", "");
                                    editor.apply();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(home.this, response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("username", sharedPreferences.getString("username",""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey",""));
                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });

        btnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(home.this, requestActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnpurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(home.this, purchaseActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(home.this, cartActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(home.this, edtprofiles.class);
                startActivity(intent);
                finish();
            }
        });

        btnstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(home.this, orderstatusActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}