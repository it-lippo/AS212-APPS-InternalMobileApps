package com.itams.visionet.customerserviceapps.adapter;

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

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.fragment.Diagram2Fragment;
import com.itams.visionet.customerserviceapps.fragment.RoomFragment;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;
import com.itams.visionet.customerserviceapps.helper.roomRealmHelper;
import com.itams.visionet.customerserviceapps.model.roomModel;
import com.itams.visionet.customerserviceapps.model.selectedPrefUnitModel;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Administrator on 28/11/2016.
 */

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>{
    private OnItemClickListener listener;
    private Context context;
    private roomRealmHelper roomhelper;
    private ArrayList<roomModel> roomModels = new ArrayList<>();
    private ArrayList<String> savedPrefUnit = new ArrayList<>();
    private SessionManagement session;
    private int selected = 0;

    public RoomAdapter(Context mContext, ArrayList<roomModel> roomModel, ArrayList<String> savedPrefUnit, roomRealmHelper roomhelper, OnItemClickListener listener){
        this.context = mContext;
        this.roomModels = roomModel;
        this.savedPrefUnit = savedPrefUnit;
        this.roomhelper = roomhelper;
        this.listener = listener;
        session = new SessionManagement(mContext);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView room_number;
        RelativeLayout cardview_room;
        RelativeLayout Order;
        TextView tvOrder;

        public ViewHolder(View itemView) {
            super(itemView);
            cardview_room = (RelativeLayout) itemView.findViewById(R.id.cardview_room);
            room_number = (TextView) itemView.findViewById(R.id.room_number);
            Order = (RelativeLayout) itemView.findViewById(R.id.Order);
            tvOrder = (TextView) itemView.findViewById(R.id.tvOrder);
        }

        public void click(final roomModel data, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getUnitStatus().equals("A")) {
                        //listener.onClick(data);
                        //listener.onClick(data);
                        ArrayList<selectedPrefUnitModel> selectedPrefUnit = roomhelper.getSelectedRoomArrayList();

                        Log.i("Selected", selectedPrefUnit.size() + " session : " + session.getAllowedPrefUnit());
                        if (session.getAllowedPrefUnit() > selectedPrefUnit.size() && !data.isSelected()) {
                            data.setSelected(true);
                            data.setOrder(String.valueOf(selectedPrefUnit.size() + 1));
                            cardview_room.setBackgroundResource(R.drawable.backgroundpesan);
                            Order.setVisibility(View.VISIBLE);
                            tvOrder.setText(String.valueOf(selectedPrefUnit.size() + 1));
                            roomhelper.addSelectedRoom(data);
                        }else if (data.isSelected()){
                            data.setSelected(false);
                            data.setOrder(null);
                            Order.setVisibility(View.GONE);
                            cardview_room.setBackgroundColor(Color.parseColor(data.getColoring()));
                            roomhelper.removeSelectedRoom(data);
                        }
                        roomhelper.addUpdateRoom(data);
                        //notifyDataSetChanged();
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
//                    update 0512 : hanya bisa memilih room yang hijau
//                        ArrayList<selectedPrefUnitModel> selectedPrefUnit = roomhelper.getSelectedRoomArrayList();
//
//                        Log.i("Selected", selectedPrefUnit.size() + " session : " + session.getAllowedPrefUnit());
//                        if (session.getAllowedPrefUnit() > selectedPrefUnit.size() && !data.isSelected()) {
//                            data.setSelected(true);
//                            data.setOrder(String.valueOf(selectedPrefUnit.size() + 1));
//                            cardview_room.setBackgroundResource(R.drawable.backgroundpesan);
//                            Order.setVisibility(View.VISIBLE);
//                            tvOrder.setText(String.valueOf(selectedPrefUnit.size() + 1));
//                            roomhelper.addSelectedRoom(data);
//                        } else if (data.isSelected()) {
//                            data.setSelected(false);
//                            data.setOrder(null);
//                            Order.setVisibility(View.GONE);
//                            cardview_room.setBackgroundColor(Color.parseColor(data.getColoring()));
//                            roomhelper.removeSelectedRoom(data);
//                        }
//                        roomhelper.addUpdateRoom(data);
//                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

                        ((RoomFragment)fragmentManager.findFragmentByTag("Room")).changedtext();
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_room, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final roomModel model = roomModels.get(position);
        holder.click(model, listener);
        holder.room_number.setText(model.getUnitNo());
        holder.cardview_room.setBackgroundColor(Color.parseColor(model.getColoring()));

//        if(model.isSelected()){
//            holder.cardview_room.setBackgroundResource(R.drawable.backgroundpesan);
//            holder.Order.setVisibility(View.VISIBLE);
//            holder.tvOrder.setText(model.getOrder());
//            //model.setSelected(true);
//        }else
//            holder.Order.setVisibility(View.GONE);


        String data_order_realm = roomhelper.getorder(model.getUnitNo());
        if(data_order_realm == null){
            holder.Order.setVisibility(View.GONE);
        }else{
            holder.cardview_room.setBackgroundResource(R.drawable.backgroundpesan);
            holder.Order.setVisibility(View.VISIBLE);
            holder.tvOrder.setText(data_order_realm);
        }

        //roomhelper.addUpdateRoom(model);
    }

    @Override
    public int getItemCount() {
        return roomModels.size();
    }

    public interface OnItemClickListener{
        void onClick(roomModel item);
    }

    public void notify_data_room( ArrayList<roomModel>roomModel,ArrayList<String> savedPrefUnit){
        this.roomModels = roomModel;
        this.savedPrefUnit = savedPrefUnit;

        notifyDataSetChanged();
    }
}
