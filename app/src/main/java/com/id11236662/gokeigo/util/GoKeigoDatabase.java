package com.id11236662.gokeigo.util;

import com.id11236662.gokeigo.model.Entry;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

/**
 * This class aids DBFlow in generating a database
 */

@Database(name = GoKeigoDatabase.NAME, version = GoKeigoDatabase.VERSION)
public class GoKeigoDatabase {

    // DBFlow will add the .db extension

    public static final String NAME = "GoKeigo";
    public static final int VERSION = 2;

    @Migration(version = 2, database = GoKeigoDatabase.class)
    public static class Migration0 extends AlterTableMigration<Entry> {

        public Migration0(Class<Entry> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.TEXT, "definition");
        }
    }
}
