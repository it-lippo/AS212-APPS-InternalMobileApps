package com.itams.visionet.customerserviceapps.model;

import com.itams.visionet.customerserviceapps.database.diagramaticClusterRealmObject;

/**
 * Created by Administrator on 24/11/2016.
 */

public class diagramaticClusterModel {
    private String cluster_id;
    private String cluster;
    private String project_id;

    public diagramaticClusterModel(diagramaticClusterRealmObject obj) {
        this.cluster_id = obj.getCluster_id();
        this.cluster = obj.getCluster();
        this.project_id = obj.getProject_id();
    }

    public diagramaticClusterModel(){}

    public String getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String toString()
    {
        return this.cluster;
    }
}
