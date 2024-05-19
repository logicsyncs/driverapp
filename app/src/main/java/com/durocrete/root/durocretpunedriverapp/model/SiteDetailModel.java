package com.durocrete.root.durocretpunedriverapp.model;

import java.io.Serializable;

/**
 * Created by root on 20/5/17.
 */
public class SiteDetailModel implements Serializable {
    private int routeId = 0;
    private int siteId = 0 ;
    private String siteName = "";
    private String siteMobileNo = "";
    private int siteLocationId = 0;
    private String materialName = "";
    private String materialId = "";
    private String quantity = "";
    private String SiteContact = "";
    private String siteaddress ="";
    private double siteLatitude = 0.0;
    private double siteLongitude = 0.0;
    private boolean isChecked = false;
    private String in_time = "";
    private String out_time = "";
    private String clientName = "";
    private String distance = "";
    private String duration ="";
    private boolean isLatLongPresent = false;
    private String status;
    private String payment_mode="";
    private String enquiry_id;
    private String collection_date;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    public String getContactPersonForCollection() {
        return ContactPersonForCollection;
    }

    public void setContactPersonForCollection(String contactPersonForCollection) {
        ContactPersonForCollection = contactPersonForCollection;
    }

    public String getContactNoForCollection() {
        return ContactNoForCollection;
    }

    public void setContactNoForCollection(String contactNoForCollection) {
        ContactNoForCollection = contactNoForCollection;
    }

    private String ContactPersonForCollection;
    private String ContactNoForCollection;


    public String getCollection_date() {
        return collection_date;
    }

    public void setCollection_date(String collection_date) {
        this.collection_date = collection_date;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getEnquiry_id() {
        return enquiry_id;
    }

    public void setEnquiry_id(String enquiry_id) {
        this.enquiry_id = enquiry_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSiteaddress() {
        return siteaddress;
    }

    public void setSiteaddress(String siteaddress) {
        this.siteaddress = siteaddress;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public int getSiteLocationId() {
        return siteLocationId;
    }

    public void setSiteLatitude(double siteLatitude) {
        this.siteLatitude = siteLatitude;
    }

    public double getSiteLatitude() {
        return siteLatitude;
    }

    public void setSiteLocationId(int siteLocationId) {
        this.siteLocationId = siteLocationId;
    }

    public double getSiteLongitude() {
        return siteLongitude;
    }

    public void setSiteLongitude(double siteLongitude) {
        this.siteLongitude = siteLongitude;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getSiteMobileNo() {
        return siteMobileNo;
    }

    public void setSiteMobileNo(String siteMobileNo) {
        this.siteMobileNo = siteMobileNo;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSiteContact() {
        return SiteContact;
    }

    public void setSiteContact(String siteContact) {
        SiteContact = siteContact;
    }

    public boolean isLatLongPresent() {
        return isLatLongPresent;
    }

    public void setLatLongPresent(boolean latLongPresent) {
        isLatLongPresent = latLongPresent;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SiteDetailModel)) {
            return false;
        }
        SiteDetailModel other = (SiteDetailModel) object;

        return this.getSiteId() == other.getSiteId();
    }

    @Override
    public int hashCode() {
        return this.getSiteId();
    }

    @Override
    public String toString() {
        return "SiteDetailModel : " + " routeId : "+ routeId + " siteId :" + siteId +" siteName : " + siteName
                +" siteNo :" + siteMobileNo + " siteLocationId : " + siteLocationId + " siteLatitude : " + siteLatitude
                + " materialName : " + materialName+ " materialId : " + materialId +  " quantity : "+ quantity + " siteLongitude : "
                + siteLongitude +" isChecked : " + isChecked + " clientName : " + clientName + " distance : " + distance + " duration : " + duration  + " isLatLongPresent: " + isLatLongPresent;
    }
}
