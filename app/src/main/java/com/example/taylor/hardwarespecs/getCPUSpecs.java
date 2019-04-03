package com.example.taylor.hardwarespecs;

import android.os.Bundle;
import android.widget.TextView;

import java.io.InputStream;

public class getCPUSpecs {

    TextView textView ;
    ProcessBuilder processBuilder;
    String Holder = "";
    String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    InputStream inputStream;
    Process process ;
    byte[] byteArry ;


}
