package com.boha.citizenapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.citizenapp.R;
import com.boha.citizenapp.adapters.PictureAdapter;
import com.boha.citylibrary.dto.AlertDTO;
import com.boha.citylibrary.transfer.PhotoUploadDTO;
import com.boha.citylibrary.util.DividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AlertPictureGridActivity extends ActionBarActivity {

    Context ctx;
    PictureAdapter adapter;
    List<PhotoUploadDTO> photoList;
    int lastIndex;
    AlertDTO alert;
    Button btnMap;
    ImageView image;
    TextView txtAlertType, txtDate, txtTime;
    static final String LOG = AlertPictureGridActivity.class.getSimpleName();
    static final Locale LOCALE = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMMM yyyy");
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_alert_gallery);
        ctx = getApplicationContext();
        setFields();
        setTitle("Alert Picture Gallery");

        alert = (AlertDTO)getIntent().getSerializableExtra("alert");
       // photoList = alert.getPhotoUploadList();
        txtAlertType.setText(alert.getAlertType().getAlertTypeNmae());
        txtDate.setText(sdfDate.format(alert.getUpdated()));
        txtTime.setText(sdfTime.format(alert.getUpdated()));

        setGrid();

    }

    private void setFields() {
        txtAlertType = (TextView)findViewById(R.id.PIC_GRID_alertType);
        txtDate = (TextView)findViewById(R.id.PIC_GRID_date);
        txtTime = (TextView)findViewById(R.id.PIC_GRID_time);
        btnMap = (Button)findViewById(R.id.PIC_GRID_btnMap);
        image = (ImageView)findViewById(R.id.PIC_GRID_image);
        list = (RecyclerView) findViewById(R.id.PIC_GRID_recyclerView);
    }


    RecyclerView list;
    private void setImage() {
//        String url = Util.getAlertImageURL(alert.getPhotoUploadList().get(0));
//        ImageLoader.getInstance().displayImage(url, image);
        image.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);
    }
    private void setGrid() {

        list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list.setItemAnimator(new DefaultItemAnimator());
        list.addItemDecoration(new DividerItemDecoration(ctx, RecyclerView.VERTICAL));

        adapter = new PictureAdapter(photoList,
                ctx, new PictureAdapter.PictureListener() {
            @Override
            public void onPictureClicked(int position) {
                Log.e(LOG, "Picture clicked..., position = " + position);
                lastIndex = position;
                Intent i = new Intent(getApplicationContext(),FullPhotoActivity.class);
                i.putExtra("alert", alert);
                startActivity(i);
            }
        });

        list.setAdapter(adapter);
        if (lastIndex < photoList.size()) {
            list.getLayoutManager().scrollToPosition(lastIndex);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_alert_picture_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
