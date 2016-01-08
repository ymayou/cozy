package irc.cpe.cozy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import irc.cpe.cozy.Adapter.ExplorerAdapter;
import irc.cpe.cozy.Contract.FolderContract;
import irc.cpe.cozy.Contract.NoteContract;
import irc.cpe.cozy.Contract.TaskNoteContract;
import irc.cpe.cozy.Dao.FolderDao;
import irc.cpe.cozy.Dao.NoteDao;
import irc.cpe.cozy.Dao.TaskNoteDao;
import irc.cpe.cozy.Model.Explorer;
import irc.cpe.cozy.Model.Folder;
import irc.cpe.cozy.Model.Note;
import irc.cpe.cozy.Model.TaskNote;
import irc.cpe.cozy.Rest.LocalService;
import irc.cpe.cozy.Rest.ServiceManager;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //Activity results
    public static final int NOTE_EDITED = 1;
    public static final int CHECKLIST_EDITED = 10;
    public static final int LOGIN_RESULT_ACT = 700;
    public static final int LOGIN_RESULT_OK = 701;

    private List<Folder> foldersList = new ArrayList<>();
    private Menu menu;
    private List<Explorer> explorers;
    private NoteDao noteDao;
    private TaskNoteDao taskNoteDao;
    private ExplorerAdapter adapter;
    private MenuItem selectedItem;

    private LocalService syncService;

    private int currentFolder = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Launch Cozy sync service
        syncService = ServiceManager.getService(getApplicationContext());

        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent noteActi = new Intent(view.getContext(), NoteActivity.class);
                //noteActi.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                noteActi.putExtra("FOLDER", (selectedItem != null) ? selectedItem.getItemId() : 0);
                startActivityForResult(noteActi, NOTE_EDITED);
            }
        });

        FloatingActionButton fabList = (FloatingActionButton) findViewById(R.id.fabList);
        fabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent list = new Intent(v.getContext(), NewCheckListActivity.class);
                list.putExtra("FOLDER", (selectedItem != null) ? selectedItem.getItemId() : 0);
                startActivityForResult(list, CHECKLIST_EDITED);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();
        updateMenu();

        noteDao = new NoteDao();
        taskNoteDao = new TaskNoteDao();
        explorers = new ArrayList<>();
        adapter = new ExplorerAdapter(this, R.layout.explorer, explorers);
        final GridView grid = (GridView) findViewById(R.id.noteGrid);
        grid.setAdapter(adapter);

        registerForContextMenu(grid);

        reloadExplorer(currentFolder);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Explorer elem = (Explorer) grid.getItemAtPosition(position);
                if (elem.getType() == Note.class)
                {
                    Intent editNote = new Intent(view.getContext(), NoteActivity.class);
                    editNote.putExtra("NOTE", elem.getId());
                    startActivityForResult(editNote, NOTE_EDITED);
                }
                else if (elem.getType() == TaskNote.class)
                {
                    Intent editTaskNote = new Intent(view.getContext(), NewCheckListActivity.class);
                    editTaskNote.putExtra("CHECKLIST", elem.getId());
                    startActivityForResult(editTaskNote, NOTE_EDITED);
                }
            }
        });

        /*grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                final int pos = position;
                new AlertDialog.Builder(NavActivity.this)
                        .setTitle("Delete")
                        .setMessage("Confirm?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                NoteDao noteDao = new NoteDao();
                                noteDao.delete(getApplicationContext(), ((Explorer) grid.getItemAtPosition(pos)).getId());
                                reloadExplorer(0);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Cancel does nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });*/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this.getApplicationContext(), SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.main:
                reloadExplorer(0);
                break;
            case R.id.connexion:
                syncService.test();
                Intent login = new Intent(this.getApplicationContext(), LoginActivity.class);
                startActivityForResult(login, LOGIN_RESULT_ACT);
                break;
            default:
                break;
        }

        if (selectedItem != null)
            selectedItem.setChecked(false);
        item.setChecked(true);
        selectedItem = item;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case NOTE_EDITED:
                if(resultCode == NoteActivity.RESULT_OK){
                    reloadExplorer(currentFolder);
                }
                if (resultCode == NoteActivity.RESULT_CANCELED) {
                    //In case of cancellation
                }
                break;
            case LOGIN_RESULT_ACT:
                if (resultCode == LOGIN_RESULT_OK) {
                    selectedItem.setVisible(false);
                    reloadExplorer(0);
                }
                break;
            case CHECKLIST_EDITED:
                if (resultCode == NewCheckListActivity.RESULT_OK)
                    reloadExplorer(currentFolder);
                break;
        }
    }//onActivityResult

    private void reloadExplorer(int idFolder)
    {
        explorers.clear();
        explorers.addAll(noteDao.selectExplorer(this.getApplicationContext(),
                NoteContract.NoteDB.COLUMN_FOLDER + "=?", // columns for the where
                new String[]{String.valueOf(idFolder)}, // where
                null, // group rows
                null, // filter by group rows
                null,
                null
        ));
        explorers.addAll(taskNoteDao.selectExplorer(this.getApplicationContext(),
                TaskNoteContract.TaskNoteDB.COLUMN_FOLDER + "=?", // columns for the where
                new String[]{String.valueOf(idFolder)}, // where
                null, // group rows
                null, // filter by group rows
                null,
                null
        ));
        adapter.notifyDataSetChanged();
    }

    private void updateMenu()
    {
        final MenuItem itemFolder = menu.findItem(0);
        if(itemFolder != null)
            menu.removeItem(0);

        SubMenu sub = menu.addSubMenu(Menu.NONE, 0, 0, "Folders");
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        FolderDao dao = new FolderDao();

        String[] columns = {
                FolderContract.FolderDB.COLUMN_ID,
                FolderContract.FolderDB.COLUMN_NAME
        };

        String sortOrder = FolderContract.FolderDB.COLUMN_ID + " DESC";

        foldersList = dao.select(this.getApplicationContext(),
                columns,
                null, // columns for the where
                null, // where
                null, // group rows
                null, // filter by group rows
                sortOrder,
                null
        );

        for (Folder f : foldersList)
        {
            sub.add(Menu.NONE, f.getId(), f.getId(), f.getName()).setIcon(R.drawable.ic_folder_white).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (selectedItem != null)
                        selectedItem.setChecked(false);
                    item.setChecked(true);
                    selectedItem = item;
                    reloadExplorer(item.getItemId());
                    currentFolder = item.getItemId();
                    return onNavigationItemSelected(item);
                }
            });

        }
        // manage Folders
        sub.add(Menu.NONE, 0, foldersList.size() + 1, "Manage folders").setIcon(R.drawable.ic_settings_black).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent manage = new Intent(getApplicationContext(), ManageFoldersActivity.class);
                startActivity(manage);
                return onNavigationItemSelected(item);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menuLocal, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.noteGrid) {
            menuLocal.add(Menu.NONE, 0, 0, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Explorer toDelete = explorers.get(info.position);
        if (toDelete.getType().equals(Note.class))
        {
            noteDao.delete(getApplicationContext(), toDelete.getId());
        }
        else if (toDelete.getType().equals(TaskNote.class))
        {
            taskNoteDao.delete(getApplicationContext(), toDelete.getId());
        }
        reloadExplorer(currentFolder);
        return true;
    }

}
