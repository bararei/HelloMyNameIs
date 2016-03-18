package com.abercrombiealicia.hellomynameis;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements FirstNameFragment.OnSubmitListener, MiddleNameFragment.OnSubmitListener {

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
    public void onSubmitClickFirstName() {

        MiddleNameFragment middleNameFragment = new MiddleNameFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, middleNameFragment);
        transaction.addToBackStack("MiddleName");
        transaction.commit();


    }

    @Override
    public void onSubmitClickMiddleName() {

    }
}
