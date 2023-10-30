package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class editcartactivity extends AppCompatActivity {

    private static Button btnQuery;
    private static EditText edtqty;


    private static TextView tvproductname, tvharvestdate, tvpqty, tvprice, tvpfid, tvid;

    private String cartuname, cartqty, cartprice, cartproid, cartproname,cartdateadded,aydi;

    public static final String CARTUSERNAME = "CARTUSERNAME";
    public static final String CARTQUANTITY = "CARTQUANTITY";
    public static final String CARTPRICE = "CARTPRICE";
    public static final String CARTPRODUCTID = "CARTPRODUCTID";
    public static final String CARTPRODUCTNAME = "CARTPRODUCTNAME";
    public static final String CARTDATEADDED = "CARTDATEADDED";

    public static final String ID = "ID";

    private static TextView tv_civ, tvusername;
    private static com.example.agriconnect.JSONParser jParser = new com.example.agriconnect.JSONParser();
    private static String urlHost = "http://192.168.1.4/agriconnect/php/cart/updatecartqty.php";

    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";

    public static String edtProductQTYY;
    public static String username;
    SharedPreferences sharedPreferences;


    private static TextView pid, pname, hard, qty, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcartactivity);

        btnQuery = (Button) findViewById(R.id.btnQuery);
        edtqty = findViewById(R.id.editTextqty);

        sharedPreferences = getSharedPreferences("Agriconnect", MODE_PRIVATE);
        username =(sharedPreferences.getString("username",""));

        pid = (TextView) findViewById(R.id.textViewid);
        pname = (TextView) findViewById(R.id.textViewpname);
        hard = (TextView) findViewById(R.id.textViewhdate);
        qty = (TextView) findViewById(R.id.textViewqty);
        price = (TextView) findViewById(R.id.textViewprice);





        tvproductname = (TextView) findViewById(R.id.textViewedtpname);
        tvharvestdate = (TextView) findViewById(R.id.textViewedtharvestd);
        tvpqty = (TextView) findViewById(R.id.textViewedtpqty);
        tvprice = (TextView) findViewById(R.id.textViewedtprice);
        tvpfid = (TextView) findViewById(R.id.textViewedtfid);
        tvid = (TextView) findViewById(R.id.textViewedtid);


        tv_civ = (TextView) findViewById(R.id.textView3);


        Intent i = getIntent();
        cartuname = i.getStringExtra(CARTUSERNAME);
        cartqty = i.getStringExtra(CARTQUANTITY);
        cartprice = i.getStringExtra(CARTPRICE);
        cartproid = i.getStringExtra(CARTPRODUCTID);
        cartproname = i.getStringExtra(CARTPRODUCTNAME);
        cartdateadded = i.getStringExtra(CARTDATEADDED);
        aydi = i.getStringExtra(ID);


        pid.setText(aydi);
        pname.setText(cartproname);
        qty.setText(cartqty);
        edtqty.setText(cartqty);
        price.setText(cartprice);


        tvproductname.setVisibility(View.GONE);
        tvharvestdate.setVisibility(View.GONE);
        tvpqty.setVisibility(View.GONE);
        tvprice.setVisibility(View.GONE);
        tvpfid.setVisibility(View.GONE);
        tvid.setVisibility(View.GONE);




        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtProductQTYY = edtqty.getText().toString();



                new editcartactivity.uploadDataToURL().execute();

                Intent intent = new Intent(editcartactivity.this, cartActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        String gens, civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(editcartactivity.this);

        public uploadDataToURL() {
        }

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

                cPostSQL = " '" + edtProductQTYY + "' ";
                cv.put("product_qty", cPostSQL);


                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if (json != null) {
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
            AlertDialog.Builder alert = new AlertDialog.Builder(editcartactivity.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(editcartactivity.this, s, Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}