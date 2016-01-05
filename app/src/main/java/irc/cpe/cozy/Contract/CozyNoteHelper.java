package irc.cpe.cozy.Contract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by You on 18/12/2015.
 */
public class CozyNoteHelper extends SQLiteOpenHelper {

    private static CozyNoteHelper instance = null;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CozyNote.db";

    private Context context;

    private CozyNoteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static CozyNoteHelper getInstance(Context context)
    {
        if (instance == null)
            instance = new CozyNoteHelper(context.getApplicationContext());
        return instance;
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
