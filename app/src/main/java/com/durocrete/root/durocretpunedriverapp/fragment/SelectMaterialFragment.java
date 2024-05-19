package com.durocrete.root.durocretpunedriverapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.MapActivity3;
import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.SharedPreference;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.adapter.MaterialNQuantityAdapter;
import com.durocrete.root.durocretpunedriverapp.adapter.MaterialSelectionAdapter;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.OnMarkerClickListener;
import com.durocrete.root.durocretpunedriverapp.listeners.OnMaterialChangeListener;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.CheckInOUTModel;
import com.durocrete.root.durocretpunedriverapp.model.MaterialDetailModel;
import com.durocrete.root.durocretpunedriverapp.network.RequestHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


//import com.example.root.driverappliacation.sparken.activity.MapActivity;


public class SelectMaterialFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, OnMaterialChangeListener {
    private static String TAG = SelectMaterialFragment.class.getSimpleName();
    private Spinner spinnerMaterial;
//    private Spinner spinnerQuantity;
    private EditText et_quantity;
    private ListView lvCollectedMaterial;
    private Button btnAddMaterial, btnNext, btnCheckOut;
    private ArrayList<MaterialDetailModel> materialDetailArrayList = null;
    private MaterialSelectionAdapter materialSelectionAdapter = null;
    private MaterialDetailModel materialDetailModel = new MaterialDetailModel();
    private MaterialNQuantityAdapter materialNQuantityAdapter = null;
    private ArrayList<MaterialDetailModel> addedMaterialList = null;
    private int siteId = 0;
    MyPreferenceManager sharedpref;

    private OnMarkerClickListener mOnMarkerClickListener = null;
    String[] quantitySpinnerDataArray = new String[]{"Select Quantity", "1", "2", "3", "5", "6", "7", "8", "9", "10"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_material, container, false);




        Bundle bundle = getArguments();
        if (bundle != null) {
            siteId = bundle.getInt(Constants.SITEID);
        }
        sharedpref=new MyPreferenceManager(getActivity());
        spinnerMaterial = (Spinner) view.findViewById(R.id.spinnerMaterial);
//        spinnerQuantity = (Spinner) view.findViewById(R.id.spinnerQuantity);
        et_quantity = (EditText) view.findViewById(R.id.et_quantity);
        lvCollectedMaterial = (ListView) view.findViewById(R.id.lvCollectedMaterial);
        btnAddMaterial = (Button) view.findViewById(R.id.btnAddMaterial);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnCheckOut = (Button) view.findViewById(R.id.btnCheckOut);
        materialSelectionAdapter = new MaterialSelectionAdapter(getActivity(), R.layout.row_select_route, new ArrayList<MaterialDetailModel>());
        spinnerMaterial.setAdapter(materialSelectionAdapter);
        materialNQuantityAdapter = new MaterialNQuantityAdapter(getActivity(), R.layout.row_material_selection, new ArrayList<MaterialDetailModel>(), this);
//        spinnerQuantity.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.row_select_route, quantitySpinnerDataArray));
        lvCollectedMaterial.setAdapter(materialNQuantityAdapter);
        addedMaterialList = new ArrayList<>();
        spinnerMaterial.setOnItemSelectedListener(this);
//        spinnerQuantity.setOnItemSelectedListener(this);
        btnAddMaterial.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnCheckOut.setOnClickListener(this);


        makeGetMaterialRequest();

