package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class orderstatus_details extends AppCompatActivity {

    public static final String CDATE = "CDATE";
    public static final String CSTATUS = "CSTATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderstatus_details);
    }
}