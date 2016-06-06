package com.id11236662.gokeigo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.Entry;
import com.id11236662.gokeigo.model.EntryManager;
import com.id11236662.gokeigo.util.Constants;
import com.id11236662.gokeigo.util.Speaker;
import com.raizlabs.android.dbflow.StringUtils;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    private EntryManager mEntryManager = EntryManager.getInstance();
    private Entry mEntry;
    private static final int SAVE_ENTRY_NOTE = 1;
    private static final int CHECK_CODE = 2;
    private FloatingActionButton mFab;
    private TextView mNotesTextView;
    private ImageSwitcher mStarImageSwitcher;
    private Speaker mSpeaker;

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

        // Initialise fab field and set OnClickListener.

        mFab = (FloatingActionButton) findViewById(R.id.activity_entry_notes_fab);
        assert mFab != null;
        mFab.setOnClickListener(this);

        // Initialise the image switcher field and set OnClickListener.

        mStarImageSwitcher = (ImageSwitcher) findViewById(R.id.activity_entry_star_image_switcher);
        assert mStarImageSwitcher != null;
        mStarImageSwitcher.setOnClickListener(this);

        // Add image views to the image switcher. Show unstarred first and starred second.

        Context context = getApplicationContext();
        ImageView unstarredImageView = new ImageView(getApplicationContext());
        unstarredImageView.setImageResource(R.drawable.ic_star_border_light_brown_48dp);
        mStarImageSwitcher.addView(unstarredImageView);

        ImageView starredImageView = new ImageView(context);
        starredImageView.setImageResource(R.drawable.ic_star_light_brown_48dp);
        mStarImageSwitcher.addView(starredImageView);

        // Set OnClickListener on to text to speech image view, and check if TTS is installed.

        ImageView textToSpeechImageView = (ImageView) findViewById(R.id.activity_entry_text_to_speech_image_view);
        assert textToSpeechImageView != null;
        textToSpeechImageView.setOnClickListener(this);
        checkTts();

        // After setting up the views, set values to these views.

        bindEntryToViews();
    }

    /**
     * Get the entry for passed intent and set its values to the views.
     */

    private void bindEntryToViews() {
        Entry selectedEntry = getIntent().getParcelableExtra(Constants.INTENT_SELECTED_ENTRY);
        if (selectedEntry != null) {

            // Get a previously saved entry if there is. That way we can get previously saved notes or
            // starred status. Save the entry for later use in the rest of the class.

            mEntry = mEntryManager.getPreviouslySavedEntryIfAvailableElseReturnPassedEntry(selectedEntry);

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
            if (mEntry.getIsCommon()) {
                commonTextView.setVisibility(View.VISIBLE);
            } else {
                commonTextView.setVisibility(View.INVISIBLE);
            }

            // Show starred if it's been starred, show the next image which is a starred image.

            if (mEntry.getIsStarred()) {
                mStarImageSwitcher.showNext();
            }

            // Make the title of the activity be the selected entry's word and reading.

            setTitle(mEntry.getWordAndReading());
        }
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
                Log.d(Constants.TAG, "EntryActivity.onClick - activity_entry_notes_fab");

                // TODO: share the entry. include ankidroid option


                break;

            case R.id.activity_entry_notes_text_view:
                Log.d(Constants.TAG, "EntryActivity.onClick - activity_entry_notes_text_view");

                // Start the EntryNoteActivity with the current notes and the word and reading,
                // and expect a result.

                Intent intent = new Intent(this, EntryNoteActivity.class);
                String notes = mNotesTextView.getText().toString();
                intent.putExtra(Constants.INTENT_ENTRY_NOTE, notes);
                intent.putExtra(Constants.INTENT_ENTRY_WORD_AND_READING, mEntry.getWordAndReading());
                startActivityForResult(intent, SAVE_ENTRY_NOTE);
                break;

            case R.id.activity_entry_star_image_switcher:
                Log.d(Constants.TAG, "EntryActivity.onClick - activity_entry_star_image_switcher");

                // Switch to the next image (unstarred or starred).

                mStarImageSwitcher.showNext();

                // "Switch on/off" the IsStarrred value.

                mEntry.switchIsStarredValue();
                break;

            case R.id.activity_entry_text_to_speech_image_view:
                Log.d(Constants.TAG, "EntryActivity.onClick - activity_entry_text_to_speech_image_view");

                // Speak the word of the entry.

                mSpeaker.speak(mEntry.getWord());
                break;
        }
    }

    /**
     * Check if a TTS engine is installed on the device.
     */

    private void checkTts() {
        // Check is performed by making use of the result of another Activity.
        // Source: http://code.tutsplus.com/tutorials/use-text-to-speech-on-android-to-read-out-incoming-messages--cms-22524

        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    /**
     * This is called if we get a result back from EntryNoteActivity
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

                // Get the resulted note, set it to the entry object, and reset the text on display.

                String notes = data.getExtras().getString(Constants.INTENT_ENTRY_NOTE);
                mEntry.setNotes(notes);
                mNotesTextView.setText(notes);
                break;

            case CHECK_CODE:

                // If positive, initialise the speaker field.
                // If no TTS is installed, redirect the user to install it.

                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    mSpeaker = new Speaker(getApplicationContext());
                } else {
                    Intent install = new Intent();
                    install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(install);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(Constants.TAG, "EntryActivity.onBackPressed - IsStarred: " + (mEntry.getIsStarred() ? "true" : "false"));

        // Update mEntry object with the note and save the entry to the DB, before going back Home.

        String notes = mNotesTextView.getText().toString();
        mEntry.setNotes(notes);
        mEntryManager.updateEntry(mEntry);

        super.onBackPressed();
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
    protected void onDestroy() {

        // Destroy the TTS service too.

        mSpeaker.destroy();
        super.onDestroy();
    }
}
