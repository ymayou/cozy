package irc.cpe.cozy.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import irc.cpe.cozy.Contract.CozyNoteHelper;
import irc.cpe.cozy.Contract.NoteContract;
import irc.cpe.cozy.Model.Explorer;
import irc.cpe.cozy.Model.Note;

/**
 * Created by You on 05/01/2016.
 */
public class NoteDao implements CommonDao<Note> {
    @Override
    public List<Note> select(Context context, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor notes = CozyNoteHelper.getInstance(context).getReadableDatabase().query(NoteContract.NoteDB.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        List<Note> list = new ArrayList<>();
        notes.moveToFirst();
        while (!notes.isAfterLast())
        {
            list.add(new Note(Integer.parseInt(notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_ID))), notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_NAME)), notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_CONTENT)), Integer.parseInt(notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_FOLDER)))));
            notes.moveToNext();
        }
        notes.close();
        return list;
    }

    @Override
    public void insert(Context context, Note object) {
        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteDB.COLUMN_NAME, object.getName());
        values.put(NoteContract.NoteDB.COLUMN_CONTENT, object.getContent());
        values.put(NoteContract.NoteDB.COLUMN_FOLDER, object.getFolder());
        CozyNoteHelper.getInstance(context).getReadableDatabase().insert(NoteContract.NoteDB.TABLE_NAME, null, values);
    }

    public List<Explorer> selectExplorer(Context context,String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    {
        String[] columns = {
                NoteContract.NoteDB.COLUMN_ID,
                NoteContract.NoteDB.COLUMN_NAME
        };

        Cursor notes  = CozyNoteHelper.getInstance(context).getReadableDatabase().query(NoteContract.NoteDB.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        List<Explorer> explorers = new ArrayList<>();
        notes.moveToFirst();
        while (!notes.isAfterLast())
        {
            explorers.add(new Explorer(Integer.parseInt(notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_ID))), notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_NAME))));
            notes.moveToNext();
        }
        notes.close();
        return explorers;
    }

    public Note selectById(Context context, int id)
    {
        String[] columns = {
                NoteContract.NoteDB.COLUMN_ID,
                NoteContract.NoteDB.COLUMN_NAME,
                NoteContract.NoteDB.COLUMN_CONTENT,
                NoteContract.NoteDB.COLUMN_FOLDER
        };

        Cursor notes  = CozyNoteHelper.getInstance(context).getReadableDatabase().query(NoteContract.NoteDB.TABLE_NAME, columns, NoteContract.NoteDB.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        notes.moveToFirst();
        Note note = new Note(Integer.parseInt(notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_ID))), notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_NAME)), notes.getColumnName(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_CONTENT)), Integer.parseInt(notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_FOLDER))));
        return note;
    }

}
