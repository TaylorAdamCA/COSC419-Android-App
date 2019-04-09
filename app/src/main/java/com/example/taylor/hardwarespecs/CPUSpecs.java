package com.example.taylor.hardwarespecs;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class CPUSpecs extends Fragment {
    private static final String TAG = "SystemSpecsFragment";
    private ArrayList<InfoObject> mInfoObject;
    InfoUtil mInfoUtil = new InfoUtil();
    RecyclerViewAdapter adapter;
    private TimerTask mTimerTask;
    private Timer timer = new Timer();
    private Handler timerHandler;
    private int numCpus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment, container, false);
        numCpus = mInfoUtil.getNumCores();
        timerHandler = new Handler();
        initInfoObjects(view);
        onTimerTick(view);
        timer.schedule(mTimerTask, 10, 2500);
        return view;
    }

    public void onTimerTick(View view) {
        final View view2 = view;
        mTimerTask = new TimerTask() {
            //this method is called every 1ms
            public void run() {
                timerHandler.post(new Runnable() {
                    public void run() {
                        //update textView
                        //ERROR:textView2 cannot be resolved
                        double[] ms = mInfoUtil.getMemorySize(view2);
                        setInfoObject("Free Memory", String.format("%.2f", ms[1] / 1048576) + " MB");
                        setInfoObject("Available Memory", String.format("%.2f", (ms[0] / 1048576) - ms[1] / 1048576) + " MB");
                        for (int i = 1; i <= numCpus; i++)
                            setInfoObject("       Cpu" + i, Double.toString(mInfoUtil.getFrequency(i - 1) / 1000) + " MHz");
                    }
                });
            }
        };
    }


    private void initInfoObjects(View view) {
        double[] ms = mInfoUtil.getMemorySize(view);
        double[] clockSpeeds = mInfoUtil.getClockSpeeds(numCpus);
        mInfoObject = new ArrayList<>();
        mInfoObject.add(new InfoObject(

                "Total Memory",
                String.format("%.2f", ms[0] / 1048576) + " MB"

        ));
        mInfoObject.add(new InfoObject(
                "Free Memory",
                String.format("%.2f", ms[1] / 1048576) + " MB"
        ));

        mInfoObject.add(new InfoObject(
                "Available Memory",
                String.format("%.2f", (
                        ms[0] - ms[1]
                ))
        ));
        mInfoObject.add(new InfoObject(
                "Number of Cores",
                Integer.toString(mInfoUtil.getNumCores())
        ));
        mInfoObject.add(new InfoObject(
                "Clock Speed Range",
                Double.toString(clockSpeeds[0] / 1000) + " MHz - " + String.format("%.2f", clockSpeeds[1] / 1000.00) + " GHz"
        ));
        for (int i = 1; i <= numCpus; i++) {
            mInfoObject.add(new InfoObject(
                    "       Cpu" + i,
                    Double.toString(mInfoUtil.getFrequency(i - 1) / 1000) + " MHz"
            ));
        }

        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: ");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new RecyclerViewAdapter(this.getContext(), mInfoObject);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void setInfoObject(String name, String info) {
        for (int i = 0; i < this.mInfoObject.size(); i++) {
            InfoObject io = mInfoObject.get(i);
            if (io.getName().equals(name)) {
                io.setInfo(info);
                adapter.notifyItemChanged(i);

            }
        }


    }


}
