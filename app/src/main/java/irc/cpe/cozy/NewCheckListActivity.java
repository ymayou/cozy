package irc.cpe.cozy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import irc.cpe.cozy.Adapter.ChecklistAdapter;
import irc.cpe.cozy.Contract.NoteTaskNoteContract;
import irc.cpe.cozy.Contract.TaskNoteContract;
import irc.cpe.cozy.Dao.TaskDao;
import irc.cpe.cozy.Dao.TaskNoteDao;
import irc.cpe.cozy.Model.Folder;
import irc.cpe.cozy.Model.ListElement;
import irc.cpe.cozy.Model.Task;
import irc.cpe.cozy.Model.TaskNote;

public class NewCheckListActivity extends AppCompatActivity {

    private ChecklistAdapter dataAdapter = null;
    private TaskNoteDao taskNoteDao;
    private TaskDao taskDao;
    private List<Task> tasks;
    private TaskNote taskNote;
    private int idFolder;
    private boolean nameChanged = false;
    public static List<Task> taskUpdated = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_check_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // tasks =  new ArrayList<>();
        taskNoteDao = new TaskNoteDao();
        taskDao = new TaskDao();

        EditText title = (EditText) findViewById(R.id.listeTitle);

        int idList = getIntent().getIntExtra("CHECKLIST", 0);
        idFolder = getIntent().getIntExtra("FOLDER", 0);
        tasks = new ArrayList<>();
        if (idList > 0) // edition
        {
            taskNote = taskNoteDao.selectById(this.getApplicationContext(), idList);
            tasks = taskNote.getTasks();
            title.setText(taskNote.getName());
        }

        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                nameChanged = true;
            }
        });

        ListView tasksView = (ListView) findViewById(R.id.listView_checklist);
        dataAdapter = new ChecklistAdapter(getApplicationContext(), R.layout.list_layout, tasks);
        tasksView.setAdapter(dataAdapter);
    }

    public void addElement(View view)
    {
        EditText title = (EditText) findViewById(R.id.listeTitle);
        if (TextUtils.isEmpty(title.getText().toString()))
        {
            title.setError(getString(R.string.error_field_required_other));
            title.requestFocus();
        }
        else
        {
            if (taskNote == null)
            {
                taskNote = new TaskNote(title.getText().toString(), null, idFolder);
                taskNote.setId(taskNoteDao.insertForId(this.getApplicationContext(), taskNote));
                taskNote.setTasks(tasks);
                taskNote.setFolder(idFolder);
            }
            EditText element = (EditText) findViewById(R.id.newElement);
            if (TextUtils.isEmpty(element.getText().toString()))
            {
                element.setError(getString(R.string.error_field_required));
                element.requestFocus();
            }
            else
            {
                Task task = new Task(false, element.getText().toString(), taskNote.getId());
                taskNote.getTasks().add(task);
                taskDao.insert(this.getApplicationContext(), task);
                element.setText("");
                dataAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                boolean ok = true;

                if (nameChanged)
                {
                    EditText name = (EditText) findViewById(R.id.listeTitle);
                    if (TextUtils.isEmpty(name.getText().toString()))
                    {
                        if (taskNote == null) {
                            // cancel
                            return super.onOptionsItemSelected(item);
                        }
                        name.setError(getString(R.string.error_field_required));
                        name.requestFocus();
                        ok = false;
                    }
                    else
                    {
                        if (taskNote == null) {
                                taskNote = new TaskNote(name.getText().toString(), null, idFolder);
                                taskNote.setId(taskNoteDao.insertForId(this.getApplicationContext(), taskNote));
                        }
                        else if (!name.getText().toString().equals(taskNote.getName()))
                        {
                            taskNote.setName(name.getText().toString());
                            taskNoteDao.update(getApplicationContext(), taskNote);
                        }
                    }
                }

                if (taskUpdated.size() > 0)
                {
                    EditText name = (EditText) findViewById(R.id.listeTitle);
                    name.requestFocus();
                    for (Task t : taskUpdated)
                        taskDao.update(getApplicationContext(), t);
                }

                if (ok) {
                    Intent returnIntent = new Intent();
                    setResult(NewCheckListActivity.RESULT_OK, returnIntent);
                    //return super.onOptionsItemSelected(item);
                    finish();
                }
                break;
        }
        return true;
    }

    public void onCheckboxClicked(View view)
    {
        ListView list = (ListView) findViewById(R.id.listView_checklist);
        Task task = (Task)list.getAdapter().getItem(list.getPositionForView((View)view.getParent()));
        task.setStatus(((CheckBox) findViewById(R.id.checkBox1)).isChecked());
        taskDao.update(getApplicationContext(), task);
    }

    public void deleteTask(View view)
    {
        ListView list = (ListView) findViewById(R.id.listView_checklist);
        Task task = (Task)list.getAdapter().getItem(list.getPositionForView((View)view.getParent()));
        taskNote.getTasks().remove(task);
        taskDao.delete(getApplicationContext(), task.getId());
        dataAdapter.notifyDataSetChanged();
    }
}
