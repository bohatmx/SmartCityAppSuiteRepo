package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.adapters.ComplaintCategoryPopupListAdapter;
import com.boha.library.adapters.ComplaintListAdapter;
import com.boha.library.adapters.ComplaintTypePopupListAdapter;
import com.boha.library.dto.ComplaintCategoryDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.dto.ComplaintUpdateStatusDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.dto.UserDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.squareup.leakcanary.RefWatcher;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Manage the User's own complaints history
 */
public class MyComplaintsFragment extends Fragment implements PageFragment {


    public static MyComplaintsFragment newInstance(ResponseDTO r) {
        MyComplaintsFragment fragment = new MyComplaintsFragment();
        Bundle args = new Bundle();
        args.putSerializable("complaints", r);
        fragment.setArguments(args);
        return fragment;
    }

    public MyComplaintsFragment() {
    }

    ResponseDTO response;
    View view;
    Context ctx;
    View handle;
    ListView listView;
    TextView txtCount, txtTitle, txtSubTitle, txtUserName;
    List<ComplaintDTO> complaintList;
    List<String> stringList;
    Activity activity;
    View topView;
    ImageView hero, noCompImage, filterIcon;
    TextView txtNoComp, FilterTxt, FilterTxt2;
    ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            response = (ResponseDTO) getArguments().getSerializable("complaints");
            complaintList = response.getComplaintList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_complaints, container, false);

        ctx = getActivity();
        activity = getActivity();
        setFields();

        if (savedInstanceState != null) {
            ResponseDTO w = (ResponseDTO) savedInstanceState.getSerializable("response");
            if (w != null) {
                response = w;
                complaintList = response.getComplaintList();
            }
        }

        setList();
        getCachedComplaintTypes();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        b.putSerializable("response", response);
        super.onSaveInstanceState(b);
    }

    ComplaintListAdapter adapter;

    private void setList() {
        if (complaintList == null) {
            complaintList = new ArrayList<>();
        }
        if (!complaintList.isEmpty()) {
            noCompImage.setVisibility(View.GONE);
            txtNoComp.setVisibility(View.GONE);
        }
        txtCount.setText("" + complaintList.size());

        adapter = new ComplaintListAdapter(ctx, R.layout.my_complaint_item, primaryDarkColor,
                ComplaintListAdapter.MY_COMPLAINTS,
                complaintList, new ComplaintListAdapter.ComplaintListListener() {
            @Override
            public void onComplaintFollowRequested(ComplaintDTO complaint) {
                underConstruction();
            }

            @Override
            public void onComplaintStatusRequested(ComplaintDTO complaint, int position) {
                if (complaint.getHref() == null) {
                    listener.onRefreshRequested(complaint);
                } else {
                    getCaseDetails(complaint.getHref(), position);
                }
            }

            @Override
            public void onComplaintCameraRequested(ComplaintDTO complaint) {
                underConstruction();
            }

            @Override
            public void onComplaintImagesRequested(ComplaintDTO complaint) {
                underConstruction();
            }
        });
        listView.setAdapter(adapter);

    }

    private void underConstruction() {
        Util.showToast(ctx, getString(R.string.under_cons));
    }

    List<ComplaintUpdateStatusDTO> complaintUpdateStatusList;

    public void setComplaintList(List<ComplaintDTO> list) {
        complaintList = list;
        setList();
    }

    private void getCaseDetails(final String href, final int position) {
        Log.e(LOG, "##getCaseDetails, href: " + href);
        RequestDTO w = new RequestDTO(RequestDTO.GET_COMPLAINT_STATUS);
        w.setReferenceNumber(href);
        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());

        listener.setBusy(true);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.setBusy(false);
                            if (response.getStatusCode() == 0) {
                                complaintUpdateStatusList = response.getComplaintUpdateStatusList();
                                complaintList.get(position).setComplaintUpdateStatusList(complaintUpdateStatusList);
                                adapter.notifyDataSetChanged();
                            }

                        }
                    });
                }
            }

            @Override
            public void onError(final String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.setBusy(false);
                            Util.showToast(ctx, message);
                        }
                    });
                }
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }

    private void setFields() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        handle = view.findViewById(R.id.FLC_handle);
        topView = view.findViewById(R.id.FLC_titleLayout);
        topView.setBackgroundColor(primaryDarkColor);
        hero = (ImageView) view.findViewById(R.id.FLC_hero);
        txtUserName = (TextView) view.findViewById(R.id.FLC_userName);
        txtTitle = (TextView) view.findViewById(R.id.FLC_title);
        txtSubTitle = (TextView) view.findViewById(R.id.FLC_subTitle);

        txtCount = (TextView) view.findViewById(R.id.FLC_count);
        txtNoComp = (TextView) view.findViewById(R.id.FLC_noComplaints);
        noCompImage = (ImageView) view.findViewById(R.id.FLC_image);
        listView = (ListView) view.findViewById(R.id.FLC_listView);
        filterIcon = (ImageView) view.findViewById(R.id.filter_icon);
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(view, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        showComplaintCategoryPopup();

                    }
                });

            }
        });
        FilterTxt = (TextView) view.findViewById(R.id.filter_Txt);
        FilterTxt2 = (TextView) view.findViewById(R.id.filter_Txt2);



        txtTitle.setText(ctx.getString(R.string.my_complaints));
        txtSubTitle.setText(getString(R.string.history));

        ProfileInfoDTO x = SharedUtil.getProfile(ctx);
        UserDTO z = SharedUtil.getUser(ctx);
        if (x != null) {
            txtUserName.setText(x.getFirstName() + " " + x.getLastName());
        }
        if (z != null) {
            txtUserName.setText(z.getEmail());
        }
        setNoCompImage();
        animateSomething();
    }


    Random random = new Random(System.currentTimeMillis());

    private void setNoCompImage() {
        noCompImage.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.happy3));
