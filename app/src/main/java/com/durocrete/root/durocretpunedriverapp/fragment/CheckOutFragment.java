package com.durocrete.root.durocretpunedriverapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.MapActivity3;
import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.SharedPreference;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.CheckInOUTModel;
import com.durocrete.root.durocretpunedriverapp.model.MaterialDetailModel;
import com.durocrete.root.durocretpunedriverapp.model.SiteDetailModel;
import com.durocrete.root.durocretpunedriverapp.network.RequestHandler;
import com.durocrete.root.durocretpunedriverapp.reports.Reportfragment;
import com.kyanogen.signatureview.SignatureView;

import java.util.ArrayList;
import java.util.HashMap;


//import com.example.root.driverappliacation.sparken.activity.MapActivity;

public class CheckOutFragment extends Fragment {
    private static String TAG = CheckOutFragment.class.getSimpleName();
    private ArrayList<MaterialDetailModel> materialList = null;
    private RadioGroup radioGroupWorkingStatus;
    private SignatureView signatureView;
    private Button btnSubmit;
    private Button btnclear;
    private Button btnreport;
    private Button btnbill;
    private TextView enquiry_id ,clientname;
    private int siteId = 0;
    private EditText reason;
    MyPreferenceManager sharedpref;
    private SiteDetailModel siteDetailModel=null;
    String value="";
    private String emptysign ="/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAJYA4oDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD+/iiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAC";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle1) {
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);

        if (getActivity().getIntent().getSerializableExtra(Constants.SITEDETAILMODEL) != null) {
            siteDetailModel = (SiteDetailModel) getActivity().getIntent().getSerializableExtra(Constants.SITEDETAILMODEL);
        }

        Bundle bundle = getArguments();



        if (bundle != null) {
            siteId = bundle.getInt(Constants.SITEID);
            materialList = (ArrayList<MaterialDetailModel>) bundle.getSerializable(Constants.SELECTED_MATERIAL_ARRAY_LIST);
        }

        radioGroupWorkingStatus = (RadioGroup) view.findViewById(R.id.radioGroupWorkingStatus);
        //   signatureView = (SignatureView) view.findViewById(R.id.signatureView);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnclear = (Button) view.findViewById(R.id.btnclear);
        reason = (EditText) view.findViewById(R.id.reason);
        btnreport = (Button) view.findViewById(R.id.btnreport);
        btnbill=(Button)view.findViewById(R.id.btnbill) ;
        clientname=(TextView)view.findViewById(R.id.clientname);
        enquiry_id=(TextView)view.findViewById(R.id.enquiry_id);
        sharedpref=new MyPreferenceManager(getActivity());

        enquiry_id.setText(siteDetailModel.getEnquiry_id().toString().trim());
        clientname.setText(sharedpref.getStringPreferences(MyPreferenceManager.Client_name));

        btnbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment report = new Reportfragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, report).addToBackStack(null).commit();
            }
        });

        btnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment report = new Reportfragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, report).addToBackStack(null).commit();
            }
        });

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignaturePopup("abc");
            }
        });

        radioGroupWorkingStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
