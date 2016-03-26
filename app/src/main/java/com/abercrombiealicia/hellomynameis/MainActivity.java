package com.abercrombiealicia.hellomynameis;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements FirstNameFragment.OnSubmitListener, MiddleNameFragment.OnSubmitListener,
                                        NavigationView.OnNavigationItemSelectedListener, ProjectFragment.OnSubmitListener, NameListFragment.OnSubmitListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public  FloatingActionButton fab;
    private NavigationView navigationView;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (findViewById(R.id.fragmentContainer) != null) {

            /*FirstNameFragment firstNameFragment = new FirstNameFragment();
            firstNameFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, firstNameFragment)
                    .addToBackStack("FirstName")
                    .commit();*/

            ProjectFragment projectFragment = new ProjectFragment();
            projectFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, projectFragment)
                    .addToBackStack("ProjectFragment")
                    .commit();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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


}
