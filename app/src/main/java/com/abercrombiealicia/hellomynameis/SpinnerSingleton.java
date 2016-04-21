package com.abercrombiealicia.hellomynameis;

import android.content.Context;

/**
 * @author Ali Abercrombie
 * Created on 4/21/2016.
 * @version 1.0.0
 *
 * This singleton holds the spinner positions for the first name spinners so they can be accessed by
 * middle name spinners and immediately set as defaults in the MiddleNameFragment.
 */
public class SpinnerSingleton {

    Context mAppContext;
    private static SpinnerSingleton mInstance;

    private int mRegionPosition;
    private int mTimePosition;
    private int mGenderPosition;

    /**
     * getter for mGenderPosition
     * @return mGenderPosition
     */
    public int getmGenderPosition() {
        return mGenderPosition;
    }

    /**
     * setter for mGender position
     * @param mGenderPosition mGenderPosition
     */
    public void setmGenderPosition(int mGenderPosition) {
        this.mGenderPosition = mGenderPosition;
    }

    /**
     * getter for mRegionPosition
     * @return mRegionPosition
     */
    public int getmRegionPosition() {
        return mRegionPosition;
    }

    /**
     * setter for mRegionPosition
     * @param mRegionPosition mRegionPosition
     */
    public void setmRegionPosition(int mRegionPosition) {
        this.mRegionPosition = mRegionPosition;
    }

    /**
     * getter for mTimePosition
     * @return mTimePosition
     */
    public int getmTimePosition() {
        return mTimePosition;
    }

    /**
     * setter for mTimePosition
     * @param mTimePosition mTimePosition
     */
    public void setmTimePosition(int mTimePosition) {
        this.mTimePosition = mTimePosition;
    }

    /**
     * Constructor for the singleton
     * @param appContext the context
     */
    private SpinnerSingleton(Context appContext){
        mAppContext = appContext;


    }

    /**
     * Checks to see if an instance of the singleton has been set already. If not, sets one, otherwise,
     * returns the set instance.
     * @param c the context
     * @return the instance of the singleton
     */
    public static SpinnerSingleton get(Context c){
        if(mInstance == null)
        {
            mInstance = new SpinnerSingleton(c.getApplicationContext());
        }
        return mInstance;
    }
}
