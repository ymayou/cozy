package irc.cpe.cozy.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import irc.cpe.cozy.Adapter.FolderAdapter1;
import irc.cpe.cozy.Dao.FolderDao;
import irc.cpe.cozy.Model.Folder;
import irc.cpe.cozy.R;

import java.util.ArrayList;
import java.util.List;

public class FolderFragment extends Fragment {

    public static final String FOLDERS = "folder-list";
    private List<Folder> folderList;
    private View view;

    public FolderFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FolderFragment newInstance(List<Folder> folders) {
        FolderFragment fragment = new FolderFragment();
        Bundle args = new Bundle();
        ArrayList<Folder> arrFolders = new ArrayList<>();
        arrFolders.addAll(folders);
        args.putSerializable(FOLDERS, arrFolders);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            folderList = new ArrayList<>();
            folderList = (ArrayList)getArguments().getSerializable(FOLDERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_folder_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.folderList);
        listView.setAdapter(new FolderAdapter1(getContext(), R.layout.fragment_folder, folderList));

        Button addBt = (Button) view.findViewById(R.id.deleteFolder);
        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView folderName = (TextView) view.findViewById(R.id.folderName);
                Folder folder = new Folder();
                folder.setName(folderName.getText().toString());
                FolderDao dao = new FolderDao();
                dao.insert(v.getContext(), folder);

                ExplorerFragment fragment = new ExplorerFragment();
                getFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();
            }
        });

        Button deleteBt =(Button) view.findViewById(R.id.deleteFolder);
        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "delete", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*@Override
    public void onClick(View v) {
        TextView folderName = (TextView) view.findViewById(R.id.folderName);
        Folder folder = new Folder();
        folder.setName(folderName.getText().toString());
        FolderDao dao = new FolderDao();
        dao.insert(v.getContext(), folder);

        ExplorerFragment fragment = new ExplorerFragment();
        getFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();
    }*/
}
