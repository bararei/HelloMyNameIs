package com.abercrombiealicia.hellomynameis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
 * @author Ali Abercrombie
 * Created on 3/19/2016.
 * @version 1.0.0
 *
 * This fragment displays the title and description of each individual project and the names attached to it.
 */
public class NameListFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TextView mProjectName;
    TextView mProjectDescription;
    TextView mMyNames;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NameListDatabaseObject nameListDatabaseObject;
    private ArrayList<NameListObject> mNamesListArrayList = new ArrayList<>();


    OnSubmitListener mCallback;

    /**
     * MainActivity implements this interface so the fragment can deliver information.
     */
    public interface OnSubmitListener {
        void onSubmitClickNameList();
    }

    /**
     * Used to make sure MainActivity has implemented the OnSubmitListener callback.
     * @param activity the activity
     */
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
     * Perform initialization of all fragments and loaders.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * Sets the view, wires up and sets the text widgets from the singleton, and sets the recyclerView.
     * Sets onClickListener behavior for adapter. Sets behavior for Floating Action Button. Finally,
     * calls mCallback.
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

        final View view = inflater.inflate(R.layout.fragment_namelist, container, false);

        ((MainActivity) getActivity()).showFab();
        //wire up the widgets
        mProjectName = (TextView) view.findViewById(R.id.namelist_project_name);
        mProjectDescription = (TextView) view.findViewById(R.id.namelist_project_description);
        mMyNames = (TextView) view.findViewById(R.id.namelist_my_names);

        //set name and description from singleton
        mProjectName.setText("Project Name: " + NameListSingleton.get(getContext()).getProjectName());
        mProjectDescription.setText("Project Description: " + NameListSingleton.get(getContext()).getmProjectDescription());

        //set RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_namelist);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Set arraylist for adapter
        createNamesArrayList();

        //finish setting recyclerView
        mAdapter = new NameListFragmentAdapter(mNamesListArrayList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        //set onClickListener behavior
        ((NameListFragmentAdapter) mAdapter).setOnItemClickListener(new NameListFragmentAdapter.MyClickListener() {
            @Override
            public void onItemClick(final int position, View v) {

                final NameListObject item;
                item = mNamesListArrayList.get(position);

                //save name to object and delete from database
                deleteNameFromList(position);
                //delete from arraylist and update adapter
                removeAtPosition(position);

                Snackbar snackbar = Snackbar
                        .make(v, "Name has been deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //if they undo...get a database instance, get names from database object, add them back in,
                                //add item back to array at same position, let the recyclerView know it's been updated.
                                DBHandler dbHandler = new DBHandler(getContext());
                                dbHandler.addNameListToDatabase(nameListDatabaseObject.getProjectID(),
                                        nameListDatabaseObject.getNameID1(), nameListDatabaseObject.getNameID2());
                                mNamesListArrayList.add(position, item);
                                mAdapter.notifyItemInserted(position);

                            }
                        });
                    snackbar.show();



            }
        });


        //Do mCallback when FAB is clicked
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

    /**
     * Sets up the namesArrayList. Creates an instance of DBHandler, gets the project name from the
     * singleton, then gets the project ID from the database and adds all names with that projectID to
     * the arrayList.
     */
    public void createNamesArrayList() {
        //Set up the arraylist stuff
        DBHandler dbHandler = new DBHandler(getContext());
        String projectName = NameListSingleton.get(getContext()).getProjectName();
        int projectID = dbHandler.getProjectId(projectName);
        mNamesListArrayList = dbHandler.getNameListFromDatabase(projectID);
    }

    /**
     * Saves the nameList item to a temporary object and then deletes it from the database.
     * @param position the position of the item in the mNamesListArrayList
     */
    public void deleteNameFromList(int position) {

        DBHandler dbHandler = new DBHandler(getContext());
        String projectName = NameListSingleton.get(getContext()).getProjectName();
        int projectID = dbHandler.getProjectId(projectName);
        String firstName = mNamesListArrayList.get(position).getmFirstName();
        String middleName = mNamesListArrayList.get(position).getmMiddleName();
        int firstNameID = dbHandler.getNameId(firstName);
        int middleNameID = dbHandler.getNameId(middleName);

        saveNameToObject(projectID, firstNameID, middleNameID);

        dbHandler.deleteNamesListFromDatabase(projectID, firstNameID, middleNameID);
    }

    /**
     * Lets the recyclerView adapter know the item has been removed and at what position
     * @param position the position of the item to be removed
     */
    public void removeAtPosition(int position) {
        mNamesListArrayList.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mNamesListArrayList.size());
    }

    /**
     * Instantiates a nameListDatabaseObject and then saves the deleted object's data to it in case
     * the user decides to undo the deletion.
     * @param projectID the projectID of the deleted item
     * @param firstNameID the nameID1 of the deleted item
     * @param middleNameID the nameID2 of the deleted item
     */
    public void saveNameToObject(int projectID, int firstNameID, int middleNameID) {
        nameListDatabaseObject = new NameListDatabaseObject();
        nameListDatabaseObject.setProjectID(projectID);
        nameListDatabaseObject.setNameID1(firstNameID);
        nameListDatabaseObject.setNameID2(middleNameID);


    }


}
