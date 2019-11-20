package com.example.inzynierka.Report.ListOfReports;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder> implements View.OnClickListener {
    List<ReportEntity> reportEntityList;
    Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        view.setOnClickListener(this);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public ReportListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDate = holder.textViewDate;
        TextView textViewLocaliztion = holder.textViewLocalization;
        ImageView imageView = holder.imageViewMainPhoto;
        if(reportEntityList == null){
            textViewTitle.setText("Dodaj swoją pierwsza relacje");
        }
        else {
            textViewTitle.setText(reportEntityList.get(position).getReportTitle());
            textViewDate.setText(reportEntityList.get(position).getReportDate().toString());
            textViewLocaliztion.setText(reportEntityList.get(position).getReportLocalization());
            Uri mainPhotoUri = reportEntityList.get(position).getMainPhoto();
            if(!mainPhotoUri.toString().equals("")) {
                Glide
                        .with(context)
                        .load(mainPhotoUri)
                        .centerCrop()
                        .into(holder.imageViewMainPhoto);
            }
            else {
                imageView.setVisibility(View.GONE);
            }
            }

        }

    @Override
    public int getItemCount() {
        if(reportEntityList!=null)
        return reportEntityList.size();
        return 0;
    }
    void setReports(List<ReportEntity> reportEntities){
        reportEntityList = reportEntities;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView textViewDate;
        TextView textViewLocalization;
        ImageView imageViewMainPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.reportItemTextViewTitle);
            this.textViewDate = (TextView) itemView.findViewById(R.id.reportItemTextViewDate);
            this.textViewLocalization = (TextView) itemView.findViewById(R.id.reportItemTextViewLocalization);
            this.imageViewMainPhoto = (ImageView) itemView.findViewById(R.id.reportItemImageViewMainPhoto);
        }
    }

}

