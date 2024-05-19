package com.durocrete.root.durocretpunedriverapp.network;

/**
 * Created by Anil Sharma and Swapnil Jadhav on 1/2/16.
 */
public interface IUrls {

    String BASE_URL = "http://210.16.103.150:83/PuneDuroCrete/salesApp/";

    String sign_in_sales = BASE_URL + "sign_in_sales.php";
    String Site_allocation = BASE_URL + "site_allocation.php";
    String Add_new_Site = BASE_URL + "add_new_site_sales.php";
    String getMaterialList = "http://210.16.103.150:83/PuneDuroCrete/get_materials.php";
    String get_client_by_siteId = BASE_URL + "get_client_details.php";
    String Update_client = BASE_URL + "update_client_by_sales.php";
    String Get_Site_Details = BASE_URL + "get_site_details.php";
    String Material_labs = BASE_URL + "material_lab.php";
    String Update_Site = BASE_URL + "update_site_by_sales.php";
    String check_in_Details = BASE_URL + "get_site_details_by_site.php";
    String Check_In = BASE_URL + "check_in_sales.php";
    String locate_location = BASE_URL + "locate_location.php";
    String Check_Out = BASE_URL + "checked_out_sales.php";
    String site_history = BASE_URL + "site_visit_log.php";
    String sales_history = BASE_URL + "site_visit_log_by_sales.php";
    String get_rcc = BASE_URL + "get_RCC.php";
    String get_Architecture = BASE_URL + "get_architecture.php";
    String Get_labs = BASE_URL + "get_labs.php";
    String GET_Material = BASE_URL + "get_materials.php";
    String Enquiry_form = BASE_URL + "enquiry_submit_sales.php";
    String Enquiry_Submit = "http://210.16.103.150:83/PuneDuroCrete/enquiry_submit.php";
    String Client_Info = "http://210.16.103.150:83/PuneDuroCrete/get_sites_byCL.php";
    String GET_Tests = "http://210.16.103.150:83/PuneDuroCrete/get_material_test.php";
    String get_enquiry_material_test = "http://210.16.103.150:83/PuneDuroCrete/get_enquiry.php";
    String Enquiry_by_site = "http://210.16.103.150:83/PuneDuroCrete/get_enquiry_bysite.php";
    String Mixmaterial_list = "http://210.16.103.150:83/PuneDuroCrete/get_material_list.php";
    String tets_request = "http://210.16.103.150:83/PuneDuroCrete/test_request_post.php";
    String get_reports = "http://210.16.103.150:83/PuneDuroCrete/get_report_list_driver.php?user_id=";
    String get_bills = "http://210.16.103.150:83/PuneDuroCrete/get_driver_bill_list.php?user_id=";
    String submit_report = "http://210.16.103.150:83/PuneDuroCrete/submit_reports.php";
    String submit_bill = "http://210.16.103.150:83/PuneDuroCrete/submit_bills.php";
}
