package com.abercrombiealicia.hellomynameis;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.Toast;

import java.util.ArrayList;

import icepick.Icepick;
import icepick.Icicle;


/**
 * @author Ali Abercrombie
 * @created 3/4/2016
 * @version 1.0.0
 *
 * This fragment displays the first name selection options available from the database based on region,
 * time period and gender selections by the user.
 */
public class FirstNameFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TextView mIntro;
    private Spinner mSpinnerRegion;
    private Spinner mSpinnerTime;
    private Spinner mSpinnerGender;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView test;
    private Button mBtnGetNames;

    private ArrayList<String> regionArrayList = new ArrayList<>();
    private ArrayList<String> timeArraylist = new ArrayList<>();
    private ArrayList<String> genderArraylist = new ArrayList<>();
    @Icicle ArrayList<String> namesArrayList = new ArrayList<>();

    private String mRegion = "";
    private String mTimePeriod = "";
    private String mGender = "";

    OnSubmitListener mCallback;

    /**
     * MainActivity implements this interface so the fragment can deliver information.
     */
    public interface OnSubmitListener {
        void onSubmitClickFirstName();
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
     * Perform initialization of all fragments and loaders. Calls Icepick library to deal with all
     * bundling and savedInstanceState handling.
     *
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * Includes 3 spinner adapters, button, and recycler view for names. Information from database is
     * used to populate spinners, user makes selection from spinners and selection is then sent back to
     * the database to populate the recycler view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state.
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first_name, container, false);

        //hides the floating action button
        ((MainActivity) getActivity()).hideFab();

        //set the basic widgets
        mIntro = (TextView) view.findViewById(R.id.first_name_intro);
        mSpinnerRegion = (Spinner) view.findViewById(R.id.spinner_region_1);
        mSpinnerTime = (Spinner) view.findViewById(R.id.spinner_time_1);
        mSpinnerGender = (Spinner) view.findViewById(R.id.spinner_gender_1);
        test = (TextView) view.findViewById(R.id.first_name_test);
        mBtnGetNames = (Button) view.findViewById(R.id.btn_get_first_names);


        final DBHandler dbHandler = new DBHandler(getContext());

        //set arrayLists from database/method
        regionArrayList = dbHandler.getRegion();
        timeArraylist = dbHandler.getTimePeriod();
        addGenderToArray();


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


        //gets the names from the database and populates the namesArrayList if all spinners are set
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

        //sets the recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_first_name);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NameFragmentAdapter(namesArrayList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        //set onclick adapter. Sets first name in singleton and then invokes callback interface.
        ((NameFragmentAdapter) mAdapter).setOnItemClickListener(new
                NameFragmentAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        NameListSingleton.get(getContext()).setFirstName(namesArrayList.get
                                (position));

                        mCallback.onSubmitClickFirstName();
                    }
                });

        return view;
    }

    /**
     * Callback method to be invoked when an item in this view has been
     * selected. Set to switch based on which spinner has been selected.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch(parent.getId()) {
            case R.id.spinner_region_1:
                mRegion = regionArrayList.get(position);
                SpinnerSingleton.get(getContext()).setmRegionPosition(position);
                break;
            case R.id.spinner_time_1:
                mTimePeriod = timeArraylist.get(position);
                SpinnerSingleton.get(getContext()).setmTimePosition(position);
                break;
            case R.id.spinner_gender_1:
                mGender = genderArraylist.get(position);
                SpinnerSingleton.get(getContext()).setmGenderPosition(position);
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

    /**
     * Since there are only two options for gender, the gender array is created here instead of from
     * a database query.
     */
    public void addGenderToArray() {
        genderArraylist.add("Male");
        genderArraylist.add("Female");
    }

    /**
     * Called when the view is changed or the fragment is destroyed. Uses Icepick to save the instance state.
     * @param outState saved information
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
