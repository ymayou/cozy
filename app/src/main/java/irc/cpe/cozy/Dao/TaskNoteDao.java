package irc.cpe.cozy.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import irc.cpe.cozy.Contract.CozyNoteHelper;
import irc.cpe.cozy.Contract.NoteTaskNoteContract;
import irc.cpe.cozy.Contract.TaskNoteContract;
import irc.cpe.cozy.Model.Explorer;
import irc.cpe.cozy.Model.Task;
import irc.cpe.cozy.Model.TaskNote;

/**
 * Created by You on 06/01/2016.
 */
public class TaskNoteDao implements CommonDao<TaskNote>{
    @Override
    public List<TaskNote> select(Context context, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor notes = CozyNoteHelper.getInstance(context).getReadableDatabase().query(TaskNoteContract.TaskNoteDB.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        List<TaskNote> list = new ArrayList<>();
        notes.moveToFirst();
        while (!notes.isAfterLast())
        {
            TaskNote task = new TaskNote();
            task.setId(Integer.parseInt(notes.getString(notes.getColumnIndex(TaskNoteContract.TaskNoteDB.COLUMN_ID))));
            task.setName(notes.getString(notes.getColumnIndex(TaskNoteContract.TaskNoteDB.TABLE_NAME)));
            task.setFolder(Integer.parseInt(notes.getString(notes.getColumnIndex(TaskNoteContract.TaskNoteDB.COLUMN_FOLDER))));
            task.setIdCozy(notes.getString(notes.getColumnIndex(TaskNoteContract.TaskNoteDB.COLUMN_COZY_ID)));

            TaskDao taskDao = new TaskDao();
            List<Task> tasks = taskDao.select(context, new String[]{NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_ID, NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE}, NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE + "=?", new String[]{String.valueOf(task.getId())}, null, null, null, null);
            task.setTasks(tasks);

            list.add(task);
            notes.moveToNext();
        }
        notes.close();
        return list;
    }

    @Override
    public TaskNote selectById(Context context, int id) {
        String[] columns = {
                TaskNoteContract.TaskNoteDB.COLUMN_ID,
                TaskNoteContract.TaskNoteDB.COLUMN_COZY_ID,
                TaskNoteContract.TaskNoteDB.COLUMN_NAME,
                TaskNoteContract.TaskNoteDB.COLUMN_FOLDER
        };

        Cursor taskNotes  = CozyNoteHelper.getInstance(context).getReadableDatabase().query(TaskNoteContract.TaskNoteDB.TABLE_NAME, columns, TaskNoteContract.TaskNoteDB.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        taskNotes.moveToFirst();
        TaskNote taskNote = new TaskNote();
        taskNote.setId(Integer.parseInt(taskNotes.getString(taskNotes.getColumnIndex(TaskNoteContract.TaskNoteDB.COLUMN_ID))));
        taskNote.setName(taskNotes.getString(taskNotes.getColumnIndex(TaskNoteContract.TaskNoteDB.COLUMN_NAME)));
        taskNote.setFolder(Integer.parseInt(taskNotes.getString(taskNotes.getColumnIndex(TaskNoteContract.TaskNoteDB.COLUMN_FOLDER))));
        taskNote.setIdCozy(taskNotes.getString(taskNotes.getColumnIndex(TaskNoteContract.TaskNoteDB.COLUMN_COZY_ID)));

        taskNotes.close();

        TaskDao taskDao = new TaskDao();
        String[] columnsTask = {
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_ID,
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_CONTENT,
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_STATUS,
                NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE
        };
        List<Task> tasks = taskDao.select(context, columnsTask, NoteTaskNoteContract.NoteTaskNoteDB.COLUMN_TASKNOTE + "=?", new String[]{String.valueOf(taskNote.getId())}, null, null, null, null);

        taskNote.setTasks(tasks);
        return taskNote;
    }

    @Override
    public void insert(Context context, TaskNote object) {
        ContentValues values = new ContentValues();
        values.put(TaskNoteContract.TaskNoteDB.COLUMN_NAME, object.getName());
        values.put(TaskNoteContract.TaskNoteDB.COLUMN_COZY_ID, object.getIdCozy());
        values.put(TaskNoteContract.TaskNoteDB.COLUMN_FOLDER, object.getFolder());
        CozyNoteHelper.getInstance(context).getReadableDatabase().insert(TaskNoteContract.TaskNoteDB.TABLE_NAME, null, values);
    }

    public int insertForId(Context context, TaskNote taskNote){
        ContentValues values = new ContentValues();
        values.put(TaskNoteContract.TaskNoteDB.COLUMN_NAME, taskNote.getName());
        values.put(TaskNoteContract.TaskNoteDB.COLUMN_COZY_ID, taskNote.getIdCozy());
        values.put(TaskNoteContract.TaskNoteDB.COLUMN_FOLDER, taskNote.getFolder());
        return (int) CozyNoteHelper.getInstance(context).getReadableDatabase().insert(TaskNoteContract.TaskNoteDB.TABLE_NAME, null, values);
    }

    @Override
    public void update(Context context, TaskNote object) {
        ContentValues values = new ContentValues();
        values.put(TaskNoteContract.TaskNoteDB.COLUMN_NAME, object.getName());
        values.put(TaskNoteContract.TaskNoteDB.COLUMN_COZY_ID, object.getIdCozy());
        values.put(TaskNoteContract.TaskNoteDB.COLUMN_FOLDER, object.getFolder());
        CozyNoteHelper.getInstance(context).getReadableDatabase().update(TaskNoteContract.TaskNoteDB.TABLE_NAME, values, TaskNoteContract.TaskNoteDB.COLUMN_ID + "=?", new String[]{String.valueOf(object.getId())});
    }



    @Override
    public void delete(Context context, int id) {
        TaskDao taskDao = new TaskDao();
        taskDao.deleteByNoteId(context, id);
        CozyNoteHelper.getInstance(context).getReadableDatabase().delete(TaskNoteContract.TaskNoteDB.TABLE_NAME, TaskNoteContract.TaskNoteDB.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public List<Explorer> selectExplorer(Context context,String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    {
        String[] columns = {
                TaskNoteContract.TaskNoteDB.COLUMN_ID,
                TaskNoteContract.TaskNoteDB.COLUMN_NAME
        };

        Cursor checkList  = CozyNoteHelper.getInstance(context).getReadableDatabase().query(TaskNoteContract.TaskNoteDB.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        List<Explorer> explorers = new ArrayList<>();
        checkList.moveToFirst();
        while (!checkList.isAfterLast())
        {
            explorers.add(new Explorer(Integer.parseInt(checkList.getString(checkList.getColumnIndex(TaskNoteContract.TaskNoteDB.COLUMN_ID))), checkList.getString(checkList.getColumnIndex(TaskNoteContract.TaskNoteDB.COLUMN_NAME)), TaskNote.class));
            checkList.moveToNext();
        }
        checkList.close();
        return explorers;
    }

    public void deleteByFolder(Context context, int folderId)
    {
        TaskDao taskDao = new TaskDao();
        for (int id : selectByFolderId(context, folderId))
        {
            taskDao.deleteByNoteId(context, id);
            delete(context, id);
        }
    }

    public List<Integer> selectByFolderId(Context context, int folderId) {
        String[] columns = {
                TaskNoteContract.TaskNoteDB.COLUMN_ID
        };
        Cursor notes = CozyNoteHelper.getInstance(context).getReadableDatabase().query(TaskNoteContract.TaskNoteDB.TABLE_NAME, columns, TaskNoteContract.TaskNoteDB.COLUMN_FOLDER + "=?", new String[]{String.valueOf(folderId)}, null, null, null, null);
        List<Integer> list = new ArrayList<>();
        notes.moveToFirst();
        while (!notes.isAfterLast())
        {
            list.add(Integer.parseInt(notes.getString(notes.getColumnIndex(TaskNoteContract.TaskNoteDB.COLUMN_ID))));
            notes.moveToNext();
        }
        notes.close();
        return list;
    }

}
