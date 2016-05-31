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
import com.id11236662.gokeigo.model.Entry;
import com.id11236662.gokeigo.util.Constants;

public class EntryContentsFragment extends Fragment {

    public EntryContentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry_contents, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();

        Entry entry = activity.getIntent().getParcelableExtra(Constants.INTENT_SELECTED_ENTRY);
        if (entry != null) {

            // Set values to all the text views with the parcelled entry!
            TextView wordTextView = (TextView) activity.findViewById(R.id.fragment_entry_dictionary_word_text_view);
            TextView readingTextView = (TextView) activity.findViewById(R.id.fragment_entry_dictionary_reading_text_view);
            TextView commonTextView = (TextView) activity.findViewById(R.id.fragment_entry_dictionary_common_status_text_view);
            TextView blurbTextView = (TextView) activity.findViewById(R.id.fragment_text_dictionary_blurb_text_view);
            TextView otherFormsTextView = (TextView) activity.findViewById(R.id.fragment_text_dictionary_other_forms_text_view);
            wordTextView.setText(entry.getWord());
            readingTextView.setText(entry.getReading());
            blurbTextView.setText(entry.getBlurb());
            otherFormsTextView.setText(entry.getOtherForms());

            // Show common text view if is common, hide if not common.
            if (entry.getIsCommonStatus()) {
                commonTextView.setVisibility(View.VISIBLE);
            } else {
                commonTextView.setVisibility(View.INVISIBLE);
            }

            // Make the title of the activity be the selected entry.
            activity.setTitle(entry.getWord());
        }
    }
}
