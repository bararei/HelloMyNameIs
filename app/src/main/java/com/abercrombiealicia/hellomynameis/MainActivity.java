package com.abercrombiealicia.hellomynameis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements FirstNameFragment.OnSubmitListener {

    DBHandler test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.fragmentContainer) != null) {

            FirstNameFragment firstNameFragment = new FirstNameFragment();
            firstNameFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, firstNameFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onSubmitClick() {

    }
}
