package com.boha.library.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boha.library.R;
import com.boha.library.adapters.AddressListAdapter;
import com.boha.library.dto.GISAddressDTO;

import java.util.List;

/**
 * Created by aubreyM on 15/07/30.
 */
public class AddressDialog extends DialogFragment {

    ListView listView;
    View view;
    AddressListAdapter adapter;
    List<GISAddressDTO> addressList;
    AddressListAdapter.AddressListener listener;

    public void setAddressList(List<GISAddressDTO> addressList) {
        this.addressList = addressList;
    }

    public void setListener(AddressListAdapter.AddressListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundle) {

        view = inflater.inflate(R.layout.fragment_dialog, null, false);
        listView = (ListView)view.findViewById(R.id.FD_list);
        getDialog().setTitle("Possible Addresses");

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        adapter = new AddressListAdapter(getActivity(), R.layout.address_item,
                addressList, new AddressListAdapter.AddressListener() {
            @Override
            public void onAddressClicked(GISAddressDTO address) {
                listener.onAddressClicked(address);
                dismiss();
            }
        });

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onAddressClicked(addressList.get(position));
                dismiss();
            }
        });

    }
}
