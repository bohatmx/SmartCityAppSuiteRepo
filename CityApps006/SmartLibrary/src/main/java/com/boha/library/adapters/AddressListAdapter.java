package com.boha.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.GISAddressDTO;

import java.util.List;

public class AddressListAdapter extends ArrayAdapter<GISAddressDTO> {

    AddressListener listener;

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<GISAddressDTO> mList;
    private Context ctx;
    static final String LOG = AddressListAdapter.class.getSimpleName();

    public AddressListAdapter(Context context, int textViewResourceId,
                              List<GISAddressDTO> list, AddressListener listener) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        this.listener = listener;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        protected TextView txtNumber, txtAddress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtNumber = (TextView) convertView.findViewById(R.id.ADDR_number);
            item.txtAddress = (TextView) convertView.findViewById(R.id.ADDR_address);
            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final GISAddressDTO p = mList.get(position);
        item.txtNumber.setText("" + (position + 1));
        StringBuilder sb = new StringBuilder();
        if (p.getStreet() != null) {
            sb.append(p.getStreet()).append("\n");
        }
        if (p.getSuburb() != null) {
            sb.append(p.getSuburb()).append("\n");
        }
        if (p.getCity() != null) {
            sb.append(p.getCity());
        }
        item.txtAddress.setText(sb.toString());
        item.txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddressClicked(p);
            }
        });

        return (convertView);
    }

    public interface AddressListener {
        public void onAddressClicked(GISAddressDTO address);
    }

}
