package com.cm3.danielhutchinson.lialpatools;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class CrewNetActivity extends ActionBarActivity {
    private boolean OnlineStatus = false;
    private static final String APP_Storage_home = "sdcard/";
    private static final String CrewnetStorage = "crewnet/";

    private static final String CrewNetLocalPath = APP_Storage_home + CrewnetStorage;
    private static final String CrewBriefFileName = "daily_crewbrief.pdf";
    public static final String CurrentRosterfFileName = "current_roster.pdf";
    private static final String NextRosterFileName = "next_roster.pdf";
    public static final String PreviousRosterfFileName = "previous_roster.pdf";
    boolean NextRosterAvailable = false;
    public String ReportType = "null";
    private Boolean CrewNetCredentialsStatus = false;

    private static final String CrewNetDir = GlobalManagement.GetGlobalDirPaths("CrewNetDir");
    private String DESTINATION_FILE = "";
    public Boolean LoginPassed = false;
    public Boolean CrewNetCredentialsChecked = false;
    public String CrewNetErrorCode = "0";
    public int LoginFailedAttempts = 0;
    public Boolean LoginLimitExceded = false;

    public ProgressDialog progressDialog;
    private String file_storage_string = "sdcard/";
    public static final int DIALOG_DOWNLOAD_PROGRESS = 1;

    public static String User_crewnet_id = ""; //removed for this snippet
    public static String user_crewnet_password = ""; //removed for this snippet
    public static final String CrewnetBase = "http://209.59.124.244/crewnet";
    public static final String CrewnetUrl = "http://209.59.124.244/crewnet/login.aspx";
    public static final String viewstate = "dDwtMjAxNjc0MDEyMDt0PDtsPGk8MT47PjtsPHQ8O2w8aTwxPjs+O2w8dDw7bDxpPDE+Oz47bDx0PDtsPGk8MT47PjtsPHQ8cDxsPHNyYzs+O2w8Li9ncmFwaGljcy9ibGFua2xvZ29ubG9nby5naWY7Pj47Oz47Pj47Pj47Pj47Pj47PoFjQ5nqGE+ozvmbjaYL4fJy99fs";
    public static final String Home_url = "http://209.59.124.244/crewnet/home.aspx";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0";
    public static final String CrewBrief_Request = CrewnetBase + "/forms/ReportCreator.aspx?ReportName=CrewBriefingReport?FlightPlanKeys=";
    public static final String ReportViewer_base = CrewnetBase + "/ReportViewer.aspx?report=";

    public static final String CurrentRosterBaseQuery = CrewnetBase + "/forms/reportcreator.aspx?reportname=RosterCurrent&periodstart=";

    public Long myCurrentTimeMillis = System.currentTimeMillis();
    public SimpleDateFormat sdf_reports = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    Date roster_stored_time = null;

    Button DownLoadRoster;
    Button DownLoadNextRoster;
    Button DownLoadPreviousRoster;

    Button ViewStoredRoster;
    Button ViewStoredNextRoster;
    Button ViewStoredPreviousRoster;

    Button DownLoadCrewbriefing;
    Button ViewStoredCrewbriefing;

    TextView CrewNetTop_textbox;
    TextView CrewNet_bottom_NoticeArea;

    Button Advice_test;
    //for testing will resolve later

    String dir="/Attendancesystem";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew_net);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        DownLoadRoster = (Button) findViewById(R.id.button_crewnet_download_roster);
        DownLoadNextRoster = (Button) findViewById(R.id.button_crewnet_download_nextroster);
        DownLoadPreviousRoster  = (Button) findViewById(R.id.button_crewnet_download_lastroster);

        ViewStoredRoster  = (Button) findViewById(R.id.button_crewnet_view_storedroster);
        ViewStoredNextRoster = (Button) findViewById(R.id.button_crewnet_view_storedNextRoster);
        ViewStoredPreviousRoster = (Button) findViewById(R.id.button_crewnet_view_storedLastRoster);

        DownLoadCrewbriefing = (Button) findViewById(R.id.button_crewnet_download_crewbriefing);
        ViewStoredCrewbriefing = (Button) findViewById(R.id.button_crewnet_view_storedcrewbrief);

        CrewNetTop_textbox = (TextView) findViewById(R.id.textView_crewnet_ui_topNotice);
        CrewNet_bottom_NoticeArea = (TextView) findViewById(R.id.textView_crewnet_ui_BottomNoticeArea);


        String [] UserCredentials = ReturnCredentials();

        User_crewnet_id = UserCredentials[2];
        user_crewnet_password = UserCredentials[3];
        LoginFailedAttempts = Integer.valueOf(NumberOfLoginFails());
        System.out.println("User Credentials: " + User_crewnet_id + user_crewnet_password);

        CheckCrewnetSupplied();
        OnlineStatus = CheckOnlineStatus();
        LoginLimitExceded = failedLoginExceded();
        ReportType = "CheckStatus";

            if (OnlineStatus == true && CrewNetCredentialsStatus == true && LoginLimitExceded == false) {

                    Toast.makeText(getApplicationContext(), "Checking login credentials & available reports....", Toast.LENGTH_LONG).show();
                    new CrewNetController().execute("CheckStatus");

            }
            if (OnlineStatus == false) {
                Toast.makeText(getApplicationContext(), "No Active Network Connection Detected. you are working in offline mode.", Toast.LENGTH_LONG).show();
            }
        if (LoginLimitExceded  == true) {
            LoginAttemptsExcededPopup();
        }



        DownLoadCrewbriefing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do stuff
                ReportType = "CrewBrief";
                new CrewNetController().execute("CrewBrief");
                //end of that stuff
            }


        });

        DownLoadRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ReportType = "CurrentRoster";
                new CrewNetController().execute("CurrentRoster");
            }
        });
        DownLoadNextRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ReportType = "NextRoster";
                new CrewNetController().execute("NextRoster");
            }
        });
        DownLoadPreviousRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ReportType = "LastRoster";
                new CrewNetController().execute("LastRoster");
            }
        });

        ViewStoredRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //display current roster
                displaypdf(CrewNetDir + CurrentRosterfFileName);
            }
        });
        ViewStoredNextRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //display current roster
                displaypdf(CrewNetDir + NextRosterFileName);
            }
        });
        ViewStoredPreviousRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //display current roster
                displaypdf(CrewNetDir + PreviousRosterfFileName);
            }
        });

        ViewStoredCrewbriefing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //display current roster
                displaypdf(CrewNetDir + CrewBriefFileName);
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
            Intent newintent = new Intent(CrewNetActivity.this, UserDetailsActivity.class);
            startActivity(newintent);

            return true;
        }

        if (id == R.id.action_about) {
            Intent newintent = new Intent(CrewNetActivity.this, AboutActivity.class);
            startActivity(newintent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void CredentialsPopup() {
        LayoutInflater inflater = this.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.popup_crewnetcredentials, null);
        final Button SetUserDetailsButton = (Button) layout.findViewById(R.id.button_crewnetcredential_popup_setcredentials);
        final Button Dismiss = (Button) layout.findViewById(R.id.button_Crewnet_set_credentials_dismiss);
        final PopupWindow SetCredentials_popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        SetCredentials_popupWindow.setFocusable(false);
        SetCredentials_popupWindow.setTouchable(true);
        SetCredentials_popupWindow.setOutsideTouchable(true);

        layout.post(new Runnable() {

            public void run() {
                SetCredentials_popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                Dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetCredentials_popupWindow.dismiss();
                    }
                });
                SetUserDetailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetCredentials_popupWindow.dismiss();
                        Intent newintent = new Intent(CrewNetActivity.this, UserDetailsActivity.class);
                        startActivity(newintent);
                    }
                });
            }

        });
    }

    private void LoginAttemptsExcededPopup() {
        LayoutInflater inflater = this.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.popup_loginattemps_exceded, null);

        final Button Dismiss = (Button) layout.findViewById(R.id.button_popup_loginattempts_dismiss_reset);
        final Button LaunchBrowser = (Button) layout.findViewById(R.id.button_loginattemptspopup_launchbrowser);
        final PopupWindow SetCredentials_popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        SetCredentials_popupWindow.setFocusable(false);
        SetCredentials_popupWindow.setTouchable(true);
        SetCredentials_popupWindow.setOutsideTouchable(true);

        layout.post(new Runnable() {

            public void run() {
                SetCredentials_popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                Dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        failedLoginCounter("reset");
                        SetCredentials_popupWindow.dismiss();
                        Intent newintent = new Intent(CrewNetActivity.this, UserDetailsActivity.class);
                        startActivity(newintent);

                    }
                });

                LaunchBrowser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //SetCredentials_popupWindow.dismiss();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CrewnetUrl)));

                    }
                });
            }

        });
    }

    public String NumberOfLoginFails()
    {
        String loginfails = "";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loginfails = sharedPreferences.getString("CrewnetLoginFails", "0");
        return loginfails;
    }

    public String[] ReturnCredentials() {
        String [] UserDetails = {null, null, null, null, "0"};
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserDetails[0] = sharedPreferences.getString("UsersName", null);
        UserDetails[1]= sharedPreferences.getString("UsersEmail", null);
        UserDetails[2] = sharedPreferences.getString("UsersCrewNetId", null);
        UserDetails[3] = sharedPreferences.getString("UsersCrewNetPass", null);
        UserDetails[4] = sharedPreferences.getString("CrewnetLoginFails", "0");
        return UserDetails;
    }

    public Boolean failedLoginExceded()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String attempt_counter_str = sharedPreferences.getString("CrewnetLoginFails", "0");
        Boolean return_flag = false;
        int attempt_counter = Integer.valueOf(attempt_counter_str);
        if (attempt_counter >= 3)
        {
            return_flag = true;
        }else {
            return_flag = false;
        }
