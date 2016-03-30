package com.abercrombiealicia.hellomynameis;

/**
 * Created by Spheven on 3/29/2016.
 */
public class NameListDatabaseObject {

    private int projectID;
    private int nameID1;
    private int nameID2;

    public int getNameID1() {
        return nameID1;
    }

    public void setNameID1(int nameID1) {
        this.nameID1 = nameID1;
    }

    public int getNameID2() {
        return nameID2;
    }

    public void setNameID2(int nameID2) {
        this.nameID2 = nameID2;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
}
