package com.example.taylor.hardwarespecs;

import android.app.ActivityManager;
import android.os.Build;
import android.view.View;


import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;

/*Helper class for returning various details about the device*/

public class InfoUtil {


     /* Returns the current memory usage and free memory in bytes */
    public long[] getMemorySize(View view) {
        long[] res = new long[2];
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

}
