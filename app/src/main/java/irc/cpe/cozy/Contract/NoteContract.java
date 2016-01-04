package irc.cpe.cozy.Contract;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by You on 18/12/2015.
 */
public class NoteContract {
    public static abstract class NoteDB implements BaseColumns {
        public static final String TABLE_NAME = "note";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_FOLDER = "folder";
    }

    private static final String SQL_CREATE_TABLE_NOTE =
            "CREATE TABLE " + NoteDB.TABLE_NAME + " (" +
                    NoteDB.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    NoteDB.COLUMN_NAME + " TEXT NOT NULL, " +
                    NoteDB.COLUMN_CONTENT + " TEXT, " +
                    NoteDB.COLUMN_FOLDER + " INTEGER " +
                    " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + NoteDB.TABLE_NAME;

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
        database.execSQL(SQL_DELETE_ENTRIES);
        onCreate(database);
    }
}
