package com.example.inzynierka.Adapters;


import android.content.Context;

import com.example.inzynierka.R;
import com.example.inzynierka.Report.AddReport.AddPhotoFragment;
import com.example.inzynierka.Report.AddReport.AddReportActivity;
import com.example.inzynierka.Report.AddReport.AddReportDescriptionFragment;
import com.example.inzynierka.Report.EditReport.EditReportActivity;
import com.example.inzynierka.Report.EditReport.EditReportDescriptionFragment;
import com.example.inzynierka.Report.EditReport.EditReportPhotoFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 2;
    Context context;
    public ViewPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm,  BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        if (context instanceof AddReportActivity) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new AddReportDescriptionFragment();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return new AddPhotoFragment();
                default:
                    return null;
            }
        } else if (context instanceof EditReportActivity) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new EditReportDescriptionFragment();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return new EditReportPhotoFragment();
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return context.getString(R.string.Description);
            case 1:
                return context.getString(R.string.photos);
            default:
                return null;
        }
    }



    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
