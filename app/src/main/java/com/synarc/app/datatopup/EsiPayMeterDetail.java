package com.synarc.app.datatopup;

public class EsiPayMeterDetail {

    String meterNumber, name;

    public EsiPayMeterDetail(String meterNumber, String name) {
        this.meterNumber = meterNumber;
        this.name = name;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
