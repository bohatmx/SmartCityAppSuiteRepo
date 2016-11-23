package com.boha.library.jsonreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

        if (cFItem.getFirstName().isEmpty() || cFItem.getFirstName() == null) {
            holder.nameTxt.setVisibility(View.GONE);
        } else if (cFItem.getSurname().isEmpty() || cFItem.getSurname() == null) {
            holder.nameTxt.setVisibility(View.GONE);
        } else {
            holder.nameTxt.setText(cFItem.getFirstName() + " " + cFItem.getSurname());
        }
        if (cFItem.getWardNo().isEmpty() || cFItem.getWardNo() == null) {
            holder.wardNo.setVisibility(View.GONE);
        } else {
            holder.wardNo.setText(cFItem.getWardNo());
        }

        if (cFItem.getParty().isEmpty() || cFItem.getParty() == null) {
            holder.partyTxt.setVisibility(View.GONE);
        } else {
            holder.partyTxt.setText(cFItem.getParty());
        }

        if (cFItem.getTelephoneWork().isEmpty() || cFItem.getTelephoneWork() == null) {
            holder.workPhoneLayout.setVisibility(View.GONE);
        } else {
            holder.workPhonenumber.setText(cFItem.getTelephoneWork());
        }
        if (cFItem.getTelephoneHome().isEmpty() || cFItem.getTelephoneHome() == null) {
            holder.homePhoneLayout.setVisibility(View.GONE);
        } else {
            holder.homePhonenumber.setText(cFItem.getTelephoneHome());
        }
        if (cFItem.getFaxNumber().isEmpty() || cFItem.getFaxNumber() == null) {
            holder.faxPhoneLayout.setVisibility(View.GONE);
        } else {
            holder.faxPhonenumber.setText(cFItem.getFaxNumber());
        }
        if (cFItem.getMobile().isEmpty() || cFItem.getMobile() == null) {
            holder.mobilePhoneLayout.setVisibility(View.GONE);
        } else {
            holder.mobilePhonenumber.setText(cFItem.getMobile());
        }
        if (cFItem.getEmailAddress().isEmpty() || cFItem.getEmailAddress() == null) {
            holder.emailLayout.setVisibility(View.GONE);
        } else {
            holder.councillorEmail.setText(cFItem.getEmailAddress());
        }

    }

    @Override
    public int getItemCount() {
        return councillorsFeedItems == null ? 0 : councillorsFeedItems.size();
    }

    boolean openLayout, closedLayout;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profileIMG, partyIMG;
        TextView nameTxt, wardTxt, wardNo, partyTxt, workPhonenumber,
                homePhonenumber, faxPhonenumber, mobilePhonenumber,
                councillorEmail;
        RelativeLayout contactLayout, workPhoneLayout, homePhoneLayout,
                        faxPhoneLayout, mobilePhoneLayout, emailLayout;
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

            contactLayout = (RelativeLayout) itemView.findViewById(R.id.councillor_contact_layout);
            workPhoneLayout = (RelativeLayout) itemView.findViewById(R.id.lay1);
            homePhoneLayout = (RelativeLayout) itemView.findViewById(R.id.lay2);
            faxPhoneLayout = (RelativeLayout) itemView.findViewById(R.id.lay3);
            mobilePhoneLayout = (RelativeLayout) itemView.findViewById(R.id.lay4);
            emailLayout = (RelativeLayout) itemView.findViewById(R.id.lay5);
            contactLayout.setVisibility(View.GONE);
            closedLayout = true;

            if (closedLayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contactLayout.setVisibility(View.VISIBLE);
                        closedLayout = false;
                        openLayout = true;
                    }
                });
            }
            if (openLayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contactLayout.setVisibility(View.GONE);
                        openLayout = false;
                        closedLayout = true;
                    }
                });
            }


        }
    }
}
