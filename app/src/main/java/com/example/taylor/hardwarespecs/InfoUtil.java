package com.example.taylor.hardwarespecs;

import android.app.ActivityManager;
import android.os.Build;
import android.util.Log;
import android.view.View;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;

/*Helper class for returning various details about the device*/

public class InfoUtil {
    private static final String TAG = "InfoUtil";


     /* Returns the current memory usage and free memory in bytes */
    public double[] getMemorySize(View view) {
        double[] res = new double[2];
        ActivityManager actManager = (ActivityManager) view.getContext().getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        res[0] = memInfo.totalMem;
        res[1] = memInfo.availMem;


        return res;
    }
    public double getFreeMemory(View view) {
        ActivityManager actManager = (ActivityManager) view.getContext().getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        return memInfo.availMem;

    }
    public int getNumCores() {
        return Runtime.getRuntime().availableProcessors();
    }
    //Current Android version data take from
    public String currentVersion(){
        double release=Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));
        String codeName="Unsupported";//below Jelly bean OR above Pie
        if(release>=4.1 && release<4.4)codeName="Jelly Bean";
        else if(release<5)codeName="Kit Kat";
        else if(release<6)codeName="Lollipop";
        else if(release<7)codeName="Marshmallow";
        else if(release<8)codeName="Nougat";
        else if(release<9)codeName="Oreo";
        else codeName="Pie";
        return codeName+" v"+release+", API Level: "+Build.VERSION.SDK_INT;
    }

        public String readFile()
    {
        String myData = "";
        File myExternalFile = new File("proc/cpuinfo");
        try {
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            Boolean read = false;
                while ((strLine = br.readLine()) != null) {
                    if(strLine.contains("Hardware")) read = true;
                    if(read) {

                        myData = myData + strLine + "\n";
                        read = false;
                    }
                    }
            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData + "\n " + getNumCores();
    }
    public double getFrequency(int cpuNumber){

        double myData = 0;
        File myExternalFile = new File("/sys/devices/system/cpu/cpu"+cpuNumber+"/cpufreq/scaling_cur_freq");

        try {
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;

           strLine = br.readLine();
           // Log.d(TAG, "getFrequency: " + strLine );
           myData = Long.parseLong(strLine);

            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData;
    }
    public double[] getClockSpeeds(int numCpus){

        double myData[] = {0, 0};
        File maxFreq = new File("/sys/devices/system/cpu/cpu7/cpufreq/scaling_max_freq");
        File minFreq = new File("/sys/devices/system/cpu/cpufreq/policy0/cpuinfo_min_freq");
        File[] files = {minFreq, maxFreq};
        for (int i = 0; i < files.length; i++) {
            try {
                File myExternalFile = files[i];
                FileInputStream fis = new FileInputStream(myExternalFile);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String strLine;

                strLine = br.readLine();
                Log.d(TAG, "getFrequency: " + strLine);
                myData[i] = Long.parseLong(strLine);

                br.close();
                in.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myData[1]/=1000;

        return myData;
    }

}
