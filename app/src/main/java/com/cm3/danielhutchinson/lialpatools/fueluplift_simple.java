package com.cm3.danielhutchinson.lialpatools;

/**
 * Created by Daniel Hutchinson on 5/4/15.
 */


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class fueluplift_simple extends Fragment{

    EditText bowser_fuelCalc;
    TextView Bowser_display;
    EditText ArrivalFuel;
    EditText DepartureFuel;
    TextView Uplift_byusr;
    String Arrivalfuel_str;
    String Departurefuel_str;
    int ArrivalFuel_ints;
    int DepartureFuel_ints;
    int UpliftInt_byUsr = 0;
    String UpliftStr_byUsr = "";
    EditText UpliftComparison_Generated;
    int UpliftComparison_Generated_ints = 0;
    TextView FuelCalcsComparison;
    int FuelCalcs_Comparison_value = 0;
    String FuelCalcs_Comparison_valueString = "0";
    String BowserFuelCalc_str;
    int BowserFuelCalc_ints = 0;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fuel_uplift_simple,
                container, false);

        bowser_fuelCalc = (EditText) view.findViewById(R.id.editText_fuelcalc_bowserDisplay_comparison);  //this is bowser Calc
        Bowser_display = (TextView) getActivity().findViewById(R.id.display_fuelcalc_bowserFuelCalcDisplay);


        Uplift_byusr = (TextView) view.findViewById(R.id.edit_text_uplift_output);  //this is the UPlift under arrival/dep fuel


        UpliftComparison_Generated = (EditText) view.findViewById(R.id.editText_fuelcalc_upliftDisplay_comparison); //this is the second uplift which is auto matched to the first
        FuelCalcsComparison = (TextView) view.findViewById(R.id.textview_fuelcalc_simple_difference);  //this is the final difference where uplift and bowser calculation are compared

        //get the user inputs for arrival and departure fuel
        ArrivalFuel = (EditText) view.findViewById(R.id.edittext_arrival_fuel);
        DepartureFuel = (EditText) view.findViewById(R.id.edittext_departure_fuel);


        UpdateFuelFrag();
        ArrivalFuel.addTextChangedListener(new TextWatcher() {
                                               @Override
                                               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                               }

                                               @Override
                                               public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                   UpdateFuelFrag();
                                               }

                                               @Override
                                               public void afterTextChanged(Editable s) {

                                               }
                                           }


        );
       DepartureFuel.addTextChangedListener(new TextWatcher() {
                                               @Override
                                               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                               }

                                               @Override
                                               public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                   UpdateFuelFrag();
                                               }

                                               @Override
                                               public void afterTextChanged(Editable s) {

                                               }
                                           }


        );


        return view;
    }

public void SetBowserFuelCalc(String ValueToSet)
    {
        String BowserVal = ValueToSet;
        bowser_fuelCalc.setText(BowserVal);
        UpdateFuelFrag();
    }

    public void UpdateFuelFrag()
    {
        //get strings from the various inputs
        Arrivalfuel_str = ArrivalFuel.getText().toString();
        Departurefuel_str = DepartureFuel.getText().toString();

        BowserFuelCalc_str = bowser_fuelCalc.getText().toString();



        //checking them to make sure no null values are present, if so set default
        if (Arrivalfuel_str .length() == 0) {Arrivalfuel_str  = "0";}
        if (Departurefuel_str .length() == 0) {Departurefuel_str  = "0";}
        if (BowserFuelCalc_str .length() == 0) {BowserFuelCalc_str  = "0";}

        //now we get the int values of the strings
        ArrivalFuel_ints = Integer.parseInt(Arrivalfuel_str );
        DepartureFuel_ints = Integer.parseInt(Departurefuel_str);
        BowserFuelCalc_ints = Integer.parseInt(BowserFuelCalc_str);


        //here we do the various math
        UpliftInt_byUsr = DepartureFuel_ints - ArrivalFuel_ints;
        FuelCalcs_Comparison_value = UpliftInt_byUsr - BowserFuelCalc_ints;
        //end of math section

        //convert output values of math results to strings as needed for writing
        UpliftStr_byUsr = String.valueOf(UpliftInt_byUsr);
        FuelCalcs_Comparison_valueString = String.valueOf(FuelCalcs_Comparison_value);

        //set the output texts
        Uplift_byusr.setText(UpliftStr_byUsr + "Lbs.");
        UpliftComparison_Generated.setText(UpliftStr_byUsr );
        FuelCalcsComparison.setText(FuelCalcs_Comparison_valueString + " Lbs.");
    }


} //end of main fragment class
