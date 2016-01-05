package com.cm3.danielhutchinson.lialpatools;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CrewAdviceActivity extends ActionBarActivity {

    public ProgressDialog progressDialog;

    private static final String APP_Storage_home = "sdcard/";
    private static final String CrewAdviceStorage = "advices/";
    private static final String FDCrewAdviceDir = "cockpitadvices/";
    private static final String CCCrewAdviceDir = "ccadvices/";
    private static final String AdviceMasterFile = "advice_master.txt";

    private static final String CrewAdviceMainPath = APP_Storage_home + CrewAdviceStorage ;
    private static final String CrewAdviceMasterFile_url= APP_Storage_home + CrewAdviceStorage + AdviceMasterFile;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 1;


boolean OnlineStatus = false;
String ControllerAction = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew_advice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         OnlineStatus = CheckOnlineStatus();
        if (OnlineStatus == true)
        {
            //Stuff to do if there is a connection
            //CrewAdviceFunctions.CrewAdviceControl();
            ControllerAction = "PreCheck";
            new AdviceController().execute("PreCheck");
        }
        else if (OnlineStatus == false)
        {
            //Stuff to do if no connection
        }




    } //end of main on create method





    public boolean CheckOnlineStatus()
    {
        boolean returnval = false;
        if (AppStatus.getInstance(this).isOnline()) {
            returnval = true;
            //Toast.makeText(getApplicationContext(), "You are online", Toast.LENGTH_LONG).show();

        } else {
            returnval = false;
            //Toast.makeText(getApplicationContext(), "you are not online", Toast.LENGTH_LONG).show();
            Log.v("Home", "############################You are not online!!!!");
        }
        return returnval;
    }



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



//----Asynch task

    public class AdviceController extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            super.onPreExecute();
            if (!ControllerAction.equals("PreCheck")) {
                showDialog(DIALOG_DOWNLOAD_PROGRESS);
                progressDialog.setProgress(0);

            }
            else if (ControllerAction.equals("PreCheck"))
            {
                Toast.makeText(getApplicationContext(), "Manifest pre check starting", Toast.LENGTH_LONG).show();
            }

        } //end of pre execute


        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            int count;
            ControllerAction = params[0];

           String Stuff ="";
            //code to run in task goes here------------------------------
            String PreCheckStatus = CrewAdviceFunctions.PreCheck();
            //CrewAdviceFunctions.CrewAdviceController("PreCheck");

            return Stuff;
        } //end of main body of asynch task

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub
            if (!ControllerAction.equals("PreCheck"))
            {
                progressDialog.setProgress(Integer.parseInt(values[0]));
            }

        } //end of progress update


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub


            if (!ControllerAction.equals("PreCheck"))
            {
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            }

        } //end of the post execute

    }



}//end of mail class
