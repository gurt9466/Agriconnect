package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
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
    EditText edttxtusername, edttxtlname, edttxtdob, edttxtaddressstreet, edttxtaddresscity, edttxtaddressregion, edttxtcontactnumber, edttxtemail, edttxtfname, edttxtpassword, edttxtrepassword;
    RadioGroup gender;
    Button cregister;
    String username, password, name_last, name_first, dob, email, address_street, address_city, address_region, contactnumber, repassword;
    TextView txtviewerror;
    ProgressBar progressBar;
    DatePicker calview;
    String valuecalendar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edttxtusername = findViewById(R.id.edtcusername);
        edttxtpassword = findViewById(R.id.edtcpasswd);
        edttxtrepassword = findViewById(R.id.edtcrepasswd2);
        edttxtlname = findViewById(R.id.edtclname);
        edttxtfname = findViewById(R.id.edtcfname);
        gender = findViewById(R.id.gender);
        edttxtdob = findViewById(R.id.cdob);
        edttxtemail = findViewById(R.id.edtcemail);
        edttxtaddressstreet = findViewById(R.id.edtcaddressstreet);
        edttxtaddresscity = findViewById(R.id.edtcaddresscity);
        edttxtaddressregion = findViewById(R.id.edtcaddressregion);
        edttxtcontactnumber = findViewById(R.id.edtccnumber);

        cregister = findViewById(R.id.Cregisterbtn);
        txtviewerror = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);
        calview = findViewById(R.id.datePicker);

        // Set an OnDateChangedListener for the DatePicker
        calview.init(calview.getYear(), calview.getMonth(), calview.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Adjust month to display as two digits
                        String formattedMonth = String.format("%02d", (monthOfYear + 1));
                        valuecalendar2 = year + "/" + formattedMonth + "/" + String.format("%02d", dayOfMonth);
                        edttxtdob.setText(valuecalendar2);
                    }
                });

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
                repassword = String.valueOf(edttxtrepassword.getText());
                password = String.valueOf(edttxtpassword.getText());
                RadioButton checkedBtn = findViewById(gender.getCheckedRadioButtonId());
                String selectedGender = checkedBtn.getText().toString();

                boolean doPasswordsMatch = password != null && repassword != null && password.trim().equals(repassword.trim());


                if (!isValidPassword(password, repassword)) {
                    progressBar.setVisibility(View.GONE);
                    txtviewerror.setVisibility(View.VISIBLE);
                    txtviewerror.setText("Invalid password. Minimum characters should be 6");
                    return;
                }
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://agriconnect.me/register/register.php"; //host ip and phpfile

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                if (response.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                                } else {
                                    txtviewerror.setText(response);
                                    txtviewerror.setVisibility(View.VISIBLE);
                                }
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        txtviewerror.setVisibility(View.VISIBLE);
                        txtviewerror.setText(error.getLocalizedMessage());
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("username", username);
                        paramV.put("name_last", name_last);
                        paramV.put("name_first", name_first);
                        paramV.put("gender", selectedGender);
                        paramV.put("dob", dob);
                        paramV.put("address_street", address_street);
                        paramV.put("address_city", address_city);
                        paramV.put("address_region", address_region);
                        paramV.put("contact_number", contactnumber);
                        paramV.put("email", email);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }

    private boolean isValidPassword(String password, String repassword) {
        boolean isLengthValid = password.length() >= 6;
        boolean doPasswordsMatch = password.trim().equals(repassword.trim());

        Log.d("Password Debug", "Do Passwords Match: " + doPasswordsMatch);

        return isLengthValid && doPasswordsMatch;
    }
}