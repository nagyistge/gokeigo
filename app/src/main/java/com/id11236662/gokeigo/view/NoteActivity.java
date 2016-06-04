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

        // Initialise the note edit text.

        mNotesEditText = (EditText) findViewById(R.id.activity_note_edit_text);

        // Set value to the note edit text with passed string.

        String notes = getIntent().getStringExtra(Constants.INTENT_ENTRY_NOTE);
        mNotesEditText.setText(notes);
        Log.d(Constants.TAG_DEBUGGING, "NoteActivity.onCreate. notes: " + notes);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(Constants.TAG_DEBUGGING, "NoteActivity.onOptionsItemSelected. item.getItemId: " + item.getItemId());

        // TODO: what happens if it was the NoteActivity that called?
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(Constants.TAG_DEBUGGING, "NoteActivity.onOptionsItemSelected. android.R.id.home: " + android.R.id.home);
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