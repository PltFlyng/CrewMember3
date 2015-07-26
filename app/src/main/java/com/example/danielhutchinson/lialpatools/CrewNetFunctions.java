package com.example.danielhutchinson.lialpatools;
/**
 * Created by Daniel on 7/25/2015.
 */

import android.net.Uri;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
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

    private static String test_id = "6575"; //removed for this snippet
    private static String test_pass ="HutchinsonD1984"; //removed for this snippet
    private static String CrewnetUrl = "http://209.59.124.244/crewnet/login.aspx";
    private static String viewstate = "dDwtMjAxNjc0MDEyMDt0PDtsPGk8MT47PjtsPHQ8O2w8aTwxPjs+O2w8dDw7bDxpPDE+Oz47bDx0PDtsPGk8MT47PjtsPHQ8cDxsPHNyYzs+O2w8Li9ncmFwaGljcy9ibGFua2xvZ29ubG9nby5naWY7Pj47Oz47Pj47Pj47Pj47Pj47PoFjQ5nqGE+ozvmbjaYL4fJy99fs";
    private static String Home_url = "http://209.59.124.244/crewnet/home.aspx";


    public static String[] Login() throws IOException {
        String test_id = "6575"; //removed for this snippet
        String test_pass = "HutchinsonD1984"; //removed for this snippet
        String CrewnetUrl = "http://209.59.124.244/crewnet/login.aspx";
        String viewstate = "dDwtMjAxNjc0MDEyMDt0PDtsPGk8MT47PjtsPHQ8O2w8aTwxPjs+O2w8dDw7bDxpPDE+Oz47bDx0PDtsPGk8MT47PjtsPHQ8cDxsPHNyYzs+O2w8Li9ncmFwaGljcy9ibGFua2xvZ29ubG9nby5naWY7Pj47Oz47Pj47Pj47Pj47Pj47PoFjQ5nqGE+ozvmbjaYL4fJy99fs";
        String Home_url = "http://209.59.124.244/crewnet/home.aspx";
        String Cookie_string_toset = "";
        String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0";

        HttpClient httpClient = new DefaultHttpClient();


        HttpPost httpPost = new HttpPost(CrewnetUrl);
        httpPost.setHeader("Host", "209.59.124.244");
        httpPost.setHeader("Referer", "http://209.59.124.244/Crewnet/login.aspx");
        httpPost.setHeader("User-Agent", USER_AGENT);


        System.out.println("Requesting : " + httpPost.getURI());

        CookieStore cookieStore = new BasicCookieStore();
        HttpContext localContext = new BasicHttpContext();

        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);




        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
        nameValuePair.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
        nameValuePair.add(new BasicNameValuePair("txtUserName", test_id));
        nameValuePair.add(new BasicNameValuePair("txtPassword", test_pass));
        nameValuePair.add(new BasicNameValuePair("cmdLogon", "Log in"));


        BasicClientCookie cookie1 = new BasicClientCookie("Name", "value");


        //Encode POST data

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));


        //make POST request.

            HttpResponse response = (HttpResponse) httpClient.execute(httpPost,localContext);

            String Cookie1 = localContext.getAttribute(ClientContext.COOKIE_STORE).toString();

            //print some test output for the log
            //Log.d("Cookie1:= ", Cookie1);

        List<Cookie> cookies = cookieStore.getCookies();
        //for (Cookie cookie : cookies) {
            //System.out.println("Name = " + cookie.getName());
            //System.out.println("Value = " + cookie.getValue());
            //System.out.println("Path = " + cookie.getPath());
            //System.out.println("---");
        //}





            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            HttpPost httppost = new HttpPost(CrewnetUrl);
        httppost.setHeader("Host", "209.59.124.244");
        httppost.setHeader("Referer", "http://209.59.124.244/Crewnet/login.aspx");
        httppost.setHeader("User-Agent", USER_AGENT);

            response = httpClient.execute(httppost, localContext);

            String headers = response.getAllHeaders().toString();

        //test
        cookies = cookieStore.getCookies();
        int i = 0;
        String [] Cookie_strings = new String[2];
        for (Cookie cookie : cookies) {
            System.out.println("Name = " + cookie.getName());
            System.out.println("Value = " + cookie.getValue());
            System.out.println("Path = " + cookie.getPath());
            System.out.println("---");

            Cookie_strings[i] = cookie.getName() +":" + cookie.getValue() + ":" + cookie.getPath();
            System.out.println("--- Cookie string: " + cookie.getName() +":" + cookie.getValue() + ":" + cookie.getPath());
            i++;


        }




        Log.d("APP Trace", "End Login Test");


        return Cookie_strings;

    }//end of the login method



    public static void LoadHomePage(String[] SessionCookies) throws IOException
    {
        System.out.println("Cookie0 passed in is " + SessionCookies[0]);
        System.out.println("Cookie1 passed in is " + SessionCookies[1]);
        String CookieString_parts[] = SessionCookies[0].split(":");
        String CookieString1 = CookieString_parts[0] + "=" + CookieString_parts[1] + "; path=" + CookieString_parts[2];

        String CookieString_parts2[] = SessionCookies[1].split(":");
        String CookieString2 = CookieString_parts2[0] + "=" + CookieString_parts2[1] + "; path=" + CookieString_parts2[2];

        //.ASPXAUTH=55293A4B75556547C4F314DC73321A79746EE18B4CCD99F4AB6EB26FC0230721221C315522AC9C9C2969E0CEEA0A8F91999E2BB61224BF173A943790B56ED304; path=/

        String test_id = "6575"; //removed for this snippet
        String test_pass = "HutchinsonD1984"; //removed for this snippet
        String CrewnetUrl = "http://209.59.124.244/crewnet/login.aspx";
        String viewstate = "dDwtMjAxNjc0MDEyMDt0PDtsPGk8MT47PjtsPHQ8O2w8aTwxPjs+O2w8dDw7bDxpPDE+Oz47bDx0PDtsPGk8MT47PjtsPHQ8cDxsPHNyYzs+O2w8Li9ncmFwaGljcy9ibGFua2xvZ29ubG9nby5naWY7Pj47Oz47Pj47Pj47Pj47Pj47PoFjQ5nqGE+ozvmbjaYL4fJy99fs";
        String Home_url = "http://209.59.124.244/crewnet/home.aspx";


        String https_url = "https://ssl.filmweb.pl/j_login";
        https_url = CrewnetUrl;
        URL url = new URL(Home_url);
        String post = "_login_redirect_url=http%253A%252F%252Fwww.filmweb.pl%252F&j_username=test.filmweb%40gmail.com&j_password=test.filmweb&_rememberMe=on&pass=zaloguj";

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("txtUserName", test_id)
                .appendQueryParameter("txtPassword", test_pass)
                .appendQueryParameter("cmdLogon", "Log in")
                .appendQueryParameter("__VIEWSTATE=", viewstate);
        String query = builder.build().getEncodedQuery();
        post = query;
        post = Home_url;

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setInstanceFollowRedirects(true);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestMethod("GET");
        con.setRequestProperty("Host", "209.59.124.244");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:13.0) Gecko/20100101 Firefox/13.0.1");
        con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        con.setRequestProperty("Accept-Language", "pl,en-us;q=0.7,en;q=0.3");
        con.setRequestProperty("Accept-Encoding", "gzip, deflate");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("Referer", "http://209.59.124.244/crewnet/login.aspx");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //con.setRequestProperty("Content-Length", post.length() + "");
        con.setRequestProperty("Cookie", CookieString1);
        con.setRequestProperty("Cookie", CookieString2);



        //OutputStream os = con.getOutputStream();
        //BufferedWriter writer = new BufferedWriter(
                //new OutputStreamWriter(os, "UTF-8"));
        //writer.write(query);
        //writer.flush();
        //writer.close();
        //os.close();

        con.connect();


        BufferedReader reader;
        String serverresponse = con.getResponseMessage();
        StringBuilder SB = new StringBuilder();
        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            SB.append(line + "\n");
        }
        reader.close();
        Log.d("Http response", SB.toString());

    }//end of the load home page









public static void CrewNetMasterControl()
{
    String [] SessionCookies = new String[2];
    try {
        SessionCookies = Login();
    } catch (IOException e) {
        e.printStackTrace();
    }
    //now that we got the session cookie we can do try to do stuff
    try {
        LoadHomePage(SessionCookies);
    } catch (IOException e) {
        e.printStackTrace();
    }


}//end of the controller function



}
