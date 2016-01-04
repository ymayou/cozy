package irc.cpe.cozy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import irc.cpe.cozy.Model.Explorer;
import irc.cpe.cozy.R;

/**
 * Created by You on 01/01/2016.
 */
public class ExplorerAdapter extends ArrayAdapter<irc.cpe.cozy.Model.Explorer> {
    int layoutId;
    public ExplorerAdapter(Context context, int resource, List<irc.cpe.cozy.Model.Explorer> objects) {
        super(context, resource, objects);
        layoutId = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        irc.cpe.cozy.Model.Explorer item = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.nameExplorer);
        name.setText(item.getName());
        /*ImageView image = (ImageView) convertView.findViewById(R.id.imageExplorer);
        image.set*/
        return convertView;
    }
}
