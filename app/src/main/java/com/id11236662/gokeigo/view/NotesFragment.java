package com.id11236662.gokeigo.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.id11236662.gokeigo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {


    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_notes, container, false);


        // Set title of the activity.

        CharSequence title = getString(R.string.title_notes);
        getActivity().setTitle(title);

        return view;
    }

    // TODO: Have a NoteAdapter, show only entries that have non-blank notes.

}
