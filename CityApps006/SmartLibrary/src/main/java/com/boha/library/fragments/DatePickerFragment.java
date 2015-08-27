package com.boha.library.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
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
        DateTime now = new DateTime();
        now = now.minusMonths(minusMonths);
        DateTime then = now.minusYears(minusYears);
        datePickerDialog.getDatePicker().setMaxDate(now.getMillis());
        datePickerDialog.getDatePicker().setMinDate(then.getMillis());
        datePickerDialog.getDatePicker().setCalendarViewShown(true);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = datePickerDialog.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                Log.d(LOG, "datePickerDialogField: " + datePickerDialogField.getName());
                String name = datePickerDialogField.getName();
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker dpx = (DatePicker) datePickerDialogField.get(datePickerDialog);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        String xname = datePickerField.getName();
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(dpx);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
        }
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (datePickerListener != null) {
            datePickerListener.onYearMonthPicked(year,month);
        }
    }
    DatePickerListener datePickerListener;
    int minusMonths, minusYears;

    public void setMinusYears(int minusYears) {
        this.minusYears = minusYears;
    }

    public void setMinusMonths(int minusMonths) {
        this.minusMonths = minusMonths;
    }

    public void setDatePickerListener(DatePickerListener datePickerListener) {
        this.datePickerListener = datePickerListener;
    }

    public interface DatePickerListener {
        void onYearMonthPicked(int year, int month);
    }
    static final String LOG = DatePickerDialog.class.getSimpleName();
}
