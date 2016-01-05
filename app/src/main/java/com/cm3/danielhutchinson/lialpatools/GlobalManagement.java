package com.cm3.danielhutchinson.lialpatools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Daniel on 7/28/2015.
 */

public class GlobalManagement {

    private static final String APP_Storage_home = "sdcard/CM3/";
    private static final String CrewnetStorage = "crewnet/";
    private static final String AdvicesStorage = "advices/";
    private static final String OtherStorage = "other/";


    private static final String CrewNetLocalPath = APP_Storage_home + CrewnetStorage;
    private static final String CrewAdvicesLocalPath = APP_Storage_home + AdvicesStorage;
    private static final String OthertLocalPath = APP_Storage_home + OtherStorage;

    public static void SetupAppGlobals ()
    {
        createDirectoryIfNeeded(APP_Storage_home);
        createDirectoryIfNeeded(CrewNetLocalPath);
        createDirectoryIfNeeded(CrewAdvicesLocalPath);
        createDirectoryIfNeeded(OthertLocalPath);

    }




    public static String GetGlobalDirPaths(String Varname)
    {
        String ReturnString = "null";
        switch (Varname){
            case "homedir" : ReturnString = APP_Storage_home;
                            break;
            case "CrewNetDir" : ReturnString = CrewNetLocalPath;
                break;
            case "AdvicesDir" : ReturnString = CrewAdvicesLocalPath;
                break;
            case "OtherDir" : ReturnString = OthertLocalPath;
                break;
            default : ReturnString = "null";
                break;
        }
        return ReturnString;
    }




    public static void createDirectoryIfNeeded(String directoryName)
    {
        File theDir = new File(directoryName);

        // if the directory does not exist, create it
        if (!theDir.exists())
        {
            System.out.println("creating directory: " + directoryName);
            theDir.mkdir();
        }
    }

    public static Boolean CheckDirExsists(String directoryName)
    {
        Boolean DirExsists = true;
        File theDir = new File(directoryName);

        // if the directory does not exist, create it
        if (!theDir.exists())
        {
            System.out.println("creating directory: " + directoryName);
            DirExsists = false;
        }
        return DirExsists;
    }

    public static Boolean CheckFileExsists(String FileName)
    {
        Boolean Exsists = true;
        File thefile = new File(FileName);

        // if the directory does not exist, create it
        if (!thefile.exists())
        {
            //System.out.println("creating file: " + FileName);
            Exsists = false;
        }
        return Exsists;
    }


    public static String ConvertTimeStampToTime(Long Timestamp)
    {
        String return_string = "0";
        String[]Date_time;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy HH:mm");
        Date resultdate = new Date(Timestamp);
        return_string = resultdate.toString();

        return return_string;
    }



} //end of the file management class
