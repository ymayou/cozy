package irc.cpe.cozy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import irc.cpe.cozy.Model.SimpleNotePreview;
import irc.cpe.cozy.R;

/**
 * Created by You on 01/01/2016.
 */
public class NotePreview extends ArrayAdapter<SimpleNotePreview> {
    int layoutId;
    public NotePreview(Context context, int resource, List<SimpleNotePreview> objects) {
        super(context, resource, objects);
        layoutId = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleNotePreview item = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.noteTitle);
        name.setText(item.getName());
        return convertView;
    }
}
