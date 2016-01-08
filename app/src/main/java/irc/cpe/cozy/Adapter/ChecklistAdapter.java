package irc.cpe.cozy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;

import irc.cpe.cozy.Model.Task;
import irc.cpe.cozy.NewCheckListActivity;
import irc.cpe.cozy.R;

public class ChecklistAdapter extends ArrayAdapter<Task> {
    int layoutId;
    public ChecklistAdapter(Context context, int resource, List<Task> objects) {
        super(context, resource, objects);
        layoutId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Task item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }

        EditText textElement = (EditText) convertView.findViewById(R.id.textElement);
        textElement.setText(item.getContent());

        CheckBox checkBox1 = (CheckBox) convertView.findViewById(R.id.checkBox1);
        checkBox1.setChecked(item.isStatus());

        textElement.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText elem = (EditText) v.findViewById(R.id.textElement);
                item.setContent(elem.getText().toString());
                if (NewCheckListActivity.taskUpdated.contains(item))
                    NewCheckListActivity.taskUpdated.remove(item);
                NewCheckListActivity.taskUpdated.add(item);
            }
        });
        return convertView;
    }
}
