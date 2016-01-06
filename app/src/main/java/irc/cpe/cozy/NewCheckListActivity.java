package irc.cpe.cozy;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import irc.cpe.cozy.Model.ListElement;

/**
 * Created by Angèle on 18/12/2015.
 */
public class NewCheckListActivity extends AppCompatActivity {
    MyCustomAdapter dataAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist_main);

        //Generate list View from ArrayList
        displayListView();
    }

    public void displayListView() {
        EditText textElement = (EditText) findViewById(R.id.textElement);
        EditText editText = new EditText(this);
        //editText.setHint(string.editText);

        //Array list of some elements
        ArrayList<ListElement> liste = new ArrayList<ListElement>();
        ListElement element = new ListElement("Penser à aller faire les courses", false);
        liste.add(element);
        element = new ListElement("Aller chez le coiffeur", false);
        liste.add(element);
        element = new ListElement("Penser à ", false);
        liste.add(element);
        element = new ListElement("Penser à ", true);
        liste.add(element);
        element = new ListElement("Penser à ", false);
        liste.add(element);

        //liste.get(1).setName("BlaBla");

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this, R.layout.list_layout, liste);
        final ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        //EditText textElement =(EditText)findViewById(R.id.textElement);

        //ListElement elem = new ListElement("New element", false);

        //Récupérer contenu element en base et insérer valeurs dans editText
        // textElement.setText(elem.getName());


        /*textElement.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // you can call or do what you want with textElement here
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });*/

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                ListElement element = (ListElement) parent.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + element.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private class MyCustomAdapter extends ArrayAdapter<ListElement> {
        private ArrayList<ListElement> liste;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<ListElement> liste) {
            super(context, textViewResourceId, liste);
            this.liste = new ArrayList<ListElement>();
            this.liste.addAll(liste);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_layout, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListElement element = liste.get(position);
            //holder.name.setText(element.getName());
            holder.name.setChecked(element.isSelected());
            holder.name.setTag(element);

            return convertView;
        }
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
