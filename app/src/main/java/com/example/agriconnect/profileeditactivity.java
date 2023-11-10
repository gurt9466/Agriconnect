package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class profileeditactivity extends AppCompatActivity {

    private static Button btnQuery;
    private static EditText fname,lname,dob,email,adressst,adressct,adressregion,contactnumber;
    private static TextView username,customerid;
    private String ccontactnumber, cadressregion,cadressct,cadressst,cemail,cdobc,clnamec,cfname,cusername, aydi;

    public static final String USERNAME = "USERNAME";
    public static final String CONSUMERMAIL = "CONSUMERMAIL";
    public static final String PNAMELAST = "PNAMELAST";
    public static final String PNAMEFIRST = "PNAMEFIRST";
    public static final String PDOB = "PDOB";
    public static final String PADDRESSSTREET = "PADDRESSSTREET";
    public static final String PADDRESSCITY = "PADDRESSCITY";
    public static final String PADDRESSREGION = "PADDRESSREGION";
    public static final String PCONTACTNUMBER = "PCONTACTNUMBER";

    public static final String ID = "ID";

    private static TextView tv_civ;
    private static com.example.agriconnect.JSONParser jParser = new com.example.agriconnect.JSONParser();
    private static String urlHost = "http://192.168.1.6/agriconnect/php/register/updateprofile.php";

    private static String TAG_MESSAGE = "message" , TAG_SUCCESS = "success";
    private static String online_dataset = "";

    public static String Consumerfname,Consumerlname,Consumeremail,Consumersreet,Consumercity,Consumerregion,Consumercontactnumber,Consumerdob;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileeditactivity);
        btnQuery = (Button) findViewById(R.id.Cregisterbtn);
        username = findViewById(R.id.edtcusername);
        fname = findViewById(R.id.edtcfname);
        lname =findViewById(R.id.edtclname);
        dob = findViewById(R.id.cdob);
        email =findViewById(R.id.edtcemail);
        adressst = findViewById(R.id.edtcaddressstreet);
        adressct =findViewById(R.id.edtcaddresscity);
        adressregion = findViewById(R.id.edtcaddressregion);
        contactnumber =findViewById(R.id.edtccnumber);
        customerid = findViewById(R.id.customerid);


        Intent i = getIntent();
        cusername = i.getStringExtra(USERNAME);
        cfname = i.getStringExtra(PNAMEFIRST);
        clnamec = i.getStringExtra(PNAMELAST);
        cdobc = i.getStringExtra(PDOB);
        cemail = i.getStringExtra(CONSUMERMAIL);
        cadressst = i.getStringExtra(PADDRESSSTREET);
        cadressct = i.getStringExtra(PADDRESSCITY);
        cadressregion = i.getStringExtra(PCONTACTNUMBER);
        ccontactnumber = i.getStringExtra(PCONTACTNUMBER);
        aydi = i.getStringExtra(ID);



        customerid.setText(aydi);
        username.setText(cusername);
        fname.setText(cfname);
        lname.setText(clnamec);
        dob.setText(cdobc);
        email.setText(cemail);
        adressst.setText(cadressst);
        adressct.setText(cadressct);
        adressregion.setText(cadressregion);
        contactnumber.setText(ccontactnumber);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Consumerfname = fname.getText().toString();
                Consumerlname = lname.getText().toString();
                Consumeremail = email.getText().toString();
                Consumersreet = adressst.getText().toString();
                Consumercity = adressct.getText().toString();
                Consumerregion = adressregion.getText().toString();
                Consumercontactnumber = contactnumber.getText().toString();
                Consumerdob = dob.getText().toString();




                new profileeditactivity.uploadDataToURL().execute();

                Intent intent = new Intent(profileeditactivity.this,edtprofiles.class);
                startActivity(intent);
                finish();
            }
        });



    }

    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        String gens,civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(profileeditactivity.this);

        public uploadDataToURL() { }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();


                cPostSQL = aydi;
                cv.put("id", cPostSQL);

                cPostSQL = " '" + username + "' ";
                cv.put("username", cPostSQL);

                cPostSQL = " '" + Consumerfname + "' ";
                cv.put("name_first", cPostSQL);

                cPostSQL = " '" + Consumerlname + "' ";
                cv.put("name_last",cPostSQL);

                cPostSQL = " '" + Consumeremail + "' ";
                cv.put("email", cPostSQL);

                cPostSQL = " '" + Consumersreet + "' ";
                cv.put("address_street", cPostSQL);

                cPostSQL = " '" + Consumercity + "' ";
                cv.put("address_city",cPostSQL);

                cPostSQL = " '" + Consumerregion + "' ";
                cv.put("address_region", cPostSQL);

                cPostSQL = " '" + Consumercontactnumber + "' ";
                cv.put("contact_number",cPostSQL);

                cPostSQL = " '" + Consumerdob + "' ";
                cv.put("dob",cPostSQL);




                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST" , cv);
                if(json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            AlertDialog.Builder alert = new AlertDialog.Builder(profileeditactivity.this);
            if (s !=null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(profileeditactivity.this, s , Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}