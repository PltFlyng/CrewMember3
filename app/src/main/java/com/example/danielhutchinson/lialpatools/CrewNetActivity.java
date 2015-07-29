package com.example.danielhutchinson.lialpatools;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;

public class CrewNetActivity extends ActionBarActivity {

    private static final String APP_Storage_home = "sdcard/";
    private static final String CrewnetStorage = "crewnet/";

   private static final String CrewNetLocalPath = APP_Storage_home + CrewnetStorage;


    Button Login;
    Button Advice_test;
    //for testing will resolve later

    String dir="/Attendancesystem";

    private ProgressDialog progressDialog;
    private String file_storage_string = "sdcard/";
    public static final int DIALOG_DOWNLOAD_PROGRESS = 1;

    public static final String test_id = "6575"; //removed for this snippet
    public static final String test_pass = "HutchinsonD1984"; //removed for this snippet
    public static final String CrewnetBase = "http://209.59.124.244/crewnet";
    public static final String CrewnetUrl = "http://209.59.124.244/crewnet/login.aspx";
    public static final String viewstate = "dDwtMjAxNjc0MDEyMDt0PDtsPGk8MT47PjtsPHQ8O2w8aTwxPjs+O2w8dDw7bDxpPDE+Oz47bDx0PDtsPGk8MT47PjtsPHQ8cDxsPHNyYzs+O2w8Li9ncmFwaGljcy9ibGFua2xvZ29ubG9nby5naWY7Pj47Oz47Pj47Pj47Pj47Pj47PoFjQ5nqGE+ozvmbjaYL4fJy99fs";
    public static final String Home_url = "http://209.59.124.244/crewnet/home.aspx";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0";
    public static final String CrewBrief_Request = CrewnetBase + "/forms/ReportCreator.aspx?ReportName=CrewBriefingReport?FlightPlanKeys=";
    public static final String ReportViewer_base = CrewnetBase + "/ReportViewer.aspx?report=";

    //------------progress updater
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
    //---------------------end of progress updater



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
                new CrewNetController().execute("CrewBrief");
                //end of that stuff
            }


        });

        Advice_test = (Button) findViewById(R.id.button_advice_test);
        Advice_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CrewAdvice.CrewAdviceControl();

            }
        });



    }//end of the on create

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

//----Asynch task

   private class CrewNetController extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            int count;
            String Operation = params[0];
            //code to run in task goes here------------------------------
            try {
                //persistent variables

                String Cookie_string_toset = "";


                //end of variables

                //Login Portion
                //Contact the server, get a session ID
                //login obtain cookies
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(CrewnetUrl);
                httpPost.setHeader("Host", "209.59.124.244");
                httpPost.setHeader("Referer", "http://209.59.124.244/Crewnet/login.aspx");
                httpPost.setHeader("User-Agent", USER_AGENT);


                System.out.println("Requesting : " + httpPost.getURI());  //test output that can be enabled for log debugging

                CookieStore cookieStore = new BasicCookieStore();
                HttpContext localContext = new BasicHttpContext();
                localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
                nameValuePair.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
                nameValuePair.add(new BasicNameValuePair("txtUserName", test_id));
                nameValuePair.add(new BasicNameValuePair("txtPassword", test_pass));
                nameValuePair.add(new BasicNameValuePair("cmdLogon", "Log in"));


                BasicClientCookie cookie1 = new BasicClientCookie("Name", "value"); //initialise the cookies just incase


                //Encode POST data

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));


            //make POST request.
