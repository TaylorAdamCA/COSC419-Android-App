package com.example.taylor.hardwarespecs;

import android.os.Build;
import android.os.Bundle;
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

import static android.content.ContentValues.TAG;


/* A class for the fragment that will display Operating System related information */
public class OSSpecs extends Fragment {
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
                "Device Model",
                Build.MODEL
        ));
        mInfoObject.add(new InfoObject(
                "Hardware",
                Build.HARDWARE
        ));
        mInfoObject.add(new InfoObject(
                "Board",
                Build.BOARD
        ));
        mInfoObject.add(new InfoObject(
                "Hardware",
                Build.BRAND
        ));
        mInfoObject.add(new InfoObject(
                "Device",
                Build.DEVICE
        ));

        mInfoObject.add(new InfoObject(
                "Display",
                Build.DISPLAY
        ));
        mInfoObject.add(new InfoObject(
                "Fingerprint",
                Build.FINGERPRINT
        ));
        mInfoObject.add(new InfoObject(
                "Host",
                Build.HOST
        ));
        mInfoObject.add(new InfoObject(
                "ID",
                Build.ID
        ));
        mInfoObject.add(new InfoObject(
                "Manufacturer",
                Build.MANUFACTURER
        ));
        mInfoObject.add(new InfoObject(
                "Bootloader",
                Build.BOOTLOADER
        ));
        mInfoObject.add(new InfoObject(
                "Brand",
                Build.BRAND
        ));
        mInfoObject.add(new InfoObject(
                "Product",
                Build.PRODUCT
        ));
        mInfoObject.add(new InfoObject(
                "Tags",
                Build.TAGS
        ));
        mInfoObject.add(new InfoObject(
                "Type",
                Build.TYPE
        ));
        mInfoObject.add(new InfoObject(
                "Unknown",
                Build.UNKNOWN
        ));
        mInfoObject.add(new InfoObject(
                "User",
                Build.USER
        ));
        mInfoObject.add(new InfoObject(
                "CPU_ABI",
                Build.CPU_ABI
        ));





        initRecyclerView(view);
    }
    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: ");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.getContext(), mInfoObject);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}
