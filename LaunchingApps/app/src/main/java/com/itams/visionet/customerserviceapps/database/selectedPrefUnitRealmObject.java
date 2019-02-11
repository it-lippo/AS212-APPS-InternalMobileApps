package com.itams.visionet.customerserviceapps.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alde.asprilla on 02/02/2017.
 */

public class selectedPrefUnitRealmObject extends RealmObject{
    @PrimaryKey
    private String UnitNo;
    private String UnitCode;
    private int Order;

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