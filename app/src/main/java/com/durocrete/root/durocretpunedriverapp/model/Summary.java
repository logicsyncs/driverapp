package com.durocrete.root.durocretpunedriverapp.model;

/**
 * Created by root on 27/6/17.
 */

public class Summary {
    String clientName;
    String in_time;
    String out_time;
    String status;
    String available_time;

    public String getAvailable_time() {
        return available_time;
    }

    public void setAvailable_time(String available_time) {
        this.available_time = available_time;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "clientName='" + clientName + '\'' +
                ", in_time='" + in_time + '\'' +
                ", out_time='" + out_time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
