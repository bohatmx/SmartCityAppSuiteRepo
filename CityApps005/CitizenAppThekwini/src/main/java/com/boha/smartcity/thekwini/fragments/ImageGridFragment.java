package com.boha.smartcity.thekwini.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.boha.library.fragments.PageFragment;
import com.boha.library.util.DividerItemDecoration;
import com.boha.smartcity.thekwini.R;
import com.boha.smartcity.thekwini.adapters.PictureGridLocalAdapter;

/**
 * Fragment to house local pictures
 */
public class ImageGridFragment extends Fragment implements PageFragment {

    private ImageGridFragmentListener mListener;

    public static ImageGridFragment newInstance() {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ImageGridFragment() {
        // Required empty public constructor
    }

    PictureGridLocalAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    View view;
    RecyclerView grid;
    Button btnmap;
    TextView txtTitle;
    Context ctx;
    String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_picture_grid, container, false);
        txtTitle = (TextView) view.findViewById(R.id.PIC_GRID_title);
        btnmap = (Button)view.findViewById(R.id.PIC_GRID_btnMap);
        grid = (RecyclerView)view.findViewById(R.id.PIC_GRID_recyclerView);
        ctx = getActivity();

//        txtTitle.setText("eThekwini");
        LinearLayoutManager lm = new LinearLayoutManager(ctx);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        grid.setLayoutManager(lm);
        grid.setItemAnimator(new DefaultItemAnimator());
        grid.addItemDecoration(new DividerItemDecoration(ctx, RecyclerView.VERTICAL));

        adapter = new PictureGridLocalAdapter(R.layout.full_photo_item,ctx,
                new PictureGridLocalAdapter.PictureGridListener() {
            @Override
            public void onPictureClicked(int position) {
                mListener.onPictureClicked(position);
            }
        });

        grid.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ImageGridFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ImageGridFragmentListener");
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

    public interface ImageGridFragmentListener {
        public void onPictureClicked(int position);
    }
    String pageTitle;

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
    static final String LOG = ImageGridFragment.class.getSimpleName();
}
