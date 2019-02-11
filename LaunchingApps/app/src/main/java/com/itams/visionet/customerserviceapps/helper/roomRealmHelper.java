package com.itams.visionet.customerserviceapps.helper;

import android.content.Context;
import android.util.Log;

import com.itams.visionet.customerserviceapps.database.roomRealmObject;
import com.itams.visionet.customerserviceapps.database.selectedPrefUnitRealmObject;
import com.itams.visionet.customerserviceapps.model.roomModel;
import com.itams.visionet.customerserviceapps.model.selectedPrefUnitModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Administrator on 28/11/2016.
 */

public class roomRealmHelper {
    private static final String TAG = "roomRealmHelper";

    Realm realm;

    public roomRealmHelper(Context context, Realm realm){ this.realm = realm; }

    public void addUpdateRoom(roomModel roomModel){
        roomRealmObject roomRealmObject = new roomRealmObject();

        roomRealmObject.setProject_id(roomModel.getProject_id());
        roomRealmObject.setCluster_id(roomModel.getCluster_id());
        roomRealmObject.setFloor_id(roomModel.getFloor_id());
        roomRealmObject.setUnitCode(roomModel.getUnitCode());
        roomRealmObject.setUnitNo(roomModel.getUnitNo());
        roomRealmObject.setUnitStatus(roomModel.getUnitStatus());
        roomRealmObject.setColoring(roomModel.getColoring());
        roomRealmObject.setOrder(roomModel.getOrder());
        roomRealmObject.setSelected(roomModel.isSelected());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(roomRealmObject);
        realm.commitTransaction();

        showLog(roomModel.getUnitNo());
    }

    public roomModel getRoombyUnitNo(String UnitNo){
        roomModel item = new roomModel();
        roomRealmObject getdata = realm.where(roomRealmObject.class).equalTo("UnitNo",UnitNo).findFirst();
        if (getdata != null) {
            item.setProject_id(getdata.getProject_id());
            item.setSelected(getdata.isSelected());
            item.setFloor_id(getdata.getFloor_id());
            item.setUnitNo(getdata.getUnitNo());
            item.setUnitCode(getdata.getUnitCode());
            item.setColoring(getdata.getColoring());
            item.setUnitStatus(getdata.getUnitStatus());
//            item.setOrder(getdata.getOrder());

            return item;
        }else
            return null;

    }



    public ArrayList<roomModel> getRoom(String floor_id){
        ArrayList<roomModel> data = new ArrayList<>();
        RealmResults<roomRealmObject> realmResults = realm.where(roomRealmObject.class).equalTo("floor_id", floor_id).findAll();
        if(realmResults.size() > 0) {
            for (int i = 0; i < realmResults.size(); i++ ){
                data.add(new roomModel(realmResults.get(i)));
            }
        }
        showLog(realmResults.size()+"");
        return data;
    }

    public void clearselectedroom(){
        RealmResults<roomRealmObject> realmResults = realm.where(roomRealmObject.class).findAll();
        if(realmResults.size() > 0) {
            for (int i = 0; i < realmResults.size(); i++ ){
                roomModel obj = new roomModel(realmResults.get(i));
                removeSelectedRoom(obj);
                obj.setSelected(false);
                obj.setOrder(null);
                addUpdateRoom(obj);
            }
        }
        showLog(realmResults.size()+"");

    }

    public void addSelectedRoom(roomModel model){
        selectedPrefUnitRealmObject obj = new selectedPrefUnitRealmObject();

        obj.setUnitCode(model.getUnitCode());
        obj.setUnitNo(model.getUnitNo());
        obj.setOrder(Integer.parseInt(model.getOrder()));

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(obj);
        realm.commitTransaction();
    }

    public void addSelectedRoom(selectedPrefUnitModel model){
        selectedPrefUnitRealmObject obj = new selectedPrefUnitRealmObject();

        obj.setUnitCode(model.getUnitCode());
        obj.setUnitNo(model.getUnitNo());
        obj.setOrder(model.getOrder());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(obj);
        realm.commitTransaction();
    }

    public void removeSelectedRoom(roomModel model){
        final RealmResults<selectedPrefUnitRealmObject> realmResults = realm.where(selectedPrefUnitRealmObject.class).equalTo("UnitNo", model.getUnitNo()).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                realmResults.deleteAllFromRealm();
            }
        });
        reOrdering();
    }

    public void reOrdering(){
        RealmResults<selectedPrefUnitRealmObject> realmResults = realm.where(selectedPrefUnitRealmObject.class).findAllSorted("Order", Sort.ASCENDING);

        if(realmResults.size() > 0){
            for(int i = 0; i < realmResults.size(); i++){

                selectedPrefUnitModel obj = new selectedPrefUnitModel(realmResults.get(i));
                obj.setOrder(i+1);
                addSelectedRoom(obj);
//                roomModel objRoom = getRoombyUnitNo(obj.getUnitNo());
//                if(objRoom != null) {
//                    objRoom.setOrder(obj.getOrder());
//                    addUpdateRoom(objRoom);
//                }
            }
        }
    }

    public void clearSelectedRoomprefunit(){
        final RealmResults<selectedPrefUnitRealmObject> realmResults = realm.where(selectedPrefUnitRealmObject.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.deleteAllFromRealm();
            }
        });
    }

    public void deleteallroom(){
        final RealmResults<roomRealmObject> realmResults = realm.where(roomRealmObject.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                realmResults.deleteAllFromRealm();
            }
        });
    }

    public JSONArray getSelectedRoom(){
        JSONArray data = new JSONArray();
        RealmResults<selectedPrefUnitRealmObject> realmResults = realm.where(selectedPrefUnitRealmObject.class).findAll();
        if(realmResults.size() > 0) {
            for (int i = 0; i < realmResults.size(); i++ ){
                JSONObject dataRoom = new JSONObject();
                try {
                    dataRoom.put("UnitCode", realmResults.get(i).getUnitCode());
                    dataRoom.put("UnitNo", realmResults.get(i).getUnitNo());
                    dataRoom.put("OrderNo", realmResults.get(i).getOrder());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                data.put(dataRoom);
            }
        }
        showLog(realmResults.size()+"");
        return data;
    }

    public ArrayList<selectedPrefUnitModel> getSelectedRoomArrayList(){
        ArrayList<selectedPrefUnitModel> data = new ArrayList<>();
        RealmResults<selectedPrefUnitRealmObject> realmResults = realm.where(selectedPrefUnitRealmObject.class).findAll();
        if(realmResults.size() > 0) {
            for (int i = 0; i < realmResults.size(); i++ ){
                data.add(new selectedPrefUnitModel(realmResults.get(i)));
            }
        }


        showLog(realmResults.size()+"");
        return data;
    }

    public String getorder(String  UnitNo){
        selectedPrefUnitRealmObject realmResults = realm.where(selectedPrefUnitRealmObject.class).equalTo("UnitNo", UnitNo).findFirst();
        if(realmResults == null)
            return null;
        else
        return String.valueOf(realmResults.getOrder());
    }


    public ArrayList<roomModel> getSelectedRoomArrayList_roomModel(){
        ArrayList<roomModel> data = new ArrayList<>();
        RealmResults<roomRealmObject> realmResults = realm.where(roomRealmObject.class).equalTo("isSelected", true).findAll();
        if(realmResults.size() > 0) {
            for (int i = 0; i < realmResults.size(); i++ ){
                data.add(new roomModel(realmResults.get(i)));
            }
        }


        showLog(realmResults.size()+"");
        return data;
    }

    public void deletallselectedroom(){
        realm.beginTransaction();
        realm.where(selectedPrefUnitRealmObject.class).findAll().clear();
        realm.commitTransaction();


    }

    public int countselectedroom(){
        int realmResults = realm.where(roomRealmObject.class).equalTo("isSelected", true).findAll().size();

        return realmResults;
    }

    public int getJumlahRoomByFloor(String floor_id){
        int jumlah = 0;

        RealmResults<roomRealmObject> realmResults = realm.where(roomRealmObject.class).equalTo("floor_id", floor_id).findAll();
        jumlah = realmResults.size();

        return jumlah;
    }

    public int getSisaRoomByFloor(String floor_id){
        int jumlah = 0;

        RealmResults<roomRealmObject> realmResults = realm.where(roomRealmObject.class).equalTo("floor_id", floor_id).equalTo("status", "0a").findAll();
        jumlah = realmResults.size();

        return jumlah;
    }

    private void showLog(String s) {
        Log.d(TAG, s);
    }
}
