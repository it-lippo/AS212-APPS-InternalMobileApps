package com.itams.visionet.customerserviceapps.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 24/11/2016.
 */

public class floorRealmObject extends RealmObject {
    @PrimaryKey
    private String floor_id;

    private String total_unit;
    private String total_sold;
    private String percentageUnitSold;
    private String cluster_id;
    private String project_id;





    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }


    public String getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }


    public String getTotal_sold() {
        return total_sold;
    }

    public void setTotal_sold(String total_sold) {
        this.total_sold = total_sold;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public String getTotal_unit() {
        return total_unit;
    }

    public void setTotal_unit(String total_unit) {
        this.total_unit = total_unit;
    }

    public String getPercentageUnitSold() {
        return percentageUnitSold;
    }

    public void setPercentageUnitSold(String percentageUnitSold) {
        this.percentageUnitSold = percentageUnitSold;
    }


}