return return_flag;

    }
    public void failedLoginCounter (String Command)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String keyname = "CrewnetLoginFails";
        int attempt_counter = 0;
        String keyvaltoset = "0";
        switch (Command) {
                    case "increment":
                                    String attempt_counter_str = sharedPreferences.getString(keyname, "0");
                                    attempt_counter = Integer.valueOf(attempt_counter_str);
                                    attempt_counter++;

                        break;
                    case "reset":
                        attempt_counter = 0;
                        break;

                }
        keyvaltoset = String.valueOf(attempt_counter);
        savePreferences(keyname, keyvaltoset);
    }

    public void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private String ExsistingFileCheck(String ReportTypeToGet)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ReportTimestamp;
        String ReportVar = "";
        Boolean FileExsists = false;
        String FileToCheck = "";


        switch (ReportTypeToGet)
        {
            case "CurrentRoster":
                ReportVar = "CurrentRoster";
                FileToCheck = CrewNetDir +CurrentRosterfFileName ;
                break;
            case "NextRoster":
                ReportVar = "NextRoster";
                FileToCheck = CrewNetDir + NextRosterFileName;
                break;
            case "LastRoster":
                ReportVar = "LastRoster";
                FileToCheck = CrewNetDir + PreviousRosterfFileName;
                break;
            case "CrewBrief":
                ReportVar = "CrewBrief";
                FileToCheck = CrewNetDir + CrewBriefFileName;
                break;
        }
        FileExsists = GlobalManagement.CheckFileExsists(FileToCheck);

        ReportTimestamp = sharedPreferences.getString(ReportVar, "0");

        if (FileExsists == false){ReportTimestamp = "0";}

        return ReportTimestamp;
    }

    public void SetDownloadButtonStatuses(Boolean CredentialsSet, Boolean CredentialsVerified)
    {
        String top_msg = "Use the buttons below to access the desired reports.";
        Boolean Downloadbutton_status = false;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String attempt_counter_str = sharedPreferences.getString("CrewnetLoginFails", "0");
        int attempt_counter = Integer.valueOf(attempt_counter_str);

        if (CredentialsSet== true && CredentialsVerified ==true)
        {
            Downloadbutton_status = true;
        } else {Downloadbutton_status = false;}

        DownLoadRoster.setEnabled(Downloadbutton_status);
        DownLoadNextRoster.setEnabled(Downloadbutton_status);
        DownLoadPreviousRoster.setEnabled(Downloadbutton_status);
        DownLoadCrewbriefing.setEnabled(Downloadbutton_status);
        DownLoadNextRoster.setEnabled(NextRosterAvailable);



        // TextView CrewNet_bottom_NoticeArea;

    }

    public void CheckCrewnetSupplied()
   {

       String top_msg = "Use the buttons below to access the desired reports.";
       SimpleDateFormat button_sdf = new SimpleDateFormat("dd-mm-yyyy HH:mm");
       Date Buttonresultdate = null;

       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

       String UserID = sharedPreferences.getString("UsersCrewNetId", "");
       String Userpass = sharedPreferences.getString("UsersCrewNetPass", "");


       if (UserID.isEmpty()){UserID = null;}
       if (Userpass.isEmpty()){Userpass = null;}

       if (UserID == null || Userpass == null) {
       CrewNetCredentialsStatus = false;
       top_msg = "The buttons below will be enabled once you have provided your CrewNet Credentials.";
       CredentialsPopup();
       } else {
           CrewNetCredentialsStatus = true;
           top_msg = "Use the buttons below to access the desired reports.";
           // handle the value
       }
       //ExsistingFileCheck();
       String CurrentRoster_stored_time = ExsistingFileCheck("CurrentRoster");
       String NextRoster_stored_time  = ExsistingFileCheck("NextRoster");
       String LastRoster_stored_time = ExsistingFileCheck("LastRoster");
       String CrewBrief_stored_time = ExsistingFileCheck("CrewBrief");

       ViewStoredRoster.setEnabled(CrewNetCredentialsStatus);
       ViewStoredNextRoster.setEnabled(CrewNetCredentialsStatus);
       ViewStoredPreviousRoster.setEnabled(CrewNetCredentialsStatus);
       ViewStoredCrewbriefing.setEnabled(CrewNetCredentialsStatus);

       Boolean dldbuttonstatus = false;
       DownLoadRoster.setEnabled(dldbuttonstatus);
       DownLoadNextRoster.setEnabled(CrewNetCredentialsStatus);
       DownLoadPreviousRoster.setEnabled(dldbuttonstatus);
       DownLoadCrewbriefing.setEnabled(dldbuttonstatus);
       DownLoadNextRoster.setEnabled(dldbuttonstatus);

       //these buttons have different states depending on if the reports exsist
       Button [] reportbuttons = {ViewStoredRoster, ViewStoredNextRoster, ViewStoredPreviousRoster, ViewStoredCrewbriefing};
       String [] button_texts = {"Stored Roster", "Stored Next Roster", "Stored Last Roster", "Stored CrewBrief"};
       String[] Button_StoredTimes = {ExsistingFileCheck("CurrentRoster"), ExsistingFileCheck("NextRoster"), ExsistingFileCheck("LastRoster"), ExsistingFileCheck("CrewBrief")};

       //Button_StoredTimes[0] = ExsistingFileCheck("CurrentRoster");
       //Button_StoredTimes[1] = ExsistingFileCheck("NextRoster");
       //Button_StoredTimes[2] = ExsistingFileCheck("LastRoster");
        //Button_StoredTimes[3] = ExsistingFileCheck("CrewBrief");
       for (int b = 0; b < reportbuttons.length; b++)
       {
           if (!Button_StoredTimes[b].equals("0"))
           {

               reportbuttons[b].setText(button_texts[b] + "\n" + Button_StoredTimes[b]);
           } else if  (Button_StoredTimes[b].equals("0"))
           {
               reportbuttons[b].setEnabled(false);
               reportbuttons[b].setText(button_texts[b] + "\n" + " ");
           }
       }

       CrewNetTop_textbox.setText(top_msg);
       //DownLoadNextRoster.setEnabled(NextRosterAvailable);
      // TextView CrewNet_bottom_NoticeArea;


   }

