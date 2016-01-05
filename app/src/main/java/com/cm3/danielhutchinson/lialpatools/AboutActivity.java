package com.cm3.danielhutchinson.lialpatools;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class AboutActivity extends ActionBarActivity {

    TextView About_changelog_txtview;
    TextView VersionDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //code for the main oncreate
        String About_changelog = ChangeLogRaw();
        String VersionData = VersionInfo();

        VersionDisplay = (TextView) findViewById(R.id.textview_ui_about_versionInfo);
        VersionDisplay.setText("CM3 Version " + VersionData);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfState ment
        if (id == R.id.action_settings) {
            Intent newintent = new Intent(AboutActivity.this, UserDetailsActivity.class);
            startActivity(newintent);

            return true;
        }

        if (id == R.id.action_about) {
            Intent newintent = new Intent(AboutActivity.this, AboutActivity.class);
            startActivity(newintent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public String ChangeLogRaw() {
        InputStream is = getResources().openRawResource(R.raw.changelog);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            Log.w("LOG", e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.w("LOG", e.getMessage());
            }
        }
        return sb.toString();
    }

    public String VersionInfo(){
        String version = "";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("tag", e.getMessage());
        }
System.out.println("version is " + version);
        return version;
    }


}//end of main class
