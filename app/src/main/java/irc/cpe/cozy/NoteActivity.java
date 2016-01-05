package irc.cpe.cozy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import irc.cpe.cozy.Rest.EditNoteActivity;
import irc.cpe.cozy.Rest.WebserviceActivity;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteActivity.this.startActivity(new Intent(NoteActivity.this, EditNoteActivity.class));
                finish();
            }
        });

        Button noteButton=(Button)findViewById(R.id.noteButton);

        noteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String title;
                String content;

                EditText textTitle=(EditText)findViewById(R.id.noteTitle);
                EditText textContent=(EditText)findViewById(R.id.noteContent);

                title = textTitle.getText().toString();
                content = textContent.getText().toString();

                // Récupérer id du folder dans lequel est entrée la note
                //Note newNote = new Note(title, content, 1);
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
