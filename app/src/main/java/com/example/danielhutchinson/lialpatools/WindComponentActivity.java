package com.example.danielhutchinson.lialpatools;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


public class WindComponentActivity extends ActionBarActivity {

    EditText RwyHdg;
    EditText WindDir;
    EditText WindVelocity;
    private int [] WindData = {0, 0};
    TextView HwTw_component_output;
    TextView XW_component_output;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wind_component);

        RwyHdg = (EditText) findViewById(R.id.ui_windcompcalc_edittext_rwyhdg);
        WindDir = (EditText) findViewById(R.id.ui_windcomp_edittext_winddir);
        WindVelocity = (EditText) findViewById(R.id.ui_windcomp_edittext_windVel);
        HwTw_component_output = (TextView) findViewById(R.id.ui_windcomp_result_hwtw);
        XW_component_output = (TextView) findViewById(R.id.ui_windcomp_result_xw);




        RwyHdg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               UpdateWindData();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        WindDir.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UpdateWindData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        WindVelocity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UpdateWindData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wind_component, menu);
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

    //my code
    public int[] WindComponentCal(String RwyHdg, String WindDir, String WindVelocity) {
        Double RwyHdg_intval = Double.parseDouble(RwyHdg);
        Double WindDir_intval = Double.parseDouble(WindDir);
        Double WindVelocity_intval = Double.parseDouble(WindVelocity);
        double WindAngle = 0;
        double Headwind_double = 0;
        double Crosswind_double = 0;

        int Headwind = 0;
        int Crosswind = 0;
       int[] WindVals = {0, 0};



        //check the input if input was supplied in two digit format covert to hundreds
        if (RwyHdg_intval <= 36) {
            RwyHdg_intval = RwyHdg_intval * 10;
        }
        if (WindDir_intval <= 36) {
            WindDir_intval = WindDir_intval * 10;
        }

        WindAngle = WindDir_intval - RwyHdg_intval;
        double WindAngle_rads = Math.toRadians(WindAngle);
        double cos_value = Math.cos(WindAngle_rads);
        double sin_value = Math.sin(WindAngle_rads);

        //cos_value = Math.round(cos_value);
        //sin_value = Math.round(sin_value);
        //BigDecimal bd_cos = new BigDecimal(cos_value).setScale(2, RoundingMode.FLOOR);
        //cos_value = bd_cos.doubleValue();

        //BigDecimal bd_sin = new BigDecimal(sin_value).setScale(2, RoundingMode.FLOOR);
        //cos_value = bd_sin.doubleValue();

        //Toast.makeText(getApplicationContext(),"WindAngle: "+ WindAngle, Toast.LENGTH_LONG).show();
        Headwind_double = WindVelocity_intval * cos_value;
        Crosswind_double = WindVelocity_intval *  sin_value;

        BigDecimal bd_hw = new BigDecimal(Headwind_double).setScale(0, RoundingMode.FLOOR);
        Headwind_double = bd_hw.doubleValue();
        Headwind = (int) Headwind_double;


        BigDecimal bd_xw = new BigDecimal(Crosswind_double).setScale(0, RoundingMode.FLOOR);
        Crosswind_double = bd_xw.doubleValue();
        Crosswind = (int) Crosswind_double;

        WindVals[0] = Headwind;
        WindVals[1] = Crosswind;

        //Toast.makeText(getApplicationContext(),"The cos value is" + cos_value, Toast.LENGTH_LONG).show();
        return WindVals;
    }

public void UpdateWindData()
    {
        int [] WindData = {0, 0};

        String textFromET_RwyHdg = RwyHdg.getText().toString();
        if (textFromET_RwyHdg.length() == 0){textFromET_RwyHdg = "0";}
        String textFromET_WindDir = WindDir.getText().toString();
        if (textFromET_WindDir.length() == 0){textFromET_WindDir = "0";}
        String textFromET_WindVelocity = WindVelocity.getText().toString();
        if (textFromET_WindVelocity.length() == 0) {textFromET_WindVelocity = "0";}
        WindData = WindComponentCal(textFromET_RwyHdg, textFromET_WindDir, textFromET_WindVelocity);




        String winddata_hw_tw_line = String.valueOf(WindData[0]);
        String winddata_xw_line = String.valueOf(WindData[1]);
        //add descriptors for the hw/tw wind data output
        if (WindData[0] < 0)
        {
            winddata_hw_tw_line = "Wind-------> "+ winddata_hw_tw_line + " (TW)";
        } else
        {
            winddata_hw_tw_line = "Wind-------> "+ winddata_hw_tw_line + " (HW)";
        }

        //add descriptors for the xwind data output
        if (WindData[1] < 0)
        {
            winddata_xw_line = "X-Wind----> "+ winddata_xw_line + " (L)";
        } else
        {
            winddata_xw_line = "X-Wind----> "+ winddata_xw_line + " (R)";
        }

        HwTw_component_output.setText(winddata_hw_tw_line);
        XW_component_output.setText(winddata_xw_line);
    }

    public void HomeClick(View view) {
        //Intent intent = new Intent(mai.this, ToActivity.class);
        Intent newintent = new Intent(WindComponentActivity.this, MainActivity.class);
        startActivity(newintent);
    }

    //end of my code


} //end of main class

