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

import com.boha.citizenapp.ethekwini.R;
import com.boha.library.util.ResidentialAddress;
import com.boha.library.util.SharedUtil;

public class AddressFragment extends Fragment {
    private AddressFragmentListener mListener;
    private EditText edNumber, eStreet, eSuburb, eCity;
    private Button btn;
    private View view;
    TextInputLayout tiNumber, tiStreet, tiSuburb, tiCity;


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
        edNumber = (EditText) view.findViewById(R.id.AE_streetNumber);
        eStreet = (EditText) view.findViewById(R.id.AE_street);
        eSuburb = (EditText) view.findViewById(R.id.AE_suburb);
        eCity = (EditText) view.findViewById(R.id.AE_city);
        btn = (Button) view.findViewById(R.id.AE_submit);

        tiNumber = (TextInputLayout) view.findViewById(R.id.AE_snLayout);
        tiStreet = (TextInputLayout) view.findViewById(R.id.AE_streetLayout);
        tiSuburb = (TextInputLayout) view.findViewById(R.id.AE_suburbLayout);
        tiCity = (TextInputLayout) view.findViewById(R.id.AE_cityLayout);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddress();
            }
        });
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edNumber.getWindowToken(), 0);

    }

    private void setAddress() {
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
        //todo - verify address via service
        SharedUtil.saveAddress(getActivity(), new ResidentialAddress(
                edNumber.getText().toString(),
                eStreet.getText().toString(), eSuburb.getText().toString(),
                eCity.getText().toString()));
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
