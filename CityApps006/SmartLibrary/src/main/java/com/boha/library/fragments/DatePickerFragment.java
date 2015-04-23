package com.boha.library.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.joda.time.DateTime;

import java.util.Calendar;

/**
 * Created by aubreyM on 15/04/16.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    DatePickerDialog datePickerDialog;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
//        ((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem()
//                .getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        DateTime now = new DateTime();
        DateTime then = now.minusYears(5);
        datePickerDialog.getDatePicker().setMaxDate(now.getMillis());
        datePickerDialog.getDatePicker().setMinDate(then.getMillis());
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (datePickerListener != null) {
            datePickerListener.onYearMonthPicked(year,month);
        }
    }
    DatePickerListener datePickerListener;

    public void setDatePickerListener(DatePickerListener datePickerListener) {
        this.datePickerListener = datePickerListener;
    }

    public interface DatePickerListener {
        void onYearMonthPicked(int year, int month);
    }
}
