package com.example.taylor.hardwarespecs;

        import android.app.ActivityManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Debug;
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
        import android.widget.TextView;

        import java.io.BufferedReader;
        import java.io.DataInputStream;
        import java.io.File;
        import java.io.FileFilter;
        import java.io.FileInputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.RandomAccessFile;
        import java.util.ArrayList;
        import java.util.Timer;
        import java.util.TimerTask;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

        import static android.content.Context.ACTIVITY_SERVICE;
        import static android.support.v4.content.ContextCompat.getSystemService;

public class CPUSpecs extends Fragment {
    private  static  final  String TAG = "SystemSpecsFragment";
    private static final Build build = new Build();
    private ArrayList<InfoObject> mInfoObject;
    TextView textView ;
    InfoUtil mInfoUtil = new InfoUtil();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab1_fragment, container, false);
        initInfoObjects(view);
        return view;
    }



//    private String readFile()
//    {
//        String myData = "";
//        File myExternalFile = new File("proc/cpuinfo");
//        try {
//            FileInputStream fis = new FileInputStream(myExternalFile);
//            DataInputStream in = new DataInputStream(fis);
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//
//            String strLine;
//            Boolean read = false;
//                while ((strLine = br.readLine()) != null) {
//                    if(strLine.contains("Hardware")) read = true;
//                    if(read) {
//                        Log.d(TAG, "readFile: " + strLine.substring(strLine.indexOf(':')));
//                        myData = myData + strLine + "\n";
//                        read = false;
//                    }
//                    }
//            br.close();
//            in.close();
//            fis.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return myData + "\n " + getNumCores();
//    }




    private void initInfoObjects(View view) {
       long[] ms = mInfoUtil.getMemorySize(view);

        mInfoObject = new ArrayList<>();
        mInfoObject.add(new InfoObject(
                "Total Memory",
                Long.toString(ms[0]/1048576) + " MB"
        ));
        mInfoObject.add(new InfoObject(
                "Free Memory",
                Long.toString(ms[1]/1048576) + " MB"
        ));
        mInfoObject.add(new InfoObject(
                "Number of Cores",
                Integer.toString(mInfoUtil.getNumCores())
        ));
        mInfoObject.add(new InfoObject(
                "Test",
                "Test"
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
    private InfoObject getInfoObject(String name) {
        for(InfoObject io : this.mInfoObject)
            if(io.getName().equals(name))
                return io;

        return null;
    }


}
