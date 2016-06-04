package com.id11236662.gokeigo.util;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * This class aids DBFlow in generating a database
 */
@Database(name = GoKeigoDatabase.NAME, version = GoKeigoDatabase.VERSION)
public class GoKeigoDatabase {

    // DBFlow will add the .db extension

    public static final String NAME = "GoKeigo";
    public static final int VERSION = 1;
}
