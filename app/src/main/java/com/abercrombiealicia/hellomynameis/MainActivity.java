package com.abercrombiealicia.hellomynameis;

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
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


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



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this);

        projectNamesArrayList = dbHandler.getProjectsForDrawerList();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    drawerLayout.closeDrawers();

                    //Log.i("LOG", " Clicked on Item " + child);
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
                        Log.i("LOG", "Singleton info is " + NameListSingleton.get(MainActivity.this)
                                .getProjectName());

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

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        fab = (FloatingActionButton) findViewById(R.id.fab);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);

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

    @Override
    public void onSubmitClickProjectList() {


        NameListFragment nameListFragment = new NameListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragmentContainer, nameListFragment);
                                        transaction.addToBackStack("NameList");
                                        transaction.commit();

    }

    @Override
    public void onSubmitClickNameList() {

        FirstNameFragment firstNameFragment = new FirstNameFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragmentContainer, firstNameFragment);
                                        transaction.addToBackStack("FirstName");
                                        transaction.commit();

    }

    @Override
    public void onSubmitClickFirstName() {

        MiddleNameFragment middleNameFragment = new MiddleNameFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, middleNameFragment);
        transaction.addToBackStack("MiddleName");
        transaction.commit();


    }

    @Override
    public void onSubmitClickMiddleName() {

        NameListFragment nameListFragment = new NameListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, nameListFragment);
        transaction.addToBackStack("NameList");
        transaction.commit();
    }

    @Override
    public void onSubmitClickIntroHelp() {

        ProjectFragment projectFragment = new ProjectFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, projectFragment);
        transaction.addToBackStack("ProjectFragment");
        transaction.commit();

        projectNamesArrayList = dbHandler.getProjectsForDrawerList();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DrawerAdapter(projectNamesArrayList);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

    }


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void hideFab() {
        fab.hide();
    }

    public void showFab() {
        fab.show();
    }

    public void updateDrawer() {
        projectNamesArrayList = dbHandler.getProjectsForDrawerList();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DrawerAdapter(projectNamesArrayList);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fab = (FloatingActionButton) findViewById(R.id.fab);


    }

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View view1 = inflater.inflate(R.layout.dialog_change_name, null);
        builder.setView(view1);

        builder.setTitle("What's Your New Name?");

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

                projectNamesArrayList = dbHandler.getProjectsForDrawerList();
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new DrawerAdapter(projectNamesArrayList);

                mRecyclerView.setAdapter(mAdapter);

                mAdapter.notifyDataSetChanged();


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
