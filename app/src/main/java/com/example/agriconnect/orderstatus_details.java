package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class orderstatus_details extends AppCompatActivity {
    private static com.example.agriconnect.JSONParser jParser = new com.example.agriconnect.JSONParser();
    SharedPreferences sharedPreferences;

    private static String urlHost = "https://agriconnect.me/orders/selectorderusername.php";//username
    private static String urlcartqty = "https://agriconnect.me/orders/selectorderqty.php";// qty
    private static String urlcartprice = "https://agriconnect.me/orders/selectorderprice.php";// price
    private static String urlHostID = "https://agriconnect.me/orders/selectorderproductid.php";// product id
    private static String urlcartproductname = "https://agriconnect.me/orders/selectorderproduct.php";// product name

    private static String urlfarmername3 = "https://agriconnect.me/orders/selectorderfname.php";// product name

    private static String urltotalA = "https://agriconnect.me/orders/selectordertotalamount.php"; //total amount

    private static  String urlordertype ="https://agriconnect.me/orders/selectordertyp.php";
    private static  String urlorderstatus ="https://agriconnect.me/orders/selectorderstatus.php";

    private static String urlorderstatus_details = "https://agriconnect.me/orders/orderreceived.php";

    private static String urlorderid = "https://agriconnect.me/orders/orderid.php";

    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";

    public static final String CDATE = "CDATE";
    public static final String CORDERID = "CORDERID";

    ListView listView;
    ImageView backimgbtn;
    private static String cItemcode = "";
    private static EditText edtitemcode;
    private static ImageView btnQuery;
    private static TextView totalamount ,sample;

    ArrayAdapter<String> adapter_cartusername;

    ArrayAdapter<String> adapter_farmernamestatus;
    ArrayAdapter<String> adapter_cartquantity;
    ArrayAdapter<String> adapter_cartprice;
    ArrayAdapter<String> adapter_cartproductname;
    ArrayAdapter <String> adapter_ID;
    ArrayAdapter <String> adapter_orderstatus;
    ArrayAdapter <String> adapter_ordertype;

    ArrayAdapter <String> adapter_orderid;
    ArrayList<String> list_cartproductname;

    ArrayList<String> list_orderid;

    ArrayList<String> list_farmernamestatus;
    ArrayList<String> list_cartordertype;
    ArrayList <String> list_cartprice;
    ArrayList <String> list_cartorderstatus;
    ArrayList <String> list_cartquantity;
    ArrayList <String> list_cartusername;
    ArrayList <String> list_ID;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderstatus_details);

        btnQuery = (ImageView) findViewById(R.id.imgreload);//auto querry
        edtitemcode = (EditText) findViewById(R.id.edtitemcode);// searchbar
        listView = (ListView) findViewById(R.id.listview);//listview
        backimgbtn = findViewById(R.id.logout2);// go to previous
        totalamount=(TextView) findViewById(R.id.textViewtotalamount);// tp



        sharedPreferences = getSharedPreferences("Agriconnect", MODE_PRIVATE);
        String username = (sharedPreferences.getString("username",""));
        Intent i = getIntent();
        date = i.getStringExtra(CDATE);




        btnQuery.performClick();

        backimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(orderstatus_details.this,orderstatusActivity.class);
                startActivity(intent);
                finish();
            }
        });




        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cItemcode = edtitemcode.getText().toString();

                new orderstatus_details.uploadDataToURL().execute();// username
                new orderstatus_details.CQUANTITY().execute();// qty
                new orderstatus_details.CPRICE().execute();// price
                new orderstatus_details.CPRODUCTNAME().execute();// product name
                new orderstatus_details.OrderType().execute();// product_id
                new orderstatus_details.Orderstatus().execute();// product_id
                new orderstatus_details.OrderId().execute();// product_id
                new orderstatus_details.FARMERNAME().execute();// farmername
                new orderstatus_details.id().execute();// product_id
                new orderstatus_details.TotalP().execute();// total price


            }
        });
        btnQuery.performClick();

    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {

        String cPOST = "", cPostSQL = "",cPostSQL2 = "",cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(orderstatus_details.this);

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
                cPostSQL2 = date;
                cv.put("code", cPostSQL);
                cv.put("code2", cPostSQL2);

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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatus_details.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }

                String wew = s;

                String str = wew;
                final String cartusername[] = str.split("-");
                list_cartusername = new ArrayList<String>(Arrays.asList(cartusername));
                adapter_cartusername = new ArrayAdapter<String>(orderstatus_details.this,
                        android.R.layout.simple_list_item_1,list_cartusername);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class CQUANTITY extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "",cPostSQL2 = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(orderstatus_details.this);

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
                cPostSQL2 = date;
                cv.put("code", cPostSQL);
                cv.put("code2", cPostSQL2);
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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatus_details.this);
            if (cqty != null) {
                if (isEmpty.equals("") && !cqty.equals("HTTPSERVER_ERROR")) { }


                String ccqty = cqty;

                String str = ccqty;
                final String ccqtys[] = str.split("-");
                list_cartquantity = new ArrayList<String>(Arrays.asList(ccqtys));
                adapter_cartquantity = new ArrayAdapter<String>(orderstatus_details.this,
                        android.R.layout.simple_list_item_1,list_cartquantity);

            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class CPRICE extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "",cPostSQL2 = "" ,cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(orderstatus_details.this);

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
                cPostSQL2 = date;
                cv.put("code", cPostSQL);
                cv.put("code2", cPostSQL2);

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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatus_details.this);
            if (cpprice != null) {
                if (isEmpty.equals("") && !cpprice.equals("HTTPSERVER_ERROR")) { }


                String ccpprice = cpprice;

                String str = ccpprice;
                final String ccpprices[] = str.split("-");
                list_cartprice = new ArrayList<String>(Arrays.asList(ccpprices));
                adapter_cartprice = new ArrayAdapter<String>(orderstatus_details.this,
                        android.R.layout.simple_list_item_1,list_cartprice);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }


    private class CPRODUCTNAME extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "",cPostSQL2 = "" ,cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(orderstatus_details.this);

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
                cPostSQL2 = date;
                cv.put("code", cPostSQL);
                cv.put("code2", cPostSQL2);

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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatus_details.this);
            if (cproductN != null) {
                if (isEmpty.equals("") && !cproductN.equals("HTTPSERVER_ERROR")) { }


                String CproductN = cproductN;

                String str = CproductN;
                final String CproductNs[] = str.split("-");
                list_cartproductname = new ArrayList<String>(Arrays.asList(CproductNs));
                adapter_cartproductname = new ArrayAdapter<String>(orderstatus_details.this,
                        android.R.layout.simple_list_item_1,list_cartproductname);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class OrderType extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cPostSQL2 ="",cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(orderstatus_details.this);

        public OrderType() {
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
                cPostSQL2 = date;
                cv.put("code", cPostSQL);
                cv.put("code2", cPostSQL2);


                JSONObject json = jParser.makeHTTPRequest(urlordertype, "POST", cv);
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
        protected void onPostExecute(String cartordERtype) {
            super.onPostExecute(cartordERtype);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatus_details.this);
            if (cartordERtype != null) {
                if (isEmpty.equals("") && !cartordERtype.equals("HTTPSERVER_ERROR")) { }


                String Cordertype = cartordERtype;

                String str = Cordertype;
                final String Cordertypes[] = str.split("-");
                list_cartordertype = new ArrayList<String>(Arrays.asList(Cordertypes));
                adapter_ordertype = new ArrayAdapter<String>(orderstatus_details.this,
                        android.R.layout.simple_list_item_1,list_cartordertype);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class OrderId extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cPostSQL2 ="",cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(orderstatus_details.this);

        public OrderId() {
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
                cPostSQL2 = date;
                cv.put("code", cPostSQL);
                cv.put("code2", cPostSQL2);


                JSONObject json = jParser.makeHTTPRequest(urlorderid, "POST", cv);
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
        protected void onPostExecute(String cartordERid) {
            super.onPostExecute(cartordERid);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatus_details.this);
            if (cartordERid != null) {
                if (isEmpty.equals("") && !cartordERid.equals("HTTPSERVER_ERROR")) { }


                String Corderid = cartordERid;

                String str = Corderid;
                final String idordertypes[] = str.split("-");
                list_orderid = new ArrayList<String>(Arrays.asList(idordertypes));
                adapter_orderid = new ArrayAdapter<String>(orderstatus_details.this,
                        android.R.layout.simple_list_item_1,list_orderid);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class Orderstatus extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cPostSQL2 ="",cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(orderstatus_details.this);

        public Orderstatus() {
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
                cPostSQL2 = date;
                cv.put("code", cPostSQL);
                cv.put("code2", cPostSQL2);


                JSONObject json = jParser.makeHTTPRequest(urlorderstatus, "POST", cv);
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
        protected void onPostExecute(String cartordERstatus) {
            super.onPostExecute(cartordERstatus);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatus_details.this);
            if (cartordERstatus != null) {
                if (isEmpty.equals("") && !cartordERstatus.equals("HTTPSERVER_ERROR")) { }


                String Corderstatus = cartordERstatus;

                String str = Corderstatus;
                final String Corderstatuss[] = str.split("-");
                list_cartorderstatus = new ArrayList<String>(Arrays.asList(Corderstatuss));
                adapter_orderstatus = new ArrayAdapter<String>(orderstatus_details.this,
                        android.R.layout.simple_list_item_1,list_cartorderstatus);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class FARMERNAME extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cPostSQL2 ="",cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(orderstatus_details.this);

        public FARMERNAME() {
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
                cPostSQL2 = date;
                cv.put("code", cPostSQL);
                cv.put("code2", cPostSQL2);


                JSONObject json = jParser.makeHTTPRequest(urlfarmername3, "POST", cv);
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
        protected void onPostExecute(String farmerorderSTATUS) {
            super.onPostExecute(farmerorderSTATUS);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatus_details.this);
            if (farmerorderSTATUS != null) {
                if (isEmpty.equals("") && !farmerorderSTATUS.equals("HTTPSERVER_ERROR")) { }


                String farmerORderstatus = farmerorderSTATUS;

                String str = farmerORderstatus;
                final String Corderstatuss[] = str.split("-");
                list_farmernamestatus = new ArrayList<String>(Arrays.asList(Corderstatuss));
                adapter_farmernamestatus = new ArrayAdapter<String>(orderstatus_details.this,
                        android.R.layout.simple_list_item_1,list_farmernamestatus);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class id extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "",cPostSQL2 = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(orderstatus_details.this);

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
                cPostSQL2 = date;
                cv.put("code", cPostSQL);
                cv.put("code2", cPostSQL2);

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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatus_details.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !aydi.equals("HTTPSERVER_ERROR")) { }

                String AYDI = aydi;

                String str = AYDI;
                final String ayds[] = str.split("-");
                list_ID = new ArrayList<String>(Arrays.asList(ayds));
                adapter_ID = new ArrayAdapter<String>(orderstatus_details.this,
                        android.R.layout.simple_list_item_1,list_ID);

                orderstatus_details.CustomListAdapter adapter = new orderstatus_details.CustomListAdapter(orderstatus_details.this, list_cartquantity, list_cartprice, list_cartproductname, list_ID, list_cartordertype,list_farmernamestatus,list_orderid,list_cartorderstatus);
                listView.setAdapter(adapter);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class TotalP extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "",cPostSQL2 = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(orderstatus_details.this);

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
                cPostSQL2 = date;
                cv.put("code", cPostSQL);
                cv.put("code2", cPostSQL2);

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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatus_details.this);
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

    private class CustomListAdapter extends BaseAdapter {
        private Context context;

        private ArrayList<String> cartqty;
        private ArrayList<String> orderid;
        private ArrayList<String> cartprice;
        private ArrayList<String> cartproductname;
        private ArrayList<String> productid;
        private ArrayList<String> dordertype;
        private ArrayList<String> dorderstatus;

        private ArrayList<String> farname3;


        public CustomListAdapter(Context context, ArrayList<String> cartqty, ArrayList<String> cartprice, ArrayList<String> cartproductname, ArrayList<String> productid,ArrayList<String> dordertype, ArrayList<String> farname3,ArrayList<String> orderid,ArrayList<String> dorderstatus) {
            this.context = context;
            this.cartqty = cartqty;
            this.cartprice = cartprice;
            this.cartproductname = cartproductname;
            this.productid = productid;
            this.dordertype =dordertype;
            this.dorderstatus =dorderstatus;
            this.farname3 = farname3;
            this.orderid = orderid;


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
            View listViewItem = inflater.inflate(R.layout.custom_list_item_7, null);

            TextView cartproductnameTextView = listViewItem.findViewById(R.id.productnameTextView);
            TextView cartQtyTextView = listViewItem.findViewById(R.id.pproqtysTextView);
            TextView cartpriceTextView = listViewItem.findViewById(R.id.ProductpsTextView);
            TextView nameoffarmer3 = listViewItem.findViewById(R.id.farmername3);
            TextView orderids = listViewItem.findViewById(R.id.adyiview2);
            TextView ordertypeTextView = listViewItem.findViewById(R.id.ordertypetxtview);
            TextView orderstatusTextView = listViewItem.findViewById(R.id.orderstatustxtview);

            Button confirm = listViewItem.findViewById(R.id.button2);

            String currentOrdertId = orderid.get(position);


            orderids.setText(orderid.get(position));
            cartproductnameTextView.setText(cartproductname.get(position));
            cartQtyTextView.setText(cartqty.get(position));
            cartpriceTextView.setText(cartprice.get(position));
            nameoffarmer3.setText(farname3.get(position));
           ordertypeTextView.setText(dordertype.get(position));
            orderstatusTextView.setText(dorderstatus.get(position));

            confirm.setTag(currentOrdertId);

          confirm.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View view) {
                  String orderId = (String) view.getTag();
                  new orderstatus_details.ORDERRECEIVED().execute(orderId);
                  btnQuery.performClick();
                  confirm.setBackgroundColor(Color.GRAY);

              }
          });



            return listViewItem;
        }

    }
    private class ORDERRECEIVED extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cPostSQL2,cMessage = "Updating";
        String gens, civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(orderstatus_details.this);

        public ORDERRECEIVED() {
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
                String orderID = params[0];
                cPostSQL2 = date;
                cv.put("order_id", orderID);


                JSONObject json = jParser.makeHTTPRequest(urlorderstatus_details, "POST", cv);
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
            AlertDialog.Builder alert = new AlertDialog.Builder(orderstatus_details.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(orderstatus_details.this, s, Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}