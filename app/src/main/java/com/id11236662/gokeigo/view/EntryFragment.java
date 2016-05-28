package com.id11236662.gokeigo.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.controller.TabAdapter;

public class EntryFragment extends Fragment {

    View mTabView;

    public EntryFragment() {
        // Required empty public constructor
    }

    public static EntryFragment newInstance() {
        return new EntryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTabView = inflater.inflate(R.layout.app_bar_entry, container, false);
        AppBarLayout appBar = (AppBarLayout) getActivity().findViewById(R.id.app_bar_main);
        appBar.addView(mTabView);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();

        // Rename the Title with the name of the application.
        activity.setTitle(R.string.app_name);

        // Set up a tab for dictionary entry and a tab each for the 3 levels of keigo.
        TabLayout tabLayout = (TabLayout) activity.findViewById(R.id.app_bar_entry_tab_layout);
        assert tabLayout != null;
        // TODO: set them as resource strings
        tabLayout.addTab(tabLayout.newTab().setText("Dictionary"));
        tabLayout.addTab(tabLayout.newTab().setText("Respectful"));
        tabLayout.addTab(tabLayout.newTab().setText("Humble"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Set up a view pager and tab adapter for the tab layout for functionality.
        final ViewPager viewPager = (ViewPager) activity.findViewById(R.id.fragment_entry_view_pager);
        // TODO:getChildFragmentManager is doing something. As soon as this fragment tries to load, app dies
        // TODO: try to load the entryactivity through a item on the search recycler view first, then come back to this.
        final TabAdapter adapter = new TabAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * Called when a tab enters the selected state.
             * @param tab The tab that was selected
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            /**
             * Called when a tab exits the selected state.
             * @param tab The tab that was unselected
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            /**
             * Called when a tab that is already selected is chosen again by the user. Some applications
             * may use this action to return to the top level of a category.
             * @param tab The tab that was reselected.
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onStop() {
        ((ViewGroup)mTabView.getParent()).removeView(mTabView);
        super.onStop();
    }
}
