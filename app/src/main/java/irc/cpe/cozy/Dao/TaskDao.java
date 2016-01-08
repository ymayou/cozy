package irc.cpe.cozy.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import irc.cpe.cozy.Contract.CozyNoteHelper;
import irc.cpe.cozy.Contract.NoteTaskNoteContract;
import irc.cpe.cozy.Model.Task;

/**
 * Created by You on 06/01/2016.
 */
public class TaskDao implements CommonDao<Task> {
    @Override
    public List<Task> select(Context context, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor tasks = CozyNoteHelper.getInstance(context).getReadableDatabase().query(NoteTaskNoteContract.NoteTaskNoteDB.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        List<Task> list = new ArrayList<>();
        tasks.moveToFirst();
        while (!tasks.isAfterLast())
        {
            list.add(new Task(Integer.parseInt(tasks.getString(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_ID))),
                    tasks.getString(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_CONTENT)),
                    (tasks.getInt(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_STATUS)) == 1),
                    Integer.parseInt(tasks.getString(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE)))));
            tasks.moveToNext();
        }
        tasks.close();
        return list;
    }

    @Override
    public Task selectById(Context context, int id) {
        String[] columns = {
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_ID,
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_CONTENT,
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_STATUS,
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE
        };

        Cursor tasks  = CozyNoteHelper.getInstance(context).getReadableDatabase().query(NoteTaskNoteContract.NoteTaskNoteDB.TABLE_NAME, columns, NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        tasks.moveToFirst();
        Task task = new Task(Integer.parseInt(tasks.getString(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_ID))),
                tasks.getString(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_CONTENT)),
                (Integer.parseInt(tasks.getString(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_STATUS))) == 1),
                Integer.parseInt(tasks.getString(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE))));
        return task;
    }

    @Override
    public void insert(Context context, Task object) {
        ContentValues values = new ContentValues();
        values.put(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_CONTENT, object.getContent());
        values.put(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_STATUS, String.valueOf(object.isStatus()));
        values.put(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE, object.getTaskNote());
        CozyNoteHelper.getInstance(context).getReadableDatabase().insert(NoteTaskNoteContract.NoteTaskNoteDB.TABLE_NAME, null, values);
    }

    @Override
    public void update(Context context, Task object) {
        ContentValues values = new ContentValues();
        values.put(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_CONTENT, object.getContent());
        values.put(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_STATUS, object.isStatus());
        values.put(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE, object.getTaskNote());
        CozyNoteHelper.getInstance(context).getReadableDatabase().update(NoteTaskNoteContract.NoteTaskNoteDB.TABLE_NAME, values, NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_ID + "=?", new String[]{String.valueOf(object.getId())});
    }

    @Override
    public void delete(Context context, int id) {
        CozyNoteHelper.getInstance(context).getReadableDatabase().delete(NoteTaskNoteContract.NoteTaskNoteDB.TABLE_NAME, NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteByNoteId(Context context, int noteId) {
        CozyNoteHelper.getInstance(context).getReadableDatabase().delete(NoteTaskNoteContract.NoteTaskNoteDB.TABLE_NAME, NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE + "=?", new String[]{String.valueOf(noteId)});
    }

    /*public Task selectByNoteId(Context context, int noteId) {
        String[] columns = {
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_ID,
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_CONTENT,
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_STATUS,
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE
        };

        Cursor tasks  = CozyNoteHelper.getInstance(context).getReadableDatabase().query(NoteTaskNoteContract.NoteTaskNoteDB.TABLE_NAME, columns, NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE + "=?", new String[]{String.valueOf(noteId)}, null, null, null, null);
        tasks.moveToFirst();
        Task task = new Task(Integer.parseInt(tasks.getString(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_ID))),
                tasks.getString(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_CONTENT)),
                (Integer.parseInt(tasks.getString(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_STATUS))) == 1),
                Integer.parseInt(tasks.getString(tasks.getColumnIndex(NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE))));
        return task;
    }*/
}
