package com.example.danielhutchinson.lialpatools;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity  {
Button b1;
EditText Num1;
EditText Box3;
String sTextFromET = "";
    protected TabHost maintabhost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BuildMainTabHost();

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

        TabHost.TabSpec tab_rostersAdvice= maintabhost.newTabSpec("Rosters & Crew Advices");
        tab_rostersAdvice.setContent(R.id.tab_rosters_advices);
        tab_rostersAdvice.setIndicator("Rosters & Crew Advices");
        maintabhost.addTab(tab_rostersAdvice);

        TabHost.TabSpec tab_docs= maintabhost.newTabSpec("Docs");
        tab_docs.setContent(R.id.tab_documents);
        tab_docs.setIndicator("Docs");
        maintabhost.addTab(tab_docs);

        //maintabhost.getTabWidget().getChildTabViewAt(2).getLayoutParams().height = 100;
        maintabhost.getTabWidget().getChildTabViewAt(2).getLayoutParams().width = 100;
        //maintabhost.getTabWidget().getChildTabViewAt(2).setBackgroundColor(Color.WHITE);

        //setup the web view
        WebView main_webView;
        main_webView = (WebView) findViewById(R.id.webView);

        main_webView.setWebViewClient(new MyWebViewClient());

        main_webView.getSettings().setJavaScriptEnabled(true);
        main_webView.loadUrl("http://www.google.com");
        //end of webview setup
        //end of tab host setup


        //set the start tab
        maintabhost.setCurrentTab(1);
    }


    //end of my code

} //end of the main class