//        int index = random.nextInt(9);
//        switch (index) {
//            case 0:
//                noCompImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.happy1));
//                return;
//            case 1:
//                noCompImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.happy2));
//                return;
//            case 2:
//                noCompImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.happy3));
//                return;
//            case 3:
//                noCompImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.happy4));
//                return;
//            case 4:
//                noCompImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.happy5));
//                return;
//            case 5:
//                noCompImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.happy6));
//                return;
//            case 6:
//                noCompImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.happy7));
//                return;
//            case 7:
//                noCompImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.happy8));
//                return;
//            case 8:
//                noCompImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.happy9));
//                return;
//            case 9:
//                noCompImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.happy10));
//                return;
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MyComplaintsListener) {
            listener = (MyComplaintsListener) activity;
        } else {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement MyComplaintsListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = CityApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
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

                    }
                });
            }
        }, 50);
    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

    static final String LOG = MyComplaintsFragment.class.getSimpleName();
    String pageTitle;

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    private MyComplaintsListener listener;

    public interface MyComplaintsListener {
        void setBusy(boolean busy);
        void onRefreshRequested(ComplaintDTO complaint);
    }

    //complaint filter
    List<ComplaintCategoryDTO> complaintCategoryList = new ArrayList<ComplaintCategoryDTO>();
    ImageView search;
    ComplaintCategoryDTO complaintCategory;
    ComplaintTypeDTO complaintType;
    ListPopupWindow categoryPopup, complaintPopup;
    ComplaintCategoryPopupListAdapter complaintCategoryPopup;

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
                Util.setComplaintCategoryIcon(complaintCategory.getComplaintCategoryName(), filterIcon, getActivity());
                filterIcon.setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_IN);
                FilterTxt.setText(complaintCategoryList.get(position).getComplaintCategoryName());
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
                Util.setComplaintTypeIcon(complaintType.getComplaintTypeName(), filterIcon, ctx);
                filterIcon.setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_IN);
                FilterTxt2.setText(list.get(position).getComplaintTypeName());
                adapter.filter(FilterTxt2.getText().toString());
            }
        });
        complaintPopup.show();
    }

    List<ComplaintTypeDTO> complaintTypeList;
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

}




