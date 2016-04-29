package com.firstandmiddleapp.firstandmiddle;

import android.content.Context;

/**
 * @author Ali Abercrombie
 * Created on 3/9/2016
 * @version 1.0.0
 *
 * This singleton stores the project name and description and first and middle names so they can be shared
 * across the Project, NameList, FirstName and MiddleName fragments.
 */
public class NameListSingleton {

    Context mAppContext;
    private static NameListSingleton mInstance;

    private String mProjectName;
    private String mProjectDescription;
    private String mFirstName;
    private String mMiddleName;

    /**
     * getter for mProjectName
     * @return mProjectName
     */
    public String getProjectName() {
        return mProjectName;
    }

    /**
     * setter for mProjectName
     * @param projectName the projectName
     */
    public void setProjectName(String projectName) {
        mProjectName = projectName;
    }

    /**
     * getter for mProjectDescription
     * @return mProjectDescription
     */
    public String getmProjectDescription() {
        return mProjectDescription;
    }

    /**
     * setter for mProjectDescription
     * @param mProjectDescription the projectDescription
     */
    public void setmProjectDescription(String mProjectDescription) {
        this.mProjectDescription = mProjectDescription;
    }

    /**
     * getter for mFirstName
     * @return mFirstName
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * setter for mFirstName
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    /**
     * getter for mMiddleName
     * @return mMiddleName
     */
    public String getMiddleName() {
        return mMiddleName;
    }

    /**
     * setter for mMiddleName
     * @param middleName the middle name
     */
    public void setMiddleName(String middleName) {
        mMiddleName = middleName;
    }

    /**
     * Constructor for the singleton
     * @param appContext the context
     */
    private NameListSingleton(Context appContext){
        mAppContext = appContext;


    }

    /**
     * Checks to see if an instance of the singleton has been set already. If not, sets one, otherwise,
     * returns the set instance.
     * @param c the context
     * @return the instance of the singleton
     */
    public static NameListSingleton get(Context c){
        if(mInstance == null)
        {
            mInstance = new NameListSingleton(c.getApplicationContext());
        }
        return mInstance;
    }
}
