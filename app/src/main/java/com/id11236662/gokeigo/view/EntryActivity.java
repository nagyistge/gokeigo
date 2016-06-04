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
import com.id11236662.gokeigo.util.Constants;
import com.raizlabs.android.dbflow.StringUtils;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    private EntryManager mEntryManager = EntryManager.getInstance();
    private Entry mEntry;
    static final int SAVE_ENTRY_NOTE = 1;
    private FloatingActionButton mFab;
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

        Entry selectedEntry = getIntent().getParcelableExtra(Constants.INTENT_SELECTED_ENTRY);
        if (selectedEntry != null) {
            Log.d(Constants.TAG_DEBUGGING, "EntryActivity.onCreate. past the selectedEntry != null");
            Entry previouslySavedEntry = mEntryManager.getEntry(selectedEntry);

            if (previouslySavedEntry != null) {
                // Save the entry for later use in the rest of the class.

                mEntry = previouslySavedEntry;


                // TODO: Update history date


            } else {

                // Save the entry for later use in the rest of the class.

                mEntry = selectedEntry;

                // Save selected entry to history for the first time.
                // TODO: If clear history, clear the dates of every entry. Only delete entries if no notes nor hasn't been starred.

                mEntryManager.insertEntry(selectedEntry);
            }

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

            // Set text to note edit text and set OnClickListener.

            assert mNotesTextView != null;
            mNotesTextView.setText(mEntry.getNotes());
            mNotesTextView.setOnClickListener(this);

            // Show other forms if available, else hide the views that can contain these values.

            assert otherFormsTextView != null;
            String otherForms = mEntry.getOtherForms();
            if (StringUtils.isNotNullOrEmpty(otherForms)) {
                otherFormsTextView.setText(otherForms);
            } else {
                otherFormsTextView.setVisibility(View.GONE);
                TextView otherFormsLabelTextView = (TextView) findViewById(R.id.activity_entry_other_forms_label_text_view);
                assert otherFormsLabelTextView != null;
                otherFormsLabelTextView.setVisibility(View.GONE);
            }

            // Show common text view if is common, hide if not common.

            assert commonTextView != null;
            if (mEntry.getIsCommonStatus()) {
                commonTextView.setVisibility(View.VISIBLE);
            } else {
                commonTextView.setVisibility(View.INVISIBLE);
            }

            // Make the title of the activity be the selected entry's word and reading.

            setTitle(mEntry.getWordAndReading());
        }

        // Initialise fab field and set click listener.

        mFab = (FloatingActionButton) findViewById(R.id.activity_entry_notes_fab);
        assert mFab != null;
        mFab.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        Log.d(Constants.TAG_DEBUGGING, "EntryActivity.onClick. view.getId: " + v.getId());

        switch (v.getId()) {
            case R.id.activity_entry_notes_fab:

                // TODO: share the entry. include ankidroid option


                break;

            case R.id.activity_entry_notes_text_view:

                // Start the NoteActivity for a result and pass the current notes to it for editing.

                Intent intent = new Intent(this, NoteActivity.class);
                String notes = mNotesTextView.getText().toString();
                intent.putExtra(Constants.INTENT_ENTRY_NOTE, notes);
                Log.d(Constants.TAG_DEBUGGING, "EntryActivity.onClick. notes: " + notes);

                startActivityForResult(intent, SAVE_ENTRY_NOTE);

                break;
        }
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
    public void onBackPressed() {
        // Update mEntry object with the note and save it to the DB, before going back Home.

        String notes = mNotesTextView.getText().toString();
        mEntry.setNotes(notes);
        mEntryManager.updateEntry(mEntry);

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(Constants.TAG_DEBUGGING, "EntryActivity.onOptionsItemSelected. item.getItemId: " + item.getItemId());

        switch (item.getItemId()) {
            case android.R.id.home:

                Log.d(Constants.TAG_DEBUGGING, "EntryActivity.onOptionsItemSelected. android.R.id.home: " + android.R.id.home);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
