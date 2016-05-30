package com.id11236662.gokeigo.view;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.ParcelableEntry;
import com.id11236662.gokeigo.util.Constants;

public class EntryDictionaryFragment extends Fragment {


    public EntryDictionaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry_dictionary, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();

        ParcelableEntry parcelableEntry = activity.getIntent().getParcelableExtra(Constants.INTENT_SELECTED_ENTRY);
        if (parcelableEntry != null) {
            TextView wordTextView = (TextView) activity.findViewById(R.id.fragment_entry_dictionary_word_text_view);
            TextView readingTextView = (TextView) activity.findViewById(R.id.fragment_entry_dictionary_reading_text_view);
            TextView commonTextView = (TextView) activity.findViewById(R.id.fragment_entry_dictionary_common_status_text_view);
            TextView blurbTextView = (TextView) activity.findViewById(R.id.fragment_text_dictionary_blurb_text_view);
            TextView otherFormsTextView = (TextView) activity.findViewById(R.id.fragment_text_dictionary_other_forms_text_view);
            wordTextView.setText(parcelableEntry.getWord());
            readingTextView.setText(parcelableEntry.getReading());
            commonTextView.setText(parcelableEntry.getCommonStatus());
            blurbTextView.setText(parcelableEntry.getBlurb());
            otherFormsTextView.setText(parcelableEntry.getOtherForms());
        }
    }
}
