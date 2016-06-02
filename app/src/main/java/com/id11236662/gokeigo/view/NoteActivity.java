package com.id11236662.gokeigo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.Entry;
import com.id11236662.gokeigo.util.ActivityConfigurator;
import com.id11236662.gokeigo.util.Constants;

public class NoteActivity extends AppCompatActivity {

    private EditText mNotesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Show the back button in the action bar.

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Lock the orientation to prevent losing notes.

        ActivityConfigurator.lockOrientation(this);

        // Initialise the note edit text.

        mNotesEditText = (EditText) findViewById(R.id.activity_note_edit_text);

        // Set value to the note edit text with the parcelled entry.

        Entry selectedEntry = getIntent().getParcelableExtra(Constants.INTENT_SELECTED_ENTRY);
        if (selectedEntry != null) {

            String notes = selectedEntry.getNotes();
            mNotesEditText.setText(notes);
            Log.d(Constants.TAG_DEBUGGING, "NoteActivity.onCreate. notes: " + notes);
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

    @Override
    public void onBackPressed() {

        // Pass back the edited note.

        Intent result = new Intent();
        String notes = mNotesEditText.getText().toString();
        result.putExtra(Constants.INTENT_ENTRY_NOTE, notes);

        Log.d(Constants.TAG_DEBUGGING, "NoteActivity.onBackPressed. notes: " + notes);

        // Set the result to OK and add the intent.

        setResult(Activity.RESULT_OK, result);

        super.onBackPressed();
    }
}
