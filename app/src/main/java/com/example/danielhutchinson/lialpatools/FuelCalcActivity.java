package com.example.danielhutchinson.lialpatools;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class FuelCalcActivity extends ActionBarActivity {

    //my variables as part of the main class
    Button b1;
    EditText Num1;
    EditText Box3;
    String TextFromET_bowser = "";

    Button Return_button;
    Button Compute;
    EditText Bowser_input;
    TextView Bowser_fuel_display;
    String BowserUnit_text = "";
    Spinner BowserUnits;
    double Bowser_int;
    public fueluplift_advanced adv_fuel_frag;
    public fueluplift_simple simple_fuel_frag;
    public String fragIdTracker_fuelcalc = "SimpleFuel Frag";

    //end of my variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_calc);

        //my code in the main on create class

        simple_fuel_frag = new fueluplift_simple();
        adv_fuel_frag = new fueluplift_advanced();

        FragmentTransaction fuel_fragtransaction;
       fuel_fragtransaction = getFragmentManager().beginTransaction().add(R.id.fragment_container_fuel, simple_fuel_frag, "SimpleFuel Frag");
        fuel_fragtransaction.addToBackStack("SimpleFuel Frag");
        SetFragTracker_fuel("SimpleFuel Frag");
        fuel_fragtransaction.commit();






        Bowser_input = (EditText)findViewById(R.id.input_edittext_fuelcalc_bowserReading);
        Bowser_fuel_display = (TextView)findViewById(R.id.display_fuelcalc_bowserFuelCalcDisplay);


       BowserUnits =(Spinner) findViewById(R.id.input_fuelcalc_bowserunits);



            Bowser_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UpdateFuelCalcs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        BowserUnit_text = BowserUnits.getSelectedItem().toString();
        BowserUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                UpdateFuelCalcs();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    //end of my code in the main on create class


    } //end of main on create class



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fuel_calc, menu);
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

    //my modules inserted into the main activity class


   private double FuelCalc(double Bowser, String Unit)
   {
       double Bowser_val =  Bowser;
       if (Unit.equals("US. Gal"))
       {
          Bowser_val = Bowser_val * 6.7;
       }
       else if (Unit.equals("Litre"))
       {
           Bowser_val = Bowser_val * 1.83;
       }
       else if (Unit.equals("Imp. Gal"))
       {
           Bowser_val = Bowser_val * 8.3;
       }

       return Bowser_val;
   }

    public void simple (View v)
    {


        FragmentTransaction fragtransaction;
        fragtransaction = getFragmentManager().beginTransaction().replace(R.id.fragment_container_fuel, adv_fuel_frag, "AdvFuel Frag");
        fragtransaction.commit();
        SetFragTracker_fuel("AdvFuel Frag");


    }

    //just a small method to set the tracker id for the fuel fragments
    public void SetFragTracker_fuel(String fragIdToSet)
    {
        fragIdTracker_fuelcalc = fragIdToSet;

        //Toast.makeText(getApplicationContext(),"Frag is now: "+ fragIdToSet, Toast.LENGTH_LONG).show();


    }

public void UpdateFuelCalcs()
    {
        TextFromET_bowser = Bowser_input.getText().toString();
        BowserUnit_text = BowserUnits.getSelectedItem().toString();

        if (TextFromET_bowser.length() == 0) {TextFromET_bowser = "0";}

        Bowser_int = Integer.parseInt(TextFromET_bowser);

        double bowser_output = FuelCalc(Bowser_int, BowserUnit_text);
        bowser_output = Math.round(bowser_output);


        String output = String.valueOf(bowser_output);
        String[] fuel_output = output.split("\\.");

        Bowser_fuel_display.setText(fuel_output[0] + " Lbs.");
        if (fragIdTracker_fuelcalc.equals("SimpleFuel Frag")) {

            simple_fuel_frag.SetBowserFuelCalc(fuel_output[0]);

        }

        if (fragIdTracker_fuelcalc.equals("AdvFuel Frag")) {
            adv_fuel_frag.SetBowserFuelCalc(fuel_output[0]);

        }

    }


    public void HomeClick(View view) {
        //Intent intent = new Intent(mai.this, ToActivity.class);
        Intent newintent = new Intent(FuelCalcActivity.this, MainActivity.class);
        startActivity(newintent);
    }


    //end of my code inserted into the main activity class



}//end of main class
