package irc.cpe.cozy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Texte dans editText
        EditText editTitle=(EditText)findViewById(R.id.editTitle);
        EditText editContent=(EditText)findViewById(R.id.editContent);

        editTitle.setText("Titre de la note");
        editContent.setText("Contenu de la note à éditer");


        Button editOk=(Button)findViewById(R.id.editOk);

        editOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(), "Modification de 'Title' sauvegardée",
                        Toast.LENGTH_LONG).show();
                EditNoteActivity.this.startActivity(new Intent(EditNoteActivity.this, NavActivity.class));
                finish();

            }

        });

        Button editCancel=(Button)findViewById(R.id.editCancel);

        editCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(), "Modification de 'Title' non sauvegardée",
                        Toast.LENGTH_LONG).show();
                EditNoteActivity.this.startActivity(new Intent(EditNoteActivity.this, NavActivity.class));
                finish();

            }

        });
    }

}
