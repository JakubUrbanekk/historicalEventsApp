package com.example.inzynierka.Report;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.inzynierka.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActionBottomDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener{
    private ItemClickListener mListener;
    String bottomText;
    String topText;

    public ActionBottomDialogFragment(String bottomText, String topText) {
      super();
      this.bottomText = bottomText;
      this.topText = topText;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bar_bottom_gallery_and_camera, container, false);
        return v;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTop = (TextView) view.findViewById(R.id.barBottomTvGallery);
        TextView tvBot = (TextView) view.findViewById(R.id.barBottomTvCamera);
        tvTop.setOnClickListener(this);
        tvBot.setOnClickListener(this);
        tvTop.setText(topText);
        tvBot.setText(bottomText);
    }


    @Override public void onClick(View view) {
        mListener.onItemClick(view);
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ItemClickListener {
        void onItemClick(View item);
    }

    public String getBottomText() {
        return bottomText;
    }

    public String getTopText() {
        return topText;
    }
}