package irc.cpe.cozy.Contract;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by You on 04/01/2016.
 */
public class NoteTaskNoteContract {
    public static abstract class NoteTaskNoteDB implements BaseColumns {
        public static final String TABLE_NAME = "noteTaskNote";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_CONTENT= "content";
        public static final String COLUMN_STATUS= "status";
        public static final String COLUMN_TASKNOTE = "taskNote";
    }

    private static final String SQL_CREATE_TABLE_NOTETASKNOTE =
            "CREATE TABLE " + NoteTaskNoteDB.TABLE_NAME + " (" +
                    NoteTaskNoteDB.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    NoteTaskNoteDB.COLUMN_CONTENT + " TEXT, " +
                    NoteTaskNoteDB.COLUMN_STATUS + " INTEGER DEFAULT 0, " +
                    NoteTaskNoteDB.COLUMN_TASKNOTE + " INTEGER NOT NULL " +
                    " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + NoteTaskNoteDB.TABLE_NAME;

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_TABLE_NOTETASKNOTE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
        database.execSQL(SQL_DELETE_ENTRIES);
        onCreate(database);
    }
}