public void UpdateNextRosterAvailableButton(boolean status)
{
    DownLoadNextRoster.setEnabled(status);
}


    public String ReportRequestUrlGenerator (String ReportType)
    {
        String URL = "null";
        Long myCurrentTimeMillis;
        Long NextRosterTimeMillis;
        SimpleDateFormat sdf;
        SimpleDateFormat sdf_nextroster;
        Date resultdate;
        String MyDateString;
        String My_NextRosterDateString;
        Calendar now = Calendar.getInstance();


        switch (ReportType){
            case "CrewBrief":
                URL = CrewBrief_Request;
                break;
            case "CurrentRoster":
                myCurrentTimeMillis = System.currentTimeMillis();
                sdf = new SimpleDateFormat("M/dd/yyyy");
                resultdate = new Date(myCurrentTimeMillis);
                MyDateString = sdf.format(resultdate).toString();
                URL = CurrentRosterBaseQuery + MyDateString + "&periodend=" + MyDateString;
                //System.out.println("My URL returned String is " + URL);
                break;

            case "LastRoster":

                System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

                // add days to current date using Calendar.add method
                now.add(Calendar.DATE, -28);

                System.out.println("date after 28 days : " + (now.get(Calendar.MONTH) + 1) + "-"
                        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
                MyDateString = (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DATE) + "/" + now.get(Calendar.YEAR);

                URL = CurrentRosterBaseQuery + MyDateString + "&periodend=" + MyDateString;
                break;

            case "CheckStatus":
            case "NextRoster":

                System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

                // add days to current date using Calendar.add method
                now.add(Calendar.DATE, 28);

                System.out.println("date after 28 days : " + (now.get(Calendar.MONTH) + 1) + "-"
                        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
                MyDateString = (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DATE) + "/" + now.get(Calendar.YEAR);

                URL = CurrentRosterBaseQuery + MyDateString + "&periodend=" + MyDateString;
                break;

            default:
                URL = "null";
                break;




        }//end of the switch case

        return URL;
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

   public class CrewNetController extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            int count;
            String Operation = params[0];
            ReportType = Operation;
                    LoginPassed = false;
            //code to run in task goes here------------------------------
            try {
                //persistent variables

                String Cookie_string_toset = "";
                //User_crewnet_id = "6575"; //removed for this snippet
                //user_crewnet_password = "HutchinsonD1984"; //removed for this snippet

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
                nameValuePair.add(new BasicNameValuePair("txtUserName", User_crewnet_id));
                nameValuePair.add(new BasicNameValuePair("txtPassword", user_crewnet_password));
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

                //for debug testing write response to log
                Log.d("Headers", "---------------------------headers--------------------");

                Header[] initialheaders = response.getAllHeaders();
                for (Header header : initialheaders ) {
                    System.out.println("Key : " + header.getName()
                            + " ,Value : " + header.getValue());
                }
                Log.d("Headers", "---------------------------headers--------------------");

//end of debug testing block

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
                if (cookies.size() >= 2 ){LoginPassed = true;}
                if (cookies.size() < 2 ){LoginPassed = false;}
                //log debug demarking end of login portion
                Log.d("APP Trace", "End Login Test");
if (LoginPassed == true) {
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

//----Declare Variables used globally in each of the switches cases

    //String ReportType = "null";
    String Report_initial_request = "null";
    //----end of case shared variables
    //options are CrewBrief, CurrentRoster, NextRoster, CheckStatus
    switch (Operation) {
        case "CrewBrief":
            ReportType = "CrewBrief";
            Report_initial_request = ReportRequestUrlGenerator(ReportType);
            break;
        case "CurrentRoster":
            ReportType = "CurrentRoster";
            Report_initial_request = ReportRequestUrlGenerator(ReportType);
            break;
        case "NextRoster":
            ReportType = "NextRoster";
            Report_initial_request = ReportRequestUrlGenerator(ReportType);
            break;
        case "LastRoster":
            ReportType = "LastRoster";
            Report_initial_request = ReportRequestUrlGenerator(ReportType);
            break;
        case "CheckStatus":
            ReportType = "CheckStatus";
            Report_initial_request = ReportRequestUrlGenerator(ReportType);
            break;

        default:
            ReportType = "null";
            Report_initial_request = "null";
            break;
    }

//query the report generator
    System.out.println("getting initial report:" + Report_initial_request);
    HttpGet httpget = new HttpGet(Report_initial_request);

    httpget.setHeader("Host", "209.59.124.244");
    httpget.addHeader("Referer", "http://209.59.124.244/crewnet/home.aspx");
    httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

    httpget.addHeader("Accept-Language", "en-US,en;q=0.5");
    httpget.addHeader("Accept-Encoding", "gzip, deflate");
    httpget.addHeader("User-Agent", USER_AGENT);

    httpget.addHeader("Cookie", CookieString1);
    httpget.addHeader("Cookie", CookieString2);
    httpget.addHeader("Connection", "keep-alive");
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
    Log.d("Http body :", responseBody);
//end of debug testing block

    entity.consumeContent();
    //at this point the raw html returned is contained in response body we need to parse it and return a report url for report creator
    //responseBody is the var containing the raw html
//parse the response to get the final url for the report or the "if available next roster check"
    String ReportURL = "none";


    ReportURL = CreatorParser(ReportType, responseBody);
    switch (ReportType) {
        case "CheckStatus":
            CrewNetCredentialsChecked = true;
            if (ReportURL.equals("Available")) {
                //ToDo Update NextRoster Button
                NextRosterAvailable = true;
                System.out.println("------------------------------roster is available");

            } else if (ReportURL.equals("Not Available")) {
                NextRosterAvailable = false; System.out.println("-------------------roster is not available");
            }
            break;
        case "CrewBrief":
        case "CurrentRoster":
        case "NextRoster":
        case "LastRoster":
            if (!ReportURL.equals("none") || !ReportType.equals("CheckStatus") ) {
                //http://209.59.124.244/Crewnet/Reports/CrewBriefingReport_216541890_6575.pdf
                System.out.println("request url is " + ReportURL);

                HttpGet reporthttpget = new HttpGet(ReportURL);

                httpget.setHeader("Host", "209.59.124.244");
                httpget.addHeader("Referer", "http://209.59.124.244/crewnet/home.aspx");
                httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

                httpget.addHeader("Accept-Language", "en-US,en;q=0.5");
                httpget.addHeader("Accept-Encoding", "gzip, deflate");
                httpget.addHeader("User-Agent", USER_AGENT);

                httpget.addHeader("Cookie", CookieString1);
                httpget.addHeader("Cookie", CookieString2);
                httpget.addHeader("Connection", "keep-alive");

                HttpResponse reportresponse = httpclient.execute(reporthttpget, localContext);
                HttpEntity reportentity = reportresponse.getEntity();


                InputStream in = reportentity.getContent();
                long nTotalBytesInStream = (long) reportentity.getContentLength(); // Total data size

                String[] SaveFile_fullpath = ReportSaveFileNameGenerator(ReportType);

                File path = new File(SaveFile_fullpath[0]);
                //path.mkdirs();
                File file = new File(path, SaveFile_fullpath[1]);
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


                DESTINATION_FILE = SaveFile_fullpath[0] + SaveFile_fullpath[1];
                myCurrentTimeMillis = System.currentTimeMillis();
                roster_stored_time = new Date(myCurrentTimeMillis);
                        //sdf_reports.format(roster_stored_time);
                savePreferences(ReportType, sdf_reports.format(roster_stored_time));
                //displaypdf(DESTINATION_FILE);
                httpclient.getConnectionManager().shutdown();
            } //this is for the report getter iff
            else {
                //warn the user that no url was provided


            }
            break;

    }

}//end of the login is true statement
  else {
    LoginPassed = false;
            //so we can Let the user know login failed


       }
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

                Log.d("updater post", ReportType);

            if (!ReportType.equals("CheckStatus")) {
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
                displaypdf(DESTINATION_FILE);
            }

            if (LoginPassed == false){
                failedLoginCounter("increment");
                LoginFailedAttempts = Integer.valueOf(NumberOfLoginFails());
                Toast.makeText(getApplicationContext(), "Login Failed!! Check user ID and Password.", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Login Attempts: " + LoginFailedAttempts+ "of 3", Toast.LENGTH_LONG).show();

            } else if (LoginPassed == true)
                {
                    failedLoginCounter("reset");
                }
            CheckCrewnetSupplied();
            System.out.println("Statuses : " + CrewNetCredentialsStatus + "" + CrewNetCredentialsChecked);
            SetDownloadButtonStatuses(CrewNetCredentialsStatus, CrewNetCredentialsChecked);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (!ReportType.equals("CheckStatus")) {
                showDialog(DIALOG_DOWNLOAD_PROGRESS);
                progressDialog.setProgress(0);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
                // TODO Auto-generated method stub
            Log.d("ANDRO_ASYNC", values[0]);
            Log.d("updater", ReportType);
            if (!ReportType.equals("CheckStatus")) {
                progressDialog.setProgress(Integer.parseInt(values[0]));
            }

        }

    }

public String CreatorParser(String ReportType, String ResponseBodyInput)
{

    String[] Body_strings = ResponseBodyInput.split("\n");
    String[] ReportLocator = {"false", "0", "0"};
    String ReportRequest = "none";
    String NextRosterFlag = "null";
    //split the response body into lines that we can use to parse for the report indicator



        for (int x = 0; x <= Body_strings.length - 1; x++) {
            //System.out.println(Body_strings[x]);

                    String[] parser_tmp = MatchStaffNum(User_crewnet_id, Body_strings[x]);

                    if (parser_tmp[0].equals("true")) {
                        ReportLocator[0] = parser_tmp[0];
                        ReportLocator[1] = parser_tmp[1];
                        ReportLocator[2] = parser_tmp[2];
                    }

        }

             //report pointer url is created

    if (ReportLocator[0].equals("true")) {
        switch (ReportType){
            case "CrewBrief":
                ReportRequest = "http://209.59.124.244/Crewnet/Reports/CrewBriefingReport_" + ReportLocator[1] + "_" + ReportLocator[2] + ".pdf";
                break;
            case "CurrentRoster":
            case "LastRoster":
            case "NextRoster":
                ReportRequest = "http://209.59.124.244/crewnet/Reports/RosterReport_" + ReportLocator[1] + "_" + ReportLocator[2] + ".pdf";
                break;
            case "CheckStatus":
                ReportRequest = "Available";
                break;
        }//end of the switch case


    } else {
                switch (ReportType) {
                    case "CrewBrief":
                    case "CurrentRoster":
                        ReportRequest = "none";
                        break;
                    case "CheckStatus":
                        ReportRequest = "Not Available";
                        break;
                }
            }

    return ReportRequest ;

}//end of method

public String[] ReportSaveFileNameGenerator(String ReportType)
{
    String [] SaveFilePath = {"null","null"};
    switch (ReportType){
        case "CrewBrief":
            SaveFilePath[0] = CrewNetDir;
            SaveFilePath[1] = CrewBriefFileName;
            break;
        case "CurrentRoster":
            SaveFilePath[0] = CrewNetDir;
            SaveFilePath[1] = CurrentRosterfFileName;
            break;
        case "LastRoster":
            SaveFilePath[0] = CrewNetDir;
            SaveFilePath[1] = PreviousRosterfFileName;
            break;
        case "NextRoster":
            SaveFilePath[0] = CrewNetDir;
            SaveFilePath[1] = NextRosterFileName;
            break;
    }//end of the switch case
    return SaveFilePath;
}



//---------
public void displaypdf(String FileName) {
    File file = new File(FileName);

    if (file.exists()) {
        Uri path = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {

            //Toast.makeText(getApplicationContext(), "No Application Available to View PDF", Toast.LENGTH_LONG).show();
        }

    }
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

    public static String NextRosterDetector(String txt)
    {
        String MatchFound = "false";
        String ReturnFlag = "null";

        //String txt="<span id=\"lblReport\" class=\"formmsgbox\">There is currently no live Roster available for you to  view.</span><!--<select name=\"cboExportOptions\" id=\"cboExportOptions\">";

        String re1=".*?";	// Non-greedy match on filler
        String re2="(no)";	// Word 1
        String re3=".*?";	// Non-greedy match on filler
        String re4="(live)";	// Word 2
        String re5=".*?";	// Non-greedy match on filler
        String re6="(Roster)";	// Word 3

        Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(txt);
        String wordAggregate = "null";
        if (m.find())
        {
            String word1=m.group(1);
            String word2=m.group(2);
            String word3=m.group(3);
            wordAggregate = word1 + " " + word2 + " " + word3;
            System.out.print("("+word1.toString()+")"+"("+word2.toString()+")"+"("+word3.toString()+")"+"\n");
            if (wordAggregate.equals("no live Roster"))
            {
                ReturnFlag = "Available";
            }
            else {
                ReturnFlag = "null";
                 }
        }

        return ReturnFlag;
    }

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



}//end of the main class
