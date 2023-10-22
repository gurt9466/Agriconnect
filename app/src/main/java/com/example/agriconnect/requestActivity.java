package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class requestActivity extends AppCompatActivity {
    EditText rpname,rpprice, rpdate;
    TextView txtviewerror2;
    Button btnpost;
    SharedPreferences sharedPreferences;
    String requestproductname, requestprice, requestproductdate,requestconsumer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        rpname = findViewById(R.id.edtrequestname);
        rpprice = findViewById(R.id.edtestimateprice);
        rpdate = findViewById(R.id.edtrequestdateexpected);
        btnpost = findViewById(R.id.Rpostbtn);

        txtviewerror2 = findViewById(R.id.error2);

        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestproductname = String.valueOf(rpname.getText());
                requestproductdate = String.valueOf(rpdate.getText());
                requestprice = String.valueOf(rpprice.getText());


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.11/agriconnect/php/request_product.php"; //host ip and phpfile

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response.equals("success")){
                                    Toast.makeText(getApplicationContext(),"Registration Successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),home.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{txtviewerror2.setText(response);
                                    txtviewerror2.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txtviewerror2.setVisibility(View.VISIBLE);
                        txtviewerror2.setText(error.getLocalizedMessage());
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("request_product_name",requestproductname);
                        paramV.put("request_product_price",requestprice);
                        paramV.put("request_product_date",requestproductdate);

                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });

    }
}