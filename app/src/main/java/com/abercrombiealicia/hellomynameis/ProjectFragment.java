package com.abercrombiealicia.hellomynameis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abercrombiealicia.hellomynameis.R.id;
import com.abercrombiealicia.hellomynameis.R.layout;
import com.abercrombiealicia.hellomynameis.RecyclerItemClickListener.OnItemClickListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * @author Ali Abercrombie
 * Created on 3/19/2016.
 * @version 1.0.0
 *
 * Displays all the projects created by the user. Uses a recyclerView.
 */
public class ProjectFragment extends Fragment implements OnItemSelectedListener,
        OnItemLongClickListener{

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private LayoutManager mLayoutManager;
    private ProjectDatabaseObject projectDatabaseObject;
    private ArrayList<ProjectObject> projectArrayList = new ArrayList<>();
    private TextView mAddAProject;

    String mProjectName;
    String mProjectDescription;

    ProjectFragment.OnSubmitListener mCallback;


    /**
     * MainActivity implements this interface so the fragment can deliver information.
     */
    public interface OnSubmitListener {
        void onSubmitClickProjectList();
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
            mCallback = (ProjectFragment.OnSubmitListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity
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
     * Sets the view and sets the recyclerView.
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(layout.fragment_project, container, false);

        AdView mAdView = (AdView) view.findViewById(id.adView);
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView.loadAd(adRequest);

        //show FAB
        ((MainActivity) getActivity()).showFab();


        DBHandler dbHandler = new DBHandler(getContext());

        //get all projects from database
        projectArrayList = dbHandler.getProjectsFromDatabase();

        //set the empty view
        mAddAProject = (TextView) view.findViewById(id.add_a_project);

        //set the recyclerView
        mRecyclerView = (RecyclerView) view.findViewById(id.recycler_view_project);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ProjectFragmentAdapter(projectArrayList);
        mRecyclerView.setAdapter(mAdapter);
        ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        //check if the recyclerView is empty and display message to add project if it is
        setEmptyViewVisibility();

        //set the onTouchListener from the custom RecyclerItemClickListener class
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new OnItemClickListener() {

            /**
             * Set project name and project description in the singleton from the
             * projectArrayList and then calls mCallback to send user to NameListFragment.
             * @param childView View of the item that was clicked.
             * @param position  Position of the item that was clicked.
             */
            @Override
            public void onItemClick(View childView, int position) {
                NameListSingleton.get(getContext()).setProjectName(projectArrayList.get
                        (position).getProjectName());
                NameListSingleton.get(getContext()).setmProjectDescription(projectArrayList.get(position).getProjectDescription());

                mCallback.onSubmitClickProjectList();
            }

            /**
             * Allows a user to delete a project from the list of projects with long press.
             * @param childView View of the item that was long pressed.
             * @param position  Position of the item that was long pressed.
             */
            @Override
            public void onItemLongPress(View childView, final int position) {
                final ProjectObject projectObject;
                projectObject = projectArrayList.get(position);

                //save project as object and then delete from database
                deleteProjectFromList(position);
                //remove from arraylist
                removeAtPosition(position);
                ((MainActivity) getActivity()).updateDrawer();

                setEmptyViewVisibility();

                //notify user item has been deleted and offer chance to undo
                Snackbar snackbar = Snackbar
                        .make(childView, "Project has been deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DBHandler dbHandler = new DBHandler(getContext());
                                //get project information from object and add back to database
                                dbHandler.reAddProjectToDatabase(projectDatabaseObject.getmProjectID(),
                                        projectDatabaseObject.getmProjectName(), projectDatabaseObject.getmProjectDescription());
                                //add back into projectArrayList at same position and notify
                                // adapter change.
                                projectArrayList.add(position, projectObject);
                                mAdapter.notifyItemInserted(position);
                                ((MainActivity) getActivity()).updateDrawer();

                                setEmptyViewVisibility();

                            }
                        });
                snackbar.show();
            }
        }) {
            /**
             * Necessary for abstract method but never used
             * @param disallowIntercept whether or not intercept is allowed
             */
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        //FAB allows user to add a new project through AlertDialog
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Builder builder = new Builder(getContext());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View view1 = inflater.inflate(layout.dialog_add_project, null);
                builder.setView(view1);

                builder.setTitle( Html.fromHtml("<font color='#DAA520'>Add A New Project</font>"));

                builder.setPositiveButton("Add Project", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText projectName = (EditText) view1.findViewById(id.input_project_name);
                        mProjectName = projectName.getText().toString();

                        EditText projectDescription = (EditText) view1.findViewById(id.input_project_description);
                        mProjectDescription = projectDescription.getText().toString();

                        if (mProjectName.isEmpty()) {
                            return;
                        }

                        //add logic here to add project information to database
                        DBHandler dbHandler = new DBHandler(getContext());
                        dbHandler.addProjectToDatabase(mProjectName,mProjectDescription);

                        projectArrayList = dbHandler.getProjectsFromDatabase();
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new ProjectFragmentAdapter(projectArrayList);

                        mRecyclerView.setAdapter(mAdapter);

                        ((MainActivity) getActivity()).updateDrawer();

                        setEmptyViewVisibility();


                    }
                });

                builder.setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();



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
     * @return false
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    /**
     * Saves the project information to a ProjectObject and then deletes it from the database
     * @param position the position of the item in the list
     */
    public void deleteProjectFromList(int position) {
        DBHandler dbHandler = new DBHandler(getContext());
        NameListSingleton.get(getContext()).setProjectName(projectArrayList.get
                (position).getProjectName());
        NameListSingleton.get(getContext()).setmProjectDescription(projectArrayList.get(position).getProjectDescription());
        int projectID = dbHandler.getProjectId(NameListSingleton.get(getContext()).getProjectName());
        String projectName = NameListSingleton.get(getContext()).getProjectName();
        String projectDescription = NameListSingleton.get(getContext()).getmProjectDescription();

        saveNameToObject(projectID, projectName, projectDescription);

        dbHandler.deleteProjectFromDatabase(projectID);

    }

    /**
     * Instantiates a projectDatabaseObject and then stores the project information to it in case
     * the user wants to undo their delete
     * @param projectID
     * @param projectName
     * @param projectDescription
     */
    public void saveNameToObject(int projectID, String projectName, String projectDescription) {
        projectDatabaseObject = new ProjectDatabaseObject();
        projectDatabaseObject.setmProjectID(projectID);
        projectDatabaseObject.setmProjectName(projectName);
        projectDatabaseObject.setmProjectDescription(projectDescription);

    }

    /**
     * Lets the recyclerView adapter know the item has been removed and at what position
     * @param position the position of the item to be removed
     */
    public void removeAtPosition(int position) {
        projectArrayList.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, projectArrayList.size());
    }

    /**
     * Checks to see if the projectArrayList is empty. If so, displays a message letting people know
     * to add a project.
     */
    public void setEmptyViewVisibility() {
        if (projectArrayList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mAddAProject.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mAddAProject.setVisibility(View.GONE);
        }
    }



}
