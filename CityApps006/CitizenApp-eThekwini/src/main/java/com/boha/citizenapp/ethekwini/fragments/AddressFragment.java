package com.boha.citizenapp.ethekwini.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boha.citizenapp.ethekwini.R;
import com.boha.library.adapters.AddressListAdapter;
import com.boha.library.dto.GISAddressDTO;
import com.boha.library.fragments.AddressDialog;
import com.boha.library.fragments.PageFragment;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.NetUtil;
import com.boha.library.util.ResidentialAddress;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

import java.util.List;

public class AddressFragment extends Fragment implements PageFragment {
    private AddressFragmentListener mListener;
    private EditText edNumber, eStreet, eSuburb, eCity;
    private Button btn;
    private View view;
    TextInputLayout tiNumber, tiStreet, tiSuburb, tiCity;
    ImageView iconSearch;
    TextView txtLabel;
    ScrollView scrollView;

    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public AddressFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.address_edit, container, false);
        setFields();
        return view;
    }

    private void setFields() {
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        edNumber = (EditText) view.findViewById(R.id.AE_streetNumber);
        eStreet = (EditText) view.findViewById(R.id.AE_street);
        eSuburb = (EditText) view.findViewById(R.id.AE_suburb);
        eCity = (EditText) view.findViewById(R.id.AE_city);
        btn = (Button) view.findViewById(R.id.AE_submit);
        txtLabel = (TextView) view.findViewById(R.id.AE_label);
        iconSearch = (ImageView) view.findViewById(R.id.AE_search);

        tiNumber = (TextInputLayout) view.findViewById(R.id.AE_snLayout);
        tiStreet = (TextInputLayout) view.findViewById(R.id.AE_streetLayout);
        tiSuburb = (TextInputLayout) view.findViewById(R.id.AE_suburbLayout);
        tiCity = (TextInputLayout) view.findViewById(R.id.AE_cityLayout);

        hideAddressFields();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddressLocally();
            }
        });
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edNumber.getWindowToken(), 0);

        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        txtLabel.setText("Please enter your Street Name and the app will help you update your residential address. " +
                "This is necessary to have your complaints serviced promptly.");


    }

    List<GISAddressDTO> addressList;
    GISAddressDTO selectedAddress;

    private void showAddressFields() {
        tiNumber.setVisibility(View.VISIBLE);
        tiSuburb.setVisibility(View.VISIBLE);
        tiCity.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);

        eStreet.setText(selectedAddress.getStreet());
        eSuburb.setText(selectedAddress.getSuburb());
        eCity.setText(selectedAddress.getCity());

        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    private void hideAddressFields() {
        tiNumber.setVisibility(View.VISIBLE);
        tiSuburb.setVisibility(View.GONE);
        tiCity.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);

        eStreet.setText("");
        eSuburb.setText("");
        eCity.setText("");
        edNumber.setText("");

    }

    private void processResult() {

        if (addressList.size() > 1) {
            showMultiAddressDialog();
            return;
        }
        selectedAddress = addressList.get(0);
        showAddressFields();
    }

    public void showMultiAddressDialog() {

        final AddressDialog addressDialog = new AddressDialog();
        addressDialog.setAddressList(addressList);
        addressDialog.setListener(new AddressListAdapter.AddressListener() {
            @Override
            public void onAddressClicked(GISAddressDTO address) {
                selectedAddress = address;
                addressDialog.dismiss();
                showAddressFields();

            }
        });

        addressDialog.show(getFragmentManager(), "fgg");
    }

    private void search() {
        if (eStreet.getText().toString().isEmpty()) {
            Util.showErrorToast(getActivity(), "Please enter the Street name");
            return;
        }
        GISAddressDTO dto = new GISAddressDTO();
        dto.setStreet(eStreet.getText().toString());

        RequestDTO w = new RequestDTO(RequestDTO.VERIFY_ADDRESS);
        w.setMunicipalityID(SharedUtil.getMunicipality(getActivity()).getMunicipalityID());
        w.setAddress(dto);

        mListener.setBusy(true);
        NetUtil.sendRequest(getActivity(), w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.setBusy(false);
                        addressList = response.getAddressList();
                        processResult();
                    }
                });
            }

            @Override
            public void onError(final String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.setBusy(false);
                        Util.showErrorToast(getActivity(), message);
                    }
                });
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }

    private void saveAddressLocally() {
        if (edNumber.getText().toString().isEmpty()) {
            tiNumber.setError("Please enter street number");
            return;
        }
        if (eStreet.getText().toString().isEmpty()) {
            tiStreet.setError("Please enter street name");
            return;
        }
        if (eSuburb.getText().toString().isEmpty()) {
            tiSuburb.setError("Please enter suburb name");
            return;
        }
        if (eCity.getText().toString().isEmpty()) {
            tiCity.setError("Please enter city name");
            return;
        }

        SharedUtil.saveAddress(getActivity(), new ResidentialAddress(
                edNumber.getText().toString().trim(),
                eStreet.getText().toString().trim(), eSuburb.getText().toString().trim(),
                eCity.getText().toString().trim()));

        mListener.onAddressUpdated();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddressFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddressFragmentListener");
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

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {

    }

    @Override
    public String getPageTitle() {
        return null;
    }

    @Override
    public void setPageTitle(String pageTitle) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface AddressFragmentListener {
        void onAddressUpdated();

        void setBusy(boolean busy);
    }

}
