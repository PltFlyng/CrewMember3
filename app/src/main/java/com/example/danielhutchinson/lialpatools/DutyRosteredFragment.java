package com.example.danielhutchinson.lialpatools;

import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.util.Calendar;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class DutyRosteredFragment extends Fragment {

    private TextView tvDisplayTime;
    private TimePicker timePicker1;
    private Button btnChangeTime;
    private Spinner NumOfSectorSpinner;
    TextView maxFDPdisplay;
    //TextView maxDutydisplay;
    TextView DutyLimitdisplay;
    TextView OrigonalReportLabel;
    Button OrigonalReportTimeButton;
    CheckBox DelayedReport;

    TextView tvOut;
    String DutyDescription_box = "";
    private int hour;
    private int minute;

    static final int TIME_DIALOG_ID = 999;

    TextView VisTst;
    //Button_DutyUI_DutySignon_displayButton



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duty_rostered,
                container, false);

//the return of the view for the fragment

        //tvDisplayTime = (TextView) view.findViewById(R.id.tvTime);
        //timePicker1 = (TimePicker) view.findViewById(R.id.timePicker1);
        btnChangeTime = (Button) view.findViewById(R.id.Button_DutyUI_DutySignon_displayButton);
        tvDisplayTime = (Button) view.findViewById(R.id.Button_DutyUI_DutySignon_displayButton);
        NumOfSectorSpinner = (Spinner) view.findViewById(R.id.spinner_dutyUI_NumOfSectors);
        tvOut = (TextView) view.findViewById(R.id.text_view_testbox);

        maxFDPdisplay = (TextView) view.findViewById(R.id.textView_Display_DutyUI_MaxFDP);
        //maxDutydisplay = (TextView) view.findViewById(R.id.textView_Display_DutyUI_MaxDuty);
        DutyLimitdisplay = (TextView) view.findViewById(R.id.textView_Display_DutyUI_DutyLimit);

        DelayedReport = (CheckBox) view.findViewById(R.id.checkBox_DutyUI_delayedCheckIn);
        OrigonalReportLabel = (TextView) view.findViewById(R.id.textview_DutyUI_label_OrigReport);
        OrigonalReportTimeButton = (Button) view.findViewById(R.id.Button_DutyUI_OrigReport_displayButton);

        OrigonalReportLabel.setVisibility(View.GONE);
        OrigonalReportTimeButton.setVisibility(View.GONE);

        VisTst = (TextView) view.findViewById(R.id.text_view_testbox);

        tvOut.setText(DutyDescription_box);
        //init the picker and the display button
        setCurrentTimeOnView();
        addListenerOnButton();
        addDisplayListener();
        SetOrigReportDisplay(00,00);
        addListenerOnOrigReportButton();


        //settestdisplay("blah");

        //listen for changes and call the update method

        DelayedReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                   if (buttonView.isChecked()) {
                                                    //checked
                                                       OrigonalReportLabel.setVisibility(View.VISIBLE);
                                                       OrigonalReportTimeButton.setVisibility(View.VISIBLE);
                                                   } else {
                                                            //not checked
                                                       OrigonalReportLabel.setVisibility(View.GONE);
                                                       OrigonalReportTimeButton.setVisibility(View.GONE);
                                                   }

                                               }
                                           });


                tvDisplayTime.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        UpdateDutyArray();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        UpdateDutyArray();
                    }
                });

        NumOfSectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                UpdateDutyArray();
                // VisTst.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        OrigonalReportTimeButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UpdateDutyArray();
            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdateDutyArray();
            }
        });






        //end test


    return view;
} //closing bracket for the fragments main view


//sub classes/my code goes here


// display current time
public void setCurrentTimeOnView() {




    final Calendar c = Calendar.getInstance();
    hour = c.get(Calendar.HOUR_OF_DAY);
    minute = c.get(Calendar.MINUTE);

    // set current time into textview
    SetDutySignOnDisplay(hour, minute);

    //set current number of sectors to 4
    NumOfSectorSpinner.setSelection(3);




    // set current time into timepicker
    //timePicker1.setCurrentHour(hour);
    //timePicker1.setCurrentMinute(minute);


}

    public void addListenerOnButton() {
        btnChangeTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onCreateDialog(TIME_DIALOG_ID).show();
                //createDialog(TIME_DIALOG_ID).show();
                Log.d("My APP", "testing the on change listener");
            }

        });

    }

    public void addListenerOnOrigReportButton() {
        final int testvar = 9;
        OrigonalReportTimeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onCreateDialog(testvar).show();
                //createDialog(TIME_DIALOG_ID).show();
                Log.d("My APP", "testing the on change listener");
            }

        });

    }



    public void SetDutySignOnDisplay(int hour, int minute)
    {
        // set current time into textview
        tvDisplayTime.setText(new StringBuilder().append(pad(hour))
                .append(":").append(pad(minute)).append(" L"));
    }

    public void SetOrigReportDisplay(int hour, int minute)
    {
        // set current time into textview
        OrigonalReportTimeButton.setText(new StringBuilder().append(pad(hour))
                .append(":").append(pad(minute)).append(" L"));
    }


    public void addDisplayListener()
    {
        tvDisplayTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onCreateDialog(TIME_DIALOG_ID).show();
                //createDialog(TIME_DIALOG_ID).show();

            }

        });
    }

    //@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(getActivity(),
                        timePickerListener, hour, minute, true);

            case 9:
                return new TimePickerDialog(getActivity(),
                        OrigReporttimePickerListener, hour, minute, true);
                        //Log.d("My APP", "testing the on change listener");
        }
        return null;
    }

    public Dialog createDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(getActivity(), timePickerListener, hour, minute, true);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;



                    SetDutySignOnDisplay(hour, minute);
                    // set current time into timepicker
                    //timePicker1.setCurrentHour(hour);
                    //timePicker1.setCurrentMinute(minute);

                }
            };

    private TimePickerDialog.OnTimeSetListener OrigReporttimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;


                    SetOrigReportDisplay(hour, minute);
                    // set current time into timepicker
                    //timePicker1.setCurrentHour(hour);
                    //timePicker1.setCurrentMinute(minute);

                }
            };



    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

