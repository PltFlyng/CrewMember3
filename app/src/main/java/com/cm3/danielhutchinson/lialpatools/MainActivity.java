package com.cm3.danielhutchinson.lialpatools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;


import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.io.File;



public class MainActivity extends ActionBarActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "erfhL1WCUnnBL4Ny61GeH2jl9";
    private static final String TWITTER_SECRET = "278CoOmKyrI4WNMAB3xZ6j3yTR8qhzUgsqnQG6XgpkHuHunnYp";

public String CurrentAppVersion_long = "";

Button b1;
EditText Num1;
EditText Box3;
String sTextFromET = "";



    protected TabHost maintabhost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
         setContentView(R.layout.activity_main);

        BuildMainTabHost();
        FirstRun();
        CurrentAppVersion_long = VersionInfo();
        SetAppVersionDescription(CurrentAppVersion_long);

} //end of the main on create function

       @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent newintent = new Intent(MainActivity.this, UserDetailsActivity.class);
            startActivity(newintent);

            return true;
        }

        if (id == R.id.action_about) {
            Intent newintent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(newintent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

////my code
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        //load the layout resource for the new configuration
        //in this case we do not wish to re-create
        //setContentView(R.layout.activity_main);


        //initialize the main tab host
        //BuildMainTabHost();


    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void FuelClick(View view) {
        //Intent intent = new Intent(mai.this, ToActivity.class);
        Intent newintent = new Intent(MainActivity.this, FuelCalcActivity.class);
        startActivity(newintent);
    }

    public void WindComponentClick(View view) {
        //Intent intent = new Intent(mai.this, ToActivity.class);
        Intent newintent = new Intent(MainActivity.this, WindComponentActivity.class);
        startActivity(newintent);
    }

    public void FlightDutyClick(View view) {
        //Intent intent = new Intent(mai.this, ToActivity.class);
        Intent newintent = new Intent(MainActivity.this, FlightDutyTimeMainActivity.class);
        startActivity(newintent);
    }

    public void CrewnetClick(View view) {
        //Intent intent = new Intent(mai.this, ToActivity.class);
        Intent newintent = new Intent(MainActivity.this, CrewNetActivity.class);
        startActivity(newintent);
    }
    public void AdviceMainClick(View view) {
        //Intent intent = new Intent(mai.this, ToActivity.class);
        Toast.makeText(getApplicationContext(), "This feature is under development for future deployment.", Toast.LENGTH_LONG).show();
        Intent newintent = new Intent(MainActivity.this,CrewAdviceActivity.class);
        startActivity(newintent);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    protected void BuildMainTabHost ()
    {
        //set up the main tab host here
        TabHost maintabhost = (TabHost) findViewById(R.id.tabHost_main);

        maintabhost.setup();

        TabHost.TabSpec tabSpec= maintabhost.newTabSpec("Main");
        tabSpec.setContent(R.id.tab_mainSplash);
        tabSpec.setIndicator("Main");

        maintabhost.addTab(tabSpec);

        TabHost.TabSpec tab_tools= maintabhost.newTabSpec("Tools");
        tab_tools.setContent(R.id.tab_tools);
        tab_tools.setIndicator("Tools");
        maintabhost.addTab(tab_tools);

        TabHost.TabSpec tab_rostersAdvice= maintabhost.newTabSpec("Rosters_Advices");
        tab_rostersAdvice.setContent(R.id.tab_rosters_advices);
        tab_rostersAdvice.setIndicator("Rosters & Advices");
        maintabhost.addTab(tab_rostersAdvice);


        if ( !isNetworkAvailable() ) { // loading offline

        }

        //TabHost.TabSpec tab_docs= maintabhost.newTabSpec("Docs");
        //tab_docs.setContent(R.id.tab_documents);
        //tab_docs.setIndicator("Docs");
        //maintabhost.addTab(tab_docs);

        //maintabhost.getTabWidget().getChildTabViewAt(2).getLayoutParams().height = 100;
        maintabhost.getTabWidget().getChildTabViewAt(2).getLayoutParams().width = 100;
        //maintabhost.getTabWidget().getChildTabViewAt(2).setBackgroundColor(Color.WHITE);



        //end of tab host setup


        //set the start tab
        maintabhost.setCurrentTab(0);
    }



public void FirstRun() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


    String FirstRun = sharedPreferences.getString("FirstRun", null);
    if (FirstRun == null) {
        // the key does not exist
        //display in long period of time
        //Toast.makeText(getApplicationContext(), "First Run!!!", Toast.LENGTH_LONG).show();
        //FirstRunToast();
        loadingPopup();   //--endable once popup code is fixed

        //code to be run on first run of the app
        GlobalManagement.SetupAppGlobals();
        //Check for SaveDIR if does not exsist make it
        savePreferences("FirstRun","no");
        savePreferences("CurrentRoster","0");
        savePreferences("NextRoster","0");
        savePreferences("LastRoster","0");
        savePreferences("Crewbrief","0");
        savePreferences("CrewnetLoginFails","0");


    } else {
        //Long myCurrentTimeMillis = System.currentTimeMillis();
        //SimpleDateFormat sdf_test = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //Date roster_stored_time = null;

        //roster_stored_time = new Date(myCurrentTimeMillis);
        //sdf_reports.format(roster_stored_time);

        //savePreferences("FirstRun","null");  //so that it pops up every time for testing
        //savePreferences("CurrentRoster", sdf_test.format(roster_stored_time));  //so that it pops up every time for testing
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.remove("FirstRun");
        //editor.commit();

        // handle the value
    }
}


    public void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }



    public void FirstRunToast() {

        // get your custom_toast.xml ayout
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.firstrun_welcome_setup,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // set a dummy image
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_launcher);

        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Welcome to CM3!!!");

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


       private void loadingPopup() {
           LayoutInflater inflater = this.getLayoutInflater();
           final View layout = inflater.inflate(R.layout.popup_welcome, null);
           final Button DismissButton = (Button) layout.findViewById(R.id.button_welcomepopup_dismiss);
           final PopupWindow Welcome_popupWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);


           Welcome_popupWindow.setFocusable(false);
           Welcome_popupWindow.setTouchable(true);
           Welcome_popupWindow.setOutsideTouchable(true);

           layout.post(new Runnable() {

               public void run() {
                   Welcome_popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

                   DismissButton.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Welcome_popupWindow.dismiss();
                       }
                   });
               }

           });



       }

    public String VersionInfo(){
        Context context = getApplicationContext(); // or activity.getApplicationContext()
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        String myVersionName = "not available"; // initialize String

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("MY Apps current version is " + myVersionName);
        return myVersionName;
    }

public void SetAppVersionDescription(String AppLongVer)
{
    String AppVerionDescriptor_welcome;
    AppVerionDescriptor_welcome ="Welcome to CM3 V" + AppLongVer;
    TextView AppVerTxt = (TextView) findViewById(R.id.textView_mainUI_WelcomeMsgTOPheader);
    AppVerTxt.setText(AppVerionDescriptor_welcome);
}

    //end of my code

} //end of the main class
