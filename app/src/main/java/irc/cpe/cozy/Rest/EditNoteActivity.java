package irc.cpe.cozy.Rest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import irc.cpe.cozy.NavActivity;
import irc.cpe.cozy.R;

public class EditNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNoteActivity.this.startActivity(new Intent(EditNoteActivity.this, NavActivity.class));
                finish();
            }
        });

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
