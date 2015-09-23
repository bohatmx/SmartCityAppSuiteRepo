package com.boha.library.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.library.R;
import com.boha.library.adapters.FullPictureAdapter;
import com.boha.library.dto.AlertDTO;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FullPhotoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    int index;
    FullPictureAdapter adapter;
    AlertDTO alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);
        recyclerView = (RecyclerView)findViewById(R.id.FI_recyclerView);

        alert = (AlertDTO)getIntent().getSerializableExtra("complaint");
        setTitle("Alert Pictures");
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_full_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();

//         if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", loc);
}