//                if (radioGroupWorkingStatus.getCheckedRadioButtonId() != -1) {
                String radioButtonText = ((RadioButton) getActivity().findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                if (radioButtonText.equalsIgnoreCase("Not Collected")) {
                    reason.setVisibility(View.VISIBLE);
                } else {
                    reason.setVisibility(View.GONE);
                }
//                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCheckOutRequest();
            }
        });
        return view;
    }

    private void makeCheckOutRequest() {
        ArrayList<String> materialIds = new ArrayList<>();
        ArrayList<String> materialQuantity = new ArrayList<>();



        for (int i = 0; i < materialList.size(); i++) {
            materialIds.add(materialList.get(i).getMaterialId());
            Log.v(TAG, "MaterialName  :" + materialList.get(i).getMaterialName() + " MaterialName : " + materialList.get(i).getMaterialId());
            materialQuantity.add(materialList.get(i).getMaterialQuantity());
            Log.v(TAG, " Material Quantity : " + materialList.get(i).getMaterialQuantity());
        }

        if (radioGroupWorkingStatus.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), "Please Select Working Status...", Toast.LENGTH_LONG).show();
        } else {
            String radioButtonText = ((RadioButton) getActivity().findViewById(radioGroupWorkingStatus.getCheckedRadioButtonId())).getText().toString();
            if (radioButtonText.equalsIgnoreCase("Collected")) {
                if (value==null || value.length()==0) {
                    Toast.makeText(getActivity(), "Please Put the Signature", Toast.LENGTH_SHORT).show();
                } else {
                    makerequest(materialIds, materialQuantity, radioButtonText);
                }
            } else {
                if (reason.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please Put the reason", Toast.LENGTH_SHORT).show();
                } else {
                    makerequest(materialIds, materialQuantity, radioButtonText);
                }

            }

        }
    }


    private void makerequest(ArrayList<String> materialIds, ArrayList<String> materialQuantity, String radioButtonText) {
        HashMap<String, String> param = new HashMap<>();
        param.put("siteId", String.valueOf(siteId));
        param.put("driverId", SharedPreference.getInstanceProfileData(getActivity()).getUserId());
        param.put("materialId", materialIds.toString().replaceAll("\\[|\\]", ""));
        param.put("quantity", materialQuantity.toString().replaceAll("\\[|\\]", ""));
        param.put("workStatus", radioButtonText);
        param.put("sign", value);
        param.put("remark", reason.getText().toString());
        param.put("enquiry_id", enquiry_id.getText().toString().trim());

//        RequestHandler.makeWebservice(true, getActivity(), Request.Method.POST,
//                URLS.getInstance().getCheckOut, param, CheckInOUTModel[].class,
//                new VolleyResponseListener<CheckInOUTModel>() {
//                    @Override
//                    public void onResponse(CheckInOUTModel[] object) {
//                        if (object[0] instanceof CheckInOUTModel) {
//                            CheckInOUTModel checkInOUTModel = (CheckInOUTModel) object[0];
//                            Log.v(TAG, " onResponse8 :" + checkInOUTModel.getResult());
//                            sharedpref.setBooleanPreferences(MyPreferenceManager.check_out_bit, false);
//                            sharedpref.setBooleanPreferences(MyPreferenceManager.done_bit, false);
//                            Toast.makeText(getActivity(), "Material Status Submitted Successfully.", Toast.LENGTH_LONG).show();
//                            sendIntent();
////                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////                                builder.setMessage("You Can Carry next Pick Up Point...");
////                                builder.setCancelable(false);
////                                builder.setPositiveButton("Accepted", new DialogInterface.OnClickListener() {
////                                    @Override
////                                    public void onClick(DialogInterface dialogInterface, int id) {
////                                       /* Bundle bundle = new Bundle();
////                                        bundle.putInt(Constants.CHECK_OUT_SITE_ID, siteId);
////                                        bundle.putString("","");
////                                        Fragment mapFragment = new MapActivity();
////                                        mapFragment.setArguments(bundle);
////                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).addToBackStack(TAG).commit();*/
////                                        sendIntent(true);
////                                    }
////                                });
////
////                                builder.setNegativeButton("Rejected", new DialogInterface.OnClickListener() {
////                                    @Override
////                                    public void onClick(DialogInterface dialogInterface, int id) {
////                                        Toast.makeText(getActivity(), "You rejected next Pick Up point...", Toast.LENGTH_SHORT).show();
////                                        sendIntent(false);
////                                    }
////                                });
////                                builder.show();
////                            }
//
//                        }
//                    }
//
//
//                    @Override
//                    public void onError(String message) {
//                        Utility.errorDialog(message, getActivity());
//                        sharedpref.setBooleanPreferences(MyPreferenceManager.check_out_bit, true);
//
//                    }
//                }
//        );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Check Out");
    }


    private void sendIntent() {
        Log.v(TAG, "" + SharedPreference.getInstanceProfileData(getActivity()).getCheckIn());
        if (SharedPreference.getInstanceProfileData(getActivity()).getCheckIn().equals("1")) {
            getActivity().finish();

        } else {
            /*Bundle bundle = new Bundle();
            bundle.putInt(Constants.CHECK_OUT_SITE_ID, siteId);
//            bundle.putBoolean(Constants.NEXT_PICK_UP_POINT_STATUS, isChecked);
            Fragment mapFragment = new MapActivity();
            mapFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).addToBackStack(TAG).commit();*/
            Intent intent = new Intent(getActivity(), MapActivity3.class);
            intent.putExtra(Constants.CHECK_OUT_SITE_ID, siteId);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

    }


    private void SignaturePopup(final String sigper_name) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.signaturepopup);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        signatureView = (SignatureView) dialog.findViewById(R.id.witnessSignaturePad);
        Button save = (Button) dialog.findViewById(R.id.btWitnessSave);
        Button Clear = (Button) dialog.findViewById(R.id.btWitnessClear);


        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearCanvas();
                value="";
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //value = Utility.getEncoded64ImageStringFromBitmap(signatureView.getSignatureBitmap(),contactName);
                Log.d("tag",value);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

}

