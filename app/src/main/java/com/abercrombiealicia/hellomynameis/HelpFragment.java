package com.abercrombiealicia.hellomynameis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Ali Abercrombie
 * @version 1.0.0
 *
 * Help Fragment displays the help text about the app for the user.
 *
 */
public class HelpFragment extends Fragment {
    TextView mWelcome;
    TextView mHelpText;

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
     * Inflates the two textviews used in the layout.
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

        View view = inflater.inflate(R.layout.fragment_help, container, false);

        //hide the Floating Action Button
        ((MainActivity) getActivity()).hideFab();

        mWelcome = (TextView) view.findViewById(R.id.help_intro);
        mHelpText = (TextView) view.findViewById(R.id.help_text);

        return view;
    }
}