        return view;
    }


    private void makeGetMaterialRequest() {
        RequestHandler.makeWebservice(true, getActivity(), Request.Method.GET, URLS.getInstance().getMaterialList, null, MaterialDetailModel[].class, new VolleyResponseListener<MaterialDetailModel>() {
            @Override
            public void onResponse(MaterialDetailModel[] object) {
                if (object[0] instanceof MaterialDetailModel) {
                    materialDetailArrayList = new ArrayList<MaterialDetailModel>();
                    MaterialDetailModel materialDetailModel = (MaterialDetailModel) object[0];
                    Log.v(TAG, materialDetailModel.toString());

                    for (MaterialDetailModel materialDetail : object) {
                        materialDetailArrayList.add(materialDetail);
                    }
                    materialSelectionAdapter.setArray(materialDetailArrayList);
//                    spinnerMaterial.setAdapter(materialSelectionAdapter);
                    /*set adapter to Quentity Spinner also*/
//                    spinnerQuantity.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, quantitySpinnerDataArray));
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        switch (parent.getId()) {
            case R.id.spinnerMaterial:
                Log.v(TAG, "spinnerMaterial is clicked : " + materialDetailArrayList.get(position).getMaterialName());
                materialDetailModel.setMaterialName(materialDetailArrayList.get(position).getMaterialName());
                materialDetailModel.setMaterialId(materialDetailArrayList.get(position).getMaterialId());
                break;
//            case R.id.spinnerQuantity:
//                Log.v(TAG, "spinnerQuantity is clicked : " + new ArrayList<String>(Arrays.asList(quantitySpinnerDataArray)).get(position));
//                materialDetailModel.setMaterialQuantity(new ArrayList<String>(Arrays.asList(quantitySpinnerDataArray)).get(position));
//                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddMaterial:

                Log.v(TAG, "btnAddMaterial is clicked MaterialID : " + materialDetailModel.getMaterialQuantity());
                Log.v("AAA", "" + materialDetailModel.getMaterialQuantity().startsWith("Select"));
                Log.v("AAA", "" + materialDetailModel.getMaterialQuantity().contains("Select"));
                Log.v("AAA", "" + materialDetailModel.getMaterialQuantity().contentEquals("Select"));
                    materialDetailModel.setMaterialQuantity(et_quantity.getText().toString());

                if (materialDetailModel != null /*&& !materialDetailModel.getMaterialQuantity().trim().equals("") && !materialDetailModel.getMaterialName().trim().equals("")*/) {
                    /*if (!materialDetailModel.getMaterialName().startsWith("Select")
                            && !materialDetailModel.getMaterialQuantity().startsWith("Select")) {*/
                    if (!materialDetailModel.getMaterialName().startsWith("Select")) {
                        if (!materialDetailModel.getMaterialQuantity().isEmpty()) {
                            Log.v(TAG, "removeDuplicateMaterial : " + removeDuplicateMaterial(materialDetailModel));
                           /* if(addedMaterialList.size() == 0) {
                                addedMaterialList.add(materialDetailModel);
                                Log.v(TAG, "addedMaterialList sizevd : " + addedMaterialList.size());
                                materialNQuantityAdapter.setArray(addedMaterialList);
                                materialDetailModel = new MaterialDetailModel();
                            }else {
                                for (int i = 0; i < addedMaterialList.size(); i++) {
                                    if (addedMaterialList.contains(materialDetailModel)) {
                                        Toast.makeText(getActivity(), " Material is already present:", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else {
                                        Log.v(TAG, "addedMaterialList size : " + addedMaterialList.size());
                                        addedMaterialList.add(materialDetailModel);
                                        materialNQuantityAdapter.setArray(addedMaterialList);
                                        materialDetailModel = new MaterialDetailModel();
                                    }
                                }
                                *//*for (int i = 0; i < addedMaterialList.size(); i++) {
                                    if (materialDetailModel.getMaterialId().equals(addedMaterialList.get(i).getMaterialId())) {
                                        Toast.makeText(getActivity(), " Material is already present:", Toast.LENGTH_SHORT).show();
                                    } else {
                                        addedMaterialList.contains(materialDetailModel);
                                        Log.v(TAG, "addedMaterialList size : " + addedMaterialList.size());
                                        materialNQuantityAdapter.setArray(addedMaterialList);
                                        materialDetailModel = new MaterialDetailModel();
                                    }
                                }*//*
                            }*/
                            if (!removeDuplicateMaterial(materialDetailModel)) {
                                materialDetailModel.setMaterialQuantity(et_quantity.getText().toString());
                                addedMaterialList.add(materialDetailModel);
                                Log.v(TAG, "addedMaterialList size : " + addedMaterialList.size());
                                materialNQuantityAdapter.setArray(addedMaterialList);
                                materialDetailModel = new MaterialDetailModel();
                                spinnerMaterial.setSelection(0);
//                                spinnerQuantity.setSelection(0);
                                et_quantity.setText("");

                            } else {
                                Toast.makeText(getActivity(), " Material is already present.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Enter Quantity ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Select Material ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Select Material and Enter Quantity first...", Toast.LENGTH_SHORT).show();
                }
                /*} else {
                    Toast.makeText(getActivity(), "Select Next Material Yype", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.btnNext:
                Log.v(TAG, "btnAddMaterial is clicked" + addedMaterialList.size());
                if (addedMaterialList.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.SITEID, siteId);
                    bundle.putSerializable(Constants.SELECTED_MATERIAL_ARRAY_LIST, addedMaterialList);
                    Fragment checkOutFragment = new CheckOutFragment();
//                getActivity().getSupportFragmentManager().beginTransaction().remove(SelectMaterialFragment.class).commit();
                    checkOutFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, checkOutFragment).addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity(), "Please Select Both Material & Quantity", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnCheckOut:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Are you Sure you want to Check Out ?")

                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        makeCheckOutRequest();
                                    }
                                })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
        }

    }

    @Override
    public void onChangeAdapterView(boolean isChanged, MaterialDetailModel position) {
        if (isChanged) {
            Log.v(TAG, " deleted material position : " + position);
            addedMaterialList.remove(position);
            materialNQuantityAdapter.setArray(addedMaterialList);
        }
    }

    private boolean removeDuplicateMaterial(MaterialDetailModel materialDetailModel) {
        if(materialDetailModel != null) {
            int count=0;
            for (int i=0;i<addedMaterialList.size();i++) {
                if (materialDetailModel.getMaterialId().equalsIgnoreCase(addedMaterialList.get(i).getMaterialId())) {
                    count++;
                }
            }
            if (count>0) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
    /*private boolean removeDuplicateMaterial(MaterialDetailModel materialDetailModel) {
        Log.v(TAG, "addedMaterialList : " + addedMaterialList.size());
        if (materialDetailModel != null) {
         *//*   for (MaterialDetailModel detailModel : addedMaterialList) {
                if (detailModel.getMaterialId().equalsIgnoreCase(materialDetailModel.getMaterialId())) {
                    return true;
                } else {
                    return false;
                }
            }*//*
            for (int i = 0; i < addedMaterialList.size(); i++) {
                if (materialDetailModel.getMaterialId().equals(addedMaterialList.get(i).getMaterialId())) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }*/

    private void makeCheckOutRequest() {
        HashMap<String, String> param = new HashMap<>();
        param.put("siteId", String.valueOf(siteId));
        param.put("driverId", SharedPreference.getInstanceProfileData(getActivity()).getUserId());
        param.put("materialId", "");
        param.put("quantity", "");
        param.put("workStatus", "");
        param.put("sign", "");
        param.put("remark", "");
        param.put("enquiry_id", "");


        Log.v(TAG, "siteId : " + siteId);
        Log.v(TAG, "driverId : " + "26");
        Log.v(TAG, "materialId :" + "");
        Log.v(TAG, "quantity : " + "");
        Log.v(TAG, "workStatus : " + "");
        Log.v(TAG, "sign : " + "");

        RequestHandler.makeWebservice(true, getActivity(), Request.Method.POST, URLS.getInstance().getCheckOut,
                param, CheckInOUTModel[].class, new VolleyResponseListener<MaterialDetailModel>() {
                    @Override
                    public void onResponse(MaterialDetailModel[] object) {
                        if (object[0] instanceof MaterialDetailModel) {
                            MaterialDetailModel MaterialDetailModel = (MaterialDetailModel) object[0];
                            sharedpref.setBooleanPreferences(MyPreferenceManager.check_out_bit, false);
                            sharedpref.setBooleanPreferences(MyPreferenceManager.done_bit, false);
                            Log.v(TAG, " onResponse8 :" + MaterialDetailModel.getMaterialName());
                            sendIntent();
                          /*  Bundle bundle = new Bundle();
                            bundle.putInt(Constants.CHECK_OUT_SITE_ID, siteId);
                            Fragment mapFragment = new MapActivity();
                            mapFragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).addToBackStack(TAG).commit();*/
                        }
                    }


                    @Override
                    public void onError(String message) {
                        Utility.errorDialog(message, getActivity());
                        sharedpref.setBooleanPreferences(MyPreferenceManager.check_out_bit, true);
                    }
                }
        );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Select Material");
    }

    private void sendIntent() {
        Log.v(TAG, "CheckInId : " + SharedPreference.getInstanceProfileData(getActivity()).getCheckIn());
        if (SharedPreference.getInstanceProfileData(getActivity()).getCheckIn().equals("1")) {
            getActivity().finish();
        } else {
            Intent intent = new Intent(getActivity(), MapActivity3.class);
            intent.putExtra(Constants.CHECK_OUT_SITE_ID, siteId);
            getActivity().startActivity(intent);
        }

    }
}

