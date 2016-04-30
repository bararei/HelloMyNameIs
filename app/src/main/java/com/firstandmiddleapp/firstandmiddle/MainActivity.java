package com.firstandmiddleapp.firstandmiddle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Sets up the Navigation Drawer and the Floating Action Button,
 * switches out each fragment as needed with interfaces from the fragments, including setting the
 * IntroFragment if it's the app's first run based on shared preferences, and handles the menu options
 * for the ActionBar.
 *
 * @author Ali Abercrombie
 * @version 1.0.0
 *
 */
public class MainActivity extends AppCompatActivity implements FirstNameFragment.OnSubmitListener, MiddleNameFragment.OnSubmitListener,
                                        NavigationView.OnNavigationItemSelectedListener, ProjectFragment.OnSubmitListener, NameListFragment.OnSubmitListener,
                                        IntroFragment.OnSubmitListener, AppStatics{

    DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public FloatingActionButton fab;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ProjectObject> projectNamesArrayList = new ArrayList<>();
    DBHandler dbHandler;
    SharedPreferences sharedPreferences;


    /**
     * Sets the view, creates the navigation drawer adapter and touch sensors, toolbar and menu items, and initializes the floating
     * action button. Checks to see if the fragment is null. If not, checks shared preferences for first run.
     * If first run, initializes IntroFragment, otherwise, initializes ProjectFragment.
     * @param savedInstanceState if the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set view
        setContentView(R.layout.activity_main);

        //initialize database
        dbHandler = new DBHandler(this);

        //set arraylist equal to method from database
        projectNamesArrayList = dbHandler.getProjectsForDrawerList();

        //set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //wire up recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerViewDrawer); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mAdapter = new DrawerAdapter(projectNamesArrayList);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //begin setting up gesture detector for recyclerview for navigation drawer
        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            /**
             * intercept the touch event and set what happens based on which item is touched in the recyclerview. If header item (item 0) is touched,
             * display a toast. If home item is touched (item 1), then go to ProjectFragment. Else start looping through the projectNamesArrayList
             * and setting the singleton values. This makes the arraylist and singleton values be the value -2.
             * @param recyclerView the recyclerview
             * @param motionEvent the motion event
             * @return true if got motionevent, false otherwise
             */
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    drawerLayout.closeDrawers();

                    if (recyclerView.getChildPosition(child) == 0 ) {

                        Toast.makeText(MainActivity.this, R.string.toast_change_name, Toast.LENGTH_LONG).show();

                    } else if (recyclerView.getChildPosition(child) == 1) {

                        if (findViewById(R.id.fragmentContainer)!= null) {
                            ProjectFragment projectFragment = new ProjectFragment();

                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainer, projectFragment);
                            transaction.addToBackStack("ProjectList");
                            transaction.commit();
                        }
                    } else {
                        NameListSingleton.get(MainActivity.this).setProjectName(projectNamesArrayList
                                .get(recyclerView.getChildPosition(child) - 2).getProjectName());
                        NameListSingleton.get(MainActivity.this).setmProjectDescription
                                (projectNamesArrayList.get(recyclerView.getChildPosition(child) - 2)
                                        .getProjectDescription());

                        if (findViewById(R.id.fragmentContainer)!= null) {
                            NameListFragment nameListFragment = new NameListFragment();

                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainer, nameListFragment);
                            transaction.addToBackStack("NameList");
                            transaction.commit();
                        }
                    }

                    return true;

                }

                return false;
            }

            /**
             * Necessary to implement addOnItemTouchListener but never used.
             * @param recyclerView the recyclerView
             * @param motionEvent the motionEvent
             */
            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            /**
             * Necessary to implement addOnItemTouchListener but never used.
             * @param disallowIntercept whether or not Intercept is allowed
             */
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        //Set toggle on drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        //wire up fab
        fab = (FloatingActionButton) findViewById(R.id.fab);

        //get shared preferences
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);

        //Check if this is the app's first run. If it is, run IntroFragment. Else, run ProjectFragment
        if (sharedPreferences.getBoolean(FIRST_RUN, true)) {
            IntroFragment introFragment = new IntroFragment();
            introFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, introFragment)
                    .commit();

            sharedPreferences.edit().putBoolean(FIRST_RUN, false).commit();

        } else {

            if (findViewById(R.id.fragmentContainer) != null) {

                if (savedInstanceState != null) {
                    return;
                }

                ProjectFragment projectFragment = new ProjectFragment();
                projectFragment.setArguments(getIntent().getExtras());

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainer, projectFragment)
                        .addToBackStack("ProjectFragment")
                        .commit();
            }

        }
    }

    /**
     * Interface method from ProjectFragment. Switches to the NameListFragment.
     */
    @Override
    public void onSubmitClickProjectList() {


        NameListFragment nameListFragment = new NameListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragmentContainer, nameListFragment);
                                        transaction.addToBackStack("NameList");
                                        transaction.commit();

    }

    /**
     * Interface method from NameListFragment. Switches to the FirstNameFragment.
     */
    @Override
    public void onSubmitClickNameList() {

        FirstNameFragment firstNameFragment = new FirstNameFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragmentContainer, firstNameFragment);
                                        transaction.addToBackStack("FirstName");
                                        transaction.commit();

    }

    /**
     * Interface method from FirstNameFragment. Switches to the MiddleNameFragment.
     */
    @Override
    public void onSubmitClickFirstName() {

        MiddleNameFragment middleNameFragment = new MiddleNameFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, middleNameFragment);
        transaction.addToBackStack("MiddleName");
        transaction.commit();


    }

    /**
     * Interface method from MiddleNameFragment. Switches to the NameListFragment.
     */
    @Override
    public void onSubmitClickMiddleName() {

        NameListFragment nameListFragment = new NameListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, nameListFragment);
        transaction.addToBackStack("NameList");
        transaction.commit();
    }

    /**
     * Interface method from IntroFragment. Switches to the ProjectFragment and resets the navigationDrawer
     * adapter so the new shared preferences information becomes available upon ProjectFragment load.
     */
    @Override
    public void onSubmitClickIntroHelp() {

        ProjectFragment projectFragment = new ProjectFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, projectFragment);
        transaction.addToBackStack("ProjectFragment");
        transaction.commit();

        updateDrawer();

    }


    /**
     * Shuts the navigationDrawer if the drawer is open. Otherwise, does normal onBackPressed stuff.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    /**
     * Inflates the Toolbar menu
     * @param menu the menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /**
     * This hook is called whenever an item in the options menu is selected. Action_settings allows
     * a user to change their name in shared preferences. Help opens the help fragment.
     *
     * @param item The menu item that was selected.
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()) {
            case R.id.action_settings:
                createDialog();
                return true;
            case R.id.help:
                HelpFragment helpFragment = new HelpFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, helpFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    /**
     * Closes the navigation drawer when an item is selected
     * @param item the selected item
     * @return true
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Called to hide the Floating Action Button inside a fragment
     */
    public void hideFab() {
        fab.hide();
    }

    /**
     * Called to show the Floating Action Button inside a fragment
     */
    public void showFab() {
        fab.show();
    }

    /**
     * Updates the navigationDrawer by reloading the arrayList and navigationDrawer adapter
     */
    public void updateDrawer() {
        projectNamesArrayList = dbHandler.getProjectsForDrawerList();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DrawerAdapter(projectNamesArrayList);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState the Bundle to be saved
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * This method is called after onStart when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Saves the Floating Action Button.
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    /**
     * Creates the AlertDialog used to set a new user name in the menu via Shared Preferences.
     */
    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View view1 = inflater.inflate(R.layout.dialog_change_name, null);
        builder.setView(view1);

        builder.setTitle( Html.fromHtml("<font color='#DAA520'>What's Your New Name?</font>"));

        builder.setPositiveButton("Change My Name!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);
                EditText newName = (EditText) view1.findViewById(R.id.input_change_name);
                String mNewName = newName.getText().toString();

                if (mNewName.isEmpty()) {
                    return;
                }

                //add new name to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(AUTHOR_NAME_KEY, mNewName);
                editor.commit();

                updateDrawer();


            }
        });

        builder.setNegativeButton("Never Mind", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


}
