package com.boha.library.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.boha.library.R;
import com.boha.library.dto.DistrictDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.SuburbDTO;
import com.boha.library.util.SharedUtil;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PickSuburbActivity extends AppCompatActivity {

    TextView txtDistrict, txtSuburb;
    Spinner distSpinner, subSpinner;
    AutoCompleteTextView autoDistrict;
    FloatingActionButton fab;
    Toolbar toolbar;
    Snackbar snackbar;
    MunicipalityDTO municipality;
    List<DistrictDTO> districts;
    List<SuburbDTO> suburbs;
    DistrictDTO district;
    SuburbDTO suburb;
    public static final String TAG = PickSuburbActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: .................................");
        setContentView(R.layout.activity_pick_suburb);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Suburb Selection");
        getSupportActionBar().setSubtitle("Enable subscription to news and alerts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        municipality = SharedUtil.getMunicipality(this);

        setFields();
        setDistrictSpinner();
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ......... quit");
        setResult(RESULT_OK);
        finish();

    }
    private void subscribe() {
        if (district == null) {
            Toast.makeText(this,"Please select District", Toast.LENGTH_SHORT).show();
            return;
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("district-" + district.getDistrictName().replaceAll(" ",""));
            showSnackBar("Subscribed to " + district.getDistrictName(),"OK","green");
            Log.w(TAG, ".......subscribed to: " + district.getDistrictName() );
        }
        if (suburb == null) {
            return;
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("suburb-" + suburb.getSuburbName().replaceAll(" ",""));
            showSnackBar("Subscribed to " + suburb.getSuburbName(),"OK","green");
            Log.e(TAG, "subscribed to suburb: " + suburb.getSuburbName() );
        }
    }

    private void setFields() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        txtDistrict = (TextView) findViewById(R.id.district);
        txtSuburb = (TextView) findViewById(R.id.suburb);
        distSpinner = (Spinner) findViewById(R.id.districtSpinner);
        subSpinner = (Spinner) findViewById(R.id.suburbSpinner);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribe();
            }
        });
    }

    private void setDistrictSpinner() {
        List<String> list = new ArrayList<>();
        districts = municipality.getDistricts();
        for (DistrictDTO d: districts) {
            list.add(d.getDistrictName());
        }
        Collections.sort(list);
        list.add(0,"Select District");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        distSpinner.setAdapter(adapter);
        distSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    district = null;
                    txtDistrict.setText("");
                    suburbs = new ArrayList<>();
                    setSuburbSpinner();
                } else {
                    district = districts.get(i - 1);
                    txtDistrict.setText(district.getDistrictName());
                    suburbs = district.getSuburbList();
                    setSuburbSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void setSuburbSpinner() {
        List<String> list = new ArrayList<>();
        for (SuburbDTO d: suburbs) {
            list.add(d.getSuburbName());
        }
        Collections.sort(list);
        if (!list.isEmpty()) {
            list.add(0,"Select Suburb");
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,list);
        subSpinner.setAdapter(adapter);
        subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    suburb = null;
                    txtSuburb.setText("");
                } else {
                    suburb = suburbs.get(i - 1);
                    txtSuburb.setText(suburb.getSuburbName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void showSnackBar(String title, String action, String color) {
        snackbar = Snackbar.make(toolbar,title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
