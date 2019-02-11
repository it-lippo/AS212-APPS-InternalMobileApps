package com.itams.visionet.customerserviceapps.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 28/11/2016.
 */

public class roomRealmObject extends RealmObject {
    @PrimaryKey
    private String UnitNo;
    private String UnitCode;
    private String project_id;
    private String cluster_id;
    private String floor_id;
    private String UnitStatus;
    private String Coloring;
    private String Order;
    private boolean isSelected;

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

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
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
}
