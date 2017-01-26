package com.boha.staffapp.ethekwini.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.library.dto.DistrictDTO;
import com.boha.library.dto.DistrictMessageDTO;
import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.dto.SuburbDTO;
import com.boha.library.dto.SuburbMessageDTO;
import com.boha.library.services.SmartCityMessagingService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.boha.staffapp.ethekwini.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    EditText districtmsgtxt, suburbmsgtxt;
    Button districtbtn, suburbbtn;
    MunicipalityDTO municipality;
    List<DistrictDTO> districts;
    Spinner distSpinner, subSpinner;
    DistrictDTO district;
    SuburbDTO suburb;
    TextView txtDistrict, txtSuburb;
    List<SuburbDTO> suburbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ctx = getApplicationContext();

        if(municipality != null) {
        municipality =  SharedUtil.getMunicipality(this);
        /*if (SharedUtil.getMunicipality(ctx) != null) {
            setDistrictSpinner();
        }*/


        setDistrictSpinner();
        setSuburbSpinner();
        } else {
            getMunicipality();
        }
        setFields();
    }



    private void setFields() {

        districtmsgtxt = (EditText) findViewById(R.id.districtmsg);
        distSpinner = (Spinner) findViewById(R.id.districtmSpinner);
        districtbtn = (Button) findViewById(R.id.districtbtn);
        subSpinner = (Spinner) findViewById(R.id.suburbmSpinner);
        suburbmsgtxt = (EditText) findViewById(R.id.suburbmsg);
        suburbbtn = (Button) findViewById(R.id.suburbbtn);

        districtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(districtbtn, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                    sendDistrictMessage();
                    }
                });
            }
        });

        suburbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(suburbbtn, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                    sendSuburbMessage();
                    }
                });
            }
        });
    }


    private void setDistrictSpinner() {
        List<String> list = new ArrayList<>();
        //Log.i(LOG, "@@@@---?>> Districts: "+ municipality.getDistricts().size());
        //if (municipality.getDistricts() != null) {
            districts = municipality.getDistricts();
        //}
         if (districts != null) {
        for (DistrictDTO d : districts) {
            list.add(d.getDistrictName());
        }
           }
        //  Collections.sort(list);
        list.add(0, "Select District");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        distSpinner.setAdapter(adapter);
        distSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    district = null;
                //    txtDistrict.setText("");
                //    suburbs = new ArrayList<>();
                 //    setSuburbSpinner();
                    return;
                } else {
                    district = districts.get(i - 1);
                //    txtDistrict.setText(district.getDistrictName());
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
                //    txtSuburb.setText("");
                    return;
                } else {
                    suburb = suburbs.get(i - 1);
                   // txtSuburb.setText(suburb.getSuburbName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    static final String MUNICIPALITY_NAME = "eThekwini";
    static final int ONE_SECOND = 1000, QUICK = 200, FIVE_SECONDS = ONE_SECOND * 5;

    private void getMunicipality() {
        municipality = SharedUtil.getMunicipality(ctx);
        if (municipality == null) {
            RequestDTO w = new RequestDTO(RequestDTO.GET_MUNICIPALITY_BY_NAME);
            w.setMunicipalityName(MUNICIPALITY_NAME);
          //  progressBar.setVisibility(View.VISIBLE);
            NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
                @Override
                public void onResponse(final ResponseDTO response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        //    progressBar.setVisibility(View.GONE);
                            if (response.getStatusCode() == 0) {

                                municipality = response.getMunicipalityList().get(0);
                                SharedUtil.saveMunicipality(ctx, municipality);
                                setDistrictSpinner();
                             /*   Util.expand(actionsView, ONE_SECOND, null);
                                heroImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Util.flashOnce(heroImage, 20, new Util.UtilAnimationListener() {
                                            @Override
                                            public void onAnimationEnded() {
                                                if (actionsView.getVisibility() == View.GONE) {
                                                    if (staff == null) {
                                                        Util.expand(actionsView, ONE_SECOND, null);
                                                    } else {
//                                                        Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
//                                                        startActivity(intent);
                                                    }
                                                }
                                                //checkVirginity();
                                            }
                                        });
                                    }
                                }); */
                            }
                        }
                    });
                }

                @Override
                public void onError(final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.showErrorToast(ctx, message);
                        }
                    });
                }

                @Override
                public void onWebSocketClose() {

                }
            });

        } else {
            Log.i(LOG, "Municipality found: " + municipality.getMunicipalityName());
            setDistrictSpinner();
        }
    }

    Context ctx;
    //{"message":"ADAMS RURAL: Google turns Firebase into its unified platform for mobile developers"
    // ,"districtName":"ADAMS RURAL","messageDate":1484815524167,"messageExpiryDate":1484816524167,"districtID":92}
    private void sendDistrictMessage() {
        RequestDTO r = new RequestDTO(RequestDTO.SEND_DISTRICT_MESSAGE);
        r.setDistrictID(district.getDistrictID());
        r.setMessage(districtmsgtxt.getText().toString());
        r.setDistrictName(district.getDistrictName());
        r.setMessageDate(new Date().getTime());
        r.setMessageExpiryDate(new Date().getTime() + (1000 * 1000));
        NetUtil.sendDistrictMessageRequest(ctx, r, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(ResponseDTO response) {
                Log.i(LOG, response.getMessage()+  ":) --->>>> working!!!");
                Util.showSnackBar(districtbtn,"District message sent successfully","OK", Color.parseColor("green"));

            }

            @Override
            public void onError(String message) {
                Log.e(LOG, message);
            }

            @Override
            public void onWebSocketClose() {

            }
        });

    }

    //{"message":"MKHAZANA: Bring your words to life with updated and entirely new emoji, and the ability to use two or more languages at the same time.",
    // "suburbName":"MKHAZANA","messageDate":1484815524169,"messageExpiryDate":1484816524169,"suburbID":566}
    private void sendSuburbMessage() {
     RequestDTO r = new RequestDTO(RequestDTO.SEND_SUBURB_MESSAGE);
        r.setSuburbID(suburb.getSuburbID());
        r.setMessage(suburbmsgtxt.getText().toString());
        r.setSuburbName(suburb.getSuburbName());
        r.setMessageDate(new Date().getTime());
        r.setMessageExpiryDate(new Date().getTime() + (1000 * 1000));
        NetUtil.sendSuburbMessageRequest(ctx, r, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(ResponseDTO response) {
                Log.d(LOG, response.getMessage() + ":) ----->> working!!");
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }

    static final String LOG = MessageActivity.class.getSimpleName();
}
