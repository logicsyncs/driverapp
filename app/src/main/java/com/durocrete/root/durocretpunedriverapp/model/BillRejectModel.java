package com.durocrete.root.durocretpunedriverapp.model;

public class BillRejectModel {
    private String rejectionReason;
    private String remark;
    private String refrence_no;
    private String status;
    private String title;
    private int value;
    private String message;

    // Constructor
    public BillRejectModel(String title, int value) {
        this.title = title;
        this.value = value;
    }

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
    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getRefrence_no() {
        return refrence_no;
    }

    public void setReferenceNo(String refrence_no) {
        this.refrence_no = refrence_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CheckInOUTModel{" +
                "rejectionReason='" + rejectionReason + '\'' +
                ", remark='" + remark + '\'' +
                ", refrence_no='" + refrence_no + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +

                '}';
    }




}
