package com.boha.citizenapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.citizenapp.R;
import com.boha.citizenapp.adapters.ComplaintPictureAdapter;
import com.boha.library.dto.AlertTypeDTO;
import com.boha.library.dto.ComplaintDTO;
import com.boha.library.dto.ComplaintImageDTO;
import com.boha.library.util.DividerItemDecoration;
import com.boha.library.util.Util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ComplaintPictureGridActivity extends ActionBarActivity {

    Context ctx;
    ComplaintPictureAdapter adapter;
    List<ComplaintImageDTO> photoList;
    int lastIndex;
    ComplaintDTO complaint;
    ImageView icon;
    TextView txtComplaintType, txtDate, txtTime, txtAddress, txtFab;
    View addressLayout, topLayout;
    boolean isOpen;
    static final String LOG = ComplaintPictureGridActivity.class.getSimpleName();
    static final Locale LOCALE = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMMM yyyy", LOCALE);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", LOCALE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_complaint_gallery);
        ctx = getApplicationContext();
        setFields();

        complaint = (ComplaintDTO) getIntent().getSerializableExtra("complaint");
        photoList = complaint.getComplaintImageList();
        txtComplaintType.setText(complaint.getComplaintType().getComplaintTypeName());
        txtDate.setText(sdfDate.format(complaint.getComplaintDate()));
        txtTime.setText(sdfTime.format(complaint.getComplaintDate()));

        switch (complaint.getComplaintType().getColor()) {
            case AlertTypeDTO.RED:
                topLayout.setBackgroundColor(ctx.getResources().getColor(R.color.indian_red));
                break;
            case AlertTypeDTO.AMBER:
                topLayout.setBackgroundColor(ctx.getResources().getColor(R.color.rebecca_purple));
                break;
            case AlertTypeDTO.GREEN:
                topLayout.setBackgroundColor(ctx.getResources().getColor(R.color.green));
                break;
        }

        Util.flashSeveralTimes(txtFab,300,3, null) ;
//        MunicipalityDTO municipality = SharedUtil.getMunicipality(ctx);
//        ActionBar actionBar = getSupportActionBar();
//        Util.setCustomActionBar(ctx,
//                actionBar,
//                municipality.getMunicipalityName(),
//                ctx.getResources().getDrawable(com.boha.citizenapp.R.drawable.logo));


        setGrid();

    }

    private void setFields() {
        topLayout = findViewById(R.id.CCX_titleLayout);
        addressLayout = findViewById(R.id.CCX_addressLayout);
        addressLayout.setVisibility(View.GONE);
        txtAddress = (TextView) findViewById(R.id.CCX_address);
        txtFab = (TextView) findViewById(R.id.CCX_fab);
        txtComplaintType = (TextView) findViewById(R.id.CCX_title);
        txtDate = (TextView) findViewById(R.id.CCX_date);
        txtTime = (TextView) findViewById(R.id.CCX_time);
        //icon = (ImageView) findViewById(R.id.CCX_icon);
        list = (RecyclerView) findViewById(R.id.PG_recyclerView);

        txtComplaintType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtComplaintType, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (isOpen) {
                            isOpen = false;
                            Util.collapse(addressLayout, 300, null);
                        } else {
                            //new GeoTask().execute();
                        }
                    }
                });
            }
        });
        txtFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtFab, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (isOpen) {
                            isOpen = false;
                            Util.collapse(addressLayout,300,null);
                        } else {
                            //new GeoTask().execute();
                        }
                    }
                });
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(icon, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(ctx, AlertMapActivity.class);
                        intent.putExtra("complaint", complaint);
                        startActivity(intent);
                    }
                });
            }
        });
    }


    RecyclerView list;

    private void setGrid() {
        LinearLayoutManager lm = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);

        lm.setSmoothScrollbarEnabled(true);
        list.setLayoutManager(lm);

        list.setItemAnimator(new DefaultItemAnimator());
        list.addItemDecoration(new DividerItemDecoration(ctx, RecyclerView.HORIZONTAL));

        adapter = new ComplaintPictureAdapter(photoList,ctx);
        list.setAdapter(adapter);
        if (lastIndex < photoList.size()) {
            list.getLayoutManager().scrollToPosition(lastIndex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_alert_picture_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    
}
