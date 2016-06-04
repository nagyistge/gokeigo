package com.id11236662.gokeigo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.util.Constants;

import java.util.Locale;

public class EntryNoteActivity extends AppCompatActivity {

    private EditText mNotesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_note);

        // Show the back button in the action bar.

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Initialise the note edit text.

        mNotesEditText = (EditText) findViewById(R.id.activity_entry_note_edit_text);

        // Set value to the note edit text with passed string.

        String notes = getIntent().getStringExtra(Constants.INTENT_ENTRY_NOTE);
        mNotesEditText.setText(notes);

        // Set the title of the notes activity with the word and reading of the entry.

        Locale currentLocale = getResources().getConfiguration().locale; // TODO: get from settings.
        String wordAndReading = getIntent().getStringExtra(Constants.INTENT_ENTRY_WORD_AND_READING);
        String title = String.format(currentLocale, getString(R.string.title_activity_notes_for), wordAndReading);
        setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                // If the top-right button, do go up an level and return true to show it's been handled.

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


        // Set the result to OK and add the intent.

        setResult(Activity.RESULT_OK, result);

        super.onBackPressed();
    }
}