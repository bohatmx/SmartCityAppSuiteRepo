package com.boha.library.jsonreader;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;

import com.boha.library.R;

import java.util.ArrayList;

/**
 * Created by Nkululeko on 2016/11/18.
 */

public class CouncillorsReadAdapter extends RecyclerView.Adapter<CouncillorsReadAdapter.MyViewHolder> {
    ArrayList<CouncillorsFeedItem> councillorsFeedItems;
    Context context;
    public CouncillorsReadAdapter(Context context, ArrayList<CouncillorsFeedItem> councillorsFeedItems) {
        this.councillorsFeedItems = councillorsFeedItems;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.councillor_item,parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final CouncillorsFeedItem cFItem = councillorsFeedItems.get(position);

        if (cFItem.getFirstName() == null) {
            holder.nameTxt.setVisibility(View.GONE);
        } else if (cFItem.getSurname() == null) {
            holder.nameTxt.setVisibility(View.GONE);
        } else {
            holder.nameTxt.setText(cFItem.getFirstName() + " " + cFItem.getSurname());
        }
        if (cFItem.getWardNo() == null) {
            holder.wardNo.setVisibility(View.GONE);
        } else {
            holder.wardNo.setText(cFItem.getWardNo());
        }

        if (cFItem.getParty() == null) {
            holder.partyTxt.setVisibility(View.GONE);
        } else {
            holder.partyTxt.setText(cFItem.getParty());
        }
        if (cFItem.getParty().matches("ANC")) {
            holder.partyIMG.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.anc));
        }else if (cFItem.getParty().matches("DA")) {
            holder.partyIMG.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.da));
        }

        if (cFItem.getTelephoneWork() == null) {
            holder.workPhoneLayout.setVisibility(View.GONE);
        } else {
            holder.workPhonenumber.setText(cFItem.getTelephoneWork());
        }
        if (cFItem.getTelephoneHome() == null) {
            holder.homePhoneLayout.setVisibility(View.GONE);
        } else {
            holder.homePhonenumber.setText(cFItem.getTelephoneHome());
        }
        if (cFItem.getFaxNumber() == null) {
            holder.faxPhoneLayout.setVisibility(View.GONE);

        } else {
            holder.faxPhonenumber.setText(cFItem.getFaxNumber());
        }
        if (cFItem.getMobile() == null) {
            holder.mobilePhoneLayout.setVisibility(View.GONE);
        } else {
            holder.mobilePhonenumber.setText(cFItem.getMobile());
        }
        if (cFItem.getEmailAddress() == null) {
            holder.emailLayout.setVisibility(View.GONE);
        } else {
            holder.councillorEmail.setText(cFItem.getEmailAddress());
        }

    }

    @Override
    public int getItemCount() {
        return councillorsFeedItems == null ? 0 : councillorsFeedItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profileIMG, partyIMG;
        TextView nameTxt, wardTxt, wardNo, partyTxt, workPhonenumber,
                homePhonenumber, faxPhonenumber, mobilePhonenumber,
                councillorEmail;
        RelativeLayout  workPhoneLayout, homePhoneLayout,
                        faxPhoneLayout, mobilePhoneLayout, emailLayout;
        CardView contactLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            profileIMG = (ImageView) itemView.findViewById(R.id.profile_image);
            nameTxt = (TextView) itemView.findViewById(R.id.profile_name);
            wardTxt = (TextView) itemView.findViewById(R.id.ward_label);
            wardNo = (TextView) itemView.findViewById(R.id.ward_number);
            partyIMG = (ImageView) itemView.findViewById(R.id.profile_party_icon);
            partyTxt = (TextView) itemView.findViewById(R.id.party_name);

            workPhonenumber = (TextView) itemView.findViewById(R.id.councillor_work_phonenumber);
            homePhonenumber = (TextView) itemView.findViewById(R.id.councillor_home_phonenumber);
            faxPhonenumber = (TextView) itemView.findViewById(R.id.councillor_fax_phonenumber);
            mobilePhonenumber = (TextView) itemView.findViewById(R.id.councillor_mobile_phonenumber);
            councillorEmail = (TextView) itemView.findViewById(R.id.councillor_email);

            contactLayout = (CardView) itemView.findViewById(R.id.councillor_contact_layout);
            workPhoneLayout = (RelativeLayout) itemView.findViewById(R.id.lay1);
            homePhoneLayout = (RelativeLayout) itemView.findViewById(R.id.lay2);
            faxPhoneLayout = (RelativeLayout) itemView.findViewById(R.id.lay3);
            mobilePhoneLayout = (RelativeLayout) itemView.findViewById(R.id.lay4);
            emailLayout = (RelativeLayout) itemView.findViewById(R.id.lay5);
            contactLayout.setVisibility(View.GONE);

            profileIMG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contactLayout.getVisibility() == View.GONE) {
                        contactLayout.setVisibility(View.VISIBLE);
                    } else {
                        contactLayout.setVisibility(View.GONE);
                    }
                }
            });
            nameTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contactLayout.getVisibility() == View.GONE) {
                        contactLayout.setVisibility(View.VISIBLE);
                    } else {
                        contactLayout.setVisibility(View.GONE);
                    }
                }
            });
            partyIMG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contactLayout.getVisibility() == View.GONE) {
                        contactLayout.setVisibility(View.VISIBLE);
                    } else {
                        contactLayout.setVisibility(View.GONE);
                    }
                }
            });


        }
    }
}
