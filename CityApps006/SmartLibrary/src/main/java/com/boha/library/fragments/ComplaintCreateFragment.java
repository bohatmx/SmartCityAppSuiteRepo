package com.boha.library.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.adapters.CategoryAdapter;
import com.boha.library.adapters.SubCategoryAdapter;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.ComplaintCategoryDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.services.PhotoUploadService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.boha.library.util.WebCheck;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import static com.boha.library.util.Util.showSnackBar;

/**
 * Fragment that manages the complaint creation flow. Complaints are categorised
 * and the fragment obtains a list of complaint categories from cache and the user
 * selects the approriate category before completing the complaint.
 * <p>
 * If the complaint is at the user's residence a cached address is used. If the
 * complaint is elsewhere, the fragment requests a fresh location from its container.
 * <p>
 * When the server responds with the expected OK response, the fragment requests the
 * container to present the PictureActivity and add images to the complaint.
 */
public class ComplaintCreateFragment extends Fragment implements PageFragment {

    private ComplaintFragmentListener mListener;


    public static ComplaintCreateFragment newInstance() {
        ComplaintCreateFragment fragment = new ComplaintCreateFragment();
        return fragment;
    }

    public ComplaintCreateFragment() {
    }

    ResponseDTO response;
    Context ctx;
    View view;
    TextView txtCategory, txtType;
    List<ComplaintTypeDTO> complaintTypeList;
    List<ComplaintCategoryDTO> complaintCategoryList;
    Activity activity;
    ImageView hero, cameraIcon, iconBack;
    RecyclerView recyclerView;
    EditText complaintDescription;
    Spinner spinner;
    AccountDTO account;
    RadioButton radioAccount, radioAnywhere;
    FirebaseAnalytics mFirebaseAnalytics;
    Snackbar snackbar;
    boolean sendAccount;

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
        Log.e(LOG, "onCreateView: +++++++++++++++++++++++++++++++++++++");
        view = inflater.inflate(R.layout.fragment_create_complaint, container, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        ctx = getActivity();
        activity = getActivity();
        setFields();
        mListener.onComplaintLocationRequested();
        UploadBroadcastReceiver receiver = new UploadBroadcastReceiver();
        IntentFilter filter = new IntentFilter(PhotoUploadService.BROADCAST_UPLOADED);

        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(getActivity());
        bm.registerReceiver(receiver, filter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(LOG, "onResume: ****************************** getting getCachedLookups");
        getCachedLookups();
    }

    private void getCachedLookups() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {

                if (response.getComplaintCategoryList() != null) {
                    complaintCategoryList = response.getComplaintCategoryList();
                    setCategoryList();
                } else {
                    Util.showErrorToast(ctx, "No available complaint categories currently");
                }
                if (response.getComplaintTypeList() != null) {
                    complaintTypeList = response.getComplaintTypeList();
                } else {
                    Log.i(LOG, "Complaint Type List is null" + complaintCategoryList.size());
                }

            }

            @Override
            public void onError() {

            }
        });
    }


    private void sendComplaint() {


        if (complaintCategory == null) {
            Util.showToast(ctx, "Please select category of complaint");
            return;
        }
        if (complaintType == null) {
            Util.showToast(ctx, "Please select complaint type");
            return;
        }
        if (radioAccount.isChecked()) {
            if (account == null) {
                snackbar = showSnackBar(txtCategory, "Please select account", "OK",
                        Color.parseColor("YELLOW"));
                return;
            }
        }

        final RequestDTO w = new RequestDTO(RequestDTO.ADD_COMPLAINT);

        complaint = new ComplaintDTO();
        if (profile != null) {
            ProfileInfoDTO pi = new ProfileInfoDTO();
            pi.setCustomerID(profile.getCustomerID());
            pi.setMunicipalityID(profile.getMunicipalityID());
            pi.setEmail(profile.getEmail());
            pi.setFirstName(profile.getFirstName());
            pi.setLastName(profile.getLastName());
            pi.setPassword(profile.getPassword());
            complaint.setProfileInfo(pi);
            if (radioAccount.isChecked()) {
                complaint.setAccountNumber(account.getAccountNumber());
            }


        }
        if (complaintDescription.getText().toString().isEmpty()) {
            Log.i(LOG, "complaint description is empty");
        } else {
            complaint.setDescription(complaintDescription.getText().toString().trim());
        }
        complaint.setCategory(complaintCategory.getComplaintCategoryName().trim());
        complaint.setSubCategory(complaintType.getComplaintTypeName());
        complaint.setComplaintDate(new Date().getTime());

        if (radioAnywhere.isChecked()) {
            if (location != null) {
                complaint.setLatitude(location.getLatitude());
                complaint.setLongitude(location.getLongitude());
            }
        }

        w.setComplaint(complaint);
        complaint.setComplaintType(complaintType);
        complaint.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        w.setMunicipalityID(complaint.getMunicipalityID());

        //todo remove when done testing
        w.setSpoof(false);
        //

        if (WebCheck.checkNetworkAvailability(ctx).isNetworkUnavailable()) {
            Util.showErrorToast(ctx, getString(R.string.no_network));
            return;
        }

        mListener.setBusy(true);
        snackbar = Util.showSnackBar(recyclerView, "Sending your complaint to the municipality ...", "OK", Color.parseColor("Cyan"));
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.setBusy(false);
                            snackbar.dismiss();
                            if (response.isMunicipalityAccessFailed()) {
                                showSnackBar(recyclerView, getString(R.string.unable_complain), getString(R.string.close), Color.parseColor("RED"));
                                return;
                            } else {
                                if (response.getComplaintList() != null && !response.getComplaintList().isEmpty()) {
                                    snackbar = Util.showSnackBar(recyclerView, getString(R.string.complaint_received), "OK", Color.parseColor("GREEN"));
                                    showCameraIcon(response.getComplaintList().get(0));
                                    setAnalyticsEvent("complaint1", "Complaint sent");
                                    mListener.onComplaintAdded(response.getComplaintList());
                                    showReferenceDialog();

                                } else {
                                    setAnalyticsEvent("complaint0", "Error complaint");
                                    snackbar = Util.showSnackBar(recyclerView, getString(R.string.process_complaint_unable), "OK", Color.parseColor("YELLOW"));
                                    return;
                                }
                            }

                        }
                    });
                }


            }


            @Override
            public void onError(final String message) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.setBusy(false);
                        FirebaseCrash.report(new Exception("Error complaint: " + message));
                        Util.showSnackBar(recyclerView, message, "Not OK", Color.parseColor("RED"));

                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });

    }

    private void setAnalyticsEvent(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.w(LOG, "analytics event sent .....");


    }

    private void showCameraIcon(final ComplaintDTO complaint) {
        cameraIcon.setVisibility(View.GONE);
        this.complaint = complaint;
        Util.scaleUp(cameraIcon, 300);
    }

    CategoryAdapter categoryAdapter;
    SubCategoryAdapter subCategoryAdapter;

    private void setCategoryList() {
        Log.d(LOG, "setCategoryList: sss s------------------------------");
        iconBack.setVisibility(View.GONE);
        txtCategory.setText("");
        txtType.setText("");
        categoryAdapter = new CategoryAdapter(complaintCategoryList, ctx, new CategoryAdapter.CategoryAdapterListener() {
            @Override
            public void onCategoryClicked(ComplaintCategoryDTO category) {
                complaintCategory = category;
                txtCategory.setText(category.getComplaintCategoryName());
                setComplaintTypeList();
            }
        });
        recyclerView.setAdapter(categoryAdapter);
    }

    private void setComplaintTypeList() {
        Log.d(LOG, "setComplaintTypeList: -----------------------------");
        iconBack.setVisibility(View.VISIBLE);
        complaintTypeList = complaintCategory.getComplaintTypeList();
        subCategoryAdapter = new SubCategoryAdapter(complaintTypeList, getActivity(), new SubCategoryAdapter.ComplaintTypeListener() {
            @Override
            public void onComplaintTypeClicked(ComplaintTypeDTO type) {
                complaintType = type;
                txtType.setText(type.getComplaintTypeName());
                showConfirmDialog();
            }
        });
        recyclerView.setAdapter(subCategoryAdapter);
    }

    AlertDialog.Builder confirmDialog;

    private void addDescriptionDialog() {
        confirmDialog = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        confirmDialog.setView(input);
        confirmDialog.setTitle("Complaint Comment")
                .setMessage("Add a comment to this complaint below\n\n" + complaintDescription.getText().toString())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        complaintDescription.setText(input.getText().toString().trim());
                        dialog.dismiss();
                        sendComplaint();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sendComplaint();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showConfirmDialog() {
        confirmDialog = new AlertDialog.Builder(getActivity());
        confirmDialog.setTitle("Confirm Complaint")
                .setMessage("Do you want to send this complaint?\n\n"
                        + complaintCategory.getComplaintCategoryName() + " - " + complaintType.getComplaintTypeName())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        addDescriptionDialog();
                        //sendComplaint();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    AlertDialog.Builder referenceDialog;

    private void showReferenceDialog() {
        referenceDialog = new AlertDialog.Builder(getActivity());
        referenceDialog.setTitle("Reference Number")
                .setMessage("Your complaint has been successfully submitted, your reference number is: \n\n"
                        + complaint.getReferenceNumber())
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    ProfileInfoDTO profile;
    ComplaintDTO complaint;

    public void onCameraCompleted() {
        mListener.onRefreshRequested();
    }

    private void setFields() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        complaintDescription = (EditText) view.findViewById(R.id.add_complaint_description);
        complaintDescription.setVisibility(View.GONE);
        txtCategory = (TextView) view.findViewById(R.id.category);
        txtType = (TextView) view.findViewById(R.id.complaintType);
        iconBack = (ImageView) view.findViewById(R.id.backIcon);
        cameraIcon = (ImageView) view.findViewById(R.id.cameraIcon);
        radioAccount = (RadioButton) view.findViewById(R.id.radioAccount);
        radioAnywhere = (RadioButton) view.findViewById(R.id.radioAnywhere);
        hero = (ImageView) view.findViewById(R.id.CC_hero);
        spinner = (Spinner) view.findViewById(R.id.spinnerAccounts);
        txtCategory.setText("");
        txtType.setText("");
        cameraIcon.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        iconBack.setVisibility(View.GONE);

        radioAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (profile.getAccountList().size() > 1) {
                        spinner.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        radioAnywhere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinner.setVisibility(View.GONE);
                    mListener.onComplaintLocationRequested();
                }
            }
        });


        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);


        profile = SharedUtil.getProfile(getActivity());
        if (profile.getAccountList().size() == 1) {
            spinner.setVisibility(View.GONE);
            account = profile.getAccountList().get(0);
        }
        if (profile.getAccountList().size() > 1) {
            setSpinner();
        }
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoryList();
                iconBack.setVisibility(View.GONE);
                cameraIcon.setVisibility(View.GONE);
                //   complaintDescription.setHint(R.string.complaint_description);
                complaintDescription.setText("");
            }
        });
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complaint != null)
                    mListener.onPictureRequired(complaint);
            }
        });


