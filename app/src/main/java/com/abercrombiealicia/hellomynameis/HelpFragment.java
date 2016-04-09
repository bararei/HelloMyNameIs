package com.abercrombiealicia.hellomynameis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ali on 4/9/2016.
 *
 */
public class HelpFragment extends Fragment {
    TextView mWelcome;
    TextView mHelpText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_help, container, false);

        mWelcome = (TextView) view.findViewById(R.id.help_intro);
        mHelpText = (TextView) view.findViewById(R.id.help_text);

        return view;
    }
}
