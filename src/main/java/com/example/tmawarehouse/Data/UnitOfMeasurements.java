package com.example.tmawarehouse.Data;

public class UnitOfMeasurements {
    private int unitID;
    private String unitName;

    public UnitOfMeasurements(int unitID, String unitName) {
        this.unitID = unitID;
        this.unitName = unitName;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
