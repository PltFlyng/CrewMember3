package com.cm3.danielhutchinson.lialpatools;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Daniel on 1/4/2016.
 */
public class CrewAdviceFunctions {
    private static String CrewAdviceMasterControlURL = "http://lialpa.org/CM3/CrewAdvices/advice_master.txt";
    private static String LocalManifestFile = "sdcard/CM3/advices/advice_master.txt";


    public static String GetMasterFileStream(String Operation) throws Exception {

        StringBuilder sb = new StringBuilder();
        BufferedWriter bw = null;
        String inputLine = null;

        URL AdviceMasterControl = new URL(CrewAdviceMasterControlURL);
        File outfile = new File(LocalManifestFile);
        BufferedReader in = new BufferedReader(new InputStreamReader(AdviceMasterControl.openStream()));
        String return_string = "";
        String [] ManifestLines;


           switch (Operation) {
            case "PreCheck":
                inputLine = in.readLine();
                sb.append(inputLine.toString());
                return_string = String.valueOf(sb);
                break;
            case "UpdateMaster":
            case "CreatMaster":

                if(!outfile .exists()){
                    outfile .createNewFile();
                }
                FileWriter fileWriter = new FileWriter(outfile);
                bw = new BufferedWriter(fileWriter);
                int endlinecount =0;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);   //for producing test output
                    sb.append(inputLine.toString());
                    sb.append("\n");
                    if (endlinecount > 0)
                    {
                        bw.write("\r\n");
                    }
                    bw.write(String.valueOf(inputLine));
                    endlinecount++;
                }
                bw.close();
                System.out.println("Manifest file successfully written...");

                return_string = String.valueOf(sb);
                ManifestLines = return_string.split("\n");
                return_string = ManifestLines[0];
                //text = text.replace("\n", "").replace("\r", "");
                break;
             }
        in.close();

        return return_string;
    }


    public static String GetLocalMasterFileStream(String Operation) throws Exception {

        String FullPath = LocalManifestFile;
        String line;
        String return_value = "";
        System.out.println("path is " + FullPath);
        File file = new File(FullPath);
        if (file.canRead() == true) {System.out.println("File is Readable");}

        //Read text from file
        StringBuilder text = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));


        if (Operation.equals("PreCheck")) {
            line = br.readLine();
            text.append(line.toString());
            text.append("\n");

            return_value = String.valueOf(text);
        } else {
            //add some other stuff to do later here
        }

        br.close();
        //System.out.println("value being returned is--> " + return_value);
        return return_value;

    }

    public static String PreCheck()
    {
        String ServerStream = "";
        String LocalStream = "";
        Boolean FileExsists = false;
        Long ServerStamp = 0L;
        Long LocalStamp = 0L;
        Boolean UpdateForced = false;
        String PreCheckStatus = "error";

        System.out.println("Precheck Method Started");
        try {
            //first we check to see if the file exsists
           FileExsists = GlobalManagement.CheckFileExsists(LocalManifestFile);
            //FileExsists = false; //for testing purposes
           if (FileExsists == false)
           {
               //if the file does not exist then we create it
               System.out.println("Initializing...");
               System.out.println("Attempting to create local manifest...");
               ServerStream = GetMasterFileStream("UpdateMaster");
               LocalStream = GetLocalMasterFileStream("PreCheck");
               PreCheckStatus = "Init-Manifest";
           }
            else
           {
               //if the file does exist then we read the stamp from the local file
               //read the stamp from the server file
               System.out.println("Manifest File Exsists Locally...");
               //precheck the streams
               ServerStream = GetMasterFileStream("PreCheck");
               LocalStream = GetLocalMasterFileStream("PreCheck");
               //print the stream contents to the log for testing
               System.out.println("Server Stream-->" + ServerStream);
               System.out.println("Local Stream-->" + LocalStream);
               LocalStamp = ParseStampLine(LocalStream);
               ServerStamp = ParseStampLine(ServerStream);

               //check to make sure local contents match online manifest
               //ServerStamp = ServerStamp + 1;
               if (ServerStamp.longValue() == LocalStamp.longValue()){
                   System.out.println("Local Manifest File is up to date");
                   PreCheckStatus = "No-Updates";
               }
               else if (ServerStamp.longValue() > LocalStamp.longValue()){
                   System.out.println("Local Manifest File is out of date");
                   System.out.println("Attempting update.....");
                   UpdateForced = true;
                   ServerStream = GetMasterFileStream("UpdateMaster");
                   LocalStream = GetLocalMasterFileStream("PreCheck");
               }
           }

            ServerStream = GetMasterFileStream("PreCheck");
            LocalStream = GetLocalMasterFileStream("PreCheck");
            LocalStamp = ParseStampLine(LocalStream);
            ServerStamp = ParseStampLine(ServerStream);

            if (UpdateForced == true){
                System.out.println("A manifest update was forced.....");
                if (ServerStamp.longValue() == LocalStamp.longValue())
                {
                    System.out.println("Local manifest update was successful.");
                    PreCheckStatus = "Updates-Pending";
                }
                else if (ServerStamp.longValue() > LocalStamp.longValue())
                {
                    System.out.println("Local manifest update failed!!!!");
                    PreCheckStatus = "error";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return PreCheckStatus;

    }

public static long ParseStampLine (String RawInput)
{
    long return_val = 0L;
    String [] ManifestFileLineStamp;
    ManifestFileLineStamp = RawInput.split(":");
    ManifestFileLineStamp[1] = ManifestFileLineStamp[1].replace("\n", "").replace("\r", "");
    return_val = Long.parseLong(ManifestFileLineStamp[1]);
    return return_val;
}

public static void CrewAdviceController(String Operation)
{
    String PreCheckStatus = "null";
    if (Operation.equals("PreCheck"))
    {
        PreCheckStatus = PreCheck();

    }
}




} //end of the main class
