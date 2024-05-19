package com.durocrete.root.durocretpunedriverapp.listeners;


import java.util.HashMap;
import java.util.List;

import com.durocrete.root.durocretpunedriverapp.model.SiteDetailModel;


public interface OnMapUIUpdateListener {
    public void onTaskCompleted(List<List<HashMap<String, String>>> list, int colorCode, SiteDetailModel siteDetailModel);
}
