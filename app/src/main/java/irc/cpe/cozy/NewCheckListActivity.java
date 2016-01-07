package irc.cpe.cozy;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import irc.cpe.cozy.Adapter.ChecklistAdapter;
import irc.cpe.cozy.Model.ListElement;

/**
 * Created by Angèle on 18/12/2015.
 */
public class NewCheckListActivity extends AppCompatActivity {
    ChecklistAdapter dataAdapter = null;
    ArrayList<ListElement> liste = new ArrayList<ListElement>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final Context c = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_checklist);

        EditText textElement = (EditText) findViewById(R.id.textElement);
        CheckBox ck = (CheckBox) findViewById(R.id.checkBox1);

        //Array list of some elements for Database
        ListElement element = null;
        element = new ListElement("", false);
        liste.add(element);

        //create an ArrayAdaptar from the String Array
        dataAdapter = new ChecklistAdapter(this,
                R.layout.list_layout, liste);
        ListView listView = (ListView) findViewById(R.id.listView_checklist);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        //EditTextChange text = null; // Surveille la modification d'un élément EditText

       /* textElement.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // you can call or do what you want with your EditText here
                /*EditText textElement = (EditText) findViewById(R.id.textElement);
                liste.get(R.id.textElement).setName(textElement.getEditableText().toString());
                ListElement element = new ListElement(textElement.getText().toString(), false);}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });*/

        checkButtonClick();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.checkBox1:
                if (checked) {
                } else
                    break;
        }
    }

    private void checkButtonClick() {   // Gère le clic sur le bouton ajouter
        ImageButton myButton = (ImageButton) findViewById(R.id.addChecklist);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText ed = (EditText) findViewById(R.id.textElement);
                if(ed.getText().toString().isEmpty()) {}
                else {
                    ListElement element = new ListElement(findViewById(R.id.newElement).toString() , false);
                    liste.add(element);     // Ajoute le nouvel élément à la liste

                    ed.setText(""); // Vide le nouvel élément
                    dataAdapter.clear();
                    dataAdapter.addAll(liste);  // Met à jour la liste
                }

                StringBuffer responseText = new StringBuffer();

                ArrayList<ListElement> listElements = dataAdapter.liste;
                for(int i=0;i<listElements.size();i++){
                    ListElement liste = listElements.get(i);
                    if(liste.isSelected()){
                        responseText.append("\n" + liste.getName() + "is added");
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();
            }
        });
    }

   /* public class EditTextChange extends AppCompatActivity {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.list_layout);

            EditText text = (EditText) findViewById(R.id.textElement);
            text.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    EditText textElement = (EditText) findViewById(R.id.textElement);
                    liste.get(R.id.textElement).setName(textElement.getEditableText().toString());
                    ListElement element = new ListElement(textElement.getText().toString(), false);
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {}
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {}
            });

        }
    }*/
}