package com.durocrete.root.durocretpunedriverapp.model;

/**
 * Enhanced to include rejection reason and remark.
 */
public class CheckInOUTModel {
    private String result = "";
    private String rejectionReason;
    private String remark;
    private String refrence_no;
    private String recieved_by;
    private String designation;
    private String contact_number;
    private String contact_name;
    private String signature;
    private String captureImage;
    private String status;
    private String flag;


    private String message;

    // Existing methods and fields remain unchanged

    // New methods for the additional fields
    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRefrence_no() {
        return refrence_no;
    }

    public void setReferenceNo(String refrence_no) {
        this.refrence_no = refrence_no;
    }

    public String getRecieved_by() {
        return recieved_by;
    }

    public void setRecieved_by(String recieved_by) {
        this.recieved_by = recieved_by;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContactName(String contactName) {
        this.contact_name = contact_name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCaptureImage() {
        return captureImage;
    }

    public void setCaptureImage(String captureImage) {
        this.captureImage = captureImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }



    @Override
    public String toString() {
        return "CheckInOUTModel{" +
                "result='" + result + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", remark='" + remark + '\'' +
                ", refrence_no='" + refrence_no + '\'' +
                ", recieved_by='" + recieved_by + '\'' +
                ", designation='" + designation + '\'' +
                ", contact_number='" + contact_number + '\'' +
                ", contact_name='" + contact_name + '\'' +
                ", signature='" + signature + '\'' +
                ", captureImage='" + captureImage + '\'' +
                ", status='" + status + '\'' +
                ", flag='" + flag + '\'' +
                ", message='" + message + '\'' +


        '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
