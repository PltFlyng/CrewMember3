package com.cm3.danielhutchinson.lialpatools;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FlightDutyTimeMainActivity extends ActionBarActivity {


    public DutyRosteredFragment Duty_rostered_frag;
    String FDP_data_storage ="";




//end of duty variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_duty_time_main);


        Duty_rostered_frag = new DutyRosteredFragment();

        FragmentTransaction Duty_fragtransaction;
        Duty_fragtransaction = getFragmentManager().beginTransaction().add(R.id.container_duty_frags, Duty_rostered_frag, "Rostered Duty Frag");
        Duty_fragtransaction.addToBackStack("Rostered Duty Frag");

        Duty_fragtransaction.commit();


        //code for the main oncreate
        FDP_data_storage = FDP_Table_Stream();

        //end of code for the main on create


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flight_duty_time_main, menu);
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
            Intent newintent = new Intent(FlightDutyTimeMainActivity.this, UserDetailsActivity.class);
            startActivity(newintent);

            return true;
        }

        if (id == R.id.action_about) {
            Intent newintent = new Intent(FlightDutyTimeMainActivity.this, AboutActivity.class);
            startActivity(newintent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//various methods to do duty computations
    public String FDP_Table_Stream() {
        InputStream is = getResources().openRawResource(R.raw.fdp_table);
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

    public String[] fdpTable_lines()
    {

        String [] Table  = FDP_data_storage.split("\n");
        return Table;
    }

    public static String[] fdpTable(String InputString)
    {

        String[] Table = InputString.split("/");

        return Table;
    }

    //method to split the time in format 00:00 into array of intgers for use
    public static int[] splitTime(String InputString)
    {
        int[] Time = {0, 0}; //initialise the array incase bad data is passed
        String[] TimeStr = InputString.split(":");
        //now Build a 2 slot array to return all the integer values
        Time[0] =  Integer.valueOf(TimeStr[0]);
        Time[1] =  Integer.valueOf(TimeStr[1]);
        return Time;
    }

    //method to return the sector bands from the header
    public static int[] SectorBands(String InputString)
    {
        int[] SectorLims = {0, 0}; //initialise the array incase bad data is passed
        String[] Sectors = InputString.split("-");
        //now Build a 2 slot array to return all the integer values
        SectorLims[0] =  Integer.valueOf(Sectors[0]);
        SectorLims[1] =  Integer.valueOf(Sectors[1]);
        return  SectorLims;
    }


    //this method splits the two time bands of format 08:00-11:00 into an array of bands for use
    public static String[] splitBands(String InputString)
    {

        String[] Bands = InputString.split("-");
        //String part1 = Band[0]; // 004
        //String part2 = Band[1]; // 034556

        return Bands;
    }

    //this method will convert a decimal time to a h:mm array format
    public static int[] convertDecimalTime(String InputString)
    {
        int[] Time = {0, 0}; //initialise the array incase bad data is passed
        String[] TimeStr = InputString.split("\\.");
        //now Build a 2 slot array to return all the integer values
        Time[0] =  Integer.valueOf(TimeStr[0]);
        Time[1] =  Integer.valueOf(TimeStr[1]);
        Time[1] = Time[1] * 6;

        return Time;
    }

    private static String padInt(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    public static int[] returnFdpExtension(int RH, int RM)
    {
        int[] Time = {0, 0}; //initialise the array incase bad data is passed

        int hoursValInMins = RH * 60;

        Double TotalMins = Double.valueOf(hoursValInMins) + Double.valueOf(RM);
        int RawHalfValueOfMins;
        RawHalfValueOfMins = (int) Math.round(TotalMins /2);

        //Math.round(RawHalfValueOfMins);

        while (RawHalfValueOfMins >= 60)
        {
            Time[0]++;
            RawHalfValueOfMins = RawHalfValueOfMins - 60;
        }
        Time[1] = RawHalfValueOfMins;


        return Time;
    }



    public Double returnMaxFDP(String CheckIn, int NumOfSectors)
    {

        String [] ChkInStrings = CheckIn.split(":");
        String CheckinStrMaster = ChkInStrings[0] + ChkInStrings[1];
        //int CheckinInteger = Integer.parseInt(CheckinStrMaster);  --old code to remove
        int CheckinInteger = Integer.valueOf(CheckinStrMaster);

        String [] FDP_data_array = fdpTable_lines();



        //to handle times between the first early morning band and the last of the night bands we need some new parameters
        int Minband= 0;
        int Maxband= 0;
        int MaxSubMin = 0; //this represents the end value of the last band which matches up to the period prior to the first morning band
        int CheckinBandInts_start = 0; //this is the min value of the band once it is determined the checkin is in this band
        int CheckinBandInts_end = 0; //this is the max value of the band once it is determined the checkin is in this band
        int[] MaxAllowFdp = {0, 0}; //manually init the array to guard against bad data passed
        Double MaxFdp_decimal = 0.0;
        int SectorBandIdent = 0; //manually init the sector band identifier.
        String [] MaxFdp_Data_array = {	"0000","0000","0.0"};

        //iterate through all the rows of the fdp file
        // read the table rows and determine some parameters like the max and minimum band limits

        String[] FdpTableRow = fdpTable(FDP_data_array[1]);
        String[] CheckinBands = splitBands(FdpTableRow[0]);

        Minband = Integer.parseInt(CheckinBands[0]);
        Maxband = Integer.parseInt(CheckinBands[1]);


        for (int n = 1; n <= FDP_data_array.length  - 1; n++)
        {
            //we read each row starting skipping row 0 which is the header
            //we then split each row into its parts that is checkin bands and fdps
            //we then split each checkin band into a start and end time and the split each start and end time into an array of integers for manipulation

            FdpTableRow = fdpTable(FDP_data_array[n]);
            CheckinBands = splitBands(FdpTableRow[0]);
            int CheckinBandInts_start_tmp = Integer.valueOf(CheckinBands[0]);
            int CheckinBandInts_end_tmp =  Integer.valueOf(CheckinBands[1]);


            if (CheckinBandInts_start_tmp < Minband)
            {
                Minband = CheckinBandInts_start_tmp;

            }
            if (CheckinBandInts_start_tmp >= Maxband)
            {
                Maxband = CheckinBandInts_start_tmp;
                MaxSubMin = CheckinBandInts_end_tmp;

            }

        } //end of the for loop that reads through the table


        //now that we have determined the start of the first time band and the end of the last time band
        //we can locate our checkin time within the bands
        //Now we determine in which band of number of sectors we fall
        // read the table cols and determine some parameters like the max and minimum sector bands

        String[] FdpTableHeader= fdpTable(FDP_data_array[0]); //FDP_data_array[0] is the first line of the text file
        //which holds the sector band headers
        //this for loop goes though each of the array items splits out the min and max
        //and uses this data to determine where in the bands we fall and returns SectorBandIdent to identify the right array item for fdp
        for (int i=1; i < FdpTableHeader.length; i++ )
        {
            int [] S_bands = SectorBands(FdpTableHeader[i]);
            if (NumOfSectors >= S_bands[0] && NumOfSectors <= S_bands[1])
            {
                SectorBandIdent = i;


            }
        }
        //SectorbandIdent variable has been set above now we know which array item in the row holds the fdp value

        for (int i=1; i < FDP_data_array.length; i++ )
        {
            //we read each row starting skipping row 0 which is the header
            //we then split each row into its parts that is checkin bands and fdps
            //we then split each checkin band into a start and end time and the split each
            //start and end time into an array of integers for manipulation
            //using SectorBandIdent value we can pull the fdp

            FdpTableRow = fdpTable(FDP_data_array[i]);
            CheckinBands = splitBands(FdpTableRow[0]);


            int CheckinBandInts_start_tmp = Integer.valueOf(CheckinBands[0]);
            int CheckinBandInts_end_tmp =  Integer.valueOf(CheckinBands[1]);



            if (CheckinInteger <= MaxSubMin)
            {
                if (CheckinInteger > 0  && CheckinInteger <= MaxSubMin)
                {
                    CheckinBandInts_start = CheckinBandInts_start_tmp;
                    CheckinBandInts_end = CheckinBandInts_end_tmp;

                    MaxFdp_decimal = Double.valueOf(FdpTableRow[SectorBandIdent]);
                }

            } else if (CheckinInteger > Maxband)
            {
                CheckinBandInts_start = CheckinBandInts_start_tmp;
                CheckinBandInts_end = MaxSubMin;
                MaxFdp_decimal = Double.valueOf(FdpTableRow[SectorBandIdent]);

            } else
            {
                if (CheckinInteger >= CheckinBandInts_start_tmp && CheckinInteger <= CheckinBandInts_end_tmp)
                {
                    CheckinBandInts_start = CheckinBandInts_start_tmp;
                    CheckinBandInts_end = CheckinBandInts_end_tmp;
                    MaxFdp_decimal = Double.valueOf(FdpTableRow[SectorBandIdent]);
                }
            }

        } //end of the for loop that checks the checkin vs bands
        //and sets the variables CheckinBandInts_start and end
        // also sets the max fdp in decimal format in a string for further processing


        //end of the block of code that  calls the methods that read the fdp input file




        //MaxAllowFdp = convertDecimalTime(MaxFdp_decimal);
        //MaxFdp_Data_array[0] = "" + CheckinBandInts_start;
        //MaxFdp_Data_array[1] = "" + CheckinBandInts_end;
        //MaxFdp_Data_array[2] = "" + MaxAllowFdp[0] + ":" + MaxAllowFdp[1];

        return MaxFdp_decimal;

    }

    public String [] Duty(int TypeFlag, String StartTime, String SignOn, int Sectors, String SplityDutyInfo, String DelayInfo)
    {
        //This class builds a new duty object and returns it in an array
        //A duty has the following properties
        //TypeFlag -- 0 for normal, 1 for reserve followed by fdp, 2 hot reserve, 3 split duty
        //Start time -- which may be checkin or reserve start
        //Sign on time -- which may be the same as start
        //SectorsOped -- number of sectors to be ops
        //SplitDutyRest -- Planned Rest time for a split duty extension and rest start time in format H-HH:MM
        //MaxAllowedFDP - the maximum allowable FDP
        //MaxDuty--Either FDP+30 or 16:30 total
        //FDPEnd--end of the FDP
        //DutLimitAbsoloute--Duty limit
        //DutyExpiry -- The more limiting of the FDP or Duty Limit
        //
        //The class takes the following as input, TypeFlag, StartTime, SignOn, Sectors,SplityDutyInfo

        //initialize the variables
        //int TypeFlag
        int[] DutyStart  = {0,0}; //holds the duty start time in an array hours in first slot mins in 2nd
        int[] SignOnTime = {0,0}; //holds the signON time in an array hours in first slot mins in 2nd
        //int Sectors
        int[] SplitDutyData = {0,0,0,0,0,0}; //holds the split duty info which is start-end-rest each in HH:MM format
        Double MaxFdp = 0.0;     //holds the Max Allowed FDP time in an array hours in first slot mins in 2nd
        int [] MaxFdp_HH_MM = {0, 0};     //holds the Max Allowed FDP time in an array hours in first slot mins in 2nd
        int[] MaxDuty  = {16,30}; //holds the MaxDuty start time in an array hours in first slot mins in 2nd
        int[] FDP_end = {0,00,00}; //holds the Max Allowed FDP time in an array hours in first slot mins in 2nd
        int[] DutyLimit  = {0,0,0}; //holds the Absoloute Duty Limit in an array hours in first slot mins in 2nd
        int[] DutyExpiry = {0,0,0}; //holds the more limiting of duty or FDP end time in an array hours in first slot mins in 2nd
        int[] DelayDetails = {0,00,00}; //holds delay info, flag and hh:mm
        int [] Fdp_penalty = {0,0};  //this is the time spent in excess of 6 hours which is applied as a penalty to the fdp
        int[] FDP_extension = {0,0}; //this is the fdp extension value which is half the rest
        //DelayInfo in format Flag+00:00 which is flag 0 - no delay, 1 delay, original report time

        //end of input

        //assign some variables from the method input
        DutyStart = splitTime(StartTime);
        SignOnTime = splitTime(SignOn);

        //split duty info
        String [] tmp_split_duty_strings =  SplityDutyInfo.split("-");
        //now split the times of the first portion and assign
        int [] tmp_split_duty_ints = splitTime(tmp_split_duty_strings[0]);
        SplitDutyData[0] = tmp_split_duty_ints[0];
        SplitDutyData[1] = tmp_split_duty_ints[1];
        //split times of second portion and assign
        tmp_split_duty_ints = splitTime(tmp_split_duty_strings[1]);
        SplitDutyData[2] = tmp_split_duty_ints[0];
        SplitDutyData[3] = tmp_split_duty_ints[1];
        //split times of third portion and assign
        //tmp_split_duty_ints = splitTime(tmp_split_duty_strings[2]);
        //SplitDutyData[4] = tmp_split_duty_ints[0];
        //SplitDutyData[5] = tmp_split_duty_ints[1];
        //split times of second portion and assign

        //convert
            //MaxFDP, MaxDuty,FDP_end, DutyLimit,DutyExpiry all already have int values. MaxDuty has values 16,30 the rest init to  0

        //Convert the delay details string to int values
        String [] tmp_delay_info_strings = DelayInfo.split("-");
        DelayDetails[0] = Integer.valueOf(tmp_delay_info_strings[0]); //the delay checkin flag in int format

        int [] tmp_delay_info_ints = splitTime(tmp_delay_info_strings[1]);

        DelayDetails[1] = Integer.valueOf(tmp_delay_info_ints[0]); //the hour value of the orig report time
        DelayDetails[2] = Integer.valueOf(tmp_delay_info_ints[1]); //the mins value of orig report time

        //Log.d("My APP", "Delay Details [1] = " + DelayDetails[1]);
        //all values re now in ints

        //first lets find a basic max allowable FDP
        //max fdp is based on more limiting of origonal or actual start time if delayed checkin
        //lets determine if there was a delayed check-in
        //if no delayed checkin we simply get a max allowable fdp for the scheduled report time as a temporary variable
        //if there was a delayed checkin the fdp is the more limiting of the fdp for original or actual report time
        //variable DelayDetails[0] holds the delay flag, 0 is no delay, DelayDetails[1] and 2 hold the HH:MM of orig report time

        //determine basic max fdp below
       switch (DelayDetails[0]) {
           case 0:
               //no delay case
               MaxFdp = returnMaxFDP(SignOn, Sectors);
               break;
           case 1:
               //case 2 there is a delay
               //Log.d("My APP", "Case 2 active");
              //Log.d("My APP", "Checkin time " + SignOnTime[0] + ":" + SignOnTime[1] + " AND orig report was " + DelayDetails[1] + ":" + DelayDetails[2]);
               String [] Delay_duration_data= SubtractTime(SignOnTime[0], SignOnTime[1],  DelayDetails[1],  DelayDetails[2]).split(":");

                //there are two types of delays delays less than 4 hours delays over 4 hours

               if(Integer.valueOf(Delay_duration_data[1]) < 4)
               {
                   //if the delay is less than 4 hours then simply use the max fdp based on sign on time
                   MaxFdp = returnMaxFDP(SignOn, Sectors);
               }
               else
               {
                   //if the delay is equal to or more than 4 hours then we use more limiting of actual or orig report time


                   if (returnMaxFDP(tmp_delay_info_strings[1], Sectors) < returnMaxFDP(SignOn, Sectors))
                   {
                       MaxFdp = returnMaxFDP(tmp_delay_info_strings[1], Sectors);
                   }
                   else
                   {
                       MaxFdp = returnMaxFDP(SignOn, Sectors);
                   }


               }

               break;
       }
        //max fdp value now assigned to var MaxFdP as a decimal
        //now convert the decimal value to HH:MM array

        MaxFdp_HH_MM = convertDecimalTime(String.valueOf(MaxFdp));
        //Log.d("My APP", "Max FDP as converted from decilam is" + MaxFdp_HH_MM[0] + ":" + MaxFdp_HH_MM[1]);
        //this is for extension by split duty
        //now we determine the type of duty and apply either a penalty in case of reserve in excess of 6 hours or a extension in case of split duty to the fdp
        //Any time spent on reserve in excess of 6 hours prior to sign on is subtracted from the allowable fdp, fdp starts at sign on time
        //for split duty, less than 3 hours consecutive rest = no extension. For rest >3 hours the extension will be for a period = to half the rest
        //The maximum length of a FDP extended by split duty is 17 hours.


        switch (TypeFlag)
        {
            case 1:
        //if the duty type flag value is set to 1 this indicates a reserve followed by an fdp

          //case of fdp following reserve
          //Log.d("My APP", "Reserve detected");
            //get time spent on reserve and assign it to a string array
          String [] time_spent_on_reserve = SubtractTime(SignOnTime[0], SignOnTime[1],  DutyStart[0],  DutyStart[1]).split(":");

          //if time spent on reserve is less than 6 hours then no fdp penalty
            if (Integer.valueOf(time_spent_on_reserve[1]) < 6)
            {
                Fdp_penalty[0] = 0;
                Fdp_penalty[1] = 0;
            }
            else if (Integer.valueOf(time_spent_on_reserve[1]) == 6 && Integer.valueOf(time_spent_on_reserve[1]) > 0)
            {
                //more than 6 hours on reserve

                Fdp_penalty[0] = Integer.valueOf(time_spent_on_reserve[1]) - 6;
                Fdp_penalty[1] = Integer.valueOf(time_spent_on_reserve[2]);
            }
            else
            {
                //more than 6 hours on reserve
                Fdp_penalty[0] = Integer.valueOf(time_spent_on_reserve[1]) - 6;
                Fdp_penalty[1] = Integer.valueOf(time_spent_on_reserve[2]);

            }
                //Log.d("My APP", "FDP Penalty--->" + Fdp_penalty[0] + ":" + Fdp_penalty[1]);
            if (Fdp_penalty[0] != 0 || Fdp_penalty[1] != 0)
                {

                    //Log.d("My APP", "MaxFDP " + MaxFdp_HH_MM [0] + ":" + MaxFdp_HH_MM [1]);
                    //do the subtraction and read the new values into a temporary local value
                    String [] adjusted_fdp = SubtractTime(MaxFdp_HH_MM[0], MaxFdp_HH_MM[1],  Fdp_penalty[0], Fdp_penalty[1]).split(":");
                    //Log.d("My APP", "Adjusted FDP--->" + adjusted_fdp[1] + ":" + adjusted_fdp[2]);

                    //reassign the values to the MaxFdp_HH_MM variable
                    MaxFdp_HH_MM[0] = Integer.valueOf(adjusted_fdp[1]);
                    MaxFdp_HH_MM[1] = Integer.valueOf(adjusted_fdp[2]);
                }

            break;

            case 2:
                //this is for hot reserve which is treated similar to a rostered duty which starts are reserve start time
                //as such no special code for this type of duty as yet
                break;

            case 3:
                //this is for extension by split duty

                //for split duty, less than 3 hours consecutive rest = no extension. For rest >3 hours the extension will be for a period = to half the rest
                //The maximum length of a FDP extended by split duty is 17 hours.

                //Log.d("My APP", "Split Duty detected");
                //Log.d("My APP", "Split Duty RestStarted at " + SplitDutyData[0] + ":" + SplitDutyData[1]);
                //Log.d("My APP", "Split Duty Ended at " + SplitDutyData[2] + ":" + SplitDutyData[3]);
                //calculate the rest
                String [] calculated_rest = SubtractTime(SplitDutyData[2], SplitDutyData[3],  SplitDutyData[0], SplitDutyData[1]).split(":");
                //assign the rest of the to the array
                SplitDutyData[4] = Integer.valueOf(calculated_rest[1]);
                SplitDutyData[5] = Integer.valueOf(calculated_rest[2]);
                //Log.d("My APP", "Split Rest was" + SplitDutyData[4] + ":" + SplitDutyData[5]);

                //extension by rest only applies if the rest was 3 hours or greater, we determine if rest was 3 hours or greater if so then apply an extension if not extension = 0
                if (SplitDutyData[4] >= 3)
                {

                   FDP_extension = returnFdpExtension(SplitDutyData[4], SplitDutyData[5]);
                   //Log.d("My APP", "Extension is  " + FDP_extension[0] + ":" + FDP_extension[1]);



                } else {
                    FDP_extension[0] = 0;
                    FDP_extension[1] = 0;

                }
                String [] adjusted_fdp = AddTime(MaxFdp_HH_MM[0], MaxFdp_HH_MM[1], FDP_extension[0], FDP_extension[1]).split(":");
                MaxFdp_HH_MM[0] = Integer.valueOf(adjusted_fdp[1]);
                MaxFdp_HH_MM[1] = Integer.valueOf(adjusted_fdp[2]);

                if (MaxFdp_HH_MM[0] > 17)
                {
                    MaxFdp_HH_MM[0] = 17;
                    MaxFdp_HH_MM[1] = 00;
                } else if (MaxFdp_HH_MM[0] == 17 && MaxFdp_HH_MM[1] > 0)
                {
                    MaxFdp_HH_MM[0] = 17;
                    MaxFdp_HH_MM[1] = 00;
                }



                break;


        } //end of the dutyflag switch

        //Log.d("My APP", "Max FDP as converted from decimal is " + MaxFdp_HH_MM[0] + ":" + MaxFdp_HH_MM[1]);
       //so now we have a maxfdp value adjusted for either reserve penalty or fdp extension
        String [] FDP_end_str_tmp = AddTime(SignOnTime[0], SignOnTime[1], MaxFdp_HH_MM[0], MaxFdp_HH_MM[1]).split(":");

        FDP_end[0] = Integer.valueOf(FDP_end_str_tmp[0]);
        FDP_end[1] = Integer.valueOf(FDP_end_str_tmp[1]);
        FDP_end[2] = Integer.valueOf(FDP_end_str_tmp[2]);
        //Log.d("My APP", "Sign on was " + SignOnTime[0] + ":" + SignOnTime[1]);
        //Log.d("My APP", "The FDP ends at  " + "DAY-" + FDP_end[0] + " " +FDP_end[1] + ":" + FDP_end[2]);

        // nowthat we have determined the end point of the fdp we should determine the end point of Max Duty
        //for a rostered flight MaxDuty is FDP + 30
        //for reserve max duty is 17 hours from reserve start

        switch (TypeFlag) {

            case 0:
            case 2:
            case 3:
                //rostered duty
                //fdp+30
                //DutyLimit  array of type int
                //hot reserve duty
                //same as case 0 treated as if the FDP based on sign on time, fdp will be limited by max sectors
                //split duty
                //max fdp for split duty is 17 hrs. we have already capped the max fdp at 17 hours in the section where we deal with split duty extension


                String [] duty_end_str_tmp = AddTime(DutyStart[0], DutyStart[1], MaxFdp_HH_MM[0], MaxFdp_HH_MM[1] + 30).split(":");
                DutyExpiry[0] = Integer.valueOf(duty_end_str_tmp[0]);
                DutyExpiry[1] = Integer.valueOf(duty_end_str_tmp[1]);
                DutyExpiry[2] = Integer.valueOf(duty_end_str_tmp[2]);

                break;
            case 1:
                //reserve duty
                //17 hrs max, minus 30 mins for post flight duties if flying, 15 if posn
                break;

        }





        //Get a max fdpstring for the sign on time
        //MaxFdp = returnMaxFDP(SignOn, Sectors);







        //output section
        //assign the output returns
        String [] Duty_Info = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0"};



        Duty_Info [0] = "DutyFlag+" + String.valueOf(TypeFlag);   //assign num of sectors as a key+value pair
        Duty_Info[1] = "DutyStart+" + String.valueOf(DutyStart[0]) + ":" + String.valueOf(DutyStart[1]);
        Duty_Info [2] = "SignOn+" + String.valueOf(SignOnTime[0]) + ":" + String.valueOf(SignOnTime[1]); //assign sign on data as keypair
        Duty_Info [3] = "Sectors+" + String.valueOf(Sectors);
        Duty_Info [4] = "SplitDuty+" + String.valueOf(SplitDutyData[0]) + ":" + String.valueOf(SplitDutyData[1])+
                        "-" + String.valueOf(SplitDutyData[2]) + ":" + String.valueOf(SplitDutyData[3])+
                         "-" + String.valueOf(SplitDutyData[4]) + ":" + String.valueOf(SplitDutyData[5]);
        Duty_Info[5] = "MaxFDP+" + String.valueOf(MaxFdp);
        Duty_Info[6] = "MaxDuty+" + String.valueOf(MaxDuty[0]) + ":" + String.valueOf(MaxDuty[1]);
        Duty_Info[7] = "FDPend+" + String.valueOf(FDP_end[0]) + ":" + padInt(FDP_end[1]) + ":" + padInt(FDP_end[2]);
        Duty_Info[8] = "DutyLimit+" + String.valueOf(DutyLimit[0]) + ":" + padInt(DutyLimit[1]) + ":" + padInt(DutyLimit[2]);
        Duty_Info [9] = "DutyExpiryt+" + String.valueOf(DutyExpiry[0]) + ":" + String.valueOf(DutyExpiry[1]) + ":" + String.valueOf(DutyExpiry[2]);
        Duty_Info [10] = "DelayDetails+" + String.valueOf(DelayDetails[0]) + "-" + String.valueOf(DelayDetails[1]) + ":" + String.valueOf(DelayDetails[2]);
        Duty_Info[11] = "FdpPenalty+" + String.valueOf(Fdp_penalty[0]) + ":" + String.valueOf(Fdp_penalty[1]);
        Duty_Info[12] = "FdpExtension+" + String.valueOf(FDP_extension[0]) + ":" + String.valueOf(FDP_extension[1]);
        Duty_Info[13] = "FdpHHMM+" + padInt(MaxFdp_HH_MM[0]) + ":" + padInt(MaxFdp_HH_MM[1]);


        return Duty_Info;
    }


    //code to add time values accounting for next days transition
    public String SubtractTime(int CH, int CM,int ORH, int ORM)
    {
        //Log.d("My APP", "Checkin time inside subtract func is " + CH + ":" + CM + " AND orig report was " + ORH + ":" + ORM);


        int DutyD = 0;
        int DutyLimH = CH - ORH;
        int DutyLimM = CM - ORM;

        //Log.d("My APP", "After raw subtraction: " + DutyLimH + ":" + DutyLimM);

        //sum the minutes and convert extras to hours
        if (DutyLimM < 0)
        {
            while (DutyLimM < 0)
            {
                DutyLimM = DutyLimM + 60;
                DutyLimH = DutyLimH - 1;

            }

        }

        //having now summed minutes and converted any additional to hours
        //we now check to see if we have more than 24 hours if so we increase the duty day counter
        //this is done to signify the duty spans days

        if (DutyLimH < 0)
        {
            while (DutyLimH < 0)
            {
                DutyLimH = DutyLimH + 24;
                DutyD = DutyD - 1;

            }

        }
        //having summed minutes and hours and corrected for duties that span midnight
        //we now produce a formatted return string

        String DutyString = "0:00:00"; //initialise the string with zeroes just as a precaution
        String DutyLimMstring = "";
        String DutyLimHstring ="";

        if (DutyLimM < 10)   //we pad the minutes with a leading 0 if less than 10
        {
            DutyLimMstring = "0" + DutyLimM;
        } else {

            DutyLimMstring ="" + DutyLimM;
        }

        //now the hours

        if (DutyLimH < 10)   //we pad the hours with a leading 0 if less than 10
        {
            DutyLimHstring = "0" + DutyLimH;
        } else {

            DutyLimHstring ="" + DutyLimH;
        }



        DutyString = "" + DutyD + ":" + DutyLimHstring + ":" +DutyLimMstring;

        //return the computed value as a formatted string
        // Format: D:HH:MM
        //Where a D value of 0 means same day and -1 means orig report was previous day
        return DutyString;

    }
    //end of dutyLimit method

    public String AddTime(int CH, int CM,int DH, int DM)
    {
        int DutyD = 0;
        int DutyLimH = CH + DH;
        int DutyLimM = CM + DM;
        //sum the minutes and convert extras to hours
        if (DutyLimM >= 60)
        {
            while (DutyLimM >= 60)
            {
                DutyLimM = DutyLimM - 60;
                DutyLimH = DutyLimH + 1;
            }

        }

        //having now summed minutes and converted any additional to hours
        //we now check to see if we have more than 24 hours if so we increase the duty day counter
        //this is done to signify the duty spans days

        if (DutyLimH >= 24)
        {
            while (DutyLimH >= 24)
            {
                DutyLimH = DutyLimH - 24;
                DutyD = DutyD + 1;
            }

        }
        //having summed minutes and hours and corrected for duties that span midnight
        //we now produce a formatted return string

        String DutyString = "0:00:00"; //initialise the string with zeroes just as a precaution
        String DutyLimMstring = "";
        String DutyLimHstring ="";

        if (DutyLimM < 10)   //we pad the minutes with a leading 0 if less than 10
        {
            DutyLimMstring = "0" + DutyLimM;
        } else {

            DutyLimMstring ="" + DutyLimM;
        }

        //now the hours

        if (DutyLimH < 10)   //we pad the hours with a leading 0 if less than 10
        {
            DutyLimHstring = "0" + DutyLimH;
        } else {

            DutyLimHstring ="" + DutyLimH;
        }


        DutyString = "" + DutyD + ":" + DutyLimHstring + ":" +DutyLimMstring;

        //return the computed value as a formatted string
        // Format: D:HH:MM
        //Where a D value of 0 means same day and 1 means next day
        return DutyString;

    }
    //end of dutyLimit method








}//end of main class
