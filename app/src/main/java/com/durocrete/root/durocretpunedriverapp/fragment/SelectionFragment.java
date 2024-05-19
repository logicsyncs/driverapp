package com.durocrete.root.durocretpunedriverapp.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.model.SiteDetailModel;
import com.durocrete.root.durocretpunedriverapp.reports.Reportfragment;


public class SelectionFragment extends Fragment {


    private Button btnmaterial_reports;
    private Button btn_reports_bills;
    private int siteId = 0;
    MyPreferenceManager preferenceManager;
    SiteDetailModel siteDetailModel;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection, container, false);

        Initview(view);

        // Add site object (13 july 2019)
        if (getActivity().getIntent().getSerializableExtra(Constants.SITEDETAILMODEL) != null) {
            siteDetailModel = (SiteDetailModel) getActivity().getIntent().getSerializableExtra(Constants.SITEDETAILMODEL);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
//            siteId = bundle.getInt(Constants.SITEID);
            siteId = siteDetailModel.getSiteId();


        }

        btnmaterial_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.SITEID, siteId);
                Fragment selectMaterialFragment = new SelectMaterialFragment();
                selectMaterialFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, selectMaterialFragment).addToBackStack(null).commit();
            }
        });

        btn_reports_bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment selectMaterialFragment = new Reportfragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, selectMaterialFragment).addToBackStack(null).commit();
            }
        });



        return view;
    }



    private void Initview(View view) {
        btnmaterial_reports=(Button)view.findViewById(R.id.btnmaterial_reports);
        btn_reports_bills=(Button)view.findViewById(R.id.btn_reports_bills);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Selection");
    }


}

