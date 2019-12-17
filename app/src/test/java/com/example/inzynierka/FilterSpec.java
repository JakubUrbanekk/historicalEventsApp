package com.example.inzynierka;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Report.ListOfReports.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilterSpec {
    Filter filter;
    List <PhotoEntity> photos;
    List <ReportEntity> reportsWithPhotos;
    List <ReportEntity> reportsWithoutPhotos;
    List <ReportEntity> reports;
    PhotoEntity photoForFirstReport1;
    PhotoEntity photoForFirstReport2;
    PhotoEntity photoForFirstReport3;
    PhotoEntity photoForSecondReport;
    ReportEntity reportWithPhotosWithMainPhoto;
    ReportEntity reportWithoutPhotos;
    ReportEntity reportWithoutMainPhoto;
    ReportEntity reportWithOnePhoto;
    @BeforeEach
    void init () throws Exception{
        photos = new ArrayList<>();
        reportsWithPhotos = new ArrayList<>();
        reportsWithoutPhotos = new ArrayList<>();
        reports = new ArrayList<>();

        reportWithPhotosWithMainPhoto = new ReportEntity();
        reportWithPhotosWithMainPhoto.setReportId(1);

        reportWithoutPhotos = new ReportEntity();
        reportWithoutPhotos.setReportId(2);

        photoForFirstReport1 = new PhotoEntity();
        photoForFirstReport2 = new PhotoEntity();
        photoForFirstReport3 = new PhotoEntity();
        photoForFirstReport1.setPhotoId(1);
        photoForFirstReport2.setPhotoId(2);
        photoForFirstReport3.setPhotoId(3);

        photoForFirstReport1.setReportId(1);
        photoForFirstReport2.setReportId(1);
        photoForFirstReport3.setReportId(1);

        reportWithoutMainPhoto = new ReportEntity();
        reportWithoutMainPhoto.setReportId(3);
        reportWithoutMainPhoto.setMainPhoto(null);

        reportWithOnePhoto = new ReportEntity();
        reportWithOnePhoto.setReportId(4);

        photoForSecondReport = new PhotoEntity();
        photoForSecondReport.setReportId(4);

        photos.add(photoForFirstReport2);
        photos.add(photoForFirstReport3);
        photos.add(photoForFirstReport1);
        photos.add(photoForSecondReport);

        reportsWithoutPhotos.add(reportWithoutMainPhoto);
        reportsWithoutPhotos.add(reportWithoutPhotos);

        reportsWithPhotos.add(reportWithOnePhoto);
        reportsWithPhotos.add(reportWithoutPhotos);

        reports.add(reportWithoutMainPhoto);
        reports.add(reportWithoutPhotos);
        reports.add(reportWithOnePhoto);
        reports.add(reportWithPhotosWithMainPhoto);

        filter = new Filter(reports, photos);
    }
    @Test
    void checkReportWithPhotosHaveCorrectPhotosList(){
        List <PhotoEntity> photoEntities = filter.getPhotosFromReport(reportWithPhotosWithMainPhoto);
        List <PhotoEntity> expected = Arrays.asList(photoForFirstReport1, photoForFirstReport2, photoForFirstReport3);
        assertThat("Raport posiada złą listę zdjęć",
                photoEntities, containsInAnyOrder(expected.toArray()));
    }
    @Test
    void checkReportWithoutPhotosHaveNoPhotosList(){
        List <PhotoEntity> photoEntities = filter.getPhotosFromReport(reportWithoutPhotos);
        assertTrue(photoEntities.isEmpty(), ()-> "Raport bez zdjęć posiada zdjęcia");
    }
    @Test
    void reportWithMinimumOnePhoto(){
        List<ReportEntity> reportEntities = filter.getReportsWithMinimumPhotosAmount(1);
        List<ReportEntity> expected = Arrays.asList(reportWithPhotosWithMainPhoto, reportWithOnePhoto);
        expected.stream().forEach(reportEntity -> System.out.println("stworzona lista " + reportEntity));
        assertThat("Powinno zwrócić raporty, z liczbą zdjęć, większą niz 0", reportEntities, containsInAnyOrder(expected.toArray()));
    }
    @Test
    void getReportsFromLastYear(){
        reportWithOnePhoto.setReportDate("12-10-2019");
        reportWithPhotosWithMainPhoto.setReportDate("15-12-2019");
        reportWithoutPhotos.setReportDate("12-10-2017");
        List<ReportEntity> expected = Arrays.asList(reportWithOnePhoto, reportWithPhotosWithMainPhoto);
        List<ReportEntity> result = filter.getReportsFromPastYears(1);
        assertThat("Raporty z poprzedniego roku", result, containsInAnyOrder(expected.toArray()));
    }
    @Test
    void getReportsFrom2017Year(){
        reportWithoutPhotos.setReportDate("12-10-2017");
        reportWithOnePhoto.setReportDate("11-11-2016");
        List<ReportEntity> expected = Arrays.asList(reportWithoutPhotos);
        List<ReportEntity> result = filter.getReportsFromSelectedYear(2017);
        assertThat("Filtr zwrocil raporty nie z 2017 roku", result, containsInAnyOrder(expected.toArray()));
    }
    @Test
    void getReportsFrom2017andJanuary(){
        reportWithoutPhotos.setReportDate("02-01-2017");
        reportWithOnePhoto.setReportDate("11-11-2016");
        List<ReportEntity> expected = Arrays.asList(reportWithoutPhotos);
        List<ReportEntity> result = filter.getReportsFromSelectedYearAndMonth(2017, 1);
        assertThat("Filtr zwrocil raporty nie z Stycznia 2017 roku", result, containsInAnyOrder(expected.toArray()));
    }
    @Test
    void getReportThatTitleSubstringIsAsd(){
        reportWithOnePhoto.setReportTitle("asdqwe");
        reportWithoutPhotos.setReportTitle("nbmasd");
        reportWithPhotosWithMainPhoto.setReportTitle("zxcbnbf");
        List<ReportEntity> expected = Arrays.asList(reportWithOnePhoto, reportWithoutPhotos);
        List<ReportEntity> result = filter.getReportsByReportTitle("Asd");
        assertThat("Filtr zwrocil bledne tytuly. Prawidlowe tytuly powinny zawierac asd", result, containsInAnyOrder(expected.toArray()));
    }
    @Test
    void getReportThatLocalizationIsWroclaw(){
        reportWithoutPhotos.setReportLocalization("Wroclaw");
        reportWithOnePhoto.setReportLocalization("Gdansk");
        reportWithPhotosWithMainPhoto.setReportLocalization("Wroclaw Psie Pole");
        List <ReportEntity> expected = Arrays.asList(reportWithoutPhotos, reportWithPhotosWithMainPhoto);
        List <ReportEntity> result = filter.getReportsByReportLocalization("wroclaw");

    }
}
