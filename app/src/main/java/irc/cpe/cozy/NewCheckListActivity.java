package irc.cpe.cozy;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

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

        checkButtonClick();
    }

    private void displayListView() {
        //Array list d'élements
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

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this, R.layout.list_layout, liste);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                ListElement element = (ListElement) parent.getItemAtPosition(position);
                element.setName("Blabla");
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + element.getName(),
                        Toast.LENGTH_LONG).show();

            }
        });
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
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_layout, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        ListElement element = (ListElement) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        element.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListElement element = (ListElement)liste.get(position);
            holder.name.setText(element.getName());
            holder.name.setChecked(element.isSelected());
            holder.name.setTag(element);

            return convertView;
        }
    }

    private void checkButtonClick() {
        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<ListElement> liste = dataAdapter.liste;
                for(int i=0;i<liste.size();i++){
                    ListElement element = liste.get(i);
                    if(element.isSelected()){
                        responseText.append("\n" + element.getName());
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();
            }
        });
    }
}
