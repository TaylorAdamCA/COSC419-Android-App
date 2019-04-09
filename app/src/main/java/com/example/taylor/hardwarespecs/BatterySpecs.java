package com.example.taylor.hardwarespecs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
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

import static android.content.ContentValues.TAG;


/* A class for the fragment that will display Operating System related information */
public class BatterySpecs extends Fragment {
    private ArrayList<InfoObject> mInfoObject;
    IntentFilter iFilter;
    Intent batteryStatus;
    RecyclerViewAdapter adapter;
    private TimerTask mTimerTask;
    private Timer timer = new Timer();
    private Handler timerHandler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab3_fragment, container, false);
        iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = view.getContext().registerReceiver(null,iFilter);
        onTimerTick(view);
        timerHandler = new Handler();
        timer.schedule(mTimerTask, 10, 2500);
        initInfoObjects(view);
        return view;
    }
    public void onTimerTick(View view) {
        mTimerTask = new TimerTask() {
            //this method is called every 1ms
            public void run() {
                timerHandler.post(new Runnable() {
                    public void run() {
                        float temp = ((float) batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0) / 10);
                        float fullVoltage = (float) ( batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0) * 0.001);
                        setInfoObject("Temperature", String.format("%.1f",temp) + " *C");
                        setInfoObject("Current Voltage", String.format("%.1f",fullVoltage) + " V");
                    }
                });
            }
        };

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

    private void initInfoObjects(View view) {
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float)scale;
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        int health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        String batteryHealth = getBatteryHealth(health);
        float temp = ((float) batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0) / 10);
        float fullVoltage = (float) ( batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0) * 0.001);

        mInfoObject = new ArrayList<>();
        mInfoObject.add(new InfoObject(
                "Health",
                batteryHealth
        ));
        mInfoObject.add(new InfoObject(
                "Currently Charging",
                 isCharging == true? "Yes": "No"
        ));
        mInfoObject.add(new InfoObject(
                "Percent",
                Integer.toString((int)batteryPct * 100) + "%"
        ));
        mInfoObject.add(new InfoObject(
                "Temperature",
                String.format("%.1f", temp) + " *C"
        ));
        mInfoObject.add(new InfoObject(
                "Current Voltage",
                String.format("%.1f", fullVoltage) + " V"
        ));

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

    private String getBatteryHealth(int health) {
        String temp = "Unknown";
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_GOOD:
                temp = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                 temp = "Cold";
                 break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                temp = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                temp = "Overheated";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                temp = "Unspecified Failure";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                temp = "Over Voltage";
            default:
                break;
        }
        return temp;
    }


}
