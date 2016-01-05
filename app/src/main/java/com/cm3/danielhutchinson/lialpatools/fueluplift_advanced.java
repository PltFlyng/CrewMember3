package com.cm3.danielhutchinson.lialpatools;

/**
 * Created by Daniel Hutchinson on 5/4/15.
 */

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class fueluplift_advanced extends Fragment{


    TextView test;
    TextView Bowser_display;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fuel_uplift_advanced,
                container, false);

        test = (TextView) view.findViewById(R.id.textView_temp);
        Bowser_display = (TextView) getActivity().findViewById(R.id.display_fuelcalc_bowserFuelCalcDisplay);

        test.setText(Bowser_display.getText().toString());

        return view;
    }


    public void SetBowserFuelCalc(String ValueToSet)
    {
        String BowserVal = ValueToSet;
        test.setText("testing"+ BowserVal);

    }

} //end of main class