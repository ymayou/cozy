package irc.cpe.cozy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import irc.cpe.cozy.Adapter.ExplorerAdapter;
import irc.cpe.cozy.Contract.CozyNoteHelper;
import irc.cpe.cozy.Contract.FolderContract;
import irc.cpe.cozy.Contract.NoteContract;
import irc.cpe.cozy.Model.Explorer;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavActivity.this.startActivity(new Intent(NavActivity.this, PersonalActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        menu.clear();
        SubMenu sub = menu.addSubMenu(Menu.NONE, 0, 0, "Folders");
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        CozyNoteHelper helper = new CozyNoteHelper(this.getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        /*Folder f = new Folder("folder 1");
        ContentValues values = new ContentValues();
        values.put(FolderContract.FolderDB.COLUMN_NAME, f.getName());
        db.insert(FolderContract.FolderDB.TABLE_NAME, null, values);*/

        String[] columns = {
                FolderContract.FolderDB.COLUMN_ID,
                FolderContract.FolderDB.COLUMN_NAME
        };

        String sortOrder = FolderContract.FolderDB.COLUMN_ID + " DESC";

        Cursor folders = db.query(
                FolderContract.FolderDB.TABLE_NAME, // table name
                columns, // columns
                null, // columns for the where
                null, // where
                null, // group rows
                null, // filter by group rows
                sortOrder // order by
        );
        folders.moveToFirst();
        int tmp = 1;
        while (!folders.isAfterLast())
        {
            sub.add(Menu.NONE, 0, tmp, folders.getString(folders.getColumnIndex(FolderContract.FolderDB.COLUMN_NAME)));
            tmp++;
            folders.moveToNext();
        }
        folders.close();




        // explorerList.add();
        // explorerList.add(new Explorer(folders.getString(folders.getColumnIndex(FolderContract.FolderDB.COLUMN_NAME))));
        /*notes.add(new ExplorerAdapter("note"));
        notes.add(new ExplorerAdapter("note"));
        notes.add(new ExplorerAdapter("note"));*/

        List<Explorer> explorerList = new ArrayList<>();
        //List<Note> noteList = new ArrayList<>();
        String [] cols = {
            NoteContract.NoteDB.COLUMN_ID,
                NoteContract.NoteDB.COLUMN_NAME,
                NoteContract.NoteDB.COLUMN_FOLDER,
                NoteContract.NoteDB.COLUMN_CONTENT
        };
        Cursor notes = db.query(
                NoteContract.NoteDB.TABLE_NAME, // table name
                cols, // columns
                null, // columns for the where
                null, // where
                null, // group rows
                null, // filter by group rows
                null // order by
        );
        notes.moveToFirst();
        while (!notes.isAfterLast())
        {
            explorerList.add(new Explorer(Integer.parseInt(notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_ID))), notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_NAME))));
            notes.moveToNext();
        }
        notes.close();
        ExplorerAdapter adapter = new ExplorerAdapter(this, R.layout.explorer, explorerList);
        //ArrayAdapter<Note> adap = new ArrayAdapter<>(this, R.layout.explorer, noteList);
        final GridView grid = (GridView) findViewById(R.id.noteGrid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editNote = new Intent(view.getContext(), NewMarkActivity.class);
                editNote.putExtra("NOTE", ((Explorer)grid.getItemAtPosition(position)).getName());
                startActivity(editNote);
            }
        });

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
