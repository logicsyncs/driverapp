package com.durocrete.root.durocretpunedriverapp.model;

/**
 * Created by root on 12/12/17.
 */


public class Report {

    String report;
    boolean ischecked;
    String present;
    private String reference;
    private boolean isChecked;
    private boolean isSelected;


    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public boolean getIsChecked() {
        return this.ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public boolean ischecked(boolean b) {
        return ischecked;
    }

//    public void setImagesList(String join) {
//    }
//
//    public void setSelected(boolean b) {
//    }


    // Constructor, other fields, and methods

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setImagesList(String join) {
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}

