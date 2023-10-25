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
    private static EditText edtproduct, edtrprice, edtrdate,edtusername;
    private static TextView tvproduct, tvrprice,tvusername,tvrdate, txt1,txt2,txt3,txt4,txt5,txt6;
    private String rusername, rproduct,rprice,rdate, aydi;

    public static final String USERNAME = "USERNAME";
    public static final String RPRODUCT = "RPRODUCT";
    public static final String RQTY = "RQTY";
    public static final String RDATE = "RDATE";

    public static final String ID = "ID";

    private static TextView tv_civ;
    private static com.example.agriconnect.JSONParser jParser = new com.example.agriconnect.JSONParser();
    private static String urlHost = "http://192.168.1.9/agriconnect/php/UpdateQty.php";

    private static String TAG_MESSAGE = "message" , TAG_SUCCESS = "success";
    private static String online_dataset = "";

    public static String Requestusername;
    public static String RequestProduct;
    public static String Requestprice;
    public static String Requestdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edtrequest_records);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        edtproduct = findViewById(R.id.editrequestproductname);
        edtrprice = findViewById(R.id.editrequestprice);
        edtrdate = findViewById(R.id.editrequestdate);

        tvusername = (TextView) findViewById(R.id.edtcfname);
        tvproduct = (TextView) findViewById(R.id.edtclname);
        tvrdate = (TextView) findViewById(R.id.editpname);
        tvrprice = (TextView) findViewById(R.id.edtpqty);


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
        rprice = i.getStringExtra(RQTY);
        rdate = i.getStringExtra(RDATE);
        aydi = i.getStringExtra(ID);



        edtproduct.setText(rproduct);
        edtrprice.setText(rprice);
        edtrdate.setText(rdate);


        tvusername.setVisibility(View.GONE);
        tvproduct.setVisibility(View.GONE);
        tvrdate.setVisibility(View.GONE);
        tvrprice.setVisibility(View.GONE);

        txt1.setVisibility(View.GONE);
        txt2.setVisibility(View.GONE);
        txt3.setVisibility(View.GONE);
        txt4.setVisibility(View.GONE);
        txt5.setVisibility(View.GONE);
        txt6.setVisibility(View.GONE);


        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Requestusername = tvusername.getText().toString();
                RequestProduct = edtproduct.getText().toString();
                Requestdate = edtrdate.getText().toString();
                Requestprice = edtrprice.getText().toString();



                new uploadDataToURL().execute();
            }
        });



    }

    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
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
                //insert anything in this cod

               // cPostSQL = Requestusername;
               // cv.put("username", cPostSQL);

                cPostSQL = aydi;
                cv.put("id", cPostSQL);

                cPostSQL = " '" + RequestProduct + "' ";
                cv.put("request_product_name", cPostSQL);

                cPostSQL = " '" + Requestprice + "' ";
                cv.put("request_product_price", cPostSQL);

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