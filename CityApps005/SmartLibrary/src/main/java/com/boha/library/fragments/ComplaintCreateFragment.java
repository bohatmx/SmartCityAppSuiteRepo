package com.boha.library.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.MyComplaintsActivity;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ComplaintCreateFragment extends Fragment implements PageFragment {

    private ComplaintFragmentListener mListener;


    public static ComplaintCreateFragment newInstance() {
        ComplaintCreateFragment fragment = new ComplaintCreateFragment();
        Bundle args = new Bundle();
        //args.putSerializable("complaintList", response);
        fragment.setArguments(args);
        return fragment;
    }

    public ComplaintCreateFragment() {
        // Required empty public constructor
    }

    ResponseDTO response;
    View view, fab, addressLayout;
    Context ctx;
    String title;
    View handle, mainLayout;
    EditText editAddress, editComment;
    Button btnSend;
    TextView txtCount, txtTitle, txtSubTitle,
            txtGetAddress, txtComplaintType;
    List<ComplaintTypeDTO> complaintTypeList;
    ComplaintTypeDTO complaintType;
    List<String> stringList;
    Activity activity;
    View topView;
    ImageView hero;
    ProgressBar progressBar;
    int logo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            response = (ResponseDTO) getArguments().getSerializable("complaintList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_complaint, container, false);

        ctx = getActivity();
        activity = getActivity();
        setFields();
//        mListener.onLocationRequested();
        getCachedComplaintTypes();
        return view;
    }

    private void getCachedComplaintTypes() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {

                if (response.getComplaintList() != null) {
                    complaintTypeList = response.getComplaintTypeList();
                    stringList = new ArrayList<String>();
                    for (ComplaintTypeDTO x : complaintTypeList) {
                        stringList.add(x.getComplaintTypeName());
                    }
                }

            }

            @Override
            public void onError() {

            }
        });
    }

    boolean confirmLocationRequested;

    private void confirmLocation() {
        AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
        d.setTitle("Confirm Complaint Location")
                .setMessage("Are you at the site of the complaint?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmLocationRequested = true;
                        progressBar.setVisibility(View.VISIBLE);
                        mListener.onLocationRequested();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void sendComplaint() {


        if (editComment.getText().toString().isEmpty()) {
            Util.showToast(ctx, "Please enter complaint");
            return;
        }
        if (complaintType == null) {
            Util.showToast(ctx, "Please select complaint type");
            return;
        }
        if (complaintType.isLocationIsRequired()) {
            if (editAddress.getText().toString().isEmpty()) {
                Util.showToast(ctx, "Please enter address");
                return;
            }
        }
        if (complaintType.isLocationIsRequired() && location == null) {
            mListener.onLocationRequested();
            return;
        }
        RequestDTO w = new RequestDTO(RequestDTO.ADD_COMPLAINT);
        final ComplaintDTO complaint = new ComplaintDTO();
        complaint.setAddress(editAddress.getText().toString());
        complaint.setRemarks(editComment.getText().toString());

        if (complaintType.isLocationIsRequired()) {
            complaint.setLatitude(location.getLatitude());
            complaint.setLongitude(location.getLongitude());
        }
        complaint.setProfileInfo(SharedUtil.getProfile(ctx));
        complaint.getProfileInfo().setAccountList(null);
        complaint.getProfileInfo().setComplaintList(null);
        complaint.getProfileInfo().setFirstName(null);
        complaint.getProfileInfo().setLastName(null);
        complaint.getProfileInfo().setEmail(null);
        complaint.getProfileInfo().setCellNumber(null);
        complaintType.setComplaintCategory(null);
        complaint.setComplaintType(complaintType);
        complaint.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        w.setComplaint(complaint);
        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        progressBar.setVisibility(View.VISIBLE);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            if (response.isMunicipalityAccessFailed()) {
                                Util.showErrorToast(ctx,ctx.getString(R.string.unable_connect_muni));
                                return;
                            }

                            ComplaintDTO x = response.getComplaintList().get(0);
                            mListener.onComplaintAdded(x);
                        }
                    });
                }

            }

            @Override
            public void onError(final String message) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Util.showErrorToast(ctx, message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });

    }

    private void setFields() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        addressLayout = view.findViewById(R.id.CC_addressLayout);
        addressLayout.setVisibility(View.GONE);
        handle = view.findViewById(R.id.CC_handle);
        topView = view.findViewById(R.id.CC_titleLayout);
        topView.setBackgroundColor(primaryDarkColor);
        mainLayout = view.findViewById(R.id.CC_middle);
        hero = (ImageView) view.findViewById(R.id.CC_hero);
        editAddress = (EditText) view.findViewById(R.id.CC_address);
        editComment = (EditText) view.findViewById(R.id.CC_comment);
        txtComplaintType = (TextView) view.findViewById(R.id.CC_complaintType);
        txtTitle = (TextView) view.findViewById(R.id.CC_title);
        txtSubTitle = (TextView) view.findViewById(R.id.CC_subTitle);
        txtGetAddress = (TextView) view.findViewById(R.id.CC_getGeoAddress);
        fab = view.findViewById(R.id.FAB_PERSON);
        btnSend = (Button) view.findViewById(R.id.button);
        btnSend.setEnabled(false);
        btnSend.setAlpha(0.4f);
        txtCount = (TextView) view.findViewById(R.id.CC_count);
        txtTitle.setText(ctx.getString(R.string.make_complaint));
        txtSubTitle.setVisibility(View.GONE);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnSend, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        sendComplaint();
                    }
                });
            }
        });
        txtGetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtGetAddress, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        progressBar.setVisibility(View.VISIBLE);
                        txtGetAddress.setEnabled(false);
                        txtGetAddress.setAlpha(0.5f);
                        checkGPS();
                    }
                });
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(fab, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent s = new Intent(getActivity(), MyComplaintsActivity.class);
                        s.putExtra("darkColor",primaryDarkColor);
                        s.putExtra("primaryColor",primaryColor);
                        s.putExtra("logo",logo);
                        startActivity(s);
                    }
                });
            }
        });

        txtComplaintType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                mListener.onLocationRequested();
                Util.flashOnce(txtComplaintType, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Util.showPopupBasicWithHeroImage(ctx,
                                activity, stringList, handle,
                                ctx.getString(R.string.comp_types),
                                new Util.UtilPopupListener() {
                                    @Override
                                    public void onItemSelected(int index) {
                                        txtComplaintType.setText(stringList.get(index));
                                        complaintType = complaintTypeList.get(index);
                                        if (complaintType.isLocationIsRequired()) {
                                            mListener.onLocationRequested();
                                            Util.expand(addressLayout, 500, null);

                                        } else {
                                            Util.collapse(addressLayout,500,null);
                                            btnSend.setEnabled(true);
                                            btnSend.setAlpha(1.0f);
                                        }

                                    }
                                });
                    }
                });
            }
        });

        animateSomething();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ComplaintFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void animateSomething() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                        Util.expand(hero, 1000, new Util.UtilAnimationListener() {
                            @Override
                            public void onAnimationEnded() {
                                Util.flashSeveralTimes(txtComplaintType, 30, 3, null);
                            }
                        });
                    }
                });
            }
        }, 500);
    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

    public interface ComplaintFragmentListener {
        public void onFindComplaintsLikeMine(ComplaintDTO complaint);

        public void onFindComplaintsAroundMe();

        public void onComplaintAdded(ComplaintDTO complaint);

        public void onLocationRequested();
    }

    Location location;

    public void setLocation(Location location) {
        Log.w(LOG, "$$$$ setLocation, acc: " + location.getAccuracy());
        this.location = location;
        if (btnSend != null) {
            btnSend.setEnabled(true);
            btnSend.setAlpha(1.0f);
            new GeoTask().execute();
        }
    }

    class GeoTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Log.e(LOG, "### start GeoTask doInBackground");
            Geocoder geocoder = new Geocoder(ctx);
            try {
                List<Address> list = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);
                if (list != null && !list.isEmpty()) {
                    address = list.get(0);
                } else {
                    return 9;
                }
            } catch (IOException e) {
                Log.e(LOG, "Impossible to connect to Geocoder", e);
                return 9;
            }
            return 0;
        }

        @Override
        public void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            txtGetAddress.setEnabled(true);
            txtGetAddress.setAlpha(1.0f);
            if (result == 0) {
                StringBuilder sb = new StringBuilder();
                int maxIndex = address.getMaxAddressLineIndex();
                int count = maxIndex + 1;
                for (int i = 0; i < count; i++) {
                    sb.append(address.getAddressLine(i));
                    if (i < (count - 1)) {
                        sb.append(", ");
                    }
                }
                if (editAddress != null) {
                    editAddress.setText(sb.toString());
                }
            } else {
                if (editAddress != null) {
                    editAddress.setText(getString(R.string.address_not_found));
                }
            }
            if (confirmLocationRequested) {
                confirmLocationRequested = false;
                sendComplaint();
            }
        }

    }

    Address address;
    static final String LOG = ComplaintCreateFragment.class.getSimpleName();
    String pageTitle;

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public void checkGPS() {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle(ctx.getString(R.string.loc_services_not));
            builder.setMessage(ctx.getString(R.string.enable_gps));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    ctx.startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } else {
            mListener.onLocationRequested();
        }
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
