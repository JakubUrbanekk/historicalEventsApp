package com.example.inzynierka.Report;


import com.example.inzynierka.Photo.AddPhotoFragment;
import com.example.inzynierka.Report.AddReport.AddReportDescriptionFragment;
import com.example.inzynierka.Report.AddReport.AddReportViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 2;
    AddReportViewModel addReportViewModel;
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm,  BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return new AddReportDescriptionFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new AddPhotoFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
