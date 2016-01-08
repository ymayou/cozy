package irc.cpe.cozy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import irc.cpe.cozy.Model.Folder;
import irc.cpe.cozy.R;

public class FolderAdapter extends ArrayAdapter<Folder> {
    int layoutId;
    public FolderAdapter(Context context, int resource, List<Folder> objects) {
        super(context, resource, objects);
        layoutId = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        irc.cpe.cozy.Model.Folder item = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }

        TextView name = (TextView)convertView.findViewById(R.id.lblFolder);
        name.setText(item.getName());
        return convertView;
    }
}
