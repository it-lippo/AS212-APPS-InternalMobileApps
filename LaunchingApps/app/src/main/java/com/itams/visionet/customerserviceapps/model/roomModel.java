package com.itams.visionet.customerserviceapps.model;

import com.itams.visionet.customerserviceapps.database.roomRealmObject;

/**
 * Created by Administrator on 28/11/2016.
 */

public class roomModel {
    private String project_id;
    private String cluster_id;
    private String floor_id;
    private String UnitNo;
    private String UnitCode;
    private String UnitStatus;
    private String Coloring;
    private String Order;
    private boolean isSelected;

    public roomModel(){
    }

    public roomModel(roomRealmObject obj){
        this.project_id = obj.getProject_id();
        this.cluster_id = obj.getCluster_id();
        this.floor_id = obj.getFloor_id();
        this.UnitNo = obj.getUnitNo();
        this.UnitCode = obj.getUnitCode();
        this.UnitStatus = obj.getUnitStatus();
        this.Coloring = obj.getColoring();
        this.Order = obj.getOrder();
        this.isSelected = obj.isSelected();
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
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

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getUnitCode() {
        return UnitCode;
    }

    public void setUnitCode(String unitCode) {
        UnitCode = unitCode;
    }

    public String getUnitStatus() {
        return UnitStatus;
    }

    public void setUnitStatus(String unitStatus) {
        UnitStatus = unitStatus;
    }

    public String getColoring() {
        return Coloring;
    }

    public void setColoring(String coloring) {
        Coloring = coloring;
    }

    public String getOrder() {
        return Order;
    }

    public void setOrder(String order) {
        Order = order;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof roomModel){
            roomModel object = (roomModel) obj;
            if(this.getUnitCode().equals(object.getUnitCode()) && this.getUnitNo().equals(object.getUnitNo()))
                return true;
            else
                return false;
        }else
            return false;
    }
}
