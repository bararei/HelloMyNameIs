package com.abercrombiealicia.hellomynameis;

import android.content.Context;

import java.util.ArrayList;
/**
 * Created by student on 3/9/2016
 */
public class NameListSingleton {

    Context mAppContext;
    private static NameListSingleton mInstance;

    private String mProjectName;
    private String mProjectDescription;
    private String mFirstName;
    private String mMiddleName;

    public String getProjectName() {
        return mProjectName;
    }

    public void setProjectName(String projectName) {
        mProjectName = projectName;
    }

    public String getmProjectDescription() {
        return mProjectDescription;
    }

    public void setmProjectDescription(String mProjectDescription) {
        this.mProjectDescription = mProjectDescription;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getMiddleName() {
        return mMiddleName;
    }

    public void setMiddleName(String middleName) {
        mMiddleName = middleName;
    }

    private NameListSingleton(Context appContext){
        mAppContext = appContext;


    }




    public static NameListSingleton get(Context c){
        if(mInstance == null)
        {
            mInstance = new NameListSingleton(c.getApplicationContext());
        }
        return mInstance;
    }
}
