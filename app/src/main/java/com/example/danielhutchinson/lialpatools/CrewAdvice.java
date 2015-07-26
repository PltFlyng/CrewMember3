package com.example.danielhutchinson.lialpatools;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.net.*;
import java.io.*;

/**
 * Created by Daniel on 7/23/2015.
 */
public class CrewAdvice {
    //master variables for the class
    static String CrewAdviceMasterControlURL = "http://lialpa.org/CM3/CrewAdvices/advice_master.txt";
    private ProgressDialog progressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 1;
    private String CrewAdviceMasterControlStream ="";

    //@Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                //progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Downloading file...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                return progressDialog;
            default:
                return null;
        }
    }

    public static String GetMasterFileStream() throws Exception {

        Long myCurrentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy+HH:mm:ss");
        Date resultdate = new Date(myCurrentTimeMillis);

        StringBuilder sb = new StringBuilder();


        System.out.println("Current Time is: " + myCurrentTimeMillis);
        System.out.println(sdf.format(resultdate));


        URL AdviceMasterControl = new URL(CrewAdviceMasterControlURL);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(AdviceMasterControl.openStream()));

        String inputLine = null;
        while ((inputLine = in.readLine()) != null)
               System.out.println(inputLine);   //for producing test output
               sb.append((inputLine + "\n"));
        in.close();
        return sb.toString();
    }

public static void CrewAdviceControl()
{
    try {
        String CrewAdviceMasterControlStream = GetMasterFileStream();
    } catch (Exception e) {
        e.printStackTrace();
    }

}



}//end of the main class
