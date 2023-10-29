package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class editcartactivity extends AppCompatActivity {

    public static final String CARTDATEADDED = "CARTDATEADDED";
    public static final String CARTPRODUCTNAME = "CARTPRODUCTNAME";
    public static final String CARTPRODUCTID = "CARTPRODUCTID";
    public static final String CARTPRICE = "CARTPRICE";
    public static final String CARTQUANTITY = "CARTQUANTITY";
    public static final String CARTUSERNAME = "CARTUSERNAME";


    public static final String ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcartactivity);
    }
}