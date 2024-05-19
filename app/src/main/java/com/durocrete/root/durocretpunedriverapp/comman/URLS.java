package com.durocrete.root.durocretpunedriverapp.comman;

public class URLS {
    private static URLS urls = null;
    //private static String baseUrl = "http://210.16.103.150:83/PuneDuroCrete/";

    private static String baseUrl = "http://20.198.108.39:8080/PuneDuro/";
   // private static String baseUrl = "http://192.169.158.217:8080/MumbaiDuro/";
   // private static String baseUrl = "http://192.169.158.217:8080/NashikDuro/";

    public static URLS getInstance() {
        if (urls == null) {
            urls = new URLS();
        }
        return urls;
    }

    public String loginDriveURL = baseUrl + "sign_in_driver1.php?driverId=";

    public String getRouteList = baseUrl + "get_routes.php";
    public String getCheckIn = baseUrl + "check_in_driver.php";

    public String getMaterialList = baseUrl + "get_materials.php";

    public String getCheckOut = baseUrl + "checked_out.php";

    public String getPickUPPoints(String driverId) {
        return baseUrl + "pick_up.php?driverId=" + driverId;
    }

    /*not in use*/
//    public String getSideList = baseUrl + "get_sites.php?selectedRouteId=";
//    public String setPickUpPoints = baseUrl + "daily_pickup_allocation.php";
    public String pick_up = baseUrl + "pick_up.php?";

    //public String get_reports = baseUrl + "get_report_list_driver.php?user_id=";
    public String get_reports = baseUrl + "get_report_list_driver_New1.php?user_id=100";
    public String get_bills = baseUrl + "get_driver_bill_list_New1.php?user_id=100";
    public String submit_report = baseUrl + "submit_reports_New1.php";
    public String submit_bill = baseUrl + "submit_bills.php";
    public String driver_list = baseUrl + "get_driver_list.php";

}
