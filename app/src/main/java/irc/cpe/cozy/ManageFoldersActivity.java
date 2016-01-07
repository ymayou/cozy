package irc.cpe.cozy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import irc.cpe.cozy.Adapter.FolderAdapter;
import irc.cpe.cozy.Contract.FolderContract;
import irc.cpe.cozy.Dao.FolderDao;
import irc.cpe.cozy.Model.Folder;

public class ManageFoldersActivity extends AppCompatActivity {

    private FolderAdapter adapter;
    ListView foldersView;
    private FolderDao dao = new FolderDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_folders);

        String[] columns = {
                FolderContract.FolderDB.COLUMN_ID,
                FolderContract.FolderDB.COLUMN_NAME
        };
        List<Folder> folders =  dao.select(getApplicationContext(), columns, null, null, null, null, null, null);
        foldersView = (ListView) findViewById(R.id.foldersList);
        adapter = new FolderAdapter(getApplicationContext(), R.layout.folder, folders);
        foldersView.setAdapter(adapter);
    }

    public void editFolder(final View view)
    {
        final Folder folder = (Folder)foldersView.getAdapter().getItem(foldersView.getPositionForView((View)view.getParent()));
        final AlertDialog.Builder editName = new AlertDialog.Builder(this);
        editName.setTitle("Edit folder name");
        editName.setCancelable(true);
        final EditText name = new EditText(this);
        name.setText(folder.getName());
        editName.setView(name);
        editName.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                folder.setName(name.getText().toString());
                dao.update(view.getContext(), folder);
                reloadData();
            }
        });
        editName.show();
    }

    public void deleteFolder(View view)
    {
        Folder folder = (Folder)foldersView.getAdapter().getItem(foldersView.getPositionForView((View)view.getParent()));
        dao.delete(view.getContext(), folder.getId());
        reloadData();
    }

    public void addFolder(View view)
    {
        EditText name = (EditText) findViewById(R.id.folderName);
        if (TextUtils.isEmpty(name.getText().toString()))
        {
            name.setError(getString(R.string.error_field_required));
            name.requestFocus();
        }
        else
        {
            Folder folder = new Folder(name.getText().toString());
            dao = new FolderDao();
            dao.insert(view.getContext(), folder);
            reloadData();
            name.setText("");
        }
    }

    public void reloadData()
    {
        String[] columns = {
                FolderContract.FolderDB.COLUMN_ID,
                FolderContract.FolderDB.COLUMN_NAME
        };
        dao = new FolderDao();
        List<Folder> folders =  dao.select(getApplicationContext(), columns, null, null, null, null, null, null);
        adapter.clear();
        adapter.addAll(folders);
        adapter.notifyDataSetChanged();
    }

}
