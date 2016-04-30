package com.firstandmiddleapp.firstandmiddle;

/**
 * Holds an individual namelist item from the database. Used to create an arraylist.
 *
 * @author Ali Abercrombie
 * Created on 3/29/2016.
 * @version 1.0.0
 */
public class NameListDatabaseObject {

    private int projectID;
    private int nameID1;
    private int nameID2;

    /**
     * getter for nameID1
     * @return nameID1
     */
    public int getNameID1() {
        return nameID1;
    }

    /**
     * setter for nameID1
     * @param nameID1 nameID1
     */
    public void setNameID1(int nameID1) {
        this.nameID1 = nameID1;
    }

    /**
     * getter for nameID2
     * @return nameID2
     */
    public int getNameID2() {
        return nameID2;
    }

    /**
     * setter for nameID2
     * @param nameID2 nameID2
     */
    public void setNameID2(int nameID2) {
        this.nameID2 = nameID2;
    }

    /**
     * getter for projectID
     * @return projectID
     */
    public int getProjectID() {
        return projectID;
    }

    /**
     * setter for projectID
     * @param projectID projectID
     */
    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
}
