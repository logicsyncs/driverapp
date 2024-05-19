package com.durocrete.root.durocretpunedriverapp.model;

/**
 * Created by root on 19/5/17.
 */
public class RouteDetailModel {
    private String TAG= RouteDetailModel.class.getSimpleName();
    private String routeId = "";
    private String routeName ="";
    private String routeStatus = "";


    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(String routeStatus) {
        this.routeStatus = routeStatus;
    }

    @Override
    public String toString() {
        return String.format(TAG ,"routeId : " + routeId + "routeName : " + routeName + " routeStatus : " + routeStatus);
    }
}
