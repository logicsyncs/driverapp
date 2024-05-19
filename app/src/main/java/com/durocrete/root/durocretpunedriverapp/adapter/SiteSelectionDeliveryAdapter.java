package com.durocrete.root.durocretpunedriverapp.adapter;

import static com.durocrete.root.durocretpunedriverapp.Utillity.Utility.isUseAppStorage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.durocrete.root.durocretpunedriverapp.MainActivity;
import com.durocrete.root.durocretpunedriverapp.PDFViewerActivity;
import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.SharedPreference;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.model.FileDownloader1;
import com.durocrete.root.durocretpunedriverapp.model.SiteDetailModel;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SiteSelectionDeliveryAdapter extends RecyclerView.Adapter<SiteSelectionDeliveryAdapter.SelectedSidesHolder>{

        private String TAG = SiteSelectionDeliveryAdapter.class.getSimpleName();
        private Activity mContext = null;
        private ArrayList<SiteDetailModel> objects = null;
        MyPreferenceManager sharedpref;


        public SiteSelectionDeliveryAdapter(Activity context, ArrayList<SiteDetailModel> arrayListSides) {
            this.mContext = context;
            this.objects = arrayListSides;

        }

        public void setArray(ArrayList<SiteDetailModel> arrayList) {
            objects.addAll(arrayList);
            this.notifyDataSetChanged();
        }

        public void clearAdapter() {
            this.objects.clear();
            this.notifyDataSetChanged();
        }


        @Override
        public void onBindViewHolder(final SiteSelectionDeliveryAdapter.SelectedSidesHolder holder, final int position) {
            final SiteDetailModel siteObject = objects.get(position);
            holder.txtSideName.setText(siteObject.getSiteName());
            holder.txtClientName.setText(siteObject.getClientName());
            holder.txtenquiryid.setText(siteObject.getEnquiry_id());
            holder.checkBoxSideSelected.setChecked(siteObject.isChecked());
            holder.txtcollectiondate.setText(siteObject.getCollection_date());
            holder.txtMaterial.setText(siteObject.getMaterialName());
            holder.txtTestreqForm.setText("Download");
            holder.txtTestreqForm.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            holder.txtlink.setText(siteObject.getPath());


            if (!(siteObject.getSiteLatitude() == 0) && !(siteObject.getSiteLongitude() == 0)) {
//            holder.llTextView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_orange));
                siteObject.setLatLongPresent(true);
            } else {
//            holder.llTextView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_blue));
                siteObject.setLatLongPresent(false);
            }

//        if (position == 0) {
//            holder.imgSiteDetails.setVisibility(View.GONE);
//        } else {
//            holder.imgSiteDetails.setVisibility(View.VISIBLE);
//        }
            String abc= String.valueOf(sharedpref.getIntPreferences(MyPreferenceManager.Siteid));

            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date start = null;
                start = new Date();
                Log.e("start", String.valueOf(start));
                Date end = null;
                // end = dateFormat.parse("2019/07/04");//((siteObject.getCollection_date());

                end = dateFormat.parse((siteObject.getCollection_date()));

                Log.e("end", String.valueOf(end));
                if (end.after(start)) {
                    holder.txtClientName.setTextColor(Color.RED);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (sharedpref.getBooleanPreferences(MyPreferenceManager.done_bit) && abc.equalsIgnoreCase(String.valueOf(siteObject.getSiteId())))
            {
                holder.llTextView.setBackgroundColor(Color.CYAN);

            }
            else
            {
                holder.llTextView.setBackgroundColor(Color.WHITE);
            }

            holder.llTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String abc= String.valueOf(sharedpref.getIntPreferences(MyPreferenceManager.Siteid));

                    if (!sharedpref.getBooleanPreferences(MyPreferenceManager.done_bit) || abc.equalsIgnoreCase(String.valueOf(siteObject.getSiteId()))) {
                        sharedpref.setIntPreferences(MyPreferenceManager.Siteid, siteObject.getSiteId());
                        sharedpref.setStringPreferences(MyPreferenceManager.Enquiry_id, siteObject.getEnquiry_id());
                        Intent intent = new Intent(mContext, MainActivity.class);
                        SharedPreference.getInstanceProfileData(mContext).setCheckIn("1");
                        intent.putExtra(Constants.SITEDETAILMODEL, siteObject);
                        mContext.startActivity(intent);

                    } else {
                        Toast.makeText(mContext, "Please Check Out First", Toast.LENGTH_SHORT).show();
                    }


                    Log.v(TAG, " llTextView is Clicked");
                /*Bundle bundle = new Bundle();
                bundle.putInt(Constants.SITEID,siteObject.getSiteId());
                CheckInActivity checkI  nFragment = new CheckInActivity();
                mContext.getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).addToBackStack(TAG).commit();*/

               /* FragmentManager fragmentManager = mContext.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.SITEID,siteObject.getSiteId());
                CheckInActivity checkInFragment = new CheckInActivity();
                checkInFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, checkInFragment);
                fragmentTransaction.commit();*/

                    Log.v(TAG, " siteDetailModel : " + siteObject.toString());
//
//                if (position == 0) {
//
//                } else  {


//                }


                }
            });


            if (position == 0) {
                holder.checkBoxSideSelected.setChecked(true);
                holder.checkBoxSideSelected.setSelected(false);
                Log.v(TAG, " checkBoxSideSelected : " + holder.checkBoxSideSelected.isChecked());
            }


            holder.checkBoxSideSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkBox = (CheckBox) view;
                    if (position == 0) {
                        checkBox.setChecked(false);
                        checkBox.setSelected(false);
                    }

                    Log.v(TAG, " CheckBox button is checked : " + objects.get(position).getSiteName());
                    if (checkBox.isChecked()) {
                        siteObject.setChecked(true);
                    } else {
                        siteObject.setChecked(false);
                    }
                }
            });

