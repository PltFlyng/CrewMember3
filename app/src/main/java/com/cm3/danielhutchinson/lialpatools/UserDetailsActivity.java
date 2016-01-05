package com.cm3.danielhutchinson.lialpatools;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class UserDetailsActivity extends ActionBarActivity {

    public static final String PREFS_NAME = "CM3_PREFS";
    public static final String PREFS_KEY = "CM3_PREFS_String";

    TextView UsersName;
    TextView User_crewNetId;
    TextView User_crewNetPass;
    TextView User_email;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        UsersName = (TextView) findViewById(R.id.editText_user_details_usersName);
        User_crewNetId = (TextView) findViewById(R.id.editText_userdetails_crewnetID);
        User_crewNetPass = (TextView) findViewById(R.id.editText_userDetails_crewnetPassword);
        User_email = (TextView) findViewById(R.id.editText_userdetails_email);
                
        loadSavedPreferences();


        UsersName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String UsersNameString = UsersName.getText().toString();
                savePreferences("UsersName", UsersNameString);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String UsersNameString = UsersName.getText().toString();
                savePreferences("UsersName", UsersNameString);
            }
        });


        User_crewNetId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String UsersCrewNetIDString = User_crewNetId.getText().toString();
                savePreferences("UsersCrewNetId", UsersCrewNetIDString);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String UsersCrewNetIDString = User_crewNetId.getText().toString();
                savePreferences("UsersCrewNetId", UsersCrewNetIDString);
            }
        });

        User_crewNetPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String UsersCrewNetPassString = User_crewNetPass.getText().toString();
                savePreferences("UsersCrewNetPass", UsersCrewNetPassString);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String UsersCrewNetPassString = User_crewNetPass.getText().toString();
                savePreferences("UsersCrewNetPass", UsersCrewNetPassString);
            }
        });

        User_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String UsersCrewNetPassString = User_email.getText().toString();
                savePreferences("UsersEmail", UsersCrewNetPassString);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String UsersCrewNetPassString = User_email.getText().toString();
                savePreferences("UsersEmail", UsersCrewNetPassString);
            }
        });

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_details, menu);

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
            Intent newintent = new Intent(UserDetailsActivity.this, UserDetailsActivity.class);
            startActivity(newintent);

            return true;
        }

        if (id == R.id.action_about) {
            Intent newintent = new Intent(UserDetailsActivity.this, AboutActivity.class);
            startActivity(newintent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPreferences.getString("UsersName", null);
        String CrewNetId = sharedPreferences.getString("UsersCrewNetId", null);
        String CrewNetPass = sharedPreferences.getString("UsersCrewNetPass", null);
        String UsersEmail = sharedPreferences.getString("UsersEmail", null);

        UsersName.setText(name);
        User_crewNetId.setText(CrewNetId);
        User_crewNetPass.setText(CrewNetPass);
        User_email.setText(UsersEmail);

    }

    public void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void HomeClick(View view) {
        //Intent intent = new Intent(mai.this, ToActivity.class);
        Intent newintent = new Intent(UserDetailsActivity.this, MainActivity.class);
        startActivity(newintent);
    }
}//end of the main class

