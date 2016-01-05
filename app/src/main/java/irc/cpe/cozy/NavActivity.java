package irc.cpe.cozy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import irc.cpe.cozy.Adapter.ExplorerAdapter;
import irc.cpe.cozy.Contract.FolderContract;
import irc.cpe.cozy.Contract.NoteContract;
import irc.cpe.cozy.Dao.FolderDao;
import irc.cpe.cozy.Dao.NoteDao;
import irc.cpe.cozy.Model.Explorer;
import irc.cpe.cozy.Model.Folder;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    List<Folder> foldersList = new ArrayList<>();

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
                //Mettre l'activité en startActivityForResult
                
                NavActivity.this.startActivity(new Intent(NavActivity.this, NoteActivity.class));
                //NavActivity.this.startActivity(new Intent(NavActivity.this, NewCheckListActivity.class));
                /*Intent i = new Intent(view.getContext(), NoteActivity.class);
                startActivityForResult(i, 1);*/
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

        FolderDao dao = new FolderDao();

        /*Folder f = new Folder("folder 1");
        dao.insert(f);*/

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
            sub.add(Menu.NONE, f.getId(), f.getId(), f.getName());
        }
        // add Folder
        sub.add(Menu.NONE, 0, foldersList.size(), "Manage folders");

        NoteDao noteDao = new NoteDao();
        List<Explorer> explorers = noteDao.selectExplorer(this.getApplicationContext(),
                null, // columns for the where
                null, // where
                null, // group rows
                null, // filter by group rows
                null,
                null
        );

        ExplorerAdapter adapter = new ExplorerAdapter(this, R.layout.explorer, explorers);
        final GridView grid = (GridView) findViewById(R.id.noteGrid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editNote = new Intent(view.getContext(), NoteActivity.class);
                editNote.putExtra("NOTE", ((Explorer) grid.getItemAtPosition(position)).getId());
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
        /*Toast.makeText(this.getApplicationContext(), id + "", Toast.LENGTH_LONG).show();
        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        if (id == 0)
        {
            // manage folders
            Intent manage = new Intent(getApplicationContext(), ManageFoldersActivity.class);
            startActivity(manage);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
