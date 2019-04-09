package com.example.taylor.hardwarespecs;

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
public class SystemSpecs extends Fragment {
    private ArrayList<InfoObject> mInfoObject;
    InfoUtil mInfoUtil = new InfoUtil();
    RecyclerViewAdapter adapter;
    private TimerTask mTimerTask;
    private Timer timer = new Timer();
    private Handler timerHandler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab2_fragment, container, false);
        onTimerTick(view);
        timerHandler = new Handler();
        timer.schedule(mTimerTask, 10, 2500);
        initInfoObjects(view);
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
                        setInfoObject("   Available Memory", String.format("%.2f", ms[1] / 1048576) + " MB");
                        setInfoObject("   In Use Memory", String.format("%.2f", (ms[0] / 1048576) - ms[1] / 1048576) + " MB");
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
        double[] ms = mInfoUtil.getMemorySize(view);
        double[] storage = mInfoUtil.getStorageInfo(view);
        mInfoObject = new ArrayList<>();
        mInfoObject.add(new InfoObject(
                "Device Model",
                Build.MODEL
        ));
        mInfoObject.add(new InfoObject(
                "OS Version",
                mInfoUtil.getCurrentVersion()
        ));
        mInfoObject.add(new InfoObject(
                "API Level",
                Integer.toString(Build.VERSION.SDK_INT)
        ));
        mInfoObject.add(new InfoObject(
                "MotherBoard",
                Build.BOARD
        ));
        mInfoObject.add(new InfoObject(
                "Brand",
                Build.BRAND
        ));
        mInfoObject.add(new InfoObject(
                "Total Memory",
                String.format("%.2f", ms[0] / 1048576) + " MB"
        ));
        mInfoObject.add(new InfoObject(
                "   Available Memory",
                String.format("%.2f", ms[1] / 1048576) + " MB"
        ));
        mInfoObject.add(new InfoObject(
                "   In Use Memory",
                String.format("%.2f", (ms[0] - ms[1])/ 1048576) + " MB"
        ));
        mInfoObject.add(new InfoObject(
                "Total Storage",
                String.format("%.2f",storage[2]/1048576/1000) + " GB"
        ));
        mInfoObject.add(new InfoObject(
                "   Free Internal Storage",
                String.format("%.2f",storage[1]/1048576/1000) + " GB"
        ));
        mInfoObject.add(new InfoObject(
                "   Free External Storage",
                String.format("%.2f",storage[0]/1048576/1000) + " GB"
        ));
        mInfoObject.add(new InfoObject(
                "Display",
                Build.DISPLAY
        ));
        mInfoObject.add(new InfoObject(
                "Screen Size (- Nav Bar)",
                        String.format("%.2f", mInfoUtil.getScreenResoltion(view)) + "'"
        ));
        mInfoObject.add(new InfoObject(
                "ABI",
                Build.SUPPORTED_64_BIT_ABIS[0]
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

}
