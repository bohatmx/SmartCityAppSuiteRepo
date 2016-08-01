package com.boha.library.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.adapters.ComplaintCategoryPopupListAdapter;
import com.boha.library.adapters.ComplaintTypePopupListAdapter;
import com.boha.library.dto.ComplaintCategoryDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.dto.GISAddressDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.ResidentialAddress;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.boha.library.util.WebCheck;
import com.squareup.leakcanary.RefWatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    View view, addressLayout;
    ScrollView scroll;
    Context ctx;
    View handle, tapLayout;
    EditText editNumber, editStreet, editSuburb, editCity, editComment;
    Button btnSend;
    TextView txtTitle, txtSubTitle,
            txtGetAddress, txtComplaintType, txtComplaintType2;
    List<ComplaintTypeDTO> complaintTypeList;
    List<String> stringList;
    List<ComplaintCategoryDTO> complaintCategoryList;
    Activity activity;
    View topView;
    ImageView hero, icon;
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
        getCachedComplaintTypes();
        return view;
    }

    private void getCachedComplaintTypes() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {

                if (response.getComplaintCategoryList() != null) {
                    complaintCategoryList = response.getComplaintCategoryList();
                }
                if (response.getComplaintTypeList() != null) {
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

    private void sendComplaint() {

        if (complaintCategory == null) {
            Util.showToast(ctx, "Please start complaint");
            return;
        }
        if (complaintType == null) {
            Util.showToast(ctx, "Please start complaint");
            return;
        }

        if (editStreet.getText().toString().isEmpty()) {
            Util.showToast(ctx, "Please enter street name");
            return;
        }

        final RequestDTO w = new RequestDTO(RequestDTO.ADD_COMPLAINT);
        final ComplaintDTO complaint = new ComplaintDTO();

        complaint.setNumber(editNumber.getText().toString());
        complaint.setStreet(editStreet.getText().toString());
        complaint.setSuburb(editSuburb.getText().toString());
        complaint.setCity(editCity.getText().toString());
        complaint.setRemarks(editComment.getText().toString());

        if (location != null) {
            complaint.setLatitude(location.getLatitude());
            complaint.setLongitude(location.getLongitude());
        }

        ProfileInfoDTO prof = SharedUtil.getProfile(ctx);
        UserDTO user = SharedUtil.getUser(ctx);
        if (prof != null) {
            complaint.setProfileInfo(prof);
            complaint.getProfileInfo().setAccountList(null);
            complaint.getProfileInfo().setComplaintList(null);

        }
        if (user != null) {
            user.setDateRegistered(null);
            user.setFirstName(null);
            user.setLastName(null);
            complaint.setUser(user);

        }
        complaint.setCategory(complaintCategory.getComplaintCategoryName());
        complaint.setSubCategory(complaintType.getComplaintTypeName());
        w.setComplaint(complaint);
        complaint.setComplaintType(complaintType);
        complaint.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        w.setMunicipalityID(complaint.getMunicipalityID());

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
                                Snackbar.make(btnSend, "Unable to send complaint. Service not available. Please try again.", Snackbar.LENGTH_LONG).show();
                                return;
                            } else {
                                if (response.getAddressList() != null && !response.getAddressList().isEmpty()) {
                                    mListener.onMultiAddressDialog(response.getAddressList());
                                    return;

                                }
                                if (response.getComplaintList() != null && !response.getComplaintList().isEmpty()) {
                                    Snackbar.make(btnSend, "Your complaint has been received", Snackbar.LENGTH_LONG).show();
                                    clearEditFields();
                                    mListener.onComplaintAdded(response.getComplaintList());
                                } else {
                                    Util.showErrorToast(ctx, "Unable to process the complaint at this time. Please try later");
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
                        Util.showToast(ctx, message);


                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });

    }


    GISAddressDTO selectedAddress;

    public void setSelectedAddress(GISAddressDTO selectedAddress) {
        this.selectedAddress = selectedAddress;
        if (selectedAddress.getCity() != null) {
            editCity.setText(selectedAddress.getCity());
        }
        if (selectedAddress.getSuburb() != null) {
            editSuburb.setText(selectedAddress.getSuburb());
        }
        if (selectedAddress.getStreet() != null) {
            editStreet.setText(selectedAddress.getStreet());
        }

    }

    private void clearEditFields() {
        btnSend.setVisibility(View.GONE);
        editComment.setText("");
        complaintType = null;
        editNumber.setText("");
        editStreet.setText("");
        editComment.setText("");
        editCity.setText("");
        editSuburb.setText("");
        txtComplaintType.setText(R.string.start_complaint);
        txtComplaintType2.setText(R.string.start_complaint);
        icon.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_action_bell));
        Util.flashSeveralTimes(txtComplaintType, 300, 4, null);
    }

    private void setFields() {

        addressLayout = view.findViewById(R.id.CC_addressLayout);
        addressLayout.setEnabled(false);
        scroll = (ScrollView)view.findViewById(R.id.CC_scroll);
        handle = view.findViewById(R.id.CC_handle);
        tapLayout = view.findViewById(R.id.CC_tapLayout);
        topView = view.findViewById(R.id.CC_titleLayout);
        topView.setBackgroundColor(primaryDarkColor);
        hero = (ImageView) view.findViewById(R.id.CC_hero);
        icon = (ImageView) view.findViewById(R.id.CC_icon);

        editNumber = (EditText) view.findViewById(R.id.CC_number);
        editStreet = (EditText) view.findViewById(R.id.CC_street);
        editSuburb = (EditText) view.findViewById(R.id.CC_suburb);
        editCity = (EditText) view.findViewById(R.id.CC_city);

        editComment = (EditText) view.findViewById(R.id.CC_comment);
        txtComplaintType = (TextView) view.findViewById(R.id.CC_complaintType);
//        txtComplaintType2 = (TextView) view.findViewById(R.id.CC_complaintType2);
        txtTitle = (TextView) view.findViewById(R.id.CC_title);
        txtSubTitle = (TextView) view.findViewById(R.id.CC_subTitle);
        txtGetAddress = (TextView) view.findViewById(R.id.CC_getGeoAddress);
        btnSend = (Button) view.findViewById(R.id.button);
        editComment.setVisibility(View.GONE);

//        btnSend.setVisibility(View.GONE);

        txtTitle.setText(ctx.getString(R.string.make_complaint));
        txtSubTitle.setVisibility(View.GONE);
        txtGetAddress.setVisibility(View.GONE);
        txtComplaintType.setText(R.string.start_complaint);
//        txtComplaintType2.setText(R.string.start_complaint);


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

        tapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Util.flashOnce(txtComplaintType, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        showComplaintCategoryPopup();
                    }
                });
            }
        });
        txtComplaintType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Util.flashOnce(txtComplaintType, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showComplaintCategoryPopup();
                    }
                });
            }
        });
