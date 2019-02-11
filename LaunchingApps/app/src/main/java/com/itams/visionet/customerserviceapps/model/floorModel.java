package com.itams.visionet.customerserviceapps.model;

import com.itams.visionet.customerserviceapps.database.floorRealmObject;

/**
 * Created by Administrator on 24/11/2016.
 */

public class floorModel {

    private String total_unit;
    private String total_sold;
    private String floor_id;
    private String percentageUnitSold;
    private String cluster_id;


    private String project_id;


    public floorModel(){}

    public floorModel(floorRealmObject obj){
        this.total_unit = obj.getTotal_unit();
        this.floor_id = obj.getFloor_id();
        this.total_sold = obj.getTotal_sold();
        this.percentageUnitSold = obj.getPercentageUnitSold();
        this.cluster_id = obj.getCluster_id();
        this.project_id = obj.getProject_id();
    }

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


    public String getPercentageUnitSold() {
        return percentageUnitSold;
    }

    public void setPercentageUnitSold(String percentageUnitSold) {
        this.percentageUnitSold = percentageUnitSold;
    }

    public String getTotal_unit() {
        return total_unit;
    }

    public void setTotal_unit(String total_unit) {
        this.total_unit = total_unit;
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





}
