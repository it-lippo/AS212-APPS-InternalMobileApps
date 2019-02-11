package com.itams.visionet.customerserviceapps.model;

import com.itams.visionet.customerserviceapps.database.diagramaticProjectRealmObject;

/**
 * Created by alde.asprilla on 02/12/2016.
 */

public class diagramaticProjectModel {
    private String project_id;
    private String project;

    public diagramaticProjectModel(diagramaticProjectRealmObject obj){
        this.project_id = obj.getProject_id();
        this.project = obj.getProject();
    }

    public diagramaticProjectModel(){}

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

    public String toString()
    {
        return this.project;
    }

    @Override
    public boolean equals(Object obj) {
        boolean sameSame = false;

        if (obj != null && obj instanceof diagramaticProjectModel)
        {
            sameSame = this.project_id.equals(((diagramaticProjectModel) obj).getProject_id()) && this.project.equals(((diagramaticProjectModel) obj).getProject());
        }

        return sameSame;
    }
}
