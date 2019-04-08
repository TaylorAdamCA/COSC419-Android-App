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
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

        import static android.content.Context.ACTIVITY_SERVICE;
        import static android.support.v4.content.ContextCompat.getSystemService;

public class CPUSpecs extends Fragment {
    private  static  final  String TAG = "SystemSpecsFragment";
    private static final Build build = new Build();
    private ArrayList<InfoObject> mInfoObject;
    TextView textView ;
    ProcessBuilder processBuilder;
    ActivityManager am;
    String Holder = "";
    String[] DATA = {"proc/cpuinfo","proc/meminfo"};
    InputStream inputStream;
    Process process ;
    byte[] byteArry ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab1_fragment, container, false);

        textView = (TextView)view.findViewById(R.id.textView);
       //textView.setText("");


        byteArry = new byte[1024];

        try{
            processBuilder = new ProcessBuilder(DATA);

            process = processBuilder.start();

            inputStream = process.getInputStream();

            while(inputStream.read(byteArry) != -1){
                Log.d(TAG, "onCreateView: "+ new String(byteArry));
                Holder = Holder + new String(byteArry);
            }

            inputStream.close();

        } catch(IOException ex){

            ex.printStackTrace();
        }
    Holder += "\n Free Memory" + getMemorySize().free + "\n Total Memory " + getMemorySize().total + " \n" + readFile();
           textView.setText(Holder);

      // initInfoObjects(view);
        return view;
    }
    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     * @return The number of cores, or 1 if failed to get result
     */
    private int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by one or more digits
                if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Default to return 1 core
            return 1;
        }
    }
    private String readFile()
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
                        Log.d(TAG, "readFile: " + strLine.substring(strLine.indexOf(':')));
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

    /* Taken from https://stackoverflow.com/questions/3170691/how-to-get-current-memory-usage-in-android/3192348#3192348
     * Returns the current memory usage and free memory in megabytes */
    private MemorySize getMemorySize() {
        final Pattern PATTERN = Pattern.compile("([a-zA-Z]+):\\s*(\\d+)");

        MemorySize result = new MemorySize();
        String line;
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
            while ((line = reader.readLine()) != null) {
                Matcher m = PATTERN.matcher(line);
                if (m.find()) {
                    String name = m.group(1);
                    String size = m.group(2);

                    if (name.equalsIgnoreCase("MemTotal")) {
                        result.total = Long.parseLong(size);
                    } else if (name.equalsIgnoreCase("MemFree") || name.equalsIgnoreCase("Buffers") ||
                            name.equalsIgnoreCase("Cached") || name.equalsIgnoreCase("SwapFree")) {
                        result.free += Long.parseLong(size);
                    }
                }
            }
            reader.close();

            result.total *= 1024;
            result.free *= 1024;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static class MemorySize {
        public long total = 0;
        public long free = 0;
    }
    private void initInfoObjects(View view) {
        mInfoObject = new ArrayList<>();
        mInfoObject.add(new InfoObject(
                "Total Memory",
                Long.toString(getMemorySize().total/1048576) + " MB"
        ));
        mInfoObject.add(new InfoObject(
                "Free Memory",
                Long.toString(getMemorySize().free/1048576) + " MB"
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
