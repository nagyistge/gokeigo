package com.id11236662.gokeigo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.Entry;
import com.id11236662.gokeigo.model.EntryManager;
import com.id11236662.gokeigo.util.ActivityConfigurator;
import com.id11236662.gokeigo.util.Constants;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    private EntryManager mEntryManager = EntryManager.getInstance();
    private Entry mEntry;
    static final int SAVE_ENTRY_NOTE = 1;
    private TextView mNotesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_entry_toolbar);
        setSupportActionBar(toolbar);

        // Show the back button in the action bar.

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Lock the orientation to prevent losing notes.

        ActivityConfigurator.lockOrientation(this);


        Entry selectedEntry = getIntent().getParcelableExtra(Constants.INTENT_SELECTED_ENTRY);
        if (selectedEntry != null) {
            Log.d(Constants.TAG_DEBUGGING, "EntryActivity.onCreate. past the selectedEntry != null");
            Entry previouslySavedEntry = mEntryManager.getEntry(selectedEntry);

            if (previouslySavedEntry != null) {
                // TODO: Update history date
            } else {

                // Save selected entry to history for the first time.
                // TODO: If clear history, clear the dates of every entry. Only delete entries if no notes nor hasn't been starred.

                mEntryManager.insertEntry(selectedEntry);
            }

            // Save the entry for later use in the rest of the class.

            mEntry = selectedEntry;

            // Set values to all the text views with the parcelled entry!

            TextView wordTextView = (TextView) findViewById(R.id.activity_entry_word_text_view);
            TextView readingTextView = (TextView) findViewById(R.id.activity_entry_reading_text_view);
            TextView commonTextView = (TextView) findViewById(R.id.activity_entry_common_status_text_view);
            TextView blurbTextView = (TextView) findViewById(R.id.activity_entry_blurb_text_view);
            TextView otherFormsTextView = (TextView) findViewById(R.id.activity_entry_other_forms_text_view);
            mNotesTextView = (TextView) findViewById(R.id.activity_entry_notes_text_view);
            assert wordTextView != null;
            wordTextView.setText(mEntry.getWord());
            assert readingTextView != null;
            readingTextView.setText(mEntry.getReading());
            assert blurbTextView != null;
            blurbTextView.setText(mEntry.getBlurb());
            assert otherFormsTextView != null;
            otherFormsTextView.setText(mEntry.getOtherForms());
            assert mNotesTextView != null;
            mNotesTextView.setText(mEntry.getNotes());

            // Show common text view if is common, hide if not common.

            assert commonTextView != null;
            if (mEntry.getIsCommonStatus()) {
                commonTextView.setVisibility(View.VISIBLE);
            } else {
                commonTextView.setVisibility(View.INVISIBLE);
            }

            // Make the title of the activity be the selected entry.

            setTitle(mEntry.getWord());
        }

        // Initialise fab field and set click listener.

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_entry_notes_fab);
        assert fab != null;
        fab.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_entry_notes_fab) {
            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra(Constants.INTENT_SELECTED_ENTRY, mEntry);

            Log.d(Constants.TAG_DEBUGGING, "EntryActivity.onClick. notes: " + mEntry.getNotes());

            startActivityForResult(intent, SAVE_ENTRY_NOTE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState
        // TODO: use note fragment again? i don't want this activity to be destroyed.
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * This is called if we get a result back from NoteActivity
     *
     * @param requestCode
     * @param resultCode  unused parameter
     * @param data        unused parameter
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check if the requestCode corresponds to what we asked for.

        switch (requestCode) {
            case SAVE_ENTRY_NOTE:

                // Get the resulted note, set it to the entry object, update it in the DB and reset
                // the text on display.

                String notes = data.getExtras().getString(Constants.INTENT_ENTRY_NOTE);

                Log.d(Constants.TAG_DEBUGGING, "EntryActivity.onActivityResult. notes: " + notes);

                mEntry.setNotes(notes);
                mEntryManager.updateEntry(mEntry);
                mNotesTextView.setText(notes);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: what happens if it was the NoteActivity that called?
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
