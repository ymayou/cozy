package irc.cpe.cozy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

import irc.cpe.cozy.Model.ListElement;

/**
 * Created by Angèle on 18/12/2015.
 */
public class NewCheckListActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        //Generate list View from ArrayList
        //EditText textElement = (EditText) findViewById(R.id.textElement);
        //EditText editText = new EditText(this);

        //Array list of some elements
        ArrayList<ListElement> liste = new ArrayList<ListElement>();
        ListElement element = new ListElement("Penser à aller faire les courses", false);
        liste.add(element);
        element = new ListElement("Aller chez le coiffeur", false);
        liste.add(element);
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
}
