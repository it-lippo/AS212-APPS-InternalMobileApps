package com.itams.visionet.customerserviceapps.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alde.asprilla on 02/12/2016.
 */

public class diagramaticProjectRealmObject extends RealmObject {
    @PrimaryKey
    private String project_id;
    private String project;

    public diagramaticProjectRealmObject(){}

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