//        txtComplaintType2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                Util.flashOnce(txtComplaintType2, 200, new Util.UtilAnimationListener() {
//                    @Override
//                    public void onAnimationEnded() {
//                        showComplaintCategoryPopup();
//                    }
//                });
//            }
//        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Util.flashOnce(txtComplaintType, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showComplaintCategoryPopup();
                    }
                });
            }
        });

        disableAddress();
        animateSomething();
    }

    private void enableAddress() {
        editNumber.setEnabled(true);
        editStreet.setEnabled(true);
        editSuburb.setEnabled(true);
        editCity.setEnabled(true);
    }

    private void disableAddress() {
        editNumber.setEnabled(false);
        editStreet.setEnabled(false);
        editSuburb.setEnabled(false);
        editCity.setEnabled(false);
    }

    ListPopupWindow categoryPopup, complaintPopup;

    public void showComplaintCategoryPopup() {
        if (complaintPopup != null) {
            complaintPopup.dismiss();
        }
        categoryPopup = new ListPopupWindow(getActivity());
        LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.hero_image_popup, null);
        TextView txt = (TextView) v.findViewById(R.id.HERO_caption);
        txt.setText("Complaints Categories");
        ImageView img = (ImageView) v.findViewById(R.id.HERO_image);
        img.setImageDrawable(Util.getRandomBackgroundImage(ctx));

        categoryPopup.setPromptView(v);
        categoryPopup.setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE);
        categoryPopup.setAdapter(new ComplaintCategoryPopupListAdapter(ctx,
                R.layout.xspinner_item, complaintCategoryList, primaryDarkColor));
        categoryPopup.setAnchorView(handle);
        categoryPopup.setHorizontalOffset(Util.getPopupHorizontalOffset(getActivity()));
        categoryPopup.setModal(true);
        categoryPopup.setWidth(Util.getPopupWidth(getActivity()));
        categoryPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryPopup.dismiss();
                complaintCategory = complaintCategoryList.get(position);
                Util.setComplaintCategoryIcon(complaintCategory.getComplaintCategoryName(), icon, getActivity());
                icon.setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_IN);
                showComplaintTypePopup(complaintCategory.getComplaintTypeList());
            }
        });
        categoryPopup.show();
    }

    public void showComplaintTypePopup(final List<ComplaintTypeDTO> list) {
        if (categoryPopup != null) {
            categoryPopup.dismiss();
        }
        complaintPopup = new ListPopupWindow(getActivity());
        LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.hero_image_popup, null);
        TextView txt = (TextView) v.findViewById(R.id.HERO_caption);
        txt.setText("Complaints");
        ImageView img = (ImageView) v.findViewById(R.id.HERO_image);
        img.setImageDrawable(Util.getRandomBackgroundImage(ctx));

        complaintPopup.setPromptView(v);
        complaintPopup.setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE);
        complaintPopup.setAdapter(new ComplaintTypePopupListAdapter(ctx,
                R.layout.xspinner_item, list, primaryDarkColor));
        complaintPopup.setAnchorView(handle);
        complaintPopup.setHorizontalOffset(Util.getPopupHorizontalOffset(getActivity()));
        complaintPopup.setModal(true);
        complaintPopup.setWidth(Util.getPopupWidth(getActivity()));
        complaintPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                complaintPopup.dismiss();
                complaintType = list.get(position);
                Util.setComplaintTypeIcon(complaintType.getComplaintTypeName(), icon, ctx);
                icon.setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_IN);
                txtComplaintType.setText(complaintCategory.getComplaintCategoryName()
                        + " - " + complaintType.getComplaintTypeName());

                selectComplaintLocationDialog();

            }
        });
        complaintPopup.show();
    }

    private void selectComplaintLocationDialog() {
        AlertDialog.Builder d = new AlertDialog.Builder(activity);
        d.setTitle("Choose Complaint Address")
                .setMessage("Is the complaint for your residential address?\n\nIf YES, the app will use the address you saved, " +
                        "\n\nif NO, the app will use GPS to find the location of the complaint.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ResidentialAddress x = SharedUtil.getAddress(activity);
                        if (x != null) {
                            editNumber.setText(x.getNumber().trim());
                            editStreet.setText(x.getStreet().trim());
                            editSuburb.setText(x.getSuburb().trim());
                            editCity.setText(x.getCity().trim());
                            showButtons();
                            editNumber.setEnabled(true);
                            editStreet.setEnabled(true);
                            editSuburb.setEnabled(true);
                            editCity.setEnabled(true);
                            addressLayout.setEnabled(true);
                            scroll.post(new Runnable() {
                                @Override

                                public void run() {

                                    scroll.scrollTo(0, scroll.getBottom());

                                }
                            });
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideButtons();
                        Snackbar.make(txtComplaintType, ctx.getString(R.string.calc_complaint_address), Snackbar.LENGTH_LONG).show();
                        mListener.setBusy(true);
                        editNumber.setText("");
                        editStreet.setText("");
                        editSuburb.setText("");
                        editCity.setText("");
                        mListener.onComplaintLocationRequested();
                    }
                })
                .show();
    }

    ComplaintCategoryDTO complaintCategory;
    ComplaintTypeDTO complaintType;

    void showButtons() {
        btnSend.setVisibility(View.VISIBLE);
        editComment.setVisibility(View.VISIBLE);
    }

    void hideButtons() {
        btnSend.setVisibility(View.GONE);
        editComment.setVisibility(View.GONE);
    }

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
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.purge();
                        timer.cancel();
                        hero.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                        Util.expand(hero, 1000, new Util.UtilAnimationListener() {
                            @Override
                            public void onAnimationEnded() {
                                Util.flashOnce(icon, 300, null);
                            }
                        });
                    }
                });
            }
        }, 500);
    }

    public void killTimer() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }

    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

    public interface ComplaintFragmentListener {
        void onFindComplaintsLikeMine(ComplaintDTO complaint);

        void onFindComplaintsAroundMe();

        void onComplaintAdded(List<ComplaintDTO> complaintList);

        void onComplaintLocationRequested();

        void setBusy(boolean busy);

        void onMultiAddressDialog(List<GISAddressDTO> list);
    }

    Location location;

    public void setLocation(Location location) {
        Log.w(LOG, "$$$$ setLocation, acc: " + location.getAccuracy());
        this.location = location;
        if (btnSend != null) {
            showButtons();
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

                    String msg = "addressLine1: " + address.getAddressLine(0)
                            + "\nAdminArea: " + address.getAdminArea()
                            + "\n featureName: " + address.getFeatureName()
                            + "\n locality: " + address.getLocality()
                            + "\n subAdminArea: " + address.getSubAdminArea()
                            + "\n subLocality: " + address.getSubLocality()
                            + "\n thoroughfare: " + address.getThoroughfare()
                            + "\n subThroughFare: " + address.getSubThoroughfare()
                            + "\n postalCode: " + address.getPostalCode()
                            + "\n maxAddressLineIndex: " + address.getMaxAddressLineIndex();

                    String addressLines = "";
                    for (int i = 0; i < address.getMaxAddressLineIndex() + 1; i++) {
                        addressLines += "\n" + address.getAddressLine(i);
                    }
                    System.out.println(msg);
                    System.out.println(addressLines);
                } else {
                    return 9;
                }

            } catch (IOException e) {
                Log.e(LOG, "Geocoder has a problem getting address", e);
                return 9;
            }
            return 0;
        }

        @Override
        public void onPostExecute(Integer result) {
            mListener.setBusy(false);
            txtGetAddress.setEnabled(true);
            txtGetAddress.setAlpha(1.0f);

            addressLayout.setEnabled(true);
            enableAddress();
            if (result == 0) {
                addressLayout.setVisibility(View.VISIBLE);
                if (address.getSubThoroughfare() != null) {
                    editNumber.setText(address.getSubThoroughfare());
                }
                if (address.getThoroughfare() != null) {
                    editStreet.setText(address.getThoroughfare());
                }
                if (address.getSubLocality() != null) {
                    editSuburb.setText(address.getSubLocality());
                }
                if (address.getLocality() != null) {
                    editCity.setText(address.getLocality());
                }
                Util.expand(addressLayout, 500, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        scroll.post(new Runnable() {
                            @Override

                            public void run() {

                                scroll.scrollTo(0, scroll.getBottom());

                            }
                        });
                    }
                });

            } else {
                Snackbar.make(addressLayout, "Unable to calculate address at this location", Snackbar.LENGTH_SHORT).show();
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


    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }


}
