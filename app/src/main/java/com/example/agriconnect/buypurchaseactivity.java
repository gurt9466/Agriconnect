package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class buypurchaseactivity extends AppCompatActivity {
    private static Button btnQuery;
    private static EditText edtqty;

    RadioGroup OrderOption;


    private static TextView btnclickhere2,address;

    private String ppname, ppharvest, pppqty, ppprice, aydi,farname,ppexpiry;

    private static String cItemcode = "";

    public static final String PNAME = "PNAME";
    public static final String PHARVEST = "PHARVEST";

    public static final String PEXPIREY = "PEXPIREY";
    public static final String Quantity = "Quantity";
    public static final String PPRICE = "PPRICE";
    public static final String PFNAME = "PFNAME";
    public static final String PFID = "PFID";
    public static final String ID = "ID";
    private static com.example.agriconnect.JSONParser jParser = new com.example.agriconnect.JSONParser();
    private static String urlHost = "https://agriconnect.me/product/uploadcart.php";

    private static String urladdress="https://agriconnect.me/product/selectaddress.php";

    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";


    ArrayList<String> list_conaddress;
    public static String Productid;
    public static String edtProductQTYY;
    public static String username;
    ImageView imageview;
    SharedPreferences sharedPreferences;


    private static TextView pid, pname, hard, qty, price,farnames,edate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buypurchaseactivity);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        edtqty = findViewById(R.id.editTextqty);
        imageview = findViewById(R.id.imageView3);
        OrderOption =findViewById(R.id.orderoptions);
        btnclickhere2 = findViewById(R.id.textViewclickhere3);



        new FetchImageUrlsTask().execute();
        new conaddress().execute();

        sharedPreferences = getSharedPreferences("Agriconnect", MODE_PRIVATE);
        username = (sharedPreferences.getString("username", ""));

        pid = (TextView) findViewById(R.id.textViewid);
        pname = (TextView) findViewById(R.id.textViewpname);
        hard = (TextView) findViewById(R.id.textViewhdate);
        qty = (TextView) findViewById(R.id.textViewqty);
        farnames = (TextView) findViewById(R.id.textViewfarmername);
        price = (TextView) findViewById(R.id.textViewprice);
        address = (TextView) findViewById(R.id.textViewdeliveryaddress2);
        edate = (TextView) findViewById(R.id.textViewedate2);

        Intent i = getIntent();
        ppname = i.getStringExtra(PNAME);
        ppharvest = i.getStringExtra(PHARVEST);
        ppexpiry = i.getStringExtra(PEXPIREY);
        pppqty = i.getStringExtra(Quantity);
        ppprice = i.getStringExtra(PPRICE);
        farname =i.getStringExtra(PFNAME);
        aydi = i.getStringExtra(ID);


        pid.setText(aydi);
        pname.setText(ppname);
        hard.setText(ppharvest);
        qty.setText(pppqty);
        edtqty.setText(pppqty);
        farnames.setText(farname);
        price.setText(ppprice);
        edate.setText(ppexpiry);


        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtProductQTYY = edtqty.getText().toString();
                Productid = pid.getText().toString();


                new uploadDataToURL().execute();


            }
        });

        btnclickhere2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(buypurchaseactivity.this, edtprofiles.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        String gens, civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(buypurchaseactivity.this);

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

                cv.put("username", username);
                cv.put("product_id", Productid);
                cv.put("product_qty", edtProductQTYY);
                cv.put("cart_price", price.getText().toString());

                RadioButton checkedBtn = findViewById(OrderOption.getCheckedRadioButtonId());
                String selectedoption = checkedBtn.getText().toString();
                cv.put("order_type", selectedoption);
                


                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    Intent intent = new Intent(buypurchaseactivity.this, purchaseActivity.class);
                    startActivity(intent);
                    finish();
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
            AlertDialog.Builder alert = new AlertDialog.Builder(buypurchaseactivity.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(buypurchaseactivity.this, s, Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }


    private class FetchImageUrlsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String imageUrl = null;
            try {
                // Retrieve the product ID from the intent
                Intent intent = getIntent();
                String productId = intent.getStringExtra(ID);


                // Construct the API URL with the product ID
                String apiUrl = "https://agriconnect.me/img/fetch_specific_img.php?id=" + productId;

                HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JSONArray jsonArray = new JSONArray(sb.toString());
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    imageUrl = jsonObject.getString("product_image");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imageUrl;
        }

        @Override
        protected void onPostExecute(String imageUrl) {
            if (imageUrl != null) {
                Glide.with(buypurchaseactivity.this).load(imageUrl).into(imageview);
            }
        }
    }

    private class conaddress extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(buypurchaseactivity.this);

        public conaddress() {
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

                cPostSQL = username;
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urladdress, "POST", cv);
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
        protected void onPostExecute(String proqtyes) {
            super.onPostExecute(proqtyes);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(buypurchaseactivity.this);
            if (proqtyes != null) {
                if (isEmpty.equals("") && !proqtyes.equals("HTTPSERVER_ERROR")) {
                }


                String pproqtyes = proqtyes;

                String str = pproqtyes;
                final String pproqtys[] = str.split("-");
                list_conaddress = new ArrayList<String>(Arrays.asList(pproqtys));

                String addressText = TextUtils.join(", ", list_conaddress);
                address.setText(addressText);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}

