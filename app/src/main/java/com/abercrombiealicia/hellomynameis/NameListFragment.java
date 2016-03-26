package com.abercrombiealicia.hellomynameis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Spheven on 3/19/2016.
 */
public class NameListFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TextView mProjectNameLabel;
    TextView mProjectName;
    TextView mProjectDescriptionLabel;
    TextView mProjectDescription;
    TextView mMyNames;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<NameListObject> mNamesListArrayList = new ArrayList<>();




    OnSubmitListener mCallback;


    //Container activity must implement this interface so that the fragment can deliver information
    public interface OnSubmitListener {
        void onSubmitClickNameList();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnSubmitListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSubmitListener");
        }
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p/>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).showFab();
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_namelist, container, false);

        //wire up the widgets
        mProjectNameLabel = (TextView) view.findViewById(R.id.namelist_project_name_label);
        mProjectName = (TextView) view.findViewById(R.id.namelist_project_name);
        mProjectDescriptionLabel = (TextView) view.findViewById(R.id.namelist_project_description_label);
        mProjectDescription = (TextView) view.findViewById(R.id.namelist_project_description);
        mMyNames = (TextView) view.findViewById(R.id.namelist_my_names);

        //set name and description from singleton
        mProjectName.setText(NameListSingleton.get(getContext()).getProjectName());
        mProjectDescription.setText(NameListSingleton.get(getContext()).getmProjectDescription());

        //RecyclerView stuff... needs to be more robust, just a placeholder
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_namelist);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        createNamesArrayList();
        for(NameListObject object : mNamesListArrayList) {
            String item = object.getmFirstName();
            Log.i("WTF", item);
        }
        mAdapter = new NameListFragmentAdapter(mNamesListArrayList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        ((NameListFragmentAdapter) mAdapter).setOnItemClickListener(new NameListFragmentAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }
        });


        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            mCallback.onSubmitClickNameList();



            }
        });

        return view;
    }


    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p/>
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void createNamesArrayList() {
        //Set up the arraylist stuff
        DBHandler dbHandler = new DBHandler(getContext());
        String projectName = NameListSingleton.get(getContext()).getProjectName();
        int projectID = dbHandler.getProjectId(projectName);
        mNamesListArrayList = dbHandler.getNameListFromDatabase(projectID);
    }


}
