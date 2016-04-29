package com.firstandmiddleapp.firstandmiddle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firstandmiddleapp.hellomynameis.R;

/**
 * @author Ali Abercrombie
 * @created 4/9/2016
 * @version 1.0.0
 *
 * Introductory fragment that is only displayed the first time the app is loaded. Displays information
 * about the app and gets the user's preferred name and stores it to shared preferences.
 */
public class IntroFragment extends Fragment implements AppStatics{
    TextView mWelcome;
    TextView mIntro;
    EditText mAuthorName;
    Button mBtnGoToProject;
    SharedPreferences sharedPreferences;

    OnSubmitListener mCallback;


    /**
     * MainActivity implements this interface so the fragment can deliver information.
     */
    //Container activity must implement this interface so that the fragment can deliver information
    public interface OnSubmitListener {
        void onSubmitClickIntroHelp();
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
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * Includes two textviews with project information, an EditText for the user's name, and a Button
     * which when clicked sends the user to the next screen.
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

        //inflate the view
        View view = inflater.inflate(R.layout.fragment_intro, container, false);

        //hide the Floating Action Button
        ((MainActivity) getActivity()).hideFab();

        //Wire up the widgets
        mWelcome = (TextView) view.findViewById(R.id.welcome);
        mIntro = (TextView) view.findViewById(R.id.project_help_intro);
        mAuthorName = (EditText) view.findViewById(R.id.enter_author_name);
        mBtnGoToProject = (Button) view.findViewById(R.id.btn_go_to_project);

        //When button is clicked, set name from mAuthorName as name in shared preferences and then
        //execute mCallback
        mBtnGoToProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authorName = mAuthorName.getText().toString();
                sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(AUTHOR_NAME_KEY, authorName);
                editor.commit();

                mCallback.onSubmitClickIntroHelp();


            }
        });

        return view;

    }
}
