package com.example.inzynierka.Report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inzynierka.R;

import androidx.fragment.app.Fragment;

public class AddPhotoFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.add_photo_fragment, container, false);

            return rootView;
        }
}
