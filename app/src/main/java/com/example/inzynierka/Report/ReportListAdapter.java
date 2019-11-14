package com.example.inzynierka.Report;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inzynierka.Database.Report.ReportDao;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;
import com.example.inzynierka.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder> implements View.OnClickListener {
    List<ReportEntity> reportEntityList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        view.setOnClickListener(this);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView textViewDescription = holder.textViewDescription;
        ImageView imageView = holder.imageViewMainPhoto;
        if(reportEntityList == null){
            textViewDescription.setText("Dodaj swoją pierwsza relacje");
        }
        else {
            textViewDescription.setText(reportEntityList.get(position).getReportDescription());
            imageView.setImageURI(reportEntityList.get(position).getMainPhoto());
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
        TextView textViewDescription;
        ImageView imageViewMainPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewDescription = (TextView) itemView.findViewById(R.id.reportItemTextViewDescription);
            this.imageViewMainPhoto = (ImageView) itemView.findViewById(R.id.reportItemImageViewMainPhoto);
        }
    }

}

