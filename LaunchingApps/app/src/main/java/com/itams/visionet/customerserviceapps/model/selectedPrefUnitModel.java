package com.itams.visionet.customerserviceapps.model;

import android.util.Log;

import com.itams.visionet.customerserviceapps.database.selectedPrefUnitRealmObject;

/**
 * Created by alde.asprilla on 02/02/2017.
 */

public class selectedPrefUnitModel {
    private String UnitNo;
    private String UnitCode;
    private int Order;

    public selectedPrefUnitModel(roomModel obj){
        this.UnitNo = obj.getUnitNo();
        this.UnitCode = obj.getUnitCode();
        this.Order = Integer.parseInt(obj.getOrder());
    }

    public selectedPrefUnitModel(selectedPrefUnitRealmObject obj){
        this.UnitNo = obj.getUnitNo();
        this.UnitCode = obj.getUnitCode();
        this.Order = obj.getOrder();
    }


    @Override
    public boolean equals(Object obj) {
        super.equals(obj);
        if(obj instanceof  roomModel){
            selectedPrefUnitModel model = new selectedPrefUnitModel((roomModel) obj);
            Log.i("modelpref",this.getUnitNo() +"" + model.getUnitNo());
            if(this.getUnitNo().equals(model.getUnitNo())){
                return true;
            }else {
                Log.i("modelpref else equals",this.getUnitNo() +"" + model.getUnitNo());
                return false;
            }
        }else {
            Log.i("modelpref else instance",this.getUnitNo());
            return false;
        }


    }

    public selectedPrefUnitModel(){}

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

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }
}