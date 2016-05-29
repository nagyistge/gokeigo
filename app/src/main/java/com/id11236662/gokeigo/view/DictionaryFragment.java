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

public class DictionaryFragment extends Fragment {


    public DictionaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dictionary, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();

        TextView textView = (TextView) activity.findViewById(R.id.fragment_dictionary_word_text_view);
        assert textView != null;
        ParcelableEntry parcelableEntry = activity.getIntent().getParcelableExtra(Constants.SELECTED_ENTRY);
        if (parcelableEntry != null) {
            textView.setText(parcelableEntry.getWord());
        }
    }
}
