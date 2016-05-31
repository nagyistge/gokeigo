package com.id11236662.gokeigo.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.EntriesResponse;
import com.id11236662.gokeigo.model.Entry;
import com.id11236662.gokeigo.model.KeigoManager;
import com.id11236662.gokeigo.util.ActivityConfigurator;
import com.id11236662.gokeigo.util.ApiClient;
import com.id11236662.gokeigo.util.Constants;
import com.id11236662.gokeigo.util.JishoService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_entry_toolbar);
        setSupportActionBar(toolbar);

        // Show the search fragment.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main_layout, new SearchFragment())
                    .commit();
        }

        // Add drawer listener to the navigation drawer.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set navigation item selected listener to the navigation drawer.
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        // Lock the orientation to prevent losing search queries.
        ActivityConfigurator.lockOrientation(this);

        // TODO: Does this even work...
        if (ActivityConfigurator.isDeviceOnline(this)) {
            new SetupKeigoManagerAsyncTask().execute();
        } else {
            Toast.makeText(this, R.string.message_no_network_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        // If the drawer is open, close it. Else, actually go back.
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager manager = getSupportFragmentManager();

        // TODO: possibly refactor
        switch (id) {
            case R.id.nav_home:
                manager.beginTransaction()
                        .replace(R.id.content_main_layout, new SearchFragment())
                        .commit();
                break;
            case R.id.nav_history:
                manager.beginTransaction()
                        .replace(R.id.content_main_layout, new HistoryFragment())
                        .commit();
                break;
            case R.id.nav_notes:
                manager.beginTransaction()
                        .replace(R.id.content_main_layout, new NotesFragment())
                        .commit();
                break;
            case R.id.nav_starred:
                manager.beginTransaction()
                        .replace(R.id.content_main_layout, new StarredFragment())
                        .commit();
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class SetupKeigoManagerAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.e(Constants.TAG_DEBUGGING, "SetupKeigoManagerAsyncTask.doInBackground");
            JishoService jishoService = ApiClient.getClient().create(JishoService.class);
            Call<EntriesResponse> callForRespectfulResult = jishoService.getEntries(Constants.KEYWORD_PREFIX_RESPECTFUL);
            Call<EntriesResponse> callForHumbleResult = jishoService.getEntries(Constants.KEYWORD_PREFIX_HUMBLE);
            try {
                KeigoManager manager = KeigoManager.getInstance();
                List<Entry> respectfulEntries = callForRespectfulResult.execute().body().getEntries();
                manager.addRespectfulEntries(respectfulEntries);
                List<Entry> humbleEntries = callForHumbleResult.execute().body().getEntries();
                manager.addHumbleEntries(humbleEntries);
            } catch (IOException e) {
                Log.e(Constants.TAG_DEBUGGING, e.toString());
            }
            return null;
        }
    }
}
