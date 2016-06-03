package com.id11236662.gokeigo.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.Entry;
import com.id11236662.gokeigo.model.EntryManager;
import com.id11236662.gokeigo.util.ActivityConfigurator;
import com.id11236662.gokeigo.util.Constants;
import com.raizlabs.android.dbflow.StringUtils;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    private EntryManager mEntryManager = EntryManager.getInstance();
    private Entry mEntry;
    private boolean mInEditMode = false;
    private FloatingActionButton mFab;
    private EditText mNotesEditText;

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
            mNotesEditText = (EditText) findViewById(R.id.activity_entry_notes_edit_text);
            assert wordTextView != null;
            wordTextView.setText(mEntry.getWord());
            assert readingTextView != null;
            readingTextView.setText(mEntry.getReading());
            assert blurbTextView != null;
            blurbTextView.setText(mEntry.getBlurb());

            // Set text to note edit text and set OnClickListener.

            assert mNotesEditText != null;
            mNotesEditText.setText(mEntry.getNotes());
            mNotesEditText.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.activity_entry_notes_fab:

                // TODO: share the entry. include ankidroid option


                break;

            case R.id.activity_entry_notes_edit_text:

                // TODO: change the highlight colour
//                if (mNotesEditText.isFocused()) {
//                    mNotesEditText.setBackgroundColor(getDrawable(Color.WHITE));
//                } else {
//                    mNotesEditText.setBackgroundColor(getResources().getColor(R.color.accent, null));
//                }
//
//                mNotesEditText.invalidate();
//                mNotesEditText.requestLayout();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        // Update mEntry object with the note and save it to the DB, before going back Home.

        String notes = mNotesEditText.getText().toString();
        mEntry.setNotes(notes);
        mEntryManager.updateEntry(mEntry);

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
