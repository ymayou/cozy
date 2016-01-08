package irc.cpe.cozy.Contract;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by You on 18/12/2015.
 */
public class TaskNoteContract {
    public static abstract class TaskNoteDB implements BaseColumns {
        public static final String TABLE_NAME = "taskNote";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FOLDER = "folder";
    }

    private static final String SQL_CREATE_TABLE_TASKNOTE =
            "CREATE TABLE " + TaskNoteDB.TABLE_NAME + " (" +
                    TaskNoteDB.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    TaskNoteDB.COLUMN_NAME + " TEXT, " +
                    TaskNoteDB.COLUMN_FOLDER + " INTEGER " +
                    " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TaskNoteDB.TABLE_NAME;

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_TABLE_TASKNOTE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
        database.execSQL(SQL_DELETE_ENTRIES);
        onCreate(database);
    }
}
