package com.boha.library.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.adapters.CategoryAdapter;
import com.boha.library.adapters.SubCategoryAdapter;
import com.boha.library.dto.AccountDTO;
import com.boha.library.dto.ComplaintCategoryDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.boha.library.util.WebCheck;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static com.boha.library.util.Util.showSnackBar;

/**
 * Fragment that manages the complaint creation flow. Complaints are categorised
 * and the fragment obtains a list of complaint categories from cache and the user
 * selects the approriate category before completing the complaint.
 *
 * If the complaint is at the user's residence a cached address is used. If the
 * complaint is elsewhere, the fragment requests a fresh location from its container.
 *
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
    TextView  txtCategory;
    List<ComplaintTypeDTO> complaintTypeList;
    List<ComplaintCategoryDTO> complaintCategoryList;
    Activity activity;
    ImageView hero, icon, iconBack;
    FloatingActionButton fabSend;
    RecyclerView recyclerView;
    Spinner spinner;


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
        getCachedLookups();
        return view;
    }

    private void getCachedLookups() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {

                if (response.getComplaintCategoryList() != null) {
                    complaintCategoryList = response.getComplaintCategoryList();
                    setCategoryList();
                }
                if (response.getComplaintTypeList() != null) {
                    complaintTypeList = response.getComplaintTypeList();
                }

            }

            @Override
            public void onError() {

            }
        });
    }

    AccountDTO account;

    Snackbar snackbar;
    boolean sendAccount;

    private void sendComplaint() {

        if (complaintCategory == null) {
            Util.showToast(ctx, "Please select category of complaint");
            return;
        }
        if (complaintType == null) {
            Util.showToast(ctx, "Please select complaint type");
            return;
        }

        final RequestDTO w = new RequestDTO(RequestDTO.ADD_COMPLAINT);
        final ComplaintDTO complaint = new ComplaintDTO();

        UserDTO user = SharedUtil.getUser(ctx);
        if (profile != null) {
            ProfileInfoDTO pi = new ProfileInfoDTO();
            pi.setCustomerID(profile.getCustomerID());
            pi.setMunicipalityID(profile.getMunicipalityID());
            pi.setEmail(profile.getEmail());
            pi.setFirstName(profile.getFirstName());
            pi.setLastName(profile.getLastName());
            pi.setPassword(profile.getPassword());
            complaint.setProfileInfo(pi);
            if (sendAccount) {
                if (account == null) {
                    Log.e(LOG, "sendComplaint: account is null ");
                    snackbar = showSnackBar(txtCategory, "Please select accountget", "OK",
                            Color.parseColor("YELLOW"));
                    return;
                }
                complaint.setAccountNumber(account.getAccountNumber());
            }

        }
//        if (user != null) {
//            user.setDateRegistered(null);
//            user.setFirstName(null);
//            user.setLastName(null);
//            complaint.setUser(user);
//
//        }
        complaint.setCategory(complaintCategory.getComplaintCategoryName());
        complaint.setSubCategory(complaintType.getComplaintTypeName());

        w.setComplaint(complaint);
        complaint.setComplaintType(complaintType);
        complaint.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        w.setMunicipalityID(complaint.getMunicipalityID());

        //todo remove when done testing
        w.setSpoof(true);
        //

        if (WebCheck.checkNetworkAvailability(ctx).isNetworkUnavailable()) {
            Util.showErrorToast(ctx, getString(R.string.no_network));
            return;
        }

        mListener.setBusy(true);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.setBusy(false);
                            if (response.isMunicipalityAccessFailed()) {
                                showSnackBar(fabSend,getString(R.string.unable_complain),getString(R.string.close),Color.parseColor("RED"));
                                return;
                            } else {
                                if (response.getComplaintList() != null && !response.getComplaintList().isEmpty()) {
                                    showSnackBar(fabSend,getString(R.string.complaint_received),"OK",Color.parseColor("GREEN"));
                                    showPictureDialog(response.getComplaintList().get(0));
                                    mListener.onComplaintAdded(response.getComplaintList());

                                } else {
                                    showSnackBar(fabSend,getString(R.string.process_complaint_unable),"OK", Color.parseColor("YELLOW"));
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
                        Util.showSnackBar(fabSend,message,"OK",Color.parseColor("RED"));

                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });

    }
    private void showPictureDialog(final ComplaintDTO complaint) {
        AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
        d.setTitle("Confirm Complaint")
                .setMessage("Do you want to take pictures this complaint?\n"
                        + complaintCategory.getComplaintCategoryName() +  " - " + complaintType.getComplaintTypeName() )
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onPictureRequired(complaint);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
    CategoryAdapter categoryAdapter;
    SubCategoryAdapter subCategoryAdapter;

    private void setCategoryList() {
        categoryAdapter = new CategoryAdapter(complaintCategoryList, getActivity(), new CategoryAdapter.CategoryAdapterListener() {
            @Override
            public void onCategoryClicked(ComplaintCategoryDTO category) {
                complaintCategory = category;
                setComplaintTypeList();
            }
        });
        recyclerView.setAdapter(categoryAdapter);
    }
    private void setComplaintTypeList() {
        iconBack.setVisibility(View.VISIBLE);
        complaintTypeList = complaintCategory.getComplaintTypeList();
        subCategoryAdapter = new SubCategoryAdapter(complaintTypeList, getActivity(), new SubCategoryAdapter.ComplaintTypeListener() {
            @Override
            public void onComplaintTypeClicked(ComplaintTypeDTO type) {
                complaintType = type;
                fabSend.setEnabled(true);
                fabSend.setAlpha(1.0f);
                mListener.onComplaintLocationRequested();
                showConfirmDialog();
            }
        });
        recyclerView.setAdapter(subCategoryAdapter);
    }
    private void showConfirmDialog() {
        AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
        d.setTitle("Confirm Complaint")
                .setMessage("Do you want to send this complaint?\n"
                        + complaintCategory.getComplaintCategoryName() +  " - " + complaintType.getComplaintTypeName() )
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendComplaint();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
    ProfileInfoDTO profile;
    private void setFields() {
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        txtCategory = (TextView) view.findViewById(R.id.category);
        fabSend = (FloatingActionButton) view.findViewById(R.id.fabSend);
        iconBack = (ImageView) view.findViewById(R.id.backIcon);
        hero = (ImageView) view.findViewById(R.id.CC_hero);
        spinner = (Spinner) view.findViewById(R.id.spinnerAccounts);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lm);

         profile = SharedUtil.getProfile(getActivity());
        if (profile.getAccountList().size() == 1) {
            spinner.setVisibility(View.GONE);
            account = profile.getAccountList().get(0);
        }
        if (profile.getAccountList().size() > 1) {
            spinner.setVisibility(View.VISIBLE);
            setSpinner();
        }
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoryList();
                iconBack.setVisibility(View.GONE);
            }
        });

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (account != null) {
                    selectComplaintLocationDialog();
                } else {
                    showSnackBar(spinner,"Please select the account", "OK", Color.parseColor("GREEN"));
                }

            }
        });

        animateSomething();
    }


    private void setSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Please select an Account");
        for (AccountDTO acc: profile.getAccountList()) {
            list.add("Account No: " + acc.getAccountNumber());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.category_spinner_item,list);
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
    private void selectComplaintLocationDialog() {


        if (complaintCategory.getComplaintCategoryName().equalsIgnoreCase("Road")
                || complaintCategory.getComplaintCategoryName().equalsIgnoreCase("Pollution")
                || complaintCategory.getComplaintCategoryName().equalsIgnoreCase("Traffic")
                || complaintCategory.getComplaintCategoryName().equalsIgnoreCase("Waste Water")) {
            sendAccount = false;
            mListener.onComplaintLocationRequested();
            return;

        }
        AlertDialog.Builder d = new AlertDialog.Builder(activity);
        d.setTitle("Choose Complaint Location")
                .setMessage("Is the complaint for your residential address?\n\nIf YES, the app will use your residential  address on the system, " +
                        "\n\nif NO, the app will use GPS to find the location of the complaint.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        location = null;
                        sendAccount = true;
                        sendComplaint();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendAccount = false;
                        mListener.onComplaintLocationRequested();
                    }
                })
                .show();
    }

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
        RefWatcher refWatcher = CityApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
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
    }

    Location location;

    public void setLocation(Location location) {
        Log.w(LOG, "$$$$ setLocation, acc: " + location.getAccuracy());
        this.location = location;

        sendComplaint();
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


}
