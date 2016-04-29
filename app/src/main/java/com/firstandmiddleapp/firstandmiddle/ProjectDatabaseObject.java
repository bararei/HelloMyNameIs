package com.firstandmiddleapp.firstandmiddle;

/**
 * @author Ali Abercrombie
 * Created on 3/29/2016.
 * @version 1.0.0
 *
 * Project Object that stores all information for a project. Used to store project information when
 * someone deletes a project from the database so it can be restored.
 */
public class ProjectDatabaseObject {

    private int mProjectID;
    private String mProjectName;
    private String mProjectDescription;

    /**
     * getter for mProjectDescription
     * @return mProjectDescription
     */
    public String getmProjectDescription() {
        return mProjectDescription;
    }

    /**
     * setter for mProjectDescription
     * @param mProjectDescription mProjectDescription
     */
    public void setmProjectDescription(String mProjectDescription) {
        this.mProjectDescription = mProjectDescription;
    }

    /**
     * getter for mProjectID
     * @return mProjectID
     */
    public int getmProjectID() {
        return mProjectID;
    }

    /**
     * setter for mProjectID
     * @param mProjectID mProjectID
     */
    public void setmProjectID(int mProjectID) {
        this.mProjectID = mProjectID;
    }

    /**
     * getter for mProjectName
     * @return mProjectName
     */
    public String getmProjectName() {
        return mProjectName;
    }

    /**
     * setter for mProjectName
     * @param mProjectName mProjectName
     */
    public void setmProjectName(String mProjectName) {
        this.mProjectName = mProjectName;
    }


}
