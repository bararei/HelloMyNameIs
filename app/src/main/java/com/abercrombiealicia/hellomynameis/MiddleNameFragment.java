package com.abercrombiealicia.hellomynameis;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Spheven on 3/17/2016.
 */
public class MiddleNameFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TextView mIntro;
    private Spinner mSpinnerRegion;
    private Spinner mSpinnerTime;
    private Spinner mSpinnerGender;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mFirstName;
    private Button mBtnGetNames;

    private ArrayList<String> regionArrayList = new ArrayList<>();
    private ArrayList<String> timeArraylist = new ArrayList<>();
    private ArrayList<String> genderArraylist = new ArrayList<>();
    private ArrayList<String> namesArrayList = new ArrayList<>();

    private String mRegion = "";
    private String mTimePeriod = "";
    private String mGender = "";

    OnSubmitListener mCallback;

    //Container activity must implement this interface so that the fragment can deliver information
    public interface OnSubmitListener {
        void onSubmitClickMiddleName();
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
     * Perform initialization of all fragments and loaders.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).hideFab();
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link (Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link} when the view is being released.
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
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_middle_name, container, false);



        //set the basic widgets
        mIntro = (TextView) view.findViewById(R.id.middle_name_intro);
        mSpinnerRegion = (Spinner) view.findViewById(R.id.spinner_region_2);
        mSpinnerTime = (Spinner) view.findViewById(R.id.spinner_time_2);
        mSpinnerGender = (Spinner) view.findViewById(R.id.spinner_gender_2);
        mFirstName = (TextView) view.findViewById(R.id.middle_name_first);
        mBtnGetNames = (Button) view.findViewById(R.id.btn_get_middle_names);




        final DBHandler dbHandler = new DBHandler(getContext());

        //set regionArrayList equal to localArrayList
        regionArrayList = dbHandler.getRegion();
        timeArraylist = dbHandler.getTimePeriod();
        addGenderToArray();

        //set mFirstName to first name from singleton
        String firstNameText = "First Name Choice: " + NameListSingleton.get(getContext()).getFirstName();
        mFirstName.setText(firstNameText);

        //wire up region adapter
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, regionArrayList);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerRegion.setAdapter(regionAdapter);
        mSpinnerRegion.setOnItemSelectedListener(this);

        //wire up time period adapter
        ArrayAdapter<String> timePeriodAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, timeArraylist);
        timePeriodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerTime.setAdapter(timePeriodAdapter);
        mSpinnerTime.setOnItemSelectedListener(this);

        //wire up gender adapter
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genderArraylist);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerGender.setAdapter(genderAdapter);
        mSpinnerGender.setOnItemSelectedListener(this);


        mBtnGetNames.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mRegion != "" && mTimePeriod != "" && mGender != "") {
                    namesArrayList = dbHandler.getNamesFromDatabase(mRegion, mTimePeriod, mGender);
                    Log.d("TAG", namesArrayList.toString());

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new NameFragmentAdapter(namesArrayList);

                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });

        //recycler view stuff
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_middle_name);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NameFragmentAdapter(namesArrayList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        //set onclick adapter
        ((NameFragmentAdapter) mAdapter).setOnItemClickListener(new
                NameFragmentAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Log.i("LOG", " Clicked on Item " + position);
                        NameListSingleton.get(getContext()).setMiddleName(namesArrayList.get
                                (position));
                        Log.i("LOG", "Singleton info is " + NameListSingleton.get(getContext()).getMiddleName());

                        String projectName = NameListSingleton.get(getContext()).getProjectName();
                        String firstName = NameListSingleton.get(getContext()).getFirstName();
                        String middleName = NameListSingleton.get(getContext()).getMiddleName();
                        Log.i("LOG", "Got names from singleton");
                        int projectID = dbHandler.getProjectId(projectName);
                        int firstNameID = dbHandler.getNameId(firstName);
                        int middleNameID = dbHandler.getNameId(middleName);
                        Log.d("LOG", "Got ids from database");

                        dbHandler.addNameListToDatabase(projectID, firstNameID, middleNameID);
                        Log.d("LOG", "Added names to database");

                        mCallback.onSubmitClickMiddleName();
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

        switch(parent.getId()) {
            case R.id.spinner_region_2:
                mRegion = regionArrayList.get(position);
                //test.setText(mRegion);
                break;
            case R.id.spinner_time_2:
                mTimePeriod = timeArraylist.get(position);
                //test.setText(mTimePeriod);
                break;
            case R.id.spinner_gender_2:
                mGender = genderArraylist.get(position);
                // test.setText(mGender);
                break;
        }


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

    public void addGenderToArray() {
        genderArraylist.add("M");
        genderArraylist.add("F");
    }

}
