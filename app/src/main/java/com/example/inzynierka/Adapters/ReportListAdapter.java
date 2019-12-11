package com.example.inzynierka.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.EditReport.EditReportActivity;
import com.example.inzynierka.Report.ListOfReports.ListOfReportsViewModel;
import com.example.inzynierka.Report.ViewReport.ViewReportActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder>{
    List<ReportEntity> reportEntityList;
    ListOfReportsViewModel viewModel;
    Context context;
    final String TITLE = "Tytuł ";
    final String DATA = "Data ";
    final String LOCALIZATION = "Lokalizacja ";
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public ReportListAdapter(Context context, ListOfReportsViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDate = holder.textViewDate;
        TextView textViewLocaliztion = holder.textViewLocalization;
        ImageView imageView = holder.imageViewMainPhoto;
        Log.e("Report adapter", getItemCount()+"");
            String title = reportEntityList.get(position).getReportTitle();
            String date = reportEntityList.get(position).getReportDate();
            String localization = reportEntityList.get(position).getReportLocalization();
            textViewTitle.setText(TITLE + title);
            if (date != null) {
                textViewDate.setText(DATA + date);
            }
            if (localization != null) {
                textViewLocaliztion.setText(LOCALIZATION + localization);
            }
            Uri mainPhotoUri = reportEntityList.get(position).getMainPhoto();
            if (!mainPhotoUri.toString().equals("")) {
                Glide
                        .with(context)
                        .load(mainPhotoUri)
                        .centerCrop()
                        .into(holder.imageViewMainPhoto);
            } else {
                Glide
                        .with(context)
                        .load(R.drawable.noimge)
                        .centerCrop()
                        .into(holder.imageViewMainPhoto);
            }
    }
    @Override
    public int getItemCount() {
        if(reportEntityList!=null)
        return reportEntityList.size();
        return 0;
    }
    public void setReports(List<ReportEntity> reportEntities){
        reportEntityList = reportEntities;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView textViewDate;
        TextView textViewLocalization;
        ImageView imageViewMainPhoto;
        Button buttonDeleteReport;
        Button buttonEditReport;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewReportActivity.class);
                    intent.putExtra("currentReport", reportEntityList.get(getAdapterPosition()).getReportId());
                    context.startActivity(intent);
                }
            });
            this.textViewTitle = (TextView) itemView.findViewById(R.id.reportItemTextViewTitle);
            this.textViewDate = (TextView) itemView.findViewById(R.id.reportItemTextViewDate);
            this.textViewLocalization = (TextView) itemView.findViewById(R.id.reportItemTextViewLocalization);
            this.imageViewMainPhoto = (ImageView) itemView.findViewById(R.id.reportItemImageViewMainPhoto);
            this.buttonDeleteReport = (Button) itemView.findViewById(R.id.reportItemDeleteReport);
            this.buttonEditReport = (Button) itemView.findViewById(R.id.reportItemEditReport);
            buttonDeleteReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(R.string.deleteReportAlertDialog)
                            .setTitle(R.string.deleteReportTitle);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            viewModel.deleteReport(reportEntityList.get(getAdapterPosition()));
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            buttonEditReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditReportActivity.class);
                    intent.putExtra("currentReport", reportEntityList.get(getAdapterPosition()).getReportId());
                    context.startActivity(intent);
                }
            });
        }

    }

}

