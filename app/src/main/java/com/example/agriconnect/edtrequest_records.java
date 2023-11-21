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

public class edtrequest_records extends AppCompatActivity {


    private static Button btnQuery;
    private static EditText edtproduct, edtrqty, edtrdate;
    private static TextView tvproduct, tvrqty,tvusername,tvrdate, txt1,txt2,txt3,txt4,txt5,txt6;
    private String rusername, rproduct,rqty,rdate, aydi;

    public static final String USERNAME = "USERNAME";
    public static final String RPRODUCT = "RPRODUCT";
    public static final String RQTY = "RQTY";
    public static final String RDATE = "RDATE";

    public static final String ID = "ID";

    private static TextView tv_civ;
    private static com.example.agriconnect.JSONParser jParser = new com.example.agriconnect.JSONParser();
    private static String urlHost = "https://hotela9barnala.net/request/UpdateQty.php";

    private static String TAG_MESSAGE = "message" , TAG_SUCCESS = "success";
    private static String online_dataset = "";

    public static String RequestProduct;
    public static String Requestqty;
    public static String Requestdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edtrequest_records);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        edtproduct = findViewById(R.id.editrequestproductname);
        edtrqty = findViewById(R.id.editrequestprice);
        edtrdate = findViewById(R.id.editrequestdate);


        tvusername = (TextView) findViewById(R.id.edtcfname);
        tvproduct = (TextView) findViewById(R.id.edtclname);
        tvrdate = (TextView) findViewById(R.id.editpname);
        tvrqty = (TextView) findViewById(R.id.edtpqty);


        tv_civ = (TextView) findViewById(R.id.textView3);

        txt1 = (TextView) findViewById(R.id.tp);
        txt2 = (TextView) findViewById(R.id.textView4);
        txt3 = (TextView) findViewById(R.id.textView2);
        txt4 = (TextView) findViewById(R.id.dop2);
        txt5 = (TextView) findViewById(R.id.dop);
        txt6 = (TextView) findViewById(R.id.textView3);


        Intent i = getIntent();
        rusername = i.getStringExtra(USERNAME);
        rproduct = i.getStringExtra(RPRODUCT);
        rqty = i.getStringExtra(RQTY);
        rdate = i.getStringExtra(RDATE);
        aydi = i.getStringExtra(ID);




        edtproduct.setText(rproduct);
        edtrqty.setText(rqty);
        edtrdate.setText(rdate);



        tvusername.setVisibility(View.GONE);
        tvproduct.setVisibility(View.GONE);
        tvrdate.setVisibility(View.GONE);
        tvrqty.setVisibility(View.GONE);

        txt1.setVisibility(View.GONE);
        txt2.setVisibility(View.GONE);
        txt3.setVisibility(View.GONE);
        txt4.setVisibility(View.GONE);
        txt5.setVisibility(View.GONE);
        txt6.setVisibility(View.GONE);




        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestProduct = edtproduct.getText().toString();
                Requestdate = edtrdate.getText().toString();
                Requestqty = edtrqty.getText().toString();



                new uploadDataToURL().execute();

                Intent intent = new Intent(edtrequest_records.this,edtrequest.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        String gens,civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtrequest_records.this);

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

                cPostSQL = " '" + RequestProduct + "' ";
                cv.put("request_product_name", cPostSQL);

                cPostSQL = " '" + Requestqty + "' ";
                cv.put("request_product_qty", cPostSQL);

                cPostSQL = " '" + Requestdate + "' ";
                cv.put("request_product_date",cPostSQL);





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
            AlertDialog.Builder alert = new AlertDialog.Builder(edtrequest_records.this);
            if (s !=null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(edtrequest_records.this, s , Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}