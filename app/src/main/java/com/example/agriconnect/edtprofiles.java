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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class edtprofiles extends AppCompatActivity {

    private static com.example.agriconnect.JSONParser jParser = new com.example.agriconnect.JSONParser();

    private static String urlHost = "https://hotela9barnala.net/register/selecetconsumerusername.php";

    private static String urlconsumerid = "https://hotela9barnala.net/register/selectconsumerid.php";
    private static String urlemail = "https://hotela9barnala.net/register/selectconsumeremail.php";
    private static String urlnamelast = "https://hotela9barnala.net/register/selectconsumersnamelast.php";
    private static String urlnamefirst = "https://hotela9barnala.net/register/selectconsumersnamefirst.php";
    private static String urldob = "https://hotela9barnala.net/register/selectconsumersdob.php";
    private static String urlstreet = "https://hotela9barnala.net/register/selectaddressstreet.php";
    private static String urlcity = "https://hotela9barnala.net/register/selectaddresscity.php";

    private static String urlregion = "https://hotela9barnala.net/register/selectaddressregion.php";
    private static String urlcontactnumber= "https://hotela9barnala.net/register/selectcontactnumber.php";

    SharedPreferences sharedPreferences;

    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String cItemcode = "";
    private static String online_dataset = "";
    private static ImageView btnQuery;
    private static EditText edtitemcode;
    ListView listView;
    TextView textView,txtDefaultusername,txtDefaultconsumerid, txtDefaultemail, txtDefaultnamelast, txtDefaultnamefirst, txtDefaultdob,txtDefaultaddressstreet,txtDefaultaddresscity,txtDefaultaddressregion,txtDefaultcontactnumber;
    ArrayAdapter<String> adapter_username;
    ArrayAdapter<String> adapter_consumerid;
    ArrayAdapter<String> adapter_email;
    ArrayAdapter<String> adapter_namelast;
    ArrayAdapter <String> adapter_namefirst;
    ArrayAdapter <String> adapter_dob;
    ArrayAdapter <String> adapter_addressstreet;
    ArrayAdapter <String> adapter_addresscity;
    ArrayAdapter <String> adapter_addressregion;
    ArrayAdapter <String> adapter_contactnumber;


    ArrayList<String> list_username;
    ArrayList <String> list_email;
    ArrayList <String> list_namelast;
    ArrayList <String> list_namefirst;

    ArrayList<String> list_dob;
    ArrayList <String> list_addressstreet;
    ArrayList <String> list_addresscity;
    ArrayList <String> list_addressregion;
    ArrayList <String> list_contactnumber;
    ArrayList <String> list_ID;

    ImageView backimgbtn;

    String cltemSelected_username,cltemSelected_consumerid, cltemSelected_email, cltemSelected_namelast, cltemSelected_namefirst,cltemSelected_dob,cltemSelected_addressstreet,cltemSelected_addresscity,cltemSelected_addressregion,cltemSelected_contactnumber;
    Context context = this;
    private String pcn, par, pac,aydi,pas,pdob,pnf,pnl,pemail,pusername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edtprofile);
        btnQuery = (ImageView) findViewById(R.id.imgreload3);
        edtitemcode = (EditText) findViewById(R.id.edtitemcode);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textView4);
        backimgbtn = findViewById(R.id.logout2);


        txtDefaultusername = (TextView) findViewById(R.id.txt_username);
        txtDefaultconsumerid = (TextView) findViewById(R.id.txt_consumerid);
        txtDefaultemail = (TextView) findViewById(R.id.txt_email);
        txtDefaultnamelast = (TextView) findViewById(R.id.txtnamelast);
        txtDefaultnamefirst = (TextView) findViewById(R.id.txt_namefirst);
        txtDefaultdob = (TextView) findViewById(R.id.txt_dob);
        txtDefaultaddressstreet = (TextView) findViewById(R.id.txt_addressstreet);
        txtDefaultaddresscity = (TextView) findViewById(R.id.txt_addresscity);
        txtDefaultaddressregion = (TextView) findViewById(R.id.txt_addressregion);
        txtDefaultcontactnumber = (TextView) findViewById(R.id.txt_contactnumber);
        sharedPreferences = getSharedPreferences("Agriconnect", MODE_PRIVATE);


        backimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edtprofiles.this,home.class);
                startActivity(intent);
                finish();
            }
        });




        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cItemcode = edtitemcode.getText().toString();
                new edtprofiles.uploadDataToURL().execute();
                new edtprofiles.EMAIL().execute();
                new edtprofiles.NAMELAST().execute();
                new edtprofiles.NAMEFIRST().execute();
                new edtprofiles.DOB().execute();
                new edtprofiles.ADDRESSSREET().execute();
                new edtprofiles.ADDRESSCITY().execute();
                new edtprofiles.ADRESSREGION().execute();
                new edtprofiles.CONTACTNUMBER().execute();
                new edtprofiles.id().execute();
            }
        });
        btnQuery.performClick();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                cltemSelected_username = adapter_username.getItem(position);
                cltemSelected_consumerid = adapter_consumerid.getItem(position);
                cltemSelected_email = adapter_email.getItem(position);
                cltemSelected_namelast = adapter_namelast.getItem(position);
                cltemSelected_namefirst = adapter_namefirst.getItem(position);
                cltemSelected_dob = adapter_dob.getItem(position);
                cltemSelected_addressstreet = adapter_addressstreet.getItem(position);
                cltemSelected_addresscity = adapter_addresscity.getItem(position);
                cltemSelected_addressregion = adapter_addressregion.getItem(position);
                cltemSelected_contactnumber = adapter_contactnumber.getItem(position);

                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Click Edit to chnge your personal Information.");

                alert_confirm.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtDefaultusername.setText(cltemSelected_username);
                        txtDefaultconsumerid.setText(cltemSelected_consumerid);
                        txtDefaultemail.setText(cltemSelected_email);
                        txtDefaultnamelast.setText(cltemSelected_namelast);
                        txtDefaultnamefirst.setText(cltemSelected_namefirst);
                        txtDefaultdob.setText(cltemSelected_dob);
                        txtDefaultaddressstreet.setText(cltemSelected_addressstreet);
                        txtDefaultaddresscity.setText(cltemSelected_addresscity);
                        txtDefaultaddressregion.setText(cltemSelected_addressregion);
                        txtDefaultcontactnumber.setText(cltemSelected_contactnumber);



                        pusername = txtDefaultusername.getText().toString().trim();
                        aydi = txtDefaultconsumerid.getText().toString().trim();
                        pemail = txtDefaultemail.getText().toString().trim();
                        pnl = txtDefaultnamelast.getText().toString().trim();
                        pnf = txtDefaultnamefirst.getText().toString().trim();
                        pdob = txtDefaultdob.getText().toString().trim();
                        pas = txtDefaultaddressstreet.getText().toString().trim();
                        pac = txtDefaultaddresscity.getText().toString().trim();
                        par = txtDefaultaddressregion.getText().toString().trim();
                        pcn = txtDefaultcontactnumber.getText().toString().trim();


                        Intent intent = new Intent(edtprofiles.this, profileeditactivity.class);
                        intent.putExtra(profileeditactivity.USERNAME, pusername);
                        intent.putExtra(profileeditactivity.CONSUMERMAIL, pemail);
                        intent.putExtra(profileeditactivity.PNAMELAST, pnl);
                        intent.putExtra(profileeditactivity.PNAMEFIRST, pnf);
                        intent.putExtra(profileeditactivity.PDOB, pdob);
                        intent.putExtra(profileeditactivity.PADDRESSSTREET, pas);
                        intent.putExtra(profileeditactivity.PADDRESSCITY, pac);
                        intent.putExtra(profileeditactivity.PADDRESSREGION, par);
                        intent.putExtra(profileeditactivity.PCONTACTNUMBER, pcn);
                        intent.putExtra(profileeditactivity.ID, aydi);
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
        ProgressDialog pDialog = new ProgressDialog(edtprofiles.this);

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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtprofiles.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }

                String wew = s;

                String str = wew;
                final String usernames[] = str.split("-");
                list_username = new ArrayList<String>(Arrays.asList(usernames));
                adapter_username = new ArrayAdapter<String>(edtprofiles.this,
                        android.R.layout.simple_list_item_1,list_username);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class EMAIL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtprofiles.this);

        public EMAIL() {
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

                JSONObject json = jParser.makeHTTPRequest(urlemail, "POST", cv);
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
        protected void onPostExecute(String CEmail) {
            super.onPostExecute(CEmail);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtprofiles.this);
            if (CEmail != null) {
                if (isEmpty.equals("") && !CEmail.equals("HTTPSERVER_ERROR")) { }


                String cemail = CEmail;

                String str = cemail;
                final String cemails[] = str.split("-");
                list_email = new ArrayList<String>(Arrays.asList(cemails));
                adapter_email = new ArrayAdapter<String>(edtprofiles.this,
                        android.R.layout.simple_list_item_1,list_email);

            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class NAMELAST extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtprofiles.this);

        public NAMELAST() {
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

                JSONObject json = jParser.makeHTTPRequest(urlnamelast, "POST", cv);
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
        protected void onPostExecute(String lastname) {
            super.onPostExecute(lastname);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtprofiles.this);
            if (lastname != null) {
                if (isEmpty.equals("") && !lastname.equals("HTTPSERVER_ERROR")) { }


                String namelast = lastname;

                String str = namelast;
                final String namelasts[] = str.split("-");
                list_namelast = new ArrayList<String>(Arrays.asList(namelasts));
                adapter_namelast = new ArrayAdapter<String>(edtprofiles.this,
                        android.R.layout.simple_list_item_1,list_namelast);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class NAMEFIRST extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtprofiles.this);

        public NAMEFIRST() {
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

                JSONObject json = jParser.makeHTTPRequest(urlnamefirst, "POST", cv);
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
        protected void onPostExecute(String firstname) {
            super.onPostExecute(firstname);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtprofiles.this);
            if (firstname != null) {
                if (isEmpty.equals("") && !firstname.equals("HTTPSERVER_ERROR")) { }


                String namefirst = firstname;

                String str = namefirst;
                final String namefirsts[] = str.split("-");
                list_namefirst = new ArrayList<String>(Arrays.asList(namefirsts));
                adapter_namefirst = new ArrayAdapter<String>(edtprofiles.this,
                        android.R.layout.simple_list_item_1,list_namefirst);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class DOB extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtprofiles.this);

        public DOB() {
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

                JSONObject json = jParser.makeHTTPRequest(urldob, "POST", cv);
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
        protected void onPostExecute(String BOD) {
            super.onPostExecute(BOD);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtprofiles.this);
            if (BOD != null) {
                if (isEmpty.equals("") && !BOD.equals("HTTPSERVER_ERROR")) { }


                String dob = BOD;

                String str = dob;
                final String dobs[] = str.split("-");
                list_dob = new ArrayList<String>(Arrays.asList(dobs));
                adapter_dob = new ArrayAdapter<String>(edtprofiles.this,
                        android.R.layout.simple_list_item_1,list_dob);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    } private class ADDRESSSREET extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtprofiles.this);

        public ADDRESSSREET() {
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

                JSONObject json = jParser.makeHTTPRequest(urlstreet, "POST", cv);
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
        protected void onPostExecute(String streetaddress) {
            super.onPostExecute(streetaddress);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtprofiles.this);
            if (streetaddress != null) {
                if (isEmpty.equals("") && !streetaddress.equals("HTTPSERVER_ERROR")) { }


                String addressstreet = streetaddress;

                String str = addressstreet;
                final String addressstreets[] = str.split("-");
                list_addressstreet = new ArrayList<String>(Arrays.asList(addressstreets));
                adapter_addressstreet = new ArrayAdapter<String>(edtprofiles.this,
                        android.R.layout.simple_list_item_1,list_namefirst);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    } private class ADDRESSCITY extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtprofiles.this);

        public ADDRESSCITY() {
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

                JSONObject json = jParser.makeHTTPRequest(urlcity, "POST", cv);
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
        protected void onPostExecute(String city) {
            super.onPostExecute(city);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtprofiles.this);
            if (city != null) {
                if (isEmpty.equals("") && !city.equals("HTTPSERVER_ERROR")) { }


                String addresscity = city;

                String str = addresscity;
                final String addresscitys[] = str.split("-");
                list_addresscity = new ArrayList<String>(Arrays.asList(addresscitys));
                adapter_addresscity = new ArrayAdapter<String>(edtprofiles.this,
                        android.R.layout.simple_list_item_1,list_addresscity);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    } private class ADRESSREGION extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtprofiles.this);

        public ADRESSREGION() {
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

                JSONObject json = jParser.makeHTTPRequest(urlregion, "POST", cv);
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
        protected void onPostExecute(String region) {
            super.onPostExecute(region);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtprofiles.this);
            if (region != null) {
                if (isEmpty.equals("") && !region.equals("HTTPSERVER_ERROR")) { }


                String adressregion = region;

                String str = adressregion;
                final String adressregions[] = str.split("-");
                list_addressregion = new ArrayList<String>(Arrays.asList(adressregions));
                adapter_addressregion = new ArrayAdapter<String>(edtprofiles.this,
                        android.R.layout.simple_list_item_1,list_addressregion);

                //listView.setAdapter(adapter_gender);



            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    } private class CONTACTNUMBER extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Updating";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(edtprofiles.this);

        public CONTACTNUMBER() {
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

                JSONObject json = jParser.makeHTTPRequest(urlcontactnumber, "POST", cv);
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
        protected void onPostExecute(String numbercontact) {
            super.onPostExecute(numbercontact);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtprofiles.this);
            if (numbercontact != null) {
                if (isEmpty.equals("") && !numbercontact.equals("HTTPSERVER_ERROR")) { }


                String contactnumber = numbercontact;

                String str = contactnumber;
                final String contactnumbers[] = str.split("-");
                list_contactnumber = new ArrayList<String>(Arrays.asList(contactnumbers));
                adapter_contactnumber = new ArrayAdapter<String>(edtprofiles.this,
                        android.R.layout.simple_list_item_1,list_contactnumber);

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
        ProgressDialog pDialog = new ProgressDialog(edtprofiles.this);

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

                JSONObject json = jParser.makeHTTPRequest(urlconsumerid, "POST", cv);
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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(edtprofiles.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !aydi.equals("HTTPSERVER_ERROR")) { }


                String AYDI = aydi;

                String str = AYDI;
                final String ayds[] = str.split("-");
                list_ID = new ArrayList<String>(Arrays.asList(ayds));
                adapter_consumerid = new ArrayAdapter<String>(edtprofiles.this,
                        android.R.layout.simple_list_item_1,list_ID);

                //listView.setAdapter(adapter_requestedproductdate);
                //textView.setText(listView.getAdapter().getCount() + " " +"record(s) found.");
                edtprofiles.CustomListAdapter customAdapter = new CustomListAdapter(edtprofiles.this, list_username, list_ID, list_namelast, list_namefirst, list_dob,list_email,list_contactnumber,list_addressstreet,list_addresscity,list_addressregion);
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
        private ArrayList<String> Cusernames;
        private ArrayList<String> Cconsumerid;
        private ArrayList<String> Cnamelast;
        private ArrayList<String> Cnamefirst;
        private ArrayList<String> Cdob;
        private ArrayList<String> Cemail;
        private ArrayList<String> Ccontactnumber;
        private ArrayList<String> Cstreet;
        private ArrayList<String> Ccity;
        private ArrayList<String> Cregion;

        private ArrayList<String> requestedProductDates;

        public CustomListAdapter(Context context, ArrayList<String> Cusernames, ArrayList<String> Cconsumerid, ArrayList<String> Cnamelast, ArrayList<String> Cnamefirst, ArrayList<String> Cdob,
                                 ArrayList<String> Cemail, ArrayList<String> Ccontactnumber, ArrayList<String> Cstreet, ArrayList<String> Ccity, ArrayList<String> Cregion) {
            this.context = context;
            this.Cusernames = Cusernames;
            this.Cconsumerid = Cconsumerid;
            this.Cnamelast = Cnamelast;
            this.Cnamefirst = Cnamefirst;
            this.Cdob = Cdob;
            this.Cemail = Cemail;
            this.Ccontactnumber = Ccontactnumber;
            this.Cstreet = Cstreet;
            this.Ccity = Ccity;
            this.Cregion = Cregion;
        }

        @Override
        public int getCount() {
            return Cusernames.size();
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
            View listViewItem = inflater.inflate(R.layout.custon_list_item_4, null);

            TextView usernameTextView = listViewItem.findViewById(R.id.usernameTextView);
            TextView CconsumeridTextView = listViewItem.findViewById(R.id.usernameTextView18);
            TextView CnamelastTextView = listViewItem.findViewById(R.id.lname);
            TextView CnamefirstTextView = listViewItem.findViewById(R.id.fnameTextView);
            TextView CdobTextView = listViewItem.findViewById(R.id.dobtv);
            TextView CemailTextView = listViewItem.findViewById(R.id.emailtv);
            TextView CcontactnumberTextView = listViewItem.findViewById(R.id.contactnumbertv);
            TextView CstreetTextView = listViewItem.findViewById(R.id.streetaddress);
            TextView CcityTextView = listViewItem.findViewById(R.id.cityaddress);
            TextView CregionTextView = listViewItem.findViewById(R.id.regionaddress);


            usernameTextView.setText(Cusernames.get(position));
            CconsumeridTextView.setText(Cconsumerid.get(position));
            CnamelastTextView.setText(Cnamelast.get(position));
           CnamefirstTextView.setText(Cnamefirst.get(position));
           CdobTextView.setText(Cdob.get(position));
           CemailTextView.setText(Cemail.get(position));
           CcontactnumberTextView.setText(Ccontactnumber.get(position));
           CstreetTextView.setText(Cstreet.get(position));
           CcityTextView.setText(Ccity.get(position));
           CregionTextView.setText(Cregion.get(position));





            return listViewItem;
        }
    }
}