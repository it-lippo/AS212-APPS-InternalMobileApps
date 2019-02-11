package com.itams.visionet.customerserviceapps.helper;

import android.content.Context;
import android.util.Log;

import com.itams.visionet.customerserviceapps.database.diagramaticClusterRealmObject;
import com.itams.visionet.customerserviceapps.database.diagramaticProjectRealmObject;
import com.itams.visionet.customerserviceapps.model.diagramaticClusterModel;
import com.itams.visionet.customerserviceapps.model.diagramaticProjectModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Administrator on 24/11/2016.
 */

public class diagramaticRealmHelper {
    Realm realm;

    public diagramaticRealmHelper(Context context, Realm realm){
        this.realm = realm;
    }

    public void addCluster(diagramaticClusterModel clusterModel){
        diagramaticClusterRealmObject obj = new diagramaticClusterRealmObject();
        obj.setCluster_id(clusterModel.getCluster_id());
        obj.setCluster(clusterModel.getCluster());
        obj.setProject_id(clusterModel.getProject_id());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(obj);
        realm.commitTransaction();
    }

    public void addProject(diagramaticProjectModel projectModel){
        diagramaticProjectRealmObject obj = new diagramaticProjectRealmObject();
        obj.setProject_id(projectModel.getProject_id());
        obj.setProject(projectModel.getProject());
        Log.i("preject id",projectModel.getProject_id());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(obj);
        realm.commitTransaction();
    }

    public ArrayList<diagramaticClusterModel> getCluster(String project_id){
        ArrayList<diagramaticClusterModel> data = new ArrayList<>();
        RealmResults<diagramaticClusterRealmObject> realmResults = realm.where(diagramaticClusterRealmObject.class).equalTo("project_id", project_id).findAll();
        if(realmResults.size() > 0) {
            for (int i = 0; i < realmResults.size(); i++ ){

                data.add(new diagramaticClusterModel(realmResults.get(i)));
            }
        }
        return data;
    }

    public ArrayList<diagramaticProjectModel> getProject(){
        ArrayList<diagramaticProjectModel> data = new ArrayList<>();
        RealmResults<diagramaticProjectRealmObject> realmResults = realm.where(diagramaticProjectRealmObject.class).findAll();
        if(realmResults.size() > 0) {
            for (int i = 0; i < realmResults.size(); i++ ){
                Log.i("terss","result" + realmResults.get(i));
                data.add(new diagramaticProjectModel(realmResults.get(i)));
            }
        }
        return data;
    }

    public diagramaticProjectModel getProjectByCode(String code){
        diagramaticProjectModel data = new diagramaticProjectModel(realm.where(diagramaticProjectRealmObject.class).equalTo("project_id", code).findFirst());

        return data;
    }
}
