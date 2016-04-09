package com.abercrombiealicia.hellomynameis;

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

/**
 * Created by Ali on 4/9/2016.
 */
public class IntroFragment extends Fragment implements AppStatics{
    TextView mWelcome;
    TextView mIntro;
    EditText mAuthorName;
    Button mBtnGoToProject;
    SharedPreferences sharedPreferences;

    OnSubmitListener mCallback;


    //Container activity must implement this interface so that the fragment can deliver information
    public interface OnSubmitListener {
        void onSubmitClickIntroHelp();
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

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_intro, container, false);

        ((MainActivity) getActivity()).hideFab();

        mWelcome = (TextView) view.findViewById(R.id.welcome);
        mIntro = (TextView) view.findViewById(R.id.project_help_intro);
        mAuthorName = (EditText) view.findViewById(R.id.enter_author_name);
        mBtnGoToProject = (Button) view.findViewById(R.id.btn_go_to_project);

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
