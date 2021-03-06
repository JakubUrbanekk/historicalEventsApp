package com.example.inzynierka.Report;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.inzynierka.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DataPickerWrapper implements android.view.View.OnFocusChangeListener, android.app.DatePickerDialog.OnDateSetListener{
    private final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private TextView display;
    private DatePickerDialog dialog = null;
    private Date currentDate = null;
    private Context context;

    public DataPickerWrapper(TextView display, Context context) {
        this.display = display;
        this.display.setFocusable(true);
        this.display.setClickable(true);
        this.display.setOnFocusChangeListener(this);
        this.setDate(new Date());
        this.context = context;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    @Override
    public void onDateSet(
        final DatePicker view,
        final int year,
        final int month,
        final int dayOfMonth) {

            final Calendar calendar = new GregorianCalendar(
                    year, month, dayOfMonth
            );

            setDate(calendar.getTime());
    }
    public void setDate(final Date date) {
        if(date == null) {
            throw new IllegalArgumentException("date may not be null");
        }

        this.currentDate = (Date) date.clone();
        this.display.setText(dateFormat.format(currentDate));
        if(this.dialog != null) {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(currentDate);
            this.dialog.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        }

    }
    void openDatePickerDialog() {

        if (dialog == null) {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(getDate());
            dialog = new DatePickerDialog(
                    display.getContext(),
                    R.style.DialogTheme,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        }
        dialog.show();
    }
    public Date getDate() {
        return currentDate;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b)
        openDatePickerDialog();
    }
}