public void settestdisplay(String[] Input)
{
     tvOut.setText(Input[0] + " " + Input[1] + " " + Input[2] + " ");
}


    public void UpdateDutyArray()
    {
        //init the variables to some default vars
        int Input_DutyFlag = 0;
        String Input_DutyStart = "0:0";
        String Input_SignOn = "00:00";
        int Input_Sectors = 1;
        String Input_splitDuty = "0:00-00:00";
        String Input_Delay_info = "0-0:0";
        //end of manual assignments for testing


        Log.d("My APP", "testing the on change listener");

        //set the sign on string to the value selected  by the picker
        String SignOnRaw = String.valueOf(btnChangeTime.getText());
        String [] SignOn_string = SignOnRaw.split(" ");
        Input_SignOn = SignOn_string[0];
        Input_DutyStart = Input_SignOn;

        //set the number of sectors based on the sector num selector
        //String text = mySpinner.getSelectedItem().toString();
        String tmp_sectors = NumOfSectorSpinner.getSelectedItem().toString();
        Input_Sectors = Integer.valueOf(tmp_sectors);

        //check see if delay box is checked if so activate the delay code
        if (DelayedReport.isChecked())
        {
            //set the sign on string to the value selected  by the picker
            String OrigReportRaw = String.valueOf(OrigonalReportTimeButton.getText());
            String [] OrigReport_string = OrigReportRaw.split(" ");
            Input_Delay_info = "1-" + OrigReport_string[0];
        }else
        {
            Input_Delay_info = "0-0:0";
        }


        //return a dutydetails array from the main FDP activity
        String [] DutyDetailsArray = ((FlightDutyTimeMainActivity)getActivity()).Duty(Input_DutyFlag, Input_DutyStart, Input_SignOn, Input_Sectors, Input_splitDuty, Input_Delay_info);

        //set the display with the returned values
        String [] tmp_max_FDP= DutyDetailsArray[13].split("\\+");
        maxFDPdisplay.setText("" + tmp_max_FDP[1]);

        String [] tmp_Duty_limit = DutyDetailsArray[7].split("\\+");
        String[] dutyLim_vals = tmp_Duty_limit[1].split(":");
        DutyLimitdisplay.setText(" " + dutyLim_vals[1] + ":" +dutyLim_vals[2] + " (On Chocks Time)");
        //TextView maxDutydisplay;
        //TextView DutyLimitdisplay;


        DutyDescription_box = "Please Note:\n" +
                               "--For rostered Duties the maximum allowable duty is the Flight Duty period (FDP) plus 30 minutes.\n \n "
                               +"----The Maximum allowable FDP based on your selected sign on time is " + tmp_max_FDP[1] + ".\n \n"
                               +"--Since this is a rostered duty you must be On Chocks by " + dutyLim_vals[1] + ":" +dutyLim_vals[2];

        String [] tmp_Delay_data_raw = DutyDetailsArray[10].split("\\+");
        String [] tmp_delay_data_array = tmp_Delay_data_raw[1].split("-");
        String [] tmp_delay_HH_MM =  tmp_delay_data_array[1].split(":");
        if (Integer.valueOf(tmp_delay_HH_MM[0]) >= 4 )
        {
            DutyDescription_box =  DutyDescription_box + "\n\n--Since the sign on time was delayed by more than 4 hours the FDP was based on the more limiting of the original or actual report time.";
        }

        tvOut.setText(DutyDescription_box);

        //Duty(int TypeFlag, String StartTime, String SignOn, int Sectors, String SplityDutyInfo)

        //String [] Duty_Details_Array = Duty(Test_DutyFlag, Test_DutyStart, Test_SignOn, Test_Sectors, Test_splitDuty, Test_Delay_info);

        Log.d("My APP", "Data Return Printer--------------");
        for (int n = 0; n <= DutyDetailsArray.length  - 1; n++)
        {
            Log.d("My APP", DutyDetailsArray[n]);
        }

        //above test will return the following
        //Duty_Details_Array[0] - DutyFlag
        //Duty_Details_Array[1] - DutyStart
        //Duty_Details_Array[2] - SignOn
        //Duty_Details_Array[3] - Sectors
        //Duty_Details_Array[4] - SplitDuty
        //Duty_Details_Array[5] - MaxFDP
        //Duty_Details_Array[6] - MaxDuty
        // Duty_Details_Array[7] - FDPEnd
        // Duty_Details_Array[8] - DutyLimit
        // Duty_Details_Array[9] - DutyExpiry
        // Duty_Details_Array[10] - DelayedCheckin


    }


    //my code ends here

    //code example
    //below shows how to call from the main lib in other class. DutyTimeMainLib is declared above
    //utyTimeLib.DutyTestMethod();
    //int test = DutyTimeMainLib.DutyTest2(2);
    //end of code example


} //end of main class
