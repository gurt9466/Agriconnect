package com.example.agriconnect;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class purchaseActivity extends AppCompatActivity {
    private static com.example.agriconnect.JSONParser jParser = new com.example.agriconnect.JSONParser();
    SharedPreferences sharedPreferences;

    private static String urlHost = "https://agriconnect.me/product/selectproductname.php";
    private static String urlHarvestDate = "https://agriconnect.me/product/selectharvestdate.php";
    private static String urlQuantity = "https://agriconnect.me/product/selectquantity.php";
    private static String urlPrice = "https://agriconnect.me/product/selectprice.php";
    private static String urlFarmerID = "https://agriconnect.me/product/selectfarmerid.php";
    private static String urlHostID = "https://agriconnect.me/product/selectproductid.php";
    private  static String urlfarmername = "https://agriconnect.me/product/selectfarmername.php";
    private  static String urladdress = "https://agriconnect.me/product/selectaddress.php";

    private static String urlEDate = "https://agriconnect.me/product/selectexpirydate.php";

    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String cItemcode = "";
    private static String online_dataset = "";
    private static ImageView btnQuery;
    private static EditText edtitemcode;
    ListView listView;
    TextView textView, txtDefaultFarmerName,txtDefaultExpireyDate,txtDefault_ID, txtDefaultProductName, txtDefaultHarvestDate, txtDefaultQuantity, txtDefaultPrice, txtDefaultFarmerID;
    ArrayAdapter<String> adapter_qty;

    ArrayAdapter<String> adapter_productname;
    ArrayAdapter<String> adapter_expireydate;

    ArrayAdapter<String> adapter_harvestdate;

    ArrayAdapter<String> adapter_farmname;
    ArrayAdapter<String> adapter_farmerid;
    ArrayAdapter<String> adapter_price;
    ArrayAdapter<String> adapter_ID;

    ArrayList<String> list_dateharvest;
    ArrayList<String> list_dateexpirey;


    ArrayList<String> list_productname;
    ArrayList<String> list_farmname;
    ArrayList<String> list_farmerid;
    ArrayList<String> list_pprice;
    ArrayList<String> list_pqty;
    ArrayList<String> list_ID;

    ImageView backimgbtn, cart;

    String cltemSelected_productname, cltemSelected_epireydate,cltemSelected_farmername,cItemSelected_ID, cltemSelected_harvestdate, cltemSelected_price, cltemSelected_farmerid, cltemSelected_quantity;
    Context context = this;
    private String pproductname, pharvestd, pexpireyd,pqty, pprice, aydi, pframerid,pframername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        btnQuery = (ImageView) findViewById(R.id.imgreload2);
        edtitemcode = (EditText) findViewById(R.id.edtitemcode);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textView4);
        backimgbtn = findViewById(R.id.logout2);
        cart = findViewById(R.id.cart2);

        txtDefault_ID = (TextView) findViewById(R.id.txt_ID);
        txtDefaultProductName = (TextView) findViewById(R.id.txt_productname);
        txtDefaultHarvestDate = (TextView) findViewById(R.id.txt_harvestdate);
        txtDefaultExpireyDate = (TextView) findViewById(R.id.txt_expireydate);
        txtDefaultQuantity = (TextView) findViewById(R.id.txt_requestqty);
        txtDefaultPrice = (TextView) findViewById(R.id.txt_requestprice);
        txtDefaultFarmerID = (TextView) findViewById(R.id.txt_requestfarmerid);
        txtDefaultFarmerName = (TextView) findViewById(R.id.txt_farmername);


        sharedPreferences = getSharedPreferences("Agriconnect", MODE_PRIVATE);
        txtDefault_ID.setVisibility(View.GONE);
        txtDefaultProductName.setVisibility(View.GONE);
        txtDefaultHarvestDate.setVisibility(View.GONE);
        txtDefaultQuantity.setVisibility(View.GONE);
        txtDefaultPrice.setVisibility(View.GONE);
        txtDefaultFarmerID.setVisibility(View.GONE);

        backimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(purchaseActivity.this, home.class);
                startActivity(intent);
                finish();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(purchaseActivity.this, cartActivity.class);
                startActivity(intent);
                finish();
            }
        });



        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cItemcode = edtitemcode.getText().toString();
                new purchaseActivity.uploadDataToURL().execute();
                new purchaseActivity.HDATE().execute();
                new purchaseActivity.EDATE().execute();
                new purchaseActivity.ProductQty().execute();
                new purchaseActivity.ProductPRICE().execute();
                new purchaseActivity.farmerID().execute();
                new purchaseActivity.FARMERNAME().execute();
                new purchaseActivity.id().execute();
                new FetchImageUrlsTask().execute("https://agriconnect.me/img/etch_image_urls.php");


            }
        });
        btnQuery.performClick();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                cltemSelected_productname = adapter_productname.getItem(position);
                cltemSelected_harvestdate = adapter_harvestdate.getItem(position);
                cltemSelected_epireydate = adapter_expireydate.getItem(position);
                cltemSelected_quantity = adapter_qty.getItem(position);
                cltemSelected_price = adapter_price.getItem(position);
                cltemSelected_farmername = adapter_farmname.getItem(position);
                cltemSelected_farmerid = adapter_farmerid.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);



                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage(" Do you wish to continue ");

                alert_confirm.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtDefaultProductName.setText(cltemSelected_productname);
                        txtDefaultHarvestDate.setText(cltemSelected_harvestdate);
                        txtDefaultExpireyDate.setText(cltemSelected_epireydate);
                        txtDefaultQuantity.setText(cltemSelected_quantity);
                        txtDefaultPrice.setText(cltemSelected_price);
                        txtDefaultFarmerID.setText(cltemSelected_farmerid);
                        txtDefaultFarmerName.setText(cltemSelected_farmername);
                        txtDefault_ID.setText(cItemSelected_ID);



                        pproductname = txtDefaultProductName.getText().toString().trim();
                        pharvestd = txtDefaultHarvestDate.getText().toString().trim();
                        pexpireyd = txtDefaultExpireyDate.getText().toString().trim();
                        pqty = txtDefaultQuantity.getText().toString().trim();
                        pprice = txtDefaultPrice.getText().toString().trim();
                        pframername = txtDefaultFarmerName.getText().toString().trim();
                        pframerid = txtDefaultFarmerID.getText().toString().trim();
                        aydi = txtDefault_ID.getText().toString().trim();



                        Intent intent = new Intent(purchaseActivity.this, buypurchaseactivity.class);
                        intent.putExtra(buypurchaseactivity.PNAME, pproductname);
                        intent.putExtra(buypurchaseactivity.PHARVEST, pharvestd);
                        intent.putExtra(buypurchaseactivity.PEXPIREY, pexpireyd);
                        intent.putExtra(buypurchaseactivity.Quantity, pqty);
                        intent.putExtra(buypurchaseactivity.PPRICE, pprice);
                        intent.putExtra(buypurchaseactivity.PFNAME, pframername);
                        intent.putExtra(buypurchaseactivity.PFID, pframerid);
                        intent.putExtra(buypurchaseactivity.ID, aydi);
                        startActivity(intent);
                    }
                });

                alert_confirm.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert_confirm.show();

            }
        });
    }

    private class uploadDataToURL extends AsyncTask<String, String, String> {

        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(purchaseActivity.this);

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
                cPostSQL = cItemcode;
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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(purchaseActivity.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                String wew = s;

                String str = wew;
                final String productname[] = str.split("-");
                list_productname = new ArrayList<String>(Arrays.asList(productname));
                adapter_productname = new ArrayAdapter<String>(purchaseActivity.this,
                        android.R.layout.simple_list_item_1, list_productname);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class HDATE extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(purchaseActivity.this);

        public HDATE() {
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

                cPostSQL = edtitemcode.getText().toString();
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHarvestDate, "POST", cv);
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
        protected void onPostExecute(String DHARVEST) {
            super.onPostExecute(DHARVEST);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(purchaseActivity.this);
            if (DHARVEST != null) {
                if (isEmpty.equals("") && !DHARVEST.equals("HTTPSERVER_ERROR")) {
                }


                String dateharvest = DHARVEST;

                String str = dateharvest;
                final String dateharvests[] = str.split("-");
                list_dateharvest = new ArrayList<String>(Arrays.asList(dateharvests));
                adapter_harvestdate = new ArrayAdapter<String>(purchaseActivity.this,
                        android.R.layout.simple_list_item_1, list_dateharvest);

            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class EDATE extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(purchaseActivity.this);

        public EDATE() {
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

                cPostSQL = edtitemcode.getText().toString();
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlEDate, "POST", cv);
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
        protected void onPostExecute(String DEXPIRY) {
            super.onPostExecute(DEXPIRY);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(purchaseActivity.this);
            if (DEXPIRY != null) {
                if (isEmpty.equals("") && !DEXPIRY.equals("HTTPSERVER_ERROR")) {
                }


                String dateexpirey = DEXPIRY;

                String str = dateexpirey;
                final String dateexpireys[] = str.split("-");
                list_dateexpirey = new ArrayList<String>(Arrays.asList(dateexpireys));
                adapter_expireydate = new ArrayAdapter<String>(purchaseActivity.this,
                        android.R.layout.simple_list_item_1, list_dateexpirey);

            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class ProductQty extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(purchaseActivity.this);

        public ProductQty() {
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

                cPostSQL = edtitemcode.getText().toString();
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlQuantity, "POST", cv);
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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(purchaseActivity.this);
            if (proqtyes != null) {
                if (isEmpty.equals("") && !proqtyes.equals("HTTPSERVER_ERROR")) {
                }


                String pproqtyes = proqtyes;

                String str = pproqtyes;
                final String pproqtys[] = str.split("-");
                list_pqty = new ArrayList<String>(Arrays.asList(pproqtys));
                adapter_qty = new ArrayAdapter<String>(purchaseActivity.this,
                        android.R.layout.simple_list_item_1, list_pqty);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class ProductPRICE extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(purchaseActivity.this);

        public ProductPRICE() {
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

                cPostSQL = edtitemcode.getText().toString();
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlPrice, "POST", cv);
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
        protected void onPostExecute(String productP) {
            super.onPostExecute(productP);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(purchaseActivity.this);
            if (productP != null) {
                if (isEmpty.equals("") && !productP.equals("HTTPSERVER_ERROR")) {
                }


                String Productp = productP;

                String str = Productp;
                final String Productps[] = str.split("-");
                list_pprice = new ArrayList<String>(Arrays.asList(Productps));
                adapter_price = new ArrayAdapter<String>(purchaseActivity.this,
                        android.R.layout.simple_list_item_1, list_pprice);

                //listView.setAdapter(adapter_gender);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class FARMERNAME extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(purchaseActivity.this);

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

                cPostSQL = edtitemcode.getText().toString();
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlfarmername, "POST", cv);
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
        protected void onPostExecute(String Farmname) {
            super.onPostExecute(Farmname);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(purchaseActivity.this);
            if (Farmname != null) {
                if (isEmpty.equals("") && !Farmname.equals("HTTPSERVER_ERROR")) {
                }


                String Namefarm = Farmname;

                String str = Namefarm;
                final String Namefarms[] = str.split("-");
                list_farmname = new ArrayList<String>(Arrays.asList(Namefarms));
                adapter_farmname = new ArrayAdapter<String>(purchaseActivity.this,
                        android.R.layout.simple_list_item_1, list_farmname);

                //listView.setAdapter(adapter_gender);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class farmerID extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(purchaseActivity.this);

        public farmerID() {
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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlFarmerID, "POST", cv);
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
        protected void onPostExecute(String FarID) {
            super.onPostExecute(FarID);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(purchaseActivity.this);
            if (FarID != null) {
                if (isEmpty.equals("") && !FarID.equals("HTTPSERVER_ERROR")) {
                }


                String farid = FarID;

                String str = farid;
                final String farids[] = str.split("-");
                list_farmerid = new ArrayList<String>(Arrays.asList(farids));
                adapter_farmerid = new ArrayAdapter<String>(purchaseActivity.this,
                        android.R.layout.simple_list_item_1, list_farmerid);

                //listView.setAdapter(adapter_gender);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class id extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(purchaseActivity.this);

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

                cPostSQL = edtitemcode.getText().toString();
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostID, "POST", cv);
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
        protected void onPostExecute(String aydi) {
            super.onPostExecute(aydi);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(purchaseActivity.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !aydi.equals("HTTPSERVER_ERROR")) {
                }

                String AYDI = aydi;

                String str = AYDI;
                final String ayds[] = str.split("-");
                list_ID = new ArrayList<String>(Arrays.asList(ayds));
                adapter_ID = new ArrayAdapter<String>(purchaseActivity.this,
                        android.R.layout.simple_list_item_1, list_ID);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class FetchImageUrlsTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... urls) {
            String url = urls[0];
            String gurtValue = edtitemcode.getText().toString();

            List<String> imageUrls = new ArrayList<>();
            try {
                // Log the value of cItemcode
                Log.d("Debug", "cItemcode value: " + gurtValue);

                // Construct the URL with the string variable
                String fullUrl = url + "?gurt=" + URLEncoder.encode(gurtValue, "UTF-8");



                HttpURLConnection conn = (HttpURLConnection) new URL(fullUrl).openConnection();
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
            CustomListAdapter adapter = new CustomListAdapter(purchaseActivity.this, list_productname, list_dateharvest, list_dateexpirey,list_pqty, list_pprice, list_farmname,list_ID, imageUrls);
            listView.setAdapter(adapter);
        }
    }



    private class CustomListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> productname;
        private ArrayList<String> dateharvests;
        private ArrayList<String> pproqtys;
        private ArrayList<String> Productps;
        private ArrayList<String> farids;
        private ArrayList<String> dateexpirys;
        private ArrayList<String> farname;
        private ArrayList<String> ayds;
        private List<String> imageUrls;


        public CustomListAdapter(Context context, ArrayList<String> productname, ArrayList<String> dateharvests,ArrayList<String> dateexpirys, ArrayList<String> pproqtys, ArrayList<String> Productps, ArrayList<String> farname,ArrayList<String> ayds, List<String> imageUrls) {
            this.context = context;
            this.productname = productname;
            this.dateharvests = dateharvests;
            this.dateexpirys = dateexpirys;
            this.pproqtys = pproqtys;
            this.Productps = Productps;
            this.farname = farname;
            this.ayds = ayds;
            this.imageUrls = imageUrls; // Initialize image URLs
        }

        @Override
        public int getCount() {
            return productname.size(); // Return the number of items
        }

        @Override
        public Object getItem(int position) {
            return position; // Return the item at the specified position
        }

        @Override
        public long getItemId(int position) {
            return position; // Return the item's ID at the specified position
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listViewItem = inflater.inflate(R.layout.custom_list_item_2, null);

            TextView productnameTextView = listViewItem.findViewById(R.id.productnameTextView);
            TextView dateharvestProductTextView = listViewItem.findViewById(R.id.dateharvestTextView);

            TextView dareexpireyProductTextView = listViewItem.findViewById(R.id.dateexpiryTextView);

            TextView pproqtysdProductQtyTextView = listViewItem.findViewById(R.id.pproqtysTextView);
            TextView productupsProductTextView = listViewItem.findViewById(R.id.ProductpsTextView);

            TextView nameoffarmer = listViewItem.findViewById(R.id.farmernameTextView);

            TextView aydsTextView = listViewItem.findViewById(R.id.adyiview);
            ImageView imageView = listViewItem.findViewById(R.id.imageView4); // ImageView for the image


            productnameTextView.setText(productname.get(position));
            dateharvestProductTextView.setText(dateharvests.get(position));

            dareexpireyProductTextView.setText(dateexpirys.get(position));

            pproqtysdProductQtyTextView.setText(pproqtys.get(position)+" KG");
            productupsProductTextView.setText(Productps.get(position));

            nameoffarmer.setText(farname.get(position));

            aydsTextView.setText(ayds.get(position));

            // Load and display the image using Glide
            if (imageUrls != null && imageUrls.size() > position) {
                String imageUrl = imageUrls.get(position);
                Glide.with(context).load(imageUrl).into(imageView);
            }

            return listViewItem;
        }
    }
}

