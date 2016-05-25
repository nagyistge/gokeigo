package com.id11236662.gokeigo.view;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.id11236662.gokeigo.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main_layout, SearchFragment.newInstance())
                    .commit();
        }

        // BEGIN TODO: creating navigation drawer from scratch
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        // TODO: Add Settings menu item
        // Intent intent = new Intent(this,SettingsActivity.class);
        //startActivity(intent);
        // END

        // TODO: To do from scratch or use the MaterialDrawer? Seems pretty easy to use.
//        final PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(R.string.drawer_item_home);
//        final PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName(R.string.drawer_item_history);
//        final PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName(R.string.drawer_item_list);
//        final PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName(R.string.drawer_item_notes);
//        final PrimaryDrawerItem item5 = new PrimaryDrawerItem().withName(R.string.drawer_item_quizzes);
//
//        //create the drawer and remember the `Drawer` result object
//        Drawer result = new DrawerBuilder()
//                .withActivity(this)
//                .withToolbar(toolbar)
//                .addDrawerItems(
//                        item1,
//                        item2,
//                        item3,
//                        item4,
//                        item5
//                )
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    /**
//                     * @param view
//                     * @param position
//                     * @param drawerItem
//                     * @return true if the event was consumed
//                     */
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        boolean result = false;
//                        if (drawerItem == item2) {
//
//                            result = true;
//                        }
//                        return result;
//                    }
//                })
//                .build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
