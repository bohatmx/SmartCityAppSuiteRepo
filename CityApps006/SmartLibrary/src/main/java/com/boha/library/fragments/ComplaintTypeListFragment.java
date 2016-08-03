package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.adapters.ComplaintListAdapter;
import com.boha.library.adapters.ComplaintTypeListAdapter;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.Util;

import java.util.ArrayList;
import java.util.List;


public class ComplaintTypeListFragment extends Fragment {

    ImageView FCTL_hero, FCTL_image;
    TextView FCTL_title, FCTL_subTitle, FCTL_userName,
             FCTL_noComplaints, FCTL_count;
    SearchView FCTL_complaints_filter;
    RelativeLayout FCTL_SEARCH_LAY;
    ListView FCTL_listView;
    ResponseDTO response;
    Context ctx;
    Activity activity;
    List<ComplaintTypeDTO> complaintTypeList;

    public ComplaintTypeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param response
     * @return A new instance of fragment ComplaintTypeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComplaintTypeListFragment newInstance(ResponseDTO response) {
        ComplaintTypeListFragment fragment = new ComplaintTypeListFragment();
        Bundle args = new Bundle();
        args.putSerializable("complaintType", response);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            response = (ResponseDTO) getArguments().getSerializable("complaintTypes");
            complaintTypeList = response.getComplaintTypeList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_complaint_type_list, container, false);

        ctx = getActivity();
        activity = getActivity();
        setFields();

        if (savedInstanceState != null) {
            ResponseDTO w = (ResponseDTO)savedInstanceState.getSerializable("response");
            if (w != null) {
                response = w;
                complaintTypeList = response.getComplaintTypeList();
            }
        }

        setList();
        return view;

    }

    ComplaintTypeListAdapter adapter;
    int primaryDarkColor;

    private void setList() {
        if (complaintTypeList == null) {
            complaintTypeList = new ArrayList<>();
        }
        if (!complaintTypeList.isEmpty()) {
            FCTL_image.setVisibility(View.GONE);
            FCTL_noComplaints.setVisibility(View.GONE);
        }
        FCTL_count.setText("" + complaintTypeList.size());

        adapter = new ComplaintTypeListAdapter(ctx, R.layout.my_complaint_item,primaryDarkColor,
                ComplaintTypeListAdapter.MY_COMPLAINTS,
                complaintTypeList, new ComplaintTypeListAdapter.ComplaintTypeListListener() {
            @Override
            public void onComplaintFollowRequested(ComplaintDTO complaint) {
             underConstruction();
            }

            @Override
            public void onComplaintStatusRequested(ComplaintDTO complaint, int position) {
            underConstruction();
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
        FCTL_listView.setAdapter(adapter);


    }

    View view;
    private void setFields() {
        FCTL_hero = (ImageView) view.findViewById(R.id.FCTL_hero);
        FCTL_image = (ImageView) view.findViewById(R.id.FCTL_image);
        FCTL_title = (TextView) view.findViewById(R.id.FCTL_title);
        FCTL_subTitle = (TextView) view.findViewById(R.id.FCTL_subTitle);
        FCTL_userName = (TextView) view.findViewById(R.id.FCTL_userName);
        FCTL_complaints_filter = (SearchView) view.findViewById(R.id.FCTL_complaints_filter);
        FCTL_listView = (ListView) view.findViewById(R.id.FCTL_listView);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ComplaintTypeListener) {
            listener = (ComplaintTypeListener) activity;
        } else {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement ComplaintTypeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private ComplaintTypeListener listener;

    public interface ComplaintTypeListener {
        void setBusy(boolean busy);

        void onRefreshRequested(ComplaintDTO complaint);
    }

    private void underConstruction() {
        Util.showToast(ctx, getString(R.string.under_cons));
    }

    /*
    *    List<ComplaintCategoryDTO> complaintCategoryList;
    ImageView search;
    ComplaintCategoryDTO complaintCategory;
    ComplaintTypeDTO complaintType;
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
                Util.setComplaintCategoryIcon(complaintCategory.getComplaintCategoryName(), search, getActivity());
                search.setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_IN);
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
                Util.setComplaintTypeIcon(complaintType.getComplaintTypeName(), search, ctx);
                search.setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_IN);
                selectComplaintsByCategory();

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

    String searchText;
    boolean isFound;

    private void selectComplaintsByCategory() {
        if (search.getContext().toString().isEmpty()) {
            return;
        }
        int index = 0;

        searchText = search.getContext().toString();
        for (int i = 0; i < response.getComplaintCategoryList().size(); i++) {
            ComplaintCategoryDTO searchComplaint = response.getComplaintCategoryList().get(i);
            if (searchComplaint.getComplaintCategoryName().contains(searchText) && searchComplaint.getComplaintTypeList().contains(searchText)) {
                isFound = true;
                break;
            }
            index++;
        }
        if (isFound == true) {
            recyclerView.setSelection(index);
        } else {
            Util.showToast(ctx, ctx.getString(R.string.complaint_not_found));
        }

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
        recyclerView.setAdapter(adapter);

    }*/

}
