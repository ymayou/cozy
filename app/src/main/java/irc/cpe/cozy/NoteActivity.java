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
import irc.cpe.cozy.Rest.WebserviceActivity;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Récupération de l'id de la note à modifier

        int id=-1;
        Bundle extras = getIntent().getExtras();
        id= extras.getInt("NOTE");

        final EditText textTitle=(EditText)findViewById(R.id.noteTitle);
        final EditText textContent=(EditText)findViewById(R.id.noteContent);



        if (id > -1){
            NoteDao noteDao = new NoteDao();
            Note note = new Note();
            //note = noteDao.select(getApplicationContext(), id);

            //Récupérer contenu note en base et insérer valeurs dans editText
            textTitle.setText(note.getName());
            textContent.setText(note.getContent());

        }

        Button noteButton=(Button)findViewById(R.id.noteButton);

        final int finalId = id;
        noteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String title;
                String content;

                title = textTitle.getText().toString();
                content = textContent.getText().toString();

                if(finalId >-1){
                    //Modification de la note existante
                    NoteDao noteDao = new NoteDao();
                    //noteDao.update(getApplicationContext(), newNote);
                }else{
                    Note newNote = new Note(title, content, 0);
                    //Insertion de la nouvelle note
                    NoteDao noteDao = new NoteDao();
                    noteDao.insert(getApplicationContext(), newNote);
                }



                Toast.makeText(getApplicationContext(), "Note '"+ title + "' sauvegardée",
                        Toast.LENGTH_LONG).show();
                finish();

                /*
                new AlertDialog.Builder(NoteActivity.this)
                        .setTitle("Entrée nouvelle note")
                        .setMessage("Voulez-vous enregistrer votre nouvelle note ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            //Action après validation
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Note non sauvegardée",
                                        Toast.LENGTH_LONG).show();
                                NoteActivity.this.startActivity(new Intent(NoteActivity.this, NoteActivity.class));
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                        */
            }

        });

        Button noteCancel=(Button)findViewById(R.id.noteCancel);

        noteCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(), "Note non sauvegardée",
                        Toast.LENGTH_LONG).show();
                NoteActivity.this.startActivity(new Intent(NoteActivity.this, NoteActivity.class));
                finish();

            }

        });

        Intent newIntent = new Intent(getApplicationContext(), WebserviceActivity.class);
        //startActivity(newIntent);
    }

}
