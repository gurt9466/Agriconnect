package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
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
    ImageView imgeditpost, backimg;
    String requestproductname, requestqty, requestproductdate,username;
    CalendarView calendar;
    String valuecalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        rpname = findViewById(R.id.edtrequestname);
        rpprice = findViewById(R.id.edtestimateprice);
        rpdate = findViewById(R.id.edtrequestdateexpected);
        btnpost = findViewById(R.id.Rpostbtn);
        imgeditpost =findViewById(R.id.reditpostimg);
        backimg = findViewById(R.id.logout2);
        calendar =findViewById(R.id.calendarView);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Adjust month to display as two digits
                String formattedMonth = String.format("%02d", (month + 1));
                valuecalendar = year + "/" + formattedMonth + "/" + String.format("%02d", dayOfMonth);
                rpdate.setText(valuecalendar);
            }
        });

        sharedPreferences = getSharedPreferences("Agriconnect", MODE_PRIVATE);

        txtviewerror2 = findViewById(R.id.error2);


        if (sharedPreferences.getString("logged", "false").equals("false")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }
        username = (sharedPreferences.getString("username",""));


        imgeditpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requestActivity.this,edtrequest.class);
                startActivity(intent);
                finish();

            }
        });

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requestActivity.this,home.class);
                startActivity(intent);
                finish();

            }
        });



        imgeditpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requestActivity.this,edtrequest.class);
                startActivity(intent);
                finish();

            }
        });

        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestproductname = String.valueOf(rpname.getText());
                requestproductdate = String.valueOf(rpdate.getText());
                requestqty = String.valueOf(rpprice.getText());


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://agriconnect.me/request/request_product.php"; //host ip and phpfile

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response.equals("success")){
                                    Toast.makeText(getApplicationContext(),"Registration Successful!", Toast.LENGTH_SHORT).show();
                                }
                                else{txtviewerror2.setText(response);
                                    txtviewerror2.setVisibility(View.VISIBLE);
                                }
                                Intent intent = new Intent(getApplicationContext(),home.class);
                                startActivity(intent);
                                finish();
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
                        paramV.put("username",username);
                        paramV.put("request_product_name",requestproductname);
                        paramV.put("request_product_qty",requestqty);
                        paramV.put("request_product_date",requestproductdate);

                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });

    }
}
