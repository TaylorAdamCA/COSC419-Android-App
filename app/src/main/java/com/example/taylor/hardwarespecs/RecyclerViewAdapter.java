package com.example.taylor.hardwarespecs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private ArrayList<InfoObject> mInfoObjects;

    public RecyclerViewAdapter(Context mContext, ArrayList<InfoObject> mInfoObjects) {
        this.mContext = mContext;
        this.mInfoObjects = mInfoObjects;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called");


        viewHolder.leftTv.setText(mInfoObjects.get(i).getName());
        viewHolder.rightTv.setText(mInfoObjects.get(i).getInfo());

    }

    @Override
    public int getItemCount() {
        return mInfoObjects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView leftTv;
        TextView rightTv;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftTv = itemView.findViewById(R.id.leftText);
            rightTv = itemView.findViewById(R.id.rightText);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