//execute the request
                HttpResponse response = (HttpResponse) httpClient.execute(httpPost,localContext);
                HttpEntity entity = response.getEntity();

                String Cookie1 = localContext.getAttribute(ClientContext.COOKIE_STORE).toString();


                List<Cookie> cookies = cookieStore.getCookies();  //to be removed pending clean up

                String headers = response.getAllHeaders().toString();


                if (entity != null) {
                    InputStream  instream = entity.getContent();
                    try {
                        //get the input stream in case we have to process any input
                        //nothing to process at the moment
                        // process content
                    } finally {
                        instream.close();            //close the input stream
                        entity.consumeContent();     //close the entity
                    }
                }

                //Run through the cookies returned
                cookies = cookieStore.getCookies();
                int i = 0;
                String [] Cookie_strings = new String[2];
                for (Cookie cookie : cookies) {
                    //build the cookie strings
                    Cookie_strings[i] = cookie.getName() +":" + cookie.getValue() + ":" + cookie.getPath();
                    System.out.println("--- Cookie string: " + cookie.getName() +":" + cookie.getValue() + ":" + cookie.getPath());
                    i++;

                    //log output for debugging
                    System.out.println("Name = " + cookie.getName());
                    System.out.println("Value = " + cookie.getValue());
                    System.out.println("Path = " + cookie.getPath());
                    System.out.println("---");
                    //end of log output for debugging

                } //end of the cookie for loop

                //log debug demarking end of login portion
                Log.d("APP Trace", "End Login Test");

            //------cooke placement for other functions begins here------------------------------
                String CookieString_parts[] = Cookie_strings[0].split(":");
                String CookieString1 = CookieString_parts[0] + "=" + CookieString_parts[1] + "; path=" + CookieString_parts[2];
                cookie1 = new BasicClientCookie("Name", "value");
                cookie1 = new BasicClientCookie(CookieString_parts[0], CookieString_parts[1]);

                String CookieString_parts2[] = Cookie_strings[1].split(":");
                String CookieString2 = CookieString_parts2[0] + "=" + CookieString_parts2[1] + "; path=" + CookieString_parts2[2];
                BasicClientCookie cookie2 = new BasicClientCookie("Name", "value");
                cookie2 = new BasicClientCookie(CookieString_parts2[0], CookieString_parts2[1]);

                DefaultHttpClient httpclient = new DefaultHttpClient();
                localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);


