package irc.cpe.cozy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import irc.cpe.cozy.Dao.NoteDao;
import irc.cpe.cozy.Model.Note;

public class NoteActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Récupération de l'id de la note à modifier
        int idNote;
        int idFolder;

        idNote = getIntent().getIntExtra("NOTE", 0);
        idFolder = getIntent().getIntExtra("FOLDER", 0);

        EditText textTitle=(EditText)findViewById(R.id.noteTitle);
        EditText textContent=(EditText)findViewById(R.id.noteContent);

        if (idNote > 0){
            NoteDao noteDao = new NoteDao();
            //Récupération du contenu note en base et insertion valeurs dans editText
            Note noteChanged = noteDao.selectById(getApplicationContext(), idNote);
            textTitle.setText(noteChanged.getName());
            textContent.setText(noteChanged.getContent());
        }

        Button noteButton=(Button)findViewById(R.id.noteButton);
        final int finalIdNote = idNote;
        final int finalIdFolder = idFolder;
        final String finalTitle = textTitle.getText().toString();
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText newTextTitle=(EditText)findViewById(R.id.noteTitle);
                EditText newTextContent=(EditText)findViewById(R.id.noteContent);
                String newTitle = newTextTitle.getText().toString();
                String newContent = newTextContent.getText().toString();
                if(finalIdNote >0){
                    //Modification de la note existante
                    NoteDao noteDao = new NoteDao();
                    Note editNote = new Note(finalIdNote, newTitle, newContent, finalIdFolder);
                    noteDao.update(getApplicationContext(), editNote);
                    Toast.makeText(getApplicationContext(), "Note '"+ newTitle + "' sauvegardée",
                            Toast.LENGTH_LONG).show();
                    Intent returnIntent = new Intent();
                    setResult(NoteActivity.RESULT_OK, returnIntent);
                    finish();
                }else{
                    Note newNote = new Note(newTitle, newContent, finalIdFolder);
                    //Insertion de la nouvelle note
                    NoteDao noteDao = new NoteDao();
                    noteDao.insert(getApplicationContext(), newNote);
                    Toast.makeText(getApplicationContext(), "Note '"+ newTitle + "' sauvegardée",
                            Toast.LENGTH_LONG).show();
                    Intent returnIntent = new Intent();
                    setResult(NoteActivity.RESULT_OK, returnIntent);
                    finish();
                }

            }

        });

        Button noteCancel=(Button)findViewById(R.id.noteCancel);
        noteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), "Note non sauvegardée",
                        Toast.LENGTH_LONG).show();
                Intent returnIntent = new Intent();
                setResult(NoteActivity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        //Intent newIntent = new Intent(getApplicationContext(), WebserviceActivity.class);
        //startActivity(newIntent);
    }

}
