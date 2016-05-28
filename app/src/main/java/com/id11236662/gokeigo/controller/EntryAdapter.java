package com.id11236662.gokeigo.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.id11236662.gokeigo.view.DictionaryFragment;
import com.id11236662.gokeigo.view.HumbleFragment;
import com.id11236662.gokeigo.view.RespectfulFragment;

public class EntryAdapter extends FragmentStatePagerAdapter {
    private int mNumberOfTabs;

    public EntryAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        mNumberOfTabs = numberOfTabs;
    }

    /**
     * Return the Fragment associated with a specified position.
     * @param position of the tab
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DictionaryFragment();
            case 1:
                return new RespectfulFragment();
            case 2:
                return new HumbleFragment();
            default:
                return null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mNumberOfTabs;
    }
}