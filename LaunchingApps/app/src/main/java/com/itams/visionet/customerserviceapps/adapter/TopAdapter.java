package com.itams.visionet.customerserviceapps.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itams.visionet.customerserviceapps.MainActivity;
import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.fragment.Diagram2Fragment;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;
import com.itams.visionet.customerserviceapps.helper.roomRealmHelper;
import com.itams.visionet.customerserviceapps.model.roomModel;
import com.itams.visionet.customerserviceapps.model.selectedPrefUnitModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Administrator on 13/12/2016.
 */

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ViewHolder> {
    Realm realm;

    private ArrayList<roomModel> roomModelArrayList = new ArrayList<>();
    private Context mContext;
    private roomRealmHelper roomhelper;
    private SessionManagement session;
    private String floor_id;
//    private boolean isLoaded;
    private int mRowIndex = -1;

    public TopAdapter(Context context, Realm realm) {
        this.mContext = context;
        this.realm = realm;
        roomhelper = new roomRealmHelper(context,realm);
        session = new SessionManagement(context);
//        isLoaded = false;
    }

    public void setData(ArrayList<roomModel> data, String floor_id) {
        if (roomModelArrayList != data) {
            roomModelArrayList.clear();
            roomModelArrayList.addAll(data);
            this.floor_id = floor_id;
            TopAdapter.this.notifyDataSetChanged();
        }
        Log.i("roomModel", roomModelArrayList.size() + "");
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView room_top;
        RelativeLayout relativeLayout;
        RelativeLayout Order;
        TextView tvOrder;

        public ViewHolder(View itemView) {
            super(itemView);
            room_top = (TextView) itemView.findViewById(R.id.room_number_top);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.top_click);
            Order = (RelativeLayout) itemView.findViewById(R.id.Order);
            tvOrder = (TextView) itemView.findViewById(R.id.tvOrder);
        }

        public void click(final roomModel data) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(isLoaded){
                        //listener.onClick(data);
//                        ArrayList<selectedPrefUnitModel> selectedPrefUnit = roomhelper.getSelectedRoomArrayList();
//
//                        Log.i("Selected", selectedPrefUnit.size() + " session : " + session.getAllowedPrefUnit());
//                        boolean isSelected = selectedPrefUnit.contains(data);
//                        if (session.getAllowedPrefUnit() > selectedPrefUnit.size() && !isSelected) {
//                            relativeLayout.setBackgroundResource(R.drawable.backgroundpesan);
//                            Order.setVisibility(View.VISIBLE);
//                            tvOrder.setText(String.valueOf(selectedPrefUnit.size() + 1));
//                            roomhelper.addSelectedRoom(data);
//                        }else if (isSelected){
//                            Order.setVisibility(View.GONE);
//                            relativeLayout.setBackgroundColor(Color.parseColor(data.getColoring()));
//                            roomhelper.removeSelectedRoom(data);
//                        }
//                        TopAdapter.this.notifyDataSetChanged();
//                        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
////                        setData(roomhelper.getRoom(floor_id), floor_id);
//                        notifyDataSetChanged();
//
//                        ((Diagram2Fragment)fragmentManager.findFragmentByTag("Cluster")).changedtext();
//                    }else{
                        if (data.getUnitStatus().equals("A")) {
                            //listener.onClick(data);
                            ArrayList<selectedPrefUnitModel> selectedPrefUnit = roomhelper.getSelectedRoomArrayList();

                            Log.i("Selected", selectedPrefUnit.size() + " session : " + session.getAllowedPrefUnit());
                            if (session.getAllowedPrefUnit() > selectedPrefUnit.size() && !data.isSelected()) {
                                data.setSelected(true);
                                data.setOrder(String.valueOf(selectedPrefUnit.size() + 1));
                                relativeLayout.setBackgroundResource(R.drawable.backgroundpesan);
                                Order.setVisibility(View.VISIBLE);
                                tvOrder.setText(String.valueOf(selectedPrefUnit.size() + 1));
                                roomhelper.addSelectedRoom(data);

                            }else if (data.isSelected()){
                                data.setSelected(false);
                                data.setOrder(null);
                                Order.setVisibility(View.GONE);
                                relativeLayout.setBackgroundColor(Color.parseColor(data.getColoring()));
                                roomhelper.removeSelectedRoom(data);
                            }
                            roomhelper.addUpdateRoom(data);
                            TopAdapter.this.notifyDataSetChanged();
                            FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                            setData(roomhelper.getRoom(floor_id), floor_id);
//                        notifyDataSetChanged();

                            ((Diagram2Fragment)fragmentManager.findFragmentByTag("Cluster")).changedtext();
                        }
//                    }
                }
            });
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cardview_grid_top, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final roomModel models = roomModelArrayList.get(position);
        holder.click(models);

        holder.room_top.setText(models.getUnitNo());

        holder.relativeLayout.setBackgroundColor(Color.parseColor(models.getColoring()));

//        if (models.isSelected() || savedPrefUnit.contains(models)) {
//            holder.relativeLayout.setBackgroundResource(R.drawable.backgroundpesan);
//            models.setSelected(true);
//        }

//        if (models.isSelected()) {
//            holder.relativeLayout.setBackgroundResource(R.drawable.backgroundpesan);
//            holder.Order.setVisibility(View.VISIBLE);
//            holder.tvOrder.setText(models.getOrder());
//        }else{
//            holder.Order.setVisibility(View.GONE);
//        }
        String data_order_realm = roomhelper.getorder(models.getUnitNo());
        if(data_order_realm == null){
            holder.Order.setVisibility(View.GONE);
        }else{
            holder.relativeLayout.setBackgroundResource(R.drawable.backgroundpesan);
            holder.Order.setVisibility(View.VISIBLE);
            holder.tvOrder.setText(data_order_realm);
        }

//        roomhelper.addUpdateRoom(models);

//        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return roomModelArrayList.size();
    }

    public interface OnItemClickListener {
        void onClick(roomModel item);
    }
}