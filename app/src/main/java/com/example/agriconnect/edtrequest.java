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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class edtrequest extends AppCompatActivity {
    // Product Name, Estimated Price, Date Expected
    private static com.example.agriconnect.JSONParser jParser = new com.example.agriconnect.JSONParser();

    private static String urlHost = "http://192.168.1.4/Agriconnect/php/selectUsername.php";
    //private static String urlHostDelete = "http://192.168.1.4/veggi/delete.php";

    //private static String urlUserName = "http://192.168.1.4/veggi/delete.php";
    private static String urlRequestProductName = "http://192.168.1.4/Agriconnect/php/selectRequestproductname.php";
    private static String urlRequestproductqty = "http://192.168.1.4/Agriconnect/php/selectRequestproductqty.php";
    private static String urlRequestproductdate = "http://192.168.1.4/Agriconnect/php/selectRequestproductdate.php";

    private static String urlHostID = "http://192.168.1.4/Agriconnect/php/selectid.php";
    SharedPreferences sharedPreferences;

    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String cItemcode = "";
    private static String online_dataset = "";
    private static Button btnQuery;
    private static EditText edtitemcode;
    ListView listView;
    TextView textView,txtDefault_ID, txtDefaultUsername, txtDefaultRequestProduct, txtDefaultRequestqty, txtDefaultdate;
    ArrayAdapter<String> adapter_username;
    ArrayAdapter<String> adapter_requestedproduct;
    ArrayAdapter<String> adapter_requestedproductqty;
    ArrayAdapter<String> adapter_requestedproductdate;
    ArrayAdapter <String> adapter_ID;

    ArrayList <String> list_username;
    ArrayList <String> list_rpname;
    ArrayList <String> list_rprice;
    ArrayList <String> list_redate;
    ArrayList <String> list_ID;

    ImageView backimgbtn;

    String cltemSelected_username,cItemSelected_ID, cltemSelected_requestedproduct, cltemSelected_requestedproductqty, cltemSelected_requestedproductdate;
    Context context = this;
    private String rusername, rproduct, rqty, rdate,aydi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edtrequest);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        edtitemcode = (EditText) findViewById(R.id.edtitemcode);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textView4);
        backimgbtn = findViewById(R.id.logout2);

        txtDefaultUsername = (TextView) findViewById(R.id.txt_username);
        txtDefaultRequestProduct = (TextView) findViewById(R.id.txt_requestproduct);
        txtDefaultRequestqty = (TextView) findViewById(R.id.txt_requestqty);
        txtDefaultdate = (TextView) findViewById(R.id.txt_requestdate);
        txtDefault_ID = (TextView) findViewById(R.id.txt_ID);
        sharedPreferences = getSharedPreferences("Agriconnect", MODE_PRIVATE);

        txtDefaultUsername.setVisibility(View.GONE);
        txtDefaultRequestProduct.setVisibility(View.GONE);
        txtDefaultRequestqty.setVisibility(View.GONE);
        txtDefaultdate.setVisibility(View.GONE);
        txtDefault_ID.setVisibility(View.GONE);

        backimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edtrequest.this,requestActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Toast.makeText(edtrequest.this, "Nothing Selected", Toast.LENGTH_SHORT).show();





        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cItemcode = edtitemcode.getText().toString();
                new uploadDataToURL().execute();
                new RPRODUCT().execute();
                new RPRICE().execute();
                new RPDATE().execute();
                new id().execute();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                cltemSelected_username = adapter_username.getItem(position);
                cltemSelected_requestedproduct = adapter_requestedproduct.getItem(position);
                cltemSelected_requestedproductqty = adapter_requestedproductqty.getItem(position);
                cltemSelected_requestedproductdate = adapter_requestedproductdate.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);

                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Do you want to edit your purchases ");
                alert_confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtDefaultUsername.setText(cltemSelected_username);
                        txtDefaultRequestProduct.setText(cltemSelected_requestedproduct);
                        txtDefaultRequestqty.setText(cltemSelected_requestedproductqty);
                        txtDefaultdate.setText(cltemSelected_requestedproductdate);
                        txtDefault_ID.setText(cItemSelected_ID);


                        rusername = txtDefaultUsername.getText().toString().trim();
                        rproduct = txtDefaultRequestProduct.getText().toString().trim();
                        rqty = txtDefaultRequestqty.getText().toString().trim();
                        rdate = txtDefaultdate.getText().toString().trim();
                        aydi = txtDefault_ID.getText().toString().trim();


                        Intent intent = new Intent(edtrequest.this, edtrequest_records.class);
                        intent.putExtra(edtrequest_records.USERNAME, rusername);
                        intent.putExtra(edtrequest_records.RPRODUCT, rproduct);
                        intent.putExtra(edtrequest_records.RQTY, rqty);
                        intent.putExtra(edtrequest_records.RDATE, rdate);
                        intent.putExtra(edtrequest_records.ID, aydi);
                        startActivity(intent);
                    }
                });

                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();

            }
        });
    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {

        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtrequest.this);

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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtrequest.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }
                //toast.makeText(act_buy_manage.this, s, Toast.LENGTH_SHORT).show();
                String wew = s;

                String str = wew;
                final String usernames[] = str.split("-");
                list_username = new ArrayList<String>(Arrays.asList(usernames));
                adapter_username = new ArrayAdapter<String>(edtrequest.this,
                        android.R.layout.simple_list_item_1,list_username);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class RPRODUCT extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtrequest.this);

        public RPRODUCT() {
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

                JSONObject json = jParser.makeHTTPRequest(urlRequestProductName, "POST", cv);
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
        protected void onPostExecute(String PNAME) {
            super.onPostExecute(PNAME);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtrequest.this);
            if (PNAME != null) {
                if (isEmpty.equals("") && !PNAME.equals("HTTPSERVER_ERROR")) { }


                String pname = PNAME;

                String str = pname;
                final String clnames[] = str.split("-");
                list_rpname = new ArrayList<String>(Arrays.asList(clnames));
                adapter_requestedproduct = new ArrayAdapter<String>(edtrequest.this,
                        android.R.layout.simple_list_item_1,list_rpname);

            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class RPRICE extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtrequest.this);

        public RPRICE() {
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

                JSONObject json = jParser.makeHTTPRequest(urlRequestproductqty, "POST", cv);
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
        protected void onPostExecute(String RPPRICE) {
            super.onPostExecute(RPPRICE);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtrequest.this);
            if (RPPRICE != null) {
                if (isEmpty.equals("") && !RPPRICE.equals("HTTPSERVER_ERROR")) { }


                String rrprice = RPPRICE;

                String str = rrprice;
                final String rrprices[] = str.split("-");
                list_rprice = new ArrayList<String>(Arrays.asList(rrprices));
                adapter_requestedproductqty = new ArrayAdapter<String>(edtrequest.this,
                        android.R.layout.simple_list_item_1,list_rprice);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class RPDATE extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtrequest.this);

        public RPDATE() {
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

                JSONObject json = jParser.makeHTTPRequest(urlRequestproductdate, "POST", cv);
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
        protected void onPostExecute(String nRPDATE) {
            super.onPostExecute(nRPDATE);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtrequest.this);
            if (nRPDATE != null) {
                if (isEmpty.equals("") && !nRPDATE.equals("HTTPSERVER_ERROR")) { }


                String rpdate = nRPDATE;

                String str = rpdate;
                final String qtys[] = str.split("-");
                list_redate = new ArrayList<String>(Arrays.asList(qtys));
                adapter_requestedproductdate = new ArrayAdapter<String>(edtrequest.this,
                        android.R.layout.simple_list_item_1,list_redate);

                //listView.setAdapter(adapter_gender);



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
        ProgressDialog pDialog = new ProgressDialog(edtrequest.this);

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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtrequest.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !aydi.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(edtrequest.this, "Data selected", Toast.LENGTH_SHORT).show();

                String AYDI = aydi;

                String str = AYDI;
                final String ayds[] = str.split("-");
                list_ID = new ArrayList<String>(Arrays.asList(ayds));
                adapter_ID = new ArrayAdapter<String>(edtrequest.this,
                        android.R.layout.simple_list_item_1,list_ID);

                //listView.setAdapter(adapter_requestedproductdate);
                //textView.setText(listView.getAdapter().getCount() + " " +"record(s) found.");
                 CustomListAdapter customAdapter = new CustomListAdapter(edtrequest.this, list_username, list_rpname, list_rprice, list_redate);
                 listView.setAdapter(customAdapter);
                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class CustomListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> usernames;
        private ArrayList<String> requestedProducts;
        private ArrayList<String> requestedProductQtys;
        private ArrayList<String> requestedProductDates;

        public CustomListAdapter(Context context, ArrayList<String> usernames, ArrayList<String> requestedProducts, ArrayList<String> requestedProductQtys, ArrayList<String> requestedProductDates) {
            this.context = context;
            this.usernames = usernames;
            this.requestedProducts = requestedProducts;
            this.requestedProductQtys = requestedProductQtys;
            this.requestedProductDates = requestedProductDates;
        }

        @Override
        public int getCount() {
            return usernames.size();
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
            View listViewItem = inflater.inflate(R.layout.custom_list_item, null);

            TextView usernameTextView = listViewItem.findViewById(R.id.usernameTextView);
            TextView requestedProductTextView = listViewItem.findViewById(R.id.requestedProductTextView);
            TextView requestedProductQtyTextView = listViewItem.findViewById(R.id.requestedProductQtyTextView);
            TextView requestedProductDateTextView = listViewItem.findViewById(R.id.requestedProductDateTextView);

            usernameTextView.setText(usernames.get(position));
            requestedProductTextView.setText(requestedProducts.get(position));
            requestedProductQtyTextView.setText(requestedProductQtys.get(position));
            requestedProductDateTextView.setText(requestedProductDates.get(position));

            return listViewItem;
        }
    }
}



