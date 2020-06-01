package com.example.inzynierka.Database.Report;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.widget.TextView;

import com.example.inzynierka.CustomDate.CustomData;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "reports")
@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReportEntity implements Parcelable {
    @PrimaryKey (autoGenerate = true)
    @NonNull
    Integer reportId;
    String reportDescription;
    CustomData reportDate;
    @Nullable
    PhotoEntity mainPhoto;
    String reportLocalization;
    String reportTitle;
    String category;
    String epoka;
    String weapon;
    String cloth;
    String accessory;
    String vehicle;

    public ReportEntity() {
    }
    public String getReportLocalization() {
        return reportLocalization;
    }
    public void setReportLocalization(String reportLocalization) {
        this.reportLocalization = reportLocalization;
    }
    public String getReportTitle() {
        return reportTitle;
    }
    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }
    @NonNull
    public Integer getReportId() {
        return reportId;
    }
    public void setReportId(@NonNull Integer reportId) {
        this.reportId = reportId;
    }
    public String getReportDescription() {
        return reportDescription;
    }
    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }
    public String getReportDate() {
        return reportDate.toString();
    }
    public void setReportDate(String reportDate) {
        CustomData customData = new CustomData(reportDate);
        this.reportDate = customData;
    }
    public Uri getMainPhoto() {
        return mainPhoto.getPhotoUri();
    }
    public PhotoEntity getMainPhotoEntity(){
        return mainPhoto;
    }
    public void setMainPhoto(PhotoEntity photoEntity){
        this.mainPhoto = photoEntity;
    }
    public CustomData getDate(){
        return reportDate;
    }
    private void setTitle(TextInputEditText titleET) {
        Editable titleText = titleET.getText();
        if (titleText != null) {
            reportTitle = titleText.toString();
        }
    }
    private void setLocalization(TextInputEditText localizationET) {
        Editable localizationText = localizationET.getText();
        if (localizationText != null) {
            reportLocalization = localizationText.toString();
        }
    }
    private void setDescription(TextInputEditText descriptionET){
            Editable descriptionText = descriptionET.getText();
            if (descriptionText!=null){
                reportDescription = descriptionText.toString();
            }
    }
    private void setDate(TextView textView){
        CharSequence dateText = textView.getText();
        if (dateText != null){
            reportDate = new CustomData(dateText.toString());
        }
    }

    public void setReportData(TextInputEditText titleET, TextInputEditText descriptionET,
                              TextInputEditText localizationET, TextView dateTV, PhotoEntity mainPhoto){
        setTitle(titleET);
        setDescription(descriptionET);
        setLocalization(localizationET);
        setDate(dateTV);
        setMainPhoto(mainPhoto);
    }
    public void setReportData(String title, String description, String localization , String date, PhotoEntity photoEntity){
        this.reportTitle= title;
        this.reportDescription = description;
        this.reportLocalization = localization;
        this.reportDate = new CustomData(date);
        this.mainPhoto = photoEntity;
    }


    protected ReportEntity(Parcel in) {
        reportId = in.readByte() == 0x00 ? null : in.readInt();
        reportDescription = in.readString();
        reportDate = (CustomData) in.readValue(CustomData.class.getClassLoader());
        mainPhoto = (PhotoEntity) in.readValue(PhotoEntity.class.getClassLoader());
        reportLocalization = in.readString();
        reportTitle = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (reportId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(reportId);
        }
        dest.writeString(reportDescription);
        dest.writeValue(reportDate);
        dest.writeValue(mainPhoto);
        dest.writeString(reportLocalization);
        dest.writeString(reportTitle);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReportEntity> CREATOR = new Parcelable.Creator<ReportEntity>() {
        @Override
        public ReportEntity createFromParcel(Parcel in) {
            return new ReportEntity(in);
        }

        @Override
        public ReportEntity[] newArray(int size) {
            return new ReportEntity[size];
        }
    };
}
