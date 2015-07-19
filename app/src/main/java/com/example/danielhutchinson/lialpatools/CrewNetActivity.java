package com.example.danielhutchinson.lialpatools;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpGet;

public class CrewNetActivity extends ActionBarActivity {

    public static final int DIALOG_DOWNLOAD_PROGRESS = 1;
    private Button DldRosterBtn;
    private Button localRosterBtn;
    private ProgressDialog progressDialog;
    private String file_storage_string = "sdcard/";
    private String curr_roster_filename = "downloaded_test_crewbrief.pdf";
    Button Login;
    //for testing will resolve later
    private String Roster_fullpath = file_storage_string+curr_roster_filename;

    private String dld_string = "http://209.59.124.244/crewnet/forms/ReportCreator.aspx?ReportName=CrewBriefingReport?FlightPlanKeys=";


    private void startDownload(String url) {
        //String url = "https://bitcoin.org/bitcoin.pdf";
        new DownloadFileAsync().execute(url);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew_net);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        Login = (Button) findViewById(R.id.button_crewnet_testLogin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do stuff
                startDownload(dld_string);
                //end of that stuff
            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crew_net, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Downloading file...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                return progressDialog;
            default:
                return null;
        }
    }







    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            int count;

            String test_id = "6575"; //removed for this snippet
            String test_pass ="HutchinsonD1984"; //removed for this snippet
            String CrewnetUrl = "http://209.59.124.244/crewnet/login.aspx";
            String viewstate = "dDwtMjAxNjc0MDEyMDt0PDtsPGk8MT47PjtsPHQ8O2w8aTwxPjs+O2w8dDw7bDxpPDE+Oz47bDx0PDtsPGk8MT47PjtsPHQ8cDxsPHNyYzs+O2w8Li9ncmFwaGljcy9ibGFua2xvZ29ubG9nby5naWY7Pj47Oz47Pj47Pj47Pj47Pj47PoFjQ5nqGE+ozvmbjaYL4fJy99fs";
            String Home_url = "http://209.59.124.244/crewnet/home.aspx";

            CookieManager cookieManager = new CookieManager();
            //CookieHandler.setDefault(cookieManager);
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));



            try {
                URL login_url = new URL("http://209.59.124.244/crewnet/login.aspx");
                HttpURLConnection httpConn = (HttpURLConnection) login_url.openConnection();
                httpConn.setReadTimeout(10000);
                httpConn.setConnectTimeout(15000);
                httpConn.setRequestMethod("POST");
                httpConn.setDoInput(true);
                httpConn.setDoOutput(true);

//build the login string
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("txtUserName", test_id)
                        .appendQueryParameter("txtPassword", test_pass)
                        .appendQueryParameter("cmdLogon", "Log in")
                        .appendQueryParameter("__VIEWSTATE=", viewstate);
                String login_query = builder.build().getEncodedQuery();
//end of login user string construction

                OutputStream os = httpConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(login_query);
                writer.flush();
                writer.close();
                os.close();

                httpConn.connect();
                for (Map.Entry<String, List<String>> k : httpConn.getHeaderFields().entrySet()) {
                    for (String v : k.getValue()){
                        System.out.println(k.getKey() + ":" + v);
                    }
                }


                BufferedReader reader;
                String serverresponse = httpConn.getResponseMessage();
                StringBuilder SB = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    SB.append(line + "\n");
                }
                reader.close();
                Log.d("Http response", SB.toString());
//end of login and get session id



                //now getthefile
                httpConn.setReadTimeout(10000);
                httpConn.setConnectTimeout(15000);
                httpConn.setRequestMethod("GET");
                httpConn.setDoInput(true);
                httpConn.setDoOutput(true);
                //HttpURLConnection httpConn = (HttpURLConnection) login_url.openConnection();
                URL url = new URL(params[0]);
                httpConn = (HttpURLConnection) url.openConnection();
                //URLConnection conexion = url.openConnection();
                //conexion.connect();
                httpConn.connect();

                int lengthofFile = httpConn.getContentLength();
                Log.d("ANDRO_ASYNC", "Length of file: " + lengthofFile);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(Roster_fullpath);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthofFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                //view_pdf(Roster_fullpath);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub
            Log.d("ANDRO_ASYNC", values[0]);
            progressDialog.setProgress(Integer.parseInt(values[0]));
        }

    }



}//end of the main class
