package com.example.taylor.hardwarespecs;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CPUSpecs extends Fragment {
    private  static  final  String TAG = "SystemSpecsFragment";
    private static final Build build = new Build();
    private ArrayList<InfoObject> mInfoObject;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab1_fragment, container, false);


        initInfoObjects(view);
        return view;
    }
    private void initInfoObjects(View view) {
        mInfoObject = new ArrayList<>();
        mInfoObject.add(new InfoObject(
                "Name",
                getCpuSpecs()
        ));
        mInfoObject.add(new InfoObject(
                "Test1",
                "Test"
        ));
        mInfoObject.add(new InfoObject(
                "Test",
                "Test"
        ));
        mInfoObject.add(new InfoObject(
                "Test",
                "Test"
        ));


        initRecyclerView(view);
    }
    private String getCpuSpecs(){
        ProcessBuilder processBuilder;
        String Holder = "";
        String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
        InputStream inputStream;
        Process process ;
        byte[] byteArry ;

        byteArry = new byte[1024];

        try{
            processBuilder = new ProcessBuilder(DATA);

            process = processBuilder.start();

            inputStream = process.getInputStream();

            while(inputStream.read(byteArry) != -1){

                Holder = Holder + new String(byteArry);
            }

            inputStream.close();

        } catch(IOException ex){

            ex.printStackTrace();
        }

       return Holder;
    }
    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: ");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.getContext(), mInfoObject);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

}
