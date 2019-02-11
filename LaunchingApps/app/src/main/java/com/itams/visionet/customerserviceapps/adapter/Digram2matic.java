package com.itams.visionet.customerserviceapps.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.helper.roomRealmHelper;
import com.itams.visionet.customerserviceapps.model.floorModel;
import com.itams.visionet.customerserviceapps.model.roomModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Administrator on 13/12/2016.
 */

public class Digram2matic extends RecyclerView.Adapter<Digram2matic.ViewHolder> {
    Realm realm;

    private ArrayList<floorModel> floorModel = new ArrayList<>();
    private Context mContext;
    private ArrayList<ArrayList<roomModel>> roomModels = new ArrayList();
    private ArrayList<roomModel> saveprefunit = new ArrayList<>();
    private  int itemvisible;

    public Digram2matic(Context context, ArrayList<floorModel> floorModel, ArrayList<ArrayList<roomModel>> roomModels, ArrayList<roomModel> saveprefunit, Realm realm) {
        super();

        this.mContext = context;
        this.floorModel = floorModel;
        this.roomModels = roomModels;
        this.saveprefunit = saveprefunit;
        this.realm = realm;
//        this.itemvisible=itemvisible;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView floor_text;
        RecyclerView list_top;
        TopAdapter roomAdapter;

        public ViewHolder(View itemView) {
            super(itemView);

            floor_text = (TextView) itemView.findViewById(R.id.floor_number);

            list_top = (RecyclerView) itemView.findViewById(R.id.recyclerview_top);

            initRecycleview();
        }

        private void initRecycleview() {
            roomAdapter = new TopAdapter(mContext, realm);

            final MyGridLayoutManager layoutManager = new MyGridLayoutManager(mContext, 2, GridLayoutManager.HORIZONTAL, false);
            layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            list_top.setLayoutManager(layoutManager);
            list_top.setHasFixedSize(true);
            list_top.computeHorizontalScrollOffset();
            list_top.setAdapter(roomAdapter);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cardview_floor_new, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final floorModel model = floorModel.get(position);

        holder.floor_text.setText(model.getFloor_id());

        Log.i("roomModels", roomModels.get(position).size() + "");

        holder.roomAdapter.setData(roomModels.get(position),model.getFloor_id());

        holder.roomAdapter.setRowIndex(position);

        holder.list_top.setAdapter(holder.roomAdapter);
    }

    @Override
    public int getItemCount() {

            return floorModel.size();

    }

    public void notify(ArrayList<floorModel> floorModel, ArrayList<ArrayList<roomModel>> roomModels){
        this.floorModel = floorModel;
        this.roomModels = roomModels;
//        this.itemvisible=itemvisible;
        notifyDataSetChanged();
    }
}
