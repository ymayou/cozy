package irc.cpe.cozy.Contract;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by You on 18/12/2015.
 */
public class FolderContract {

    public static abstract class FolderDB implements BaseColumns {
        public static final String TABLE_NAME = "folder";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PARENTFOLDER = "parentFolder";
    }

    private static final String SQL_CREATE_TABLE_DEPENSE =
        "CREATE TABLE " + FolderDB.TABLE_NAME + " (" +
            FolderDB.COLUMN_ID + " INTEGER PRIMARY KEY," +
            FolderDB.COLUMN_NAME + " TEXT NOT NULL, " +
            FolderDB.COLUMN_PARENTFOLDER + " TEXT " +
    " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FolderDB.TABLE_NAME;

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_TABLE_DEPENSE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
        database.execSQL(SQL_DELETE_ENTRIES);
        onCreate(database);
    }
}