//        fabSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                if (complaintCategory == null) return;
//
//                if (complaintCategory.getComplaintCategoryName().equalsIgnoreCase("Road")
//                        || complaintCategory.getComplaintCategoryName().equalsIgnoreCase("Pollution")
//                        || complaintCategory.getComplaintCategoryName().equalsIgnoreCase("Traffic")
//                        || complaintCategory.getComplaintCategoryName().equalsIgnoreCase("Waste Water")) {
//                    sendAccount = false;
//                    mListener.onComplaintLocationRequested();
//                    return;
//
//                }
//
//                selectComplaintLocationDialog();
//
//
//            }
//        });
        setCategoryList();
        animateSomething();
    }

    private void setSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Please select an Account");
        for (AccountDTO acc : profile.getAccountList()) {
            list.add("Account No: " + acc.getAccountNumber());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.category_spinner_item, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                if (position == 0) {
                    account = null;
                } else {
                    account = profile.getAccountList().get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    AlertDialog.Builder locationDialog;

    ComplaintCategoryDTO complaintCategory;
    ComplaintTypeDTO complaintType;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ComplaintFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ComplaintFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = CityApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }

    Timer timer;

    @Override
    public void animateSomething() {
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (getActivity() == null) return;
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        timer.purge();
//                        timer.cancel();
//                        hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));
//                        Util.expand(hero, 1000, new Util.UtilAnimationListener() {
//                            @Override
//                            public void onAnimationEnded() {
//                                Util.flashOnce(icon, 300, null);
//                            }
//                        });
//                    }
//                });
//            }
//        }, 500);
    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

    public interface ComplaintFragmentListener {

        void onComplaintAdded(List<ComplaintDTO> complaintList);

        void onComplaintLocationRequested();

        void setBusy(boolean busy);

        void onPictureRequired(ComplaintDTO complaint);

        void onRefreshRequested();

    }

    Location location;

    public void setLocation(Location location) {
        Log.w(LOG, "$$$$==============================>>  setLocation, acc: " + location.getAccuracy());
        this.location = location;
    }

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

    private void showSnack() {
        try {
            Util.showSnackBar(txtCategory, "Photo has been uploaded to server", "OK", Color.parseColor("green"));
        } catch (Exception e) {
            //ignore
        }
    }

    private class UploadBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            showSnack();
        }
    }

}
