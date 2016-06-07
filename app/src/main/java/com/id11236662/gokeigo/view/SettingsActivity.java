package com.id11236662.gokeigo.view;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.EntryManager;
import com.id11236662.gokeigo.util.Constants;

/**
 * A PreferenceActivity that presents a set of application settings.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use a PreferenceFragment in the PreferenceActivity as described in the Android Dev site.

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new AppPreferenceFragment()).commit();

        // Show the back button in the action bar.

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || AppPreferenceFragment.class.getName().equals(fragmentName);
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

    public static class AppPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            PreferenceManager manager = getPreferenceManager();
            Preference clearHistoryPreference = manager.findPreference(Constants.PREF_KEY_CLEAR_HISTORY);
            clearHistoryPreference.setOnPreferenceClickListener(this);
        }

        /**
         * Called when a Preference has been clicked.
         *
         * @param preference The Preference that was clicked.
         * @return True if the click was handled.
         */

        @Override
        public boolean onPreferenceClick(Preference preference) {

            switch (preference.getKey()) {

                case Constants.PREF_KEY_CLEAR_HISTORY:

                    // TODO: Yes / No confirmation.

                    EntryManager.getInstance().clearHistory();
                    break;
            }

            return false;
        }
    }
}
