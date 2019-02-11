package com.itams.visionet.customerserviceapps.helper;

import android.content.Context;
import android.util.Log;

import com.itams.visionet.customerserviceapps.database.floorRealmObject;
import com.itams.visionet.customerserviceapps.model.floorModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Administrator on 24/11/2016.
 */

public class floorRealmHelper {
    private static final String TAG = "floorRealmHelper";

    Realm realm;

    public floorRealmHelper(Context context, Realm realm){
        this.realm = realm;
    }

    public void addFloor(floorModel model){
        floorRealmObject classObject = new floorRealmObject();
        classObject.setFloor_id(model.getFloor_id());
        classObject.setTotal_unit(model.getTotal_unit());
        classObject.setTotal_sold(model.getTotal_sold());
        classObject.setPercentageUnitSold(model.getPercentageUnitSold());
        classObject.setCluster_id(model.getCluster_id());
        classObject.setProject_id(model.getProject_id());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(classObject);
        realm.commitTransaction();

        showLog(classObject.getFloor_id());
    }

    public ArrayList<floorModel> getFloor(String cluster_id){
        ArrayList<floorModel> data = new ArrayList<>();
        RealmResults<floorRealmObject> realmResults = realm.where(floorRealmObject.class).equalTo("cluster_id",cluster_id).findAll();
        realmResults = realmResults.sort("floor_id", Sort.DESCENDING);
        if(realmResults.size() > 0) {
            for (int i = 0; i < realmResults.size(); i++ ){
                data.add(new floorModel(realmResults.get(i)));
            }
        }

        showLog(realmResults.size() + "");
        return data;
    }

    private void showLog(String s) {
        Log.d(TAG, s);
    }
}
