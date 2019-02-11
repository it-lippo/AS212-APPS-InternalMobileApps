package com.itams.visionet.customerserviceapps.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.fragment.RoomFragment;
import com.itams.visionet.customerserviceapps.helper.roomRealmHelper;
import com.itams.visionet.customerserviceapps.model.floorModel;
import com.itams.visionet.customerserviceapps.model.roomModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 24/11/2016.
 */

public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<floorModel> floorModels = new ArrayList<>();
    private ArrayList<String> savedPrefUnit = new ArrayList<>();
    private roomRealmHelper roomHelper;

    public FloorAdapter(Context context, ArrayList<floorModel> floorModels, ArrayList<String> savedPrefUnit, roomRealmHelper roomHelper){
        this.mContext = context;
        this.floorModels = floorModels;
        this.savedPrefUnit = savedPrefUnit;
        this.roomHelper = roomHelper;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView floorText;
        TextView sisaText;
        TextView isiText;
        TextView persen;
        ProgressBar mProgressBar;

        LinearLayout onClick;

        public ViewHolder(View v) {
            super(v);
            floorText = (TextView) v.findViewById(R.id.floor_text);
            sisaText = (TextView) v.findViewById(R.id.text_total);
            isiText = (TextView) v.findViewById(R.id.text_available);
            mProgressBar = (ProgressBar) v.findViewById(R.id.chart);
            onClick = (LinearLayout) v.findViewById(R.id.onClick);
            persen = (TextView) v.findViewById(R.id.persen_number);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_diagramatic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final floorModel floorModel = floorModels.get(position);
        holder.floorText.setText(floorModel.getFloor_id());


        int total = roomHelper.getJumlahRoomByFloor(floorModel.getTotal_unit());
        String total_room = floorModel.getTotal_unit();
        String total_available = floorModel.getTotal_unit();
        int available_room = Integer.parseInt(floorModel.getTotal_unit())- Integer.parseInt(floorModel.getTotal_sold());
//        int available = roomHelper.getSisaRoomByFloor(String.valueOf(available_room));

//        int used = total - available_room;

        holder.sisaText.setText("TOTAL UNIT : " + total_room);
        holder.isiText.setText("AVAILABLE UNIT : " + available_room);
        holder.persen.setText((int)Math.round(convertToPersen(available_room,Integer.parseInt(total_room))) + "%");
        holder.mProgressBar.setProgress((int)Math.round(convertToPersen(Double.parseDouble(floorModel.getTotal_sold()),Integer.parseInt(total_room) )));
//        holder.mProgressBar.setMax(100);


        holder.onClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RoomFragment();
                Bundle bundle = new Bundle();
                bundle.putString("floor_id", floorModel.getFloor_id());
                bundle.putString("project_id", floorModel.getProject_id());
                bundle.putString("cluster_id", floorModel.getCluster_id());
                bundle.putStringArrayList("savedPrefUnit", savedPrefUnit);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerView, fragment, "Room");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private double convertToPersen(double val1, double val2) {
        double result = val1 / val2 * 100;
        return result;
    }

    @Override
    public int getItemCount() {
        Log.i("sizze adapter", String.valueOf(floorModels.size()));
        return floorModels.size();
    }
}