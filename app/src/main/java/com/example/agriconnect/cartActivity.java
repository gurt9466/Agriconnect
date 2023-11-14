package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;

public class cartActivity extends AppCompatActivity {

    private static com.example.agriconnect.JSONParser jParser = new com.example.agriconnect.JSONParser();
    SharedPreferences sharedPreferences;

    private static String urlHost = "http://192.168.19.31/Agriconnect/php/cart/selectcartusername.php";
    private static String urlcartqty = "http://192.168.19.31/Agriconnect/php/cart/selectcartqty.php";
    private static String urlcartprice = "http://192.168.19.31/Agriconnect/php/cart/selectcartprice.php";
    private static String urlcartproductid = "http://192.168.19.31/Agriconnect/php/cart/selectcartproductid.php";
    private static String urlcartproductname = "http://192.168.19.31/Agriconnect/php/cart/selectcartproductname.php";
    private static String urlcartdatedd = "http://192.168.19.31/Agriconnect/php/cart/selectcartdateadded.php";
    private static String urltotalA = "http://192.168.19.31/Agriconnect/php/cart/selecttotalamount.php";
    private static String urlHostID = "http://192.168.19.31/Agriconnect/php/cart/selectcartid.php";

    private static String uploadcheckout = "http://192.168.19.31/agriconnect/php/product/selectcheckout.php";

    private static String urlHostDelete = "http://192.168.19.31/Agriconnect/php/cart/delete.php";

    String urlimges = "http://192.168.19.31/agriconnect/php/img/cart_iamge.php";

    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String cItemcode = "";
    private static String online_dataset = "";
    private static ImageView btnQuery;
    private static Button checkout;
    private static TextView totalamount;
    private static EditText edtitemcode;
    ListView listView;
    TextView textView,txtDefault_ID, txtDefaultcartusername, txtDefaultcartquantity, txtDefaultcartprice, txtDefaultcartproductid,txtDefaultcartproductname,txtDefaultdateadded;
    ArrayAdapter<String> adapter_cartusername;
    ArrayAdapter<String> adapter_carttotalprice;
    ArrayAdapter<String> adapter_cartquantity;
    ArrayAdapter<String> adapter_cartprice;
    ArrayAdapter<String> adapter_cartproductid;
    ArrayAdapter<String> adapter_cartproductname;
    ArrayAdapter<String> adapter_dateadded;
    ArrayAdapter <String> adapter_ID;

    ArrayList<String> list_carttotalprice;
    ArrayList<String> list_cartdateadded;
    ArrayList<String> list_cartproductname;
    ArrayList <String> list_cartproductid;
    ArrayList <String> list_cartprice;
    ArrayList <String> list_cartquantity;
    ArrayList <String> list_cartusername;
    ArrayList <String> list_ID;

    ImageView backimgbtn;

