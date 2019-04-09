package com.example.taylor.hardwarespecs;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;


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

    public int getNumCores() {
        return Runtime.getRuntime().availableProcessors();
    }
    //Current Android version data take from
    public String getCurrentVersion(){
        double release=Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));
        String codeName="Unsupported";//below Jelly bean OR above Pie
        if(release>=4.1 && release<4.4)codeName="Jelly Bean";
        else if(release<5)codeName="Kit Kat";
        else if(release<6)codeName="Lollipop";
        else if(release<7)codeName="Marshmallow";
        else if(release<8)codeName="Nougat";
        else if(release<9)codeName="Oreo";
        else codeName="Pie";
        return codeName+" v"+release;
    }

        public String getCpuModel() {
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
                    if(read)
                        myData = myData + strLine + "\n";

                    }
            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData;
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
    public  double[] getStorageInfo(View view) {
        double freeBytesInternal = new File(view.getContext().getFilesDir().getAbsoluteFile().toString()).getUsableSpace();
        double freeBytesExternal = new File(view.getContext().getExternalFilesDir(null).toString()).getFreeSpace();
        double totalBytes = new File(view.getContext().getFilesDir().getAbsoluteFile().toString()).getTotalSpace();
        double[] storage = {freeBytesExternal, freeBytesInternal, totalBytes};
        return storage;
    }
    public double getScreenResoltion(View view) {
        Context context = view.getContext();
        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        double x = Math.pow(displayMetrics.widthPixels / displayMetrics.xdpi, 2);
        double y = Math.pow(displayMetrics.heightPixels / displayMetrics.ydpi, 2);
        return (Math.round((Math.sqrt(x+y)) * 10.0) / 10.0);

    }



}
