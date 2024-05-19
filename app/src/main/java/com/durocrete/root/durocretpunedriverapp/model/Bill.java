package com.durocrete.root.durocretpunedriverapp.model;

/**
 * Created by root on 12/12/17.
 */

public class Bill {

    String Bills;
    String present;
    boolean ischecked;

    public boolean getIsChecked() {
        return this.ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public String getBills() {
        return Bills;
    }

    public void setBills(String bills) {
        Bills = bills;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public boolean ischecked(boolean b) {
        return ischecked;
    }
}
