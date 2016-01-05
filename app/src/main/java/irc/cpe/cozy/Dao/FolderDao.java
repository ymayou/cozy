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
    public void insert(Context context, Folder object) {
        ContentValues values = new ContentValues();
        values.put(FolderContract.FolderDB.COLUMN_NAME, object.getName());
        CozyNoteHelper.getInstance(context).getReadableDatabase().insert(FolderContract.FolderDB.TABLE_NAME, null, values);
    }
}
