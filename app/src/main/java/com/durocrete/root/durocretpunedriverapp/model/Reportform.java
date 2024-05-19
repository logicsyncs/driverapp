package com.durocrete.root.durocretpunedriverapp.model;

import java.util.ArrayList;

/**
 * Created by root on 12/12/17.
 */

public class Reportform {

    ArrayList reports;
    String user_id;
    String contact_person;
    String Signature;
    String contact_person_no;
    String enquiry_id;

    public String getEnquiry_id() {
        return enquiry_id;
    }

    public void setEnquiry_id(String enquiry_id) {
        this.enquiry_id = enquiry_id;
    }

    public String getContact_person_no() {
        return contact_person_no;
    }

    public void setContact_person_no(String contact_person_no) {
        this.contact_person_no = contact_person_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public ArrayList getReports() {
        return reports;
    }

    public void setReports(ArrayList reports) {
        this.reports = reports;
    }




}
