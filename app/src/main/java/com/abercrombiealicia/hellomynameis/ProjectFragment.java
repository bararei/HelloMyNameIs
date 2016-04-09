package com.abercrombiealicia.hellomynameis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Spheven on 3/19/2016.
 */
public class ProjectFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        AdapterView.OnItemLongClickListener{

    TextView mIntro;
    TextView mMyProject;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProjectDatabaseObject projectDatabaseObject;
    private ArrayList<ProjectObject> projectArrayList = new ArrayList<>();

    String mProjectName;
    String mProjectDescription;


    OnSubmitListener mCallback;


    //Container activity must implement this interface so that the fragment can deliver information
    public interface OnSubmitListener {
        void onSubmitClickProjectList();
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

        final View view = inflater.inflate(R.layout.fragment_project, container, false);

        ((MainActivity) getActivity()).showFab();


        DBHandler dbHandler = new DBHandler(getContext());
        projectArrayList = dbHandler.getProjectsFromDatabase();

        //RecyclerView stuff... needs to be more robust, just a placeholder
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_project);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ProjectFragmentAdapter(projectArrayList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        registerForContextMenu(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
                Log.i("LOG", " Clicked on Item " + position);
                NameListSingleton.get(getContext()).setProjectName(projectArrayList.get
                        (position).getProjectName());
                NameListSingleton.get(getContext()).setmProjectDescription(projectArrayList.get(position).getProjectDescription());
                Log.i("LOG", "Singleton info is " + NameListSingleton.get(getContext()).getProjectName());

                mCallback.onSubmitClickProjectList();
            }

            @Override
            public void onItemLongPress(View childView, final int position) {
                final ProjectObject projectObject;
                projectObject = projectArrayList.get(position);

                deleteProjectFromList(position);
                removeAtPosition(position);

                Snackbar snackbar = Snackbar
                        .make(childView, "Project has been deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DBHandler dbHandler = new DBHandler(getContext());
                                dbHandler.reAddProjectToDatabase(projectDatabaseObject.getmProjectID(),
                                        projectDatabaseObject.getmProjectName(), projectDatabaseObject.getmProjectDescription());
                                projectArrayList.add(position, projectObject);
                                mAdapter.notifyItemInserted(position);

                            }
                        });
                snackbar.show();
            }
        }) {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View view1 = inflater.inflate(R.layout.dialog_add_project, null);
                builder.setView(view1);

                builder.setTitle("Add a New Project");

                builder.setPositiveButton("Add Project", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText projectName = (EditText) view1.findViewById(R.id.input_project_name);
                        mProjectName = projectName.getText().toString();

                        EditText projectDescription = (EditText) view1.findViewById(R.id.input_project_description);
                        mProjectDescription = projectDescription.getText().toString();

                        if (mProjectName.isEmpty()) {
                            return;
                        }

                        //add logic here to add project information to database
                        DBHandler dbHandler = new DBHandler(getContext());
                        dbHandler.addProjectToDatabase(mProjectName,mProjectDescription);
                        Log.d("LOG", mProjectName + " added to database");

                        projectArrayList = dbHandler.getProjectsFromDatabase();
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new ProjectFragmentAdapter(projectArrayList);

                        mRecyclerView.setAdapter(mAdapter);

                        ((MainActivity) getActivity()).updateDrawer();


                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

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

    public void saveNameToObject(int projectID, String projectName, String projectDescription) {
        projectDatabaseObject = new ProjectDatabaseObject();
        projectDatabaseObject.setmProjectID(projectID);
        projectDatabaseObject.setmProjectName(projectName);
        projectDatabaseObject.setmProjectDescription(projectDescription);

    }

    public void removeAtPosition(int position) {
        projectArrayList.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, projectArrayList.size());
    }



}
