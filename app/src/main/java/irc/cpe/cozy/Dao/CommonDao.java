package irc.cpe.cozy.Dao;

import android.content.Context;

import java.util.List;

/**
 * Created by You on 05/01/2016.
 */
public interface CommonDao<T> {
    List<T> select(Context context, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);
    T selectById(Context context, int id);
    void insert(Context context, T object);
    void update(Context context, T object);
    void delete(Context context, int id);
}
