package irc.cpe.cozy.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

import irc.cpe.cozy.Model.ListElement;

/**
 * Created by Angèle on 07/01/2016.
 */
public class ChecklistAdapter extends ArrayAdapter<ListElement> {
    public ArrayList<ListElement> liste;
    int layoutId;

    public ChecklistAdapter(Context context, int textViewResourceId,
                            ArrayList<ListElement> liste){
        super(context, textViewResourceId, liste);
        this.liste = new ArrayList<ListElement>();
        this.liste.addAll(liste);
        layoutId = textViewResourceId;
    }

    public class ViewHolder {
        String text;
        CheckBox isChecked;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }

        return convertView;
    }
}
