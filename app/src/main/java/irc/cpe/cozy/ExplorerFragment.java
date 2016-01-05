package irc.cpe.cozy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import irc.cpe.cozy.Adapter.ExplorerAdapter;
import irc.cpe.cozy.Contract.CozyNoteHelper;
import irc.cpe.cozy.Contract.NoteContract;
import irc.cpe.cozy.Model.Explorer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExplorerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExplorerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExplorerFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<Explorer> explorerList;

    public ExplorerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExplorerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExplorerFragment newInstance(String param1, String param2) {
        ExplorerFragment fragment = new ExplorerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        explorerList = new ArrayList<>();
        SQLiteDatabase db = CozyNoteHelper.getInstance(getContext()).getReadableDatabase();
        String [] cols = {
                NoteContract.NoteDB.COLUMN_ID,
                NoteContract.NoteDB.COLUMN_NAME,
                NoteContract.NoteDB.COLUMN_FOLDER,
                NoteContract.NoteDB.COLUMN_CONTENT
        };
        Cursor notes = db.query(
                NoteContract.NoteDB.TABLE_NAME, // table name
                cols, // columns
                null, // columns for the where
                null, // where
                null, // group rows
                null, // filter by group rows
                null // order by
        );
        notes.moveToFirst();
        while (!notes.isAfterLast())
        {
            explorerList.add(new Explorer(Integer.parseInt(notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_ID))), notes.getString(notes.getColumnIndex(NoteContract.NoteDB.COLUMN_NAME))));
            notes.moveToNext();
        }
        notes.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_explorer, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.noteGrid);

        ExplorerAdapter adapter = new ExplorerAdapter(getActivity(), R.layout.explorer, explorerList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
            mListener.onItemSelected(id);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onItemSelected(long id);
    }
}