//cookie.setDomain("your domain");
                cookie1.setPath("/");
                cookie2.setPath("/");

                cookieStore.addCookie(cookie1);
                cookieStore.addCookie(cookie2);
                localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
                httpclient.setCookieStore(cookieStore);
                // end of cookie placement for subsequent requests-----------------------------------------------------------------------------------

                switch (Operation) {
                    case "CrewBrief":


                        HttpGet httpget = new HttpGet(CrewBrief_Request);

                        httpget.setHeader("Host", "209.59.124.244");
                        httpget.addHeader("Referer", "http://209.59.124.244/Crewnet/default.aspx");
                        httpget.addHeader("User-Agent", USER_AGENT);

                        httpget.addHeader("Cookie", CookieString1);
                        httpget.addHeader("Cookie", CookieString2);

                        HttpResponse homeresponse = httpclient.execute(httpget, localContext);
                        entity = homeresponse.getEntity();
                        String responseBody = "";
                        responseBody = EntityUtils.toString(homeresponse.getEntity());

//for debug testing write response to log
                        Log.d("Headers", "---------------------------headers--------------------");

                        Header[] homeheaders = response.getAllHeaders();
                        for (Header header : homeheaders) {
                            System.out.println("Key : " + header.getName()
                                    + " ,Value : " + header.getValue());
                        }
                        Log.d("Headers", "---------------------------headers--------------------");
                        Log.d("Http get Response:", homeresponse.toString());
                        //Log.d("Http body :", responseBody);
//end of debug testing block

                        entity.consumeContent();

                        String[] Body_strings = responseBody.split("\n");
                        String[] CrewBriefLocator = {"false", "0", "0"};

                        //split the response body into lines that we can use to parse for the report indicator
                        for (int x = 0; x <= Body_strings.length - 1; x++) {
                            //System.out.println(Body_strings[x]);

                            String[] parser_tmp = MatchStaffNum(test_id, Body_strings[x]);

                            if (parser_tmp[0].equals("true")) {
                                CrewBriefLocator[0] = parser_tmp[0];
                                CrewBriefLocator[1] = parser_tmp[1];
                                CrewBriefLocator[2] = parser_tmp[2];
                            }

                        }
                        //report pointer url is created
                        String CrewbriefReportRequest = "none";
                        if (CrewBriefLocator[0].equals("true")) {
                            CrewbriefReportRequest = "http://209.59.124.244/Crewnet/Reports/CrewBriefingReport_" + CrewBriefLocator[1] + "_" + CrewBriefLocator[2] + ".pdf";
                        } else {CrewbriefReportRequest = "none";}

                        if (!CrewbriefReportRequest.equals("none"))
                        {
                            //http://209.59.124.244/Crewnet/Reports/CrewBriefingReport_216541890_6575.pdf
                            System.out.println("request url is " + CrewbriefReportRequest);

                            HttpGet reporthttpget = new HttpGet(CrewbriefReportRequest);
                            //Post Data
                            List<NameValuePair> CBnameValuePair = new ArrayList<NameValuePair>(4);
                            CBnameValuePair.add(new BasicNameValuePair("__VIEWSTATE", "dDwtNjE4MTAxMzgwOzs+mx+tMJxpkDyVNRQqrIe3HPBGFug="));
                            CBnameValuePair.add(new BasicNameValuePair("cmdExport", "Export"));


                            //Encode POST data
                            //reporthttpget.setEntity(new UrlEncodedFormEntity(CBnameValuePair)); //this is for post only

                            reporthttpget.setHeader("Host", "209.59.124.244");
                            reporthttpget.addHeader("Referer", "http://209.59.124.244/Crewnet/default.aspx");
                            reporthttpget.addHeader("User-Agent", USER_AGENT);

                            reporthttpget.addHeader("Cookie", CookieString1);
                            reporthttpget.addHeader("Cookie", CookieString2);

                            HttpResponse reportresponse = httpclient.execute(reporthttpget, localContext);
                            HttpEntity reportentity = reportresponse.getEntity();

                            String DESTINATION_PATH ="sdcard/downloaded_test_crewbrief.pdf";

                            InputStream in = reportentity.getContent();

                            long nTotalBytesInStream = (long) reportentity.getContentLength(); // Total data size
                            File path = new File("sdcard/");
                            //path.mkdirs();
                            File file = new File(path, "test_crewbrief.pdf");
                            FileOutputStream fos = new FileOutputStream(file);

                            byte[] buffer = new byte[1024];
                            int len1 = 0;
                            long total = 0;


                            while ((len1 = in.read(buffer)) != -1) {
                                fos.write(buffer, 0, len1);
                                total += len1;
                                publishProgress("" + (int) ((total * 100) / nTotalBytesInStream));
                        }
                            in.close();
                            fos.close();

                            httpclient.getConnectionManager().shutdown();



                        }



                        break;

                } //end of the operation case switcher



                //this is the catch clause for the entire task
     } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
     } catch (ClientProtocolException e) {
                e.printStackTrace();
     } catch (IOException e) {
                e.printStackTrace();
     }
            //end of catch clause for the task






            //code to run in task ends here------------------------------

            //this is the null return for the asynch task
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







//---------
    public void displaypdf() {

        File file = null;
        file = new File(Environment.getExternalStorageDirectory()+dir+ "/sample.pdf");
        Toast.makeText(getApplicationContext(), file.toString() , Toast.LENGTH_LONG).show();
        if(file.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        }
        else
            Toast.makeText(getApplicationContext(), "File path is incorrect." , Toast.LENGTH_LONG).show();
    }


    public static String[] MatchStaffNum(String StaffNum, String txt)
    {
        String MatchFound = "false";
        String[] ReturnArray = new String [3];
        ReturnArray[0] = MatchFound;
        ReturnArray[1] = "";
        ReturnArray[2] = "";


        String re1=".*?";	// Non-greedy match on filler
        String re2="(\\d+)";	// Integer Number 1
        String re3=".*?";	// Non-greedy match on filler
        String re4="(" + StaffNum + ")";	// Integer Number 2

        Pattern p = Pattern.compile(re1+re2+re3+re4,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(txt);
        if (m.find())
        {
            String int1=m.group(1);
            String int2=m.group(2);
            //print some test output for the log
            System.out.print("("+int1.toString()+")"+"("+int2.toString()+")"+"\n");
            //end of test output

            MatchFound = "true";
            ReturnArray[0] = MatchFound;
            ReturnArray[1] = int1;
            ReturnArray[2] = int2;
        }
        return ReturnArray;
    }


}//end of the main class
