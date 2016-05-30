package com.id11236662.gokeigo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.controller.EntryAdapter;
import com.id11236662.gokeigo.util.ActivityConfigurator;
import com.id11236662.gokeigo.util.Constants;

public class EntryActivity extends AppCompatActivity {

    private Fragment mFragmentInEditMode;
    private boolean mIsInEditMode = false;

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

        SetupTabLayout();
    }

    private void SetupTabLayout() {
        // Set up a tab for dictionary entry and a tab each for the 3 levels of keigo.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_entry_tab_layout);
        assert tabLayout != null;
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_dictionary));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_keigo));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_notes));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.content_entry_view_pager);
        final EntryAdapter adapter = new EntryAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        assert viewPager != null;
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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

    @Override
    public void onBackPressed() {
        Log.d(Constants.TAG_DEBUGGING, "onBackPressed: mIsInEditMode - " + (mIsInEditMode ? "true" : "false"));
        // If a fragment is still in editing mode, show a dialogue to confirm if should discard changes.
        if (mFragmentInEditMode != null && mIsInEditMode) {
            DialogFragment dialogFragment = new YesNoDialog();
            Bundle args = new Bundle();
            args.putString(Constants.DIALOG_TITLE, "Discard changes to notes?");
            args.putString(Constants.DIALOG_MESSAGE, "Your changes are not saved. Are you sure you want to discard the changes?");
            dialogFragment.setArguments(args);
            dialogFragment.setTargetFragment(mFragmentInEditMode, Constants.REQUEST_CODE_YES_NO);
            dialogFragment.show(getSupportFragmentManager(), Constants.TAG_FRAGMENT_YES_NO);
        } else {
            super.onBackPressed();
        }
    }

    public void setIsInEditMode(Fragment fragmentInEditMode, boolean isInEditMode) {
        mFragmentInEditMode = fragmentInEditMode;
        mIsInEditMode = isInEditMode;
    }

    public static class YesNoDialog extends DialogFragment {
        public YesNoDialog() {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args = getArguments();
            String title = args.getString(Constants.DIALOG_TITLE, "");
            String message = args.getString(Constants.DIALOG_MESSAGE, "");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
                        }
                    })
                    .create();
        }
    }
}
