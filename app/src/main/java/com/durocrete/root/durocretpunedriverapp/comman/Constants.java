package com.durocrete.root.durocretpunedriverapp.comman;

import java.util.ArrayList;

/**
 * Created by root on 16/5/17.
 */
public class Constants {
    /*key for SharedPreference*/
    public static final String SH_SHARED_PREF  ="sharedPreferenceData";
    public static final String KEY_SP_PICK_UP_POINT_DATA= "pickUPPointsDataList";

    public static final String RESPONSE_KEY = "result";
    public static final String RESPONSE_MESSAGE = "message";
    public static final int RESPONSE_SUCCESS = 1;
    public static final int RESPONSE_SUCCESS2 = 2;
    public static final int RESPONSE_ERROR = 0;
    public static final String RESPONSE_INFO = "detail";
    public static final String SELECTED_SIDE_ARRAY_LIST="selectedSideArrayList";
    public static final String SITEID = "siteIn";
    public static final String SITEDETAILMODEL = "siteDetailModel";
    public static final String SELECTED_MATERIAL_ARRAY_LIST="selectedMaterialArrayList";
    public static final String CHECK_OUT_SITE_ID = "checkOutId";
    public static final String NEXT_PICK_UP_POINT_STATUS = "nextPickUpPointStatus";
    public static final String SELECTED_ROUTE_ID = "selectedRouteID";


    public static final int REQ_CODE_QUESTION_SELECTION = 201;
    public static final int storageRequestPermissionCode = 101;
    public static final int storageRequestPermissionCodeForQ = 108;

    public static final int IMAGE_EDIT = 102;
    public static int imageCount = 1;

    public static final int totalImageCount = 2;

    public static final int GALLERY_REQUEST = 100;
    public static final int CAMERA_REQUEST = 101;
    public static final int PERMISSION_REQUEST_CODE = 105;


    public static boolean IS_DATA_UPLOAD = false;
    public static String OBERVATION_ANS = "ObsAns";


    public static ArrayList<String> answeredList = new ArrayList<>();


    public static boolean IS_DATA_UPLOAD_SUCCESS = false;

}




