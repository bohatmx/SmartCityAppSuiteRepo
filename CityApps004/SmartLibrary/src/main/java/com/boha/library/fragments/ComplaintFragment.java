package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.adapters.ComplaintAdapter;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintTypeDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.CacheUtil;
import com.boha.library.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ComplaintFragment extends Fragment implements PageFragment{

    private ComplaintFragmentListener mListener;


    public static ComplaintFragment newInstance() {
        ComplaintFragment fragment = new ComplaintFragment();
        Bundle args = new Bundle();
        //args.putSerializable("complaintList", response);
        fragment.setArguments(args);
        return fragment;
    }

    public ComplaintFragment() {
        // Required empty public constructor
    }

    ResponseDTO response;
    View view, fab;
    Context ctx;
    String title;
    View handle, noComplaintsLayout, mainLayout;
    EditText editAddress, editComment;
    Button btnSend;
    RecyclerView recyclerView;
    TextView txtCount, txtTitle, txtSubTitle, txtComplaintType;
    List<ComplaintTypeDTO> complaintTypeList;
    List<ComplaintDTO> complaintList;
    ComplaintAdapter complaintAdapter;
    List<String> stringList;
    Activity activity;
    View topView;


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
        title = getString(R.string.my_complaints);
        setFields();

        mListener.onLocationRequested();
        getCachedComplaints();
        return view;
    }

    private void getCachedComplaints() {
        CacheUtil.getCacheLoginData(ctx, new CacheUtil.CacheRetrievalListener() {
            @Override
            public void onCacheRetrieved(ResponseDTO response) {
                if (response.getComplaintTypeList() != null) {
                    complaintTypeList = response.getComplaintTypeList();
                }
                if (response.getComplaintList() != null) {
                    complaintList = response.getComplaintList();
                }
                setList();
            }

            @Override
            public void onError() {

            }
        });
    }
    private void setList() {
        if (complaintList == null) {
            noComplaintsLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noComplaintsLayout.setVisibility(View.GONE);
            complaintAdapter = new ComplaintAdapter(complaintList, ctx, new ComplaintAdapter.ComplaintListener() {
                @Override
                public void onComplaintSelected(int position) {

                }
            });
            recyclerView.setAdapter(complaintAdapter);
        }
        stringList = new ArrayList<>();
        if (complaintTypeList != null) {
            for (ComplaintTypeDTO x: complaintTypeList) {
                stringList.add(x.getComplaintTypeName());
            }
        }
    }
    private void setFields() {
        handle = view.findViewById(R.id.CC_handle);
        topView = view.findViewById(R.id.CC_titleLayout);
        topView.setBackgroundColor(primaryDarkColor);
        mainLayout = view.findViewById(R.id.CC_middle);
        mainLayout.setVisibility(View.GONE);
        noComplaintsLayout  = view.findViewById(R.id.CC_noComplaintsLayout);
        editAddress = (EditText)view.findViewById(R.id.CC_address);
        editComment = (EditText)view.findViewById(R.id.CC_comment);
        txtComplaintType = (TextView)view.findViewById(R.id.CC_complaintType);
        txtTitle = (TextView)view.findViewById(R.id.CC_title);
        txtSubTitle = (TextView)view.findViewById(R.id.CC_subTitle);
        fab = view.findViewById(R.id.FAB);
        btnSend= (Button)view.findViewById(R.id.CC_btnSend);
        btnSend.setEnabled(false);
        btnSend.setAlpha(0.4f);
        txtCount = (TextView)view.findViewById(R.id.CC_count);
        recyclerView = (RecyclerView)view.findViewById(R.id.CC_recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(ctx);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);

        txtTitle.setText("Service Complaints");
        txtSubTitle.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(fab, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (mainLayout.getVisibility() == View.GONE) {
                            Util.expand(mainLayout, 1000, null);
                        } else {
                            Util.collapse(mainLayout, 1000, null);
                        }
                    }
                });
            }
        });

        txtComplaintType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLocationRequested();
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

                                    }
                                });
                    }
                });
            }
        });
        Util.flashOnce(txtComplaintType, 200,  null);
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

    }

    int primaryColor,  primaryDarkColor;
    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }
    public interface ComplaintFragmentListener {
        public void onFindComplaintsLikeMine(ComplaintDTO complaint);
        public void onFindComplaintsAroundMe();
        public void onLocationRequested();
    }
    Location location;

    public void setLocation(Location location) {
        Log.d(LOG, "### setLocation");
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

        }

    }
    Address address;
    static final String LOG = ComplaintFragment.class.getSimpleName();
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
