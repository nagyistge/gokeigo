package com.id11236662.gokeigo.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.util.Constants;
import com.id11236662.gokeigo.util.MenuTint;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntryNotesFragment extends Fragment implements View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private EditText mNotesEditText;
    private MenuItem mSaveItem;
    private FloatingActionButton mFAB;
    private boolean isInEditMode = false;

    public EntryNotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Display the actions, which is just a save action, on the toolbar.
        setHasOptionsMenu(true);

        // Initialise notes field and disable it so it's read-only.
        final EntryActivity activity = (EntryActivity) getActivity();
        mNotesEditText = (EditText) activity.findViewById(R.id.fragment_entry_notes_edit_text);
        assert mNotesEditText != null;
        mNotesEditText.setEnabled(isInEditMode);

        // Initialise fab field and set click listener.
        mFAB = (FloatingActionButton) activity.findViewById(R.id.fragment_entry_notes_fab);
        assert mFAB != null;
        mFAB.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        menu.setGroupVisible(R.id.main_search_group, false);
        menu.setGroupVisible(R.id.main_save_group, false);

        // Manually tint the save icon as it's provided separately from AppCompat.
        // And set click listener.
        mSaveItem = menu.findItem(R.id.action_save);
        MenuTint.colorMenuItem(mSaveItem, Color.WHITE, null);
        mSaveItem.setOnMenuItemClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Constants.TAG_DEBUGGING, "EntryNotesFragment.onActivityResult: requestCode - " + requestCode + " resultCode - " + resultCode);
        if (requestCode == Constants.REQUEST_CODE_YES_NO) {
            final EntryActivity activity = (EntryActivity) getActivity();
            if (resultCode == Activity.RESULT_OK) {
                activity.setIsInEditMode(null, false);
                activity.onBackPressed();
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        isInEditMode = true;
        mFAB.hide();
        mNotesEditText.setEnabled(isInEditMode);
        mSaveItem.setVisible(isInEditMode);
        final EntryActivity activity = (EntryActivity) getActivity();
        activity.setIsInEditMode(this, isInEditMode);
    }

    /**
     * Called when a menu item has been invoked.  This is the first code
     * that is executed; if it returns true, no other callbacks will be
     * executed.
     *
     * @param item The menu item that was invoked.
     * @return Returns true to consume this click and prevent others from
     * executing.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        isInEditMode = false;
        mFAB.show();
        mNotesEditText.setEnabled(isInEditMode);
        mSaveItem.setVisible(isInEditMode);
        final EntryActivity activity = (EntryActivity) getActivity();
        activity.setIsInEditMode(this, isInEditMode);

        // TODO: Persist the note
        return true;
    }
}
