package com.id11236662.gokeigo.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.controller.TabAdapter;

public class EntryActivity extends AppCompatActivity {

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

        // Set up a tab for dictionary entry and a tab each for the 3 levels of keigo.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_entry_tab_layout);
        assert tabLayout != null;
        // TODO: set them as resource strings
        tabLayout.addTab(tabLayout.newTab().setText("Dictionary"));
        tabLayout.addTab(tabLayout.newTab().setText("Respectful"));
        tabLayout.addTab(tabLayout.newTab().setText("Humble"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.content_entry_view_pager);
        final TabAdapter adapter = new TabAdapter (getSupportFragmentManager(),
                tabLayout.getTabCount());
        assert viewPager != null;
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * Called when a tab enters the selected state.
             *
             * @param tab The tab that was selected
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            /**
             * Called when a tab exits the selected state.
             *
             * @param tab The tab that was unselected
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            /**
             * Called when a tab that is already selected is chosen again by the user. Some applications
             * may use this action to return to the top level of a category.
             *
             * @param tab The tab that was reselected.
             */
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
}
