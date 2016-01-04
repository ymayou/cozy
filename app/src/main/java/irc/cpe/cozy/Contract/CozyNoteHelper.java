package irc.cpe.cozy.Contract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by You on 18/12/2015.
 */
public class CozyNoteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CozyNote.db";

    public CozyNoteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FolderContract.onCreate(db);
        NoteContract.onCreate(db);
        TaskNoteContract.onCreate(db);
        NoteTaskNoteContract.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FolderContract.onUpgrade(db);
        NoteContract.onUpgrade(db);
        TaskNoteContract.onUpgrade(db);
        NoteTaskNoteContract.onUpgrade(db);
    }
}
