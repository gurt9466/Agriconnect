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

public class orderstatusActivity extends AppCompatActivity {
    private static com.example.agriconnect.JSONParser jParser = new com.example.agriconnect.JSONParser();
    private static String urlHost = "https://agriconnect.me/request/selectdate.php"; // Date of purchase

    private static String urlorderid = "https://agriconnect.me/request/selectstatus.php"; // status
    SharedPreferences sharedPreferences;
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static ImageView btnQuery;
    private static EditText edtitemcode;
    private static String cItemcode = "";

    ListView listView;
    ImageView backimgbtn;
    TextView txtdefaltdate, txtdefaltorderid;

    String cltemSelected_date,cItemSelected_orderid;

    ArrayAdapter<String> adapter_date;
    ArrayAdapter<String> adapter_orderid;
    ArrayList <String> list_date;
    ArrayList <String> list_orderid;
    Context context = this;

    private String corderid, cdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderstatus);

        btnQuery = (ImageView) findViewById(R.id.imgreload);
        listView = (ListView) findViewById(R.id.listview);
        backimgbtn = findViewById(R.id.logout2);
        edtitemcode = (EditText) findViewById(R.id.edtitemcode);

        txtdefaltdate = (TextView) findViewById(R.id.txt_date);
        txtdefaltorderid = (TextView) findViewById(R.id.txt_status);
        sharedPreferences = getSharedPreferences("Agriconnect", MODE_PRIVATE);

        txtdefaltdate.setVisibility(View.GONE);
        txtdefaltorderid.setVisibility(View.GONE);

        backimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(orderstatusActivity.this, home.class);
                startActivity(intent);
                finish();
            }
        });





        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cItemcode = edtitemcode.getText().toString();
                new orderstatusActivity.uploadDataToURL().execute();
                new orderstatusActivity.ODRDERID().execute();

            }
        });
        btnQuery.performClick();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position < adapter_date.getCount() && position < adapter_orderid.getCount()) {
                    cltemSelected_date = adapter_date.getItem(position);
                    cItemSelected_orderid = adapter_orderid.getItem(position);

                } else {
                    // Handle the case where the position is out of bounds
                    Toast.makeText(orderstatusActivity.this, "Invalid selection", Toast.LENGTH_SHORT).show();
                }



            androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Check Orders");

                alert_confirm.setPositiveButton("View", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtdefaltdate.setText(cltemSelected_date);
                        txtdefaltorderid.setText(cItemSelected_orderid);

                        cdate = txtdefaltdate.getText().toString().trim();



                        Intent intent = new Intent(orderstatusActivity.this,orderstatus_details. class);
                        intent.putExtra(orderstatus_details.CORDERID, corderid);
                        intent.putExtra(orderstatus_details.CDATE, cdate);
                        startActivity(intent);
                    }
                });

                alert_confirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
            ProgressDialog pDialog = new ProgressDialog(orderstatusActivity.this);

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
                android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatusActivity.this);
                if (s != null) {
                    if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }

                    String wew = s;

                    String str = wew;
                    final String usernames[] = str.split("-");
                    list_date = new ArrayList<String>(Arrays.asList(usernames));
                    adapter_date = new ArrayAdapter<String>(orderstatusActivity.this,
                            android.R.layout.simple_list_item_1,list_date);


                } else {
                    alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                    alert.setTitle("Error");
                    alert.show();
                }
            }
        }

        private class ODRDERID extends AsyncTask<String, String, String> {
            String cPOST = "", cPostSQL = "", cMessage = "Updating";
            int nPostValueIndex;
            ProgressDialog pDialog = new ProgressDialog(orderstatusActivity.this);

            public ODRDERID() {
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
            protected void onPostExecute(String ccorderid) {
                super.onPostExecute(ccorderid);
                pDialog.dismiss();
                String isEmpty = "";
                android.app.AlertDialog.Builder alert = new AlertDialog.Builder(orderstatusActivity.this);
                if (ccorderid != null) {
                    if (isEmpty.equals("") && !ccorderid.equals("HTTPSERVER_ERROR")) { }


                    String cCstat = ccorderid;

                    String str = cCstat;
                    final String cCstats[] = str.split("-");
                    list_orderid = new ArrayList<String>(Arrays.asList(cCstats));
                    adapter_orderid = new ArrayAdapter<String>(orderstatusActivity.this,
                            android.R.layout.simple_list_item_1,list_orderid);

                    orderstatusActivity.CustomListAdapter adapter = new orderstatusActivity.CustomListAdapter(orderstatusActivity.this, list_date, list_orderid);
                    listView.setAdapter(adapter);

                } else {
                    alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                    alert.setTitle("Error");
                    alert.show();
                }
            }
        }

    private class CustomListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> date;
        private ArrayList<String> status;

        public CustomListAdapter(Context context, ArrayList<String> date, ArrayList<String> status) {
            this.context = context;
            this.date = date;
            this.status = status;
        }

        @Override
        public int getCount() {
            return date.size(); // Assuming date and status ArrayLists have the same size
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
            View listViewItem = inflater.inflate(R.layout.custom_list_item_5, null);

            TextView dateTextView = listViewItem.findViewById(R.id.datetxtview);


            // Ensure the position is within the bounds of the 'date' ArrayList
            if (position < date.size()) {
                dateTextView.setText(date.get(position));
            } else {
                // Handle the case where the position is out of bounds
                dateTextView.setText("No record Found");
            }
            return listViewItem;
        }
    }
}
