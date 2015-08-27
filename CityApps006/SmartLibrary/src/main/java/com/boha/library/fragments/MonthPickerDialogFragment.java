package com.boha.library.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.boha.library.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by aubreyM on 15/08/21.
 */
public class MonthPickerDialogFragment extends DialogFragment {

    Spinner monthSpinner, yearSpinner;
    ImageView iconCancel, iconSearch;
    MonthPickerListener listener;
    public interface MonthPickerListener {
        void onDatePicked(int month, int year);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.month_selection, container, false);
        monthSpinner = (Spinner)rootView.findViewById(R.id.spinnerMonth);
        yearSpinner = (Spinner)rootView.findViewById(R.id.spinnerYear);

        iconCancel = (ImageView)rootView.findViewById(R.id.iconCancel);
        iconSearch = (ImageView)rootView.findViewById(R.id.iconSearch);

        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onDatePicked(selectedMonth,selectedYear);
                dismiss();
            }
        });
        iconCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getDialog().setTitle("Request Statements");
        setMonthSpinner();
        setYearSpinner();
        return rootView;
    }

    public void setListener(MonthPickerListener listener) {
        this.listener = listener;
    }

    int selectedMonth, selectedYear;

    void setMonthSpinner() {
        List<String> list = Arrays.asList("January", "February", "March", "April", "May", "June",
                "July","August","September", "October","November","December");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.month_spinner, list);
        monthSpinner.setAdapter(adapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedMonth = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.roll(Calendar.MONTH, false);

        int lastMonth = calendar.get(Calendar.MONTH);
        monthSpinner.setSelection(lastMonth);


    }
    void setYearSpinner() {
        Calendar calendar = GregorianCalendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        final List<String> list = new ArrayList<>();
        list.add("" + currentYear);
        list.add("" + (currentYear - 1));
        list.add("" + (currentYear - 2));
        list.add("" + (currentYear - 3));
        list.add("" + (currentYear - 4));
        list.add("" + (currentYear - 5));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.year_spinner_item, list);
        yearSpinner.setAdapter(adapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedYear = Integer.parseInt(list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectedYear = currentYear;
    }
}
