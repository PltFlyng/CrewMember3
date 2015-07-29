
package com.example.danielhutchinson.lialpatools;
/**
 * Created by Daniel on 7/25/2015.
 */
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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


public class CrewNetFunctions {

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






    public static String[] GetCrewBrief_CurrReportVal(String txt) {
        //txt="<script> win = window.open(\"ReportViewer.aspx?report=CrewBriefingReport_1655914080_6575.pdf\",\"ReportViewer\",\"toolbar=no,menubar=no,location=no,directories=no,scrollbars,channelmode=yes,fullscreen=yes\");location.href='./../home.aspx';</script>";
        String MatchFound = "false";
        String[] ReturnArray = new String [2];
        ReturnArray[0] = MatchFound;
        ReturnArray[1] = "";

        String re1 = ".*?";    // Non-greedy match on filler
        String re2 = "(\\d+)";    // Integer Number 1

        Pattern p = Pattern.compile(re1 + re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(txt);
        if (m.find()) {
            String int1 = m.group(1);
            System.out.print("(" + int1.toString() + ")" + "\n");
            MatchFound = "true";
            ReturnArray[0] = MatchFound;
            ReturnArray[1] = int1;

        }
        return ReturnArray;
    }






    public static String[] Login(String Operation) throws IOException {

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
        File path = new File("sdcard/");
        //path.mkdirs();
        File file = new File(path, "test_crewbrief.pdf");
        FileOutputStream fos = new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int len1 = 0;
        while ((len1 = in.read(buffer)) != -1) {
            fos.write(buffer, 0, len1);
        }

        fos.close();

        httpclient.getConnectionManager().shutdown();



    }



break;

} //end of the operation case switcher

//-------------------------

        return Cookie_strings;

    }//end of the login method




    public static void CrewNetMasterControl()
    {
        String [] SessionCookies = new String[2];
        try {
            SessionCookies = Login("CrewBrief");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //now that we got the session cookie we can do try to do stuff
        //LoadHomePage(SessionCookies);


    }//end of the controller function



}
