package com.abercrombiealicia.hellomynameis;

/**
 * @author Ali Abercrombie
 * Created on 3/23/2016.
 * @version 1.0.0
 *
 * Holds the project name and project description. Used to create navigation drawer and
 * ProjectFragment recyclerView arrayLists
 */
public class ProjectObject {

    private String projectName;
    private String projectDescription;

    /**
     * getter for projectName
     * @return projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * setter for projectName
     * @param projectName projectName
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    /**
     * getter for projectDescription
     * @return projectDescription
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * setter for projectDescription
     * @param projectDescription projectDescription
     */
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }


}
