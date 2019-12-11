package com.example.inzynierka.Report.AddReport;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.inzynierka.R;
import com.example.inzynierka.Report.DataPickerWrapper;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class AddReportDescriptionFragment extends Fragment implements TextWatcher {
    AddReportViewModel addReportViewModel;
    TextInputEditText editTextReportTitle;
    TextInputEditText editTextDescription;
    TextInputEditText editTextLocalization;
    TextView textViewDate;
    Activity activity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_add_report_description, container, false);

        addReportViewModel =  ViewModelProviders.of(getActivity()).get(AddReportViewModel.class);
        Log.i("Fragment", addReportViewModel.toString());
        editTextReportTitle = rootView.findViewById(R.id.addReportEditTextTitle);
        textViewDate = rootView.findViewById(R.id.addReportTextViewDate);
        editTextReportTitle.addTextChangedListener(this);
        editTextDescription = (TextInputEditText) rootView.findViewById(R.id.reportAddEditTextReportDescription);
        editTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addReportViewModel.setReportDescription(editTextDescription.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTextLocalization = (TextInputEditText) rootView.findViewById(R.id.reportAddEditTextLocalization);
        editTextLocalization.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addReportViewModel.setReportLocalization(editTextLocalization.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        DataPickerWrapper dataPickerWrapper = new DataPickerWrapper(textViewDate, getContext(), addReportViewModel);
        addReportViewModel.setReportDate(dataPickerWrapper.getDateFormat().format(dataPickerWrapper.getDate()));

        return rootView;
    }
    private String getTitleText(View view){
        return editTextReportTitle.getText().toString();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        addReportViewModel.setReportTitle(charSequence.toString());
        Log.i("Zmiana tekstu ", addReportViewModel.getReportTitle()+"");
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
