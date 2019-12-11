package com.example.inzynierka.Report.EditReport;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.R;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class EditReportDescriptionFragment extends Fragment {

    private EditReportModel viewModel;
    TextInputEditText editTextReportTitle;
    TextInputEditText editTextDescription;
    TextInputEditText editTextLocalization;
    TextView textViewDate;
    ReportEntity currentReport;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_add_report_description, container, false);
        initView(rootView);
        viewModel = ViewModelProviders.of(getActivity()).get(EditReportModel.class);
        currentReport = viewModel.getCurrentReport();
        setTexts();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void initView(View view){
        editTextDescription = (TextInputEditText) view.findViewById(R.id.reportAddEditTextReportDescription);
        editTextReportTitle = (TextInputEditText) view.findViewById(R.id.addReportEditTextTitle);
        editTextLocalization = (TextInputEditText) view.findViewById(R.id.reportAddEditTextLocalization);
        textViewDate = (TextView) view.findViewById(R.id.addReportTextViewDate);
        editTextReportTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentReport.setReportTitle(editTextReportTitle.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentReport.setReportLocalization(editTextDescription.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextLocalization.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentReport.setReportLocalization(editTextLocalization.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void setTexts(){
        editTextReportTitle.setText(currentReport.getReportTitle());
        String reportDescription = currentReport.getReportDescription();
        if (editTextDescription!=null)
        editTextDescription.setText(reportDescription);
        String reportLocaliztion = currentReport.getReportLocalization();
        if(reportLocaliztion!=null)
        editTextLocalization.setText(reportLocaliztion);
        textViewDate.setText(currentReport.getReportDate());
    }
}