    String totalprice,cltemSelected_cartusername,cItemSelected_ID, cltemSelected_cartquantity, cltemSelected_cartprice, cltemSelected_cartproductid,cltemSelected_cartproductname, cltemSelected_dateadded;
    Context context = this;
    private String Cart_Username, Cart_Quantity, Cart_price, Cart_productid,aydi,Cart_porductname,Cart_dateadded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        checkout = (Button) findViewById(R.id.button);
        btnQuery = (ImageView) findViewById(R.id.imgreload);
        edtitemcode = (EditText) findViewById(R.id.edtitemcode);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textView4);
        backimgbtn = findViewById(R.id.logout2);
        totalamount=(TextView) findViewById(R.id.textViewtotalamount);

        txtDefault_ID = (TextView) findViewById(R.id.txt_ID);
        txtDefaultcartusername = (TextView) findViewById(R.id.txt_cartusername);
        txtDefaultcartquantity = (TextView) findViewById(R.id.txt_cartqty);
        txtDefaultcartprice = (TextView) findViewById(R.id.txt_cartprice);
        txtDefaultcartproductid = (TextView) findViewById(R.id.txt_cartproductid);
        txtDefaultcartproductname = (TextView) findViewById(R.id.txt_cartproductname);
        txtDefaultdateadded = (TextView) findViewById(R.id.txt_cartdateadded);

        sharedPreferences = getSharedPreferences("Agriconnect", MODE_PRIVATE);
        String username = (sharedPreferences.getString("username",""));

        txtDefault_ID.setVisibility(View.GONE);
        txtDefaultcartusername.setVisibility(View.GONE);
        txtDefaultcartquantity.setVisibility(View.GONE);
        txtDefaultcartprice.setVisibility(View.GONE);
        txtDefaultcartproductid.setVisibility(View.GONE);
        txtDefaultcartproductname.setVisibility(View.GONE);
        txtDefaultdateadded.setVisibility(View.GONE);

        btnQuery.performClick();

        backimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cartActivity.this,home.class);
                startActivity(intent);
                finish();
            }
        });


        Toast.makeText(cartActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();


        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cItemcode = edtitemcode.getText().toString();

                new cartActivity.uploadDataToURL().execute();
                new cartActivity.CQUANTITY().execute();
                new cartActivity.CPRICE().execute();
                new cartActivity.CPRODUCTID().execute();
                new cartActivity.CPRODUCTNAME().execute();
                new cartActivity.CDateDD().execute();
                new cartActivity.id().execute();
                new cartActivity.TotalP().execute();
                FetchImageUrlsTask task = new FetchImageUrlsTask(username);
                task.execute(urlimges);

            }
        });
        btnQuery.performClick();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                cltemSelected_cartusername = adapter_cartusername.getItem(position);
                cltemSelected_cartquantity = adapter_cartquantity.getItem(position);
                cltemSelected_cartprice = adapter_cartprice.getItem(position);
                cltemSelected_cartproductid = adapter_cartproductid.getItem(position);
                cltemSelected_cartproductname = adapter_cartproductname.getItem(position);
                cltemSelected_dateadded = adapter_dateadded.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);

                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage(" Do you wish to continue ");

                alert_confirm.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        txtDefaultcartusername.setText(cltemSelected_cartusername);
                        txtDefaultcartquantity.setText(cltemSelected_cartquantity);
                        txtDefaultcartprice.setText(cltemSelected_cartprice);
                        txtDefaultcartproductid.setText(cltemSelected_cartproductid);
                        txtDefaultcartproductname.setText(cltemSelected_cartproductname);
                        txtDefaultdateadded.setText(cltemSelected_dateadded);
                        txtDefault_ID.setText(cItemSelected_ID);




                        Cart_Username = txtDefaultcartusername.getText().toString().trim();
                        Cart_Quantity = txtDefaultcartquantity.getText().toString().trim();
                        Cart_price = txtDefaultcartprice.getText().toString().trim();
                        Cart_productid = txtDefaultcartproductid.getText().toString().trim();
                        Cart_porductname = txtDefaultcartproductname.getText().toString().trim();
                        Cart_dateadded = txtDefaultdateadded.getText().toString().trim();
                        aydi = txtDefault_ID.getText().toString().trim();


                        Intent intent = new Intent(cartActivity.this, editcartactivity.class);
                        intent.putExtra(editcartactivity.CARTUSERNAME, Cart_Username);
                        intent.putExtra(editcartactivity.CARTQUANTITY, Cart_Quantity);
                        intent.putExtra(editcartactivity.CARTPRICE, Cart_price);
                        intent.putExtra(editcartactivity.CARTPRODUCTID, Cart_productid);
                        intent.putExtra(editcartactivity.CARTPRODUCTNAME, Cart_porductname);
                        intent.putExtra(editcartactivity.CARTDATEADDED, Cart_dateadded);
                        intent.putExtra(editcartactivity.ID, aydi);
                        startActivity(intent);
                    }
                });

                alert_confirm.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtDefault_ID.setText(cItemSelected_ID);
                        aydi = txtDefault_ID.getText().toString().trim();
                        new cartActivity.delete().execute();

                    }
                });
                alert_confirm.show();

            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cartActivity.this,purchaseActivity.class);
                startActivity(intent);
                finish();
                new cartActivity.CHECKOUTF().execute();
            }
        });
    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {

        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(cartActivity.this);

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
                cPostSQL = (sharedPreferences.getString("username",""));
                cv.put("code", cPostSQL);

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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }
                //toast.makeText(act_buy_manage.this, s, Toast.LENGTH_SHORT).show();
                String wew = s;

                String str = wew;
                final String cartusername[] = str.split("-");
                list_cartusername = new ArrayList<String>(Arrays.asList(cartusername));
                adapter_cartusername = new ArrayAdapter<String>(cartActivity.this,
                        android.R.layout.simple_list_item_1,list_cartusername);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class CQUANTITY extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(cartActivity.this);

        public CQUANTITY() {
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

                cPostSQL = (sharedPreferences.getString("username",""));
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlcartqty, "POST", cv);
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


            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String cqty) {
            super.onPostExecute(cqty);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity.this);
            if (cqty != null) {
                if (isEmpty.equals("") && !cqty.equals("HTTPSERVER_ERROR")) { }


                String ccqty = cqty;

                String str = ccqty;
                final String ccqtys[] = str.split("-");
                list_cartquantity = new ArrayList<String>(Arrays.asList(ccqtys));
                adapter_cartquantity = new ArrayAdapter<String>(cartActivity.this,
                        android.R.layout.simple_list_item_1,list_cartquantity);

            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class CPRICE extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(cartActivity.this);

        public CPRICE() {
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

                cPostSQL = (sharedPreferences.getString("username",""));
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlcartprice, "POST", cv);
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


            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String cpprice) {
            super.onPostExecute(cpprice);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity.this);
            if (cpprice != null) {
                if (isEmpty.equals("") && !cpprice.equals("HTTPSERVER_ERROR")) { }


                String ccpprice = cpprice;

                String str = ccpprice;
                final String ccpprices[] = str.split("-");
                list_cartprice = new ArrayList<String>(Arrays.asList(ccpprices));
                adapter_cartprice = new ArrayAdapter<String>(cartActivity.this,
                        android.R.layout.simple_list_item_1,list_cartprice);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class CPRODUCTID extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(cartActivity.this);

        public CPRODUCTID() {
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

                cPostSQL = (sharedPreferences.getString("username",""));
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlcartproductid, "POST", cv);
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


            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String cproductID) {
            super.onPostExecute(cproductID);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity.this);
            if (cproductID != null) {
                if (isEmpty.equals("") && !cproductID.equals("HTTPSERVER_ERROR")) { }


                String cCproductID = cproductID;

                String str = cCproductID;
                final String cCproductIDs[] = str.split("-");
                list_cartproductid = new ArrayList<String>(Arrays.asList(cCproductIDs));
                adapter_cartproductid = new ArrayAdapter<String>(cartActivity.this,
                        android.R.layout.simple_list_item_1,list_cartproductid);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class CPRODUCTNAME extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(cartActivity.this);

        public CPRODUCTNAME() {
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

                cPostSQL = (sharedPreferences.getString("username",""));
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlcartproductname, "POST", cv);
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


            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String cproductN) {
            super.onPostExecute(cproductN);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity.this);
            if (cproductN != null) {
                if (isEmpty.equals("") && !cproductN.equals("HTTPSERVER_ERROR")) { }


                String CproductN = cproductN;

                String str = CproductN;
                final String CproductNs[] = str.split("-");
                list_cartproductname = new ArrayList<String>(Arrays.asList(CproductNs));
                adapter_cartproductname = new ArrayAdapter<String>(cartActivity.this,
                        android.R.layout.simple_list_item_1,list_cartproductname);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class CDateDD extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(cartActivity.this);

        public CDateDD() {
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

                cPostSQL = (sharedPreferences.getString("username",""));
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlcartdatedd, "POST", cv);
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


            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String cartDateDD) {
            super.onPostExecute(cartDateDD);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity.this);
            if (cartDateDD != null) {
                if (isEmpty.equals("") && !cartDateDD.equals("HTTPSERVER_ERROR")) { }


                String cCartDateDD = cartDateDD;

                String str = cCartDateDD;
                final String cCartDateDDs[] = str.split("-");
                list_cartdateadded = new ArrayList<String>(Arrays.asList(cCartDateDDs));
                adapter_dateadded = new ArrayAdapter<String>(cartActivity.this,
                        android.R.layout.simple_list_item_1,list_cartdateadded);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class TotalP extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(cartActivity.this);

        public TotalP() {
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

                cPostSQL = (sharedPreferences.getString("username",""));
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urltotalA, "POST", cv);
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


            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String tootalp) {
            super.onPostExecute(tootalp);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity.this);
            if (tootalp != null) {
                if (isEmpty.equals("") && !tootalp.equals("HTTPSERVER_ERROR")) { }
                String otalprice = tootalp;
                totalamount.setText(otalprice);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }


    private class CHECKOUTF extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        String gens, civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(cartActivity.this);

        public CHECKOUTF() {
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

                sharedPreferences = getSharedPreferences("Agriconnect", MODE_PRIVATE);
                String username = (sharedPreferences.getString("username",""));

                cv.put("username", username);


                JSONObject json = jParser.makeHTTPRequest(uploadcheckout, "POST", cv);
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
            AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(cartActivity.this, s, Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class id extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(cartActivity.this);

        public id() {
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

                cPostSQL = (sharedPreferences.getString("username",""));
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostID, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1){
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
        protected void onPostExecute(String aydi) {
            super.onPostExecute(aydi);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !aydi.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(cartActivity.this, "Data selected", Toast.LENGTH_SHORT).show();

                String AYDI = aydi;

                String str = AYDI;
                final String ayds[] = str.split("-");
                list_ID = new ArrayList<String>(Arrays.asList(ayds));
                adapter_ID = new ArrayAdapter<String>(cartActivity.this,
                        android.R.layout.simple_list_item_1,list_ID);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class delete extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(cartActivity.this);

        public delete() {
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

                cPostSQL = cItemSelected_ID;
                cv.put("id", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostDelete, "POST", cv);
                if (json != null) {
                    nSuccess =json.getInt(TAG_SUCCESS);
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
        protected void onPostExecute(String del) {
            super.onPostExecute(del);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(cartActivity.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !del.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(cartActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class FetchImageUrlsTask extends AsyncTask<String, Void, List<String>> {
        private String username;

        public FetchImageUrlsTask(String username) {
            this.username = username;
        }

        @Override
        protected List<String> doInBackground(String... urls) {
            String url = urls[0] + "?username=" + username;
            List<String> imageUrls = new ArrayList<>();
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JSONArray jsonArray = new JSONArray(sb.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    imageUrls.add(jsonObject.getString("product_image"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imageUrls;
        }

        @Override
        protected void onPostExecute(List<String> imageUrls) {
            CustomListAdapter adapter = new CustomListAdapter(cartActivity.this, list_cartquantity, list_cartprice, list_cartproductname, list_ID, imageUrls);
            listView.setAdapter(adapter);
        }
    }



    private class CustomListAdapter extends BaseAdapter {
        private Context context;

        private ArrayList<String> cartqty;
        private ArrayList<String> cartprice;
        private ArrayList<String> cartproductname;
        private ArrayList<String> cartaydi;
        private List<String> imageUrls;


        public CustomListAdapter(Context context, ArrayList<String> cartqty, ArrayList<String> cartprice, ArrayList<String> cartproductname, ArrayList<String> cartaydi,List<String> imageUrls) {
            this.context = context;
            this.cartqty = cartqty;
            this.cartprice = cartprice;
            this.cartproductname = cartproductname;
            this.cartaydi = cartaydi;
            this.imageUrls = imageUrls; // Initialize image URLs

        }


        @Override
        public int getCount() {
            return cartproductname.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listViewItem = inflater.inflate(R.layout.custom_list_item_3, null);

            TextView cartproductnameTextView = listViewItem.findViewById(R.id.productnameTextView);
            TextView cartQtyTextView = listViewItem.findViewById(R.id.pproqtysTextView);
            TextView cartpriceTextView = listViewItem.findViewById(R.id.ProductpsTextView);
            TextView cartaydiTextView = listViewItem.findViewById(R.id.adyiview2);
            ImageView imageView = listViewItem.findViewById(R.id.imageView4); // ImageView for the image

            cartproductnameTextView.setText(cartproductname.get(position));
            cartQtyTextView.setText(cartqty.get(position));
            cartpriceTextView.setText(cartprice.get(position));
            cartaydiTextView.setText(cartaydi.get(position));


            if (imageUrls != null && imageUrls.size() > position) {
                String imageUrl = imageUrls.get(position);
                Glide.with(context).load(imageUrl).into(imageView);
            }

            return listViewItem;
        }
    }
}