package irc.cpe.cozy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import irc.cpe.cozy.Adapter.FolderAdapter;
import irc.cpe.cozy.Contract.FolderContract;
import irc.cpe.cozy.Dao.FolderDao;
import irc.cpe.cozy.Model.Folder;

public class ManageFoldersActivity extends AppCompatActivity {

    private FolderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_folders);

        String[] columns = {
                FolderContract.FolderDB.COLUMN_ID,
                FolderContract.FolderDB.COLUMN_NAME
        };
        FolderDao dao = new FolderDao();
        List<Folder> folders =  dao.select(getApplicationContext(), columns, null, null, null, null, null, null);
        ListView foldersView = (ListView) findViewById(R.id.foldersList);
        adapter = new FolderAdapter(getApplicationContext(), R.layout.folder, folders);
        foldersView.setAdapter(adapter);
    }

    public void editFolder(View view)
    {
        Toast.makeText(view.getContext(), "edit", Toast.LENGTH_SHORT).show();
    }

    public void deleteFolder(View view)
    {
        Toast.makeText(view.getContext(), "delete", Toast.LENGTH_SHORT).show();
    }

    public void addFolder(View view)
    {
        EditText name = (EditText) findViewById(R.id.folderName);
        Folder folder = new Folder(name.getText().toString());
        FolderDao dao = new FolderDao();
        dao.insert(view.getContext(), folder);
        reloadData();
    }

    public void reloadData()
    {
        String[] columns = {
                FolderContract.FolderDB.COLUMN_ID,
                FolderContract.FolderDB.COLUMN_NAME
        };
        FolderDao dao = new FolderDao();
        List<Folder> folders =  dao.select(getApplicationContext(), columns, null, null, null, null, null, null);
        adapter.clear();
        adapter.addAll(folders);
        adapter.notifyDataSetChanged();
    }

}
