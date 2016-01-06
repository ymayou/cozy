package irc.cpe.cozy.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import irc.cpe.cozy.Contract.CozyNoteHelper;
import irc.cpe.cozy.Contract.FolderContract;
import irc.cpe.cozy.Model.Folder;

/**
 * Created by You on 05/01/2016.
 */
public class FolderDao implements CommonDao<Folder> {
    @Override
    public List<Folder> select(Context context, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor folders = CozyNoteHelper.getInstance(context).getReadableDatabase().query(FolderContract.FolderDB.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        List<Folder> list = new ArrayList<>();
        folders.moveToFirst();
        while (!folders.isAfterLast())
        {
            list.add(new Folder(Integer.parseInt(folders.getString(folders.getColumnIndex(FolderContract.FolderDB.COLUMN_ID))), folders.getString(folders.getColumnIndex(FolderContract.FolderDB.COLUMN_NAME))));
            folders.moveToNext();
        }
        folders.close();
        return list;
    }

    @Override
    public Folder selectById(Context context, int id) {
        String[] columns = {
                FolderContract.FolderDB.COLUMN_ID,
                FolderContract.FolderDB.COLUMN_NAME
        };

        Cursor folders  = CozyNoteHelper.getInstance(context).getReadableDatabase().query(FolderContract.FolderDB.TABLE_NAME, columns, FolderContract.FolderDB.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        folders.moveToFirst();
        Folder folder = new Folder(Integer.parseInt(folders.getString(folders.getColumnIndex(FolderContract.FolderDB.COLUMN_ID))), folders.getString(folders.getColumnIndex(FolderContract.FolderDB.COLUMN_NAME)));
        folders.close();
        return folder;
    }

    @Override
    public void insert(Context context, Folder object) {
        ContentValues values = new ContentValues();
        values.put(FolderContract.FolderDB.COLUMN_NAME, object.getName());
        CozyNoteHelper.getInstance(context).getReadableDatabase().insert(FolderContract.FolderDB.TABLE_NAME, null, values);
    }

    @Override
    public void update(Context context, Folder object) {
        ContentValues values = new ContentValues();
        values.put(FolderContract.FolderDB.COLUMN_NAME, object.getName());
        CozyNoteHelper.getInstance(context).getReadableDatabase().update(FolderContract.FolderDB.TABLE_NAME, values, FolderContract.FolderDB.COLUMN_ID + "=?", new String[]{String.valueOf(object.getId())});
    }

    @Override
    public void delete(Context context, int id) {
        CozyNoteHelper.getInstance(context).getReadableDatabase().delete(FolderContract.FolderDB.TABLE_NAME, FolderContract.FolderDB.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}