//            holder.txtTestreqForm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int indext = holder.txtlink.getText().toString().trim().lastIndexOf('/');
//                    final String name = holder.txtlink.getText().toString().trim().substring(indext + 1);
//                    makeDirectory(view.getContext(), holder.txtlink.getText().toString().trim(), name);
//
//                }
//            });


        }
        private void makeDirectory(Context context, String url, String name) {
            Log.e("FileName",name);

            File folderDownload = isUseAppStorage?context.getFilesDir():new File(Environment.getExternalStorageDirectory()+File.separator+"Downloads"+File.separator+"Durocrete");
            folderDownload.mkdir();


            File directory = new File(folderDownload, name);
            Log.v("www directory ", directory.toString());

            if (directory.exists() && directory.toString().contains("pdf") && directory.length() > 0) {
                Log.v("www", "pdf");
                Intent viewPDF= new Intent(context, PDFViewerActivity.class);
                viewPDF.putExtra("filePath",directory.getAbsolutePath());
                context.startActivity(viewPDF);
                Log.v("www", "pdf");

                Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_LONG).show();

            } else {
                Log.v("www", "is not presenet");
                new FileDownloader1((Activity) context, directory, folderDownload).execute(url);
            }


        }
        @Override
        public int getItemCount() {
            return objects.size();
        }

        public ArrayList<SiteDetailModel> getSideObjectArrayList() {
            return objects;
        }





    @Override
    public SelectedSidesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_show_sides, parent, false);

        return new SelectedSidesHolder(rowView);
    }


        /*Create Holder Class for adapter*/


        public class SelectedSidesHolder extends RecyclerView.ViewHolder {
            public TextView txtSideName, txtClientName, txtenquiryid, txtcollectiondate, txtMaterial, txtTestreqForm, txtlink;
            private CheckBox checkBoxSideSelected;
            LinearLayout llTextView,material1,collectiondate,testReqForm;
            private ImageView imgSiteDetails;

            public SelectedSidesHolder(View view) {
                super(view);
                llTextView = (LinearLayout) view.findViewById(R.id.llTextView);
                txtSideName = (TextView) view.findViewById(R.id.txtSideName);
                txtClientName = (TextView) view.findViewById(R.id.txtClientName);
                txtenquiryid = (TextView) view.findViewById(R.id.txtenquiryid);
                checkBoxSideSelected = (CheckBox) view.findViewById(R.id.checkBoxSideSelected);
                imgSiteDetails = (ImageView) view.findViewById(R.id.imgSiteDetails);
                sharedpref = new MyPreferenceManager(mContext);

                // Hide the fields here
                txtcollectiondate = (TextView) view.findViewById(R.id.txtcollectiondate);
                txtcollectiondate.setVisibility(View.GONE);
                txtMaterial = (TextView) view.findViewById(R.id.txtMaterial);
                txtMaterial.setVisibility(View.GONE);
                txtTestreqForm = view.findViewById(R.id.txtTestReqForm);
                txtTestreqForm.setVisibility(View.GONE);
                txtlink = view.findViewById(R.id.txtlink);
                material1=(LinearLayout)view.findViewById(R.id.material1);
                material1.setVisibility(View.GONE);
                collectiondate=(LinearLayout)view.findViewById(R.id.collectiondate);
                collectiondate.setVisibility(View.GONE);
                testReqForm=(LinearLayout)view.findViewById(R.id.testReqForm);
                testReqForm.setVisibility(View.GONE);
            }
        }


        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
    }

