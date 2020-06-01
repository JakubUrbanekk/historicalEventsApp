package com.example.inzynierka.addons;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.inzynierka.Database.informations.EventDetails;
import com.example.inzynierka.Database.informations.EventDetailsRepository;
import com.example.inzynierka.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import lombok.extern.java.Log;

@Log
public class CustomBottomDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private ItemClickListener mListener;
    private LinearLayout layout;
    private Activity activity;
    private List<EventDetails> textViewMessages;

    public CustomBottomDialog(Activity activity) {
        this.activity = activity;
        textViewMessages = new ArrayList<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bar_bottom_events_titles, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = view.findViewById(R.id.eventsTitlesLinearLayout);
        log.info("Details for view " + textViewMessages);
        textViewMessages.forEach(details -> {
            String message = details.getTitle();
            String url = details.getDescription();
            TextView textView = new TextView(activity.getApplicationContext());
            textView.setText(message);
            textView.setTextSize(24);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            log.info("Message in view " + message);
            if(url != null && !url.isEmpty()){
                log.info("Setting black color");
                textView.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.quantum_black_100));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_big_dot, 0, 0, 0);
            }
            else {
                log.info("Setting grey color");
                textView.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.quantum_grey300));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_big_dot_grey, 0, 0, 0);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 8, 8, 8);
            textView.setLayoutParams(params);
            textView.setOnClickListener(this);
            layout.addView(textView);
        });
    }


    @Override
    public void onClick(View view) {
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

    public int listSize(){
        return textViewMessages.size();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        textViewMessages.clear();
    }

    public interface ItemClickListener {
        void onItemClick(View item);
    }

    public void addTextViewMessage(List<String> titles) {
        new ActivityDescriptionRequestTask(titles, this).execute();
    }


    public class ActivityDescriptionRequestTask extends AsyncTask<Void, Void, List<EventDetails>> {
        List<String> titles;
        EventDetailsRepository repository;
        CustomBottomDialog customBottomDialog;

        public ActivityDescriptionRequestTask(List<String> title, CustomBottomDialog customBottomDialog) {
            this.titles = title;
            repository = new EventDetailsRepository(activity.getApplication());
            this.customBottomDialog = customBottomDialog;
        }

        @Override
        protected List<EventDetails> doInBackground(Void... voids) {
            List<EventDetails> details = new ArrayList<>();
            titles.forEach(s -> {
                EventDetails detail = repository.findEventsByTitle(s);
                details.add(detail);
            });
            log.info("Found details " + details);
            return details;
        }

        @Override
        protected void onPostExecute(List<EventDetails> eventDetails) {
            FragmentActivity activity2 = (FragmentActivity) activity;
            textViewMessages = eventDetails;
            customBottomDialog.show(activity2.getSupportFragmentManager(), "asd");
        }
    }
}