package com.abercrombiealicia.hellomynameis;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Spheven on 3/2/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "hellomynameisDB.db";
    private static final String TABLE_PROJECTS = "projects";
    private static final String TABLE_NAMELIST = "name_list";
    private static final String TABLE_NAMES = "names";

    private static final String COLUMN_PROJECT_ID = "id";
    private static final String COLUMN_PROJECT_NAME = "name";
    private static final String COLUMN_PROJECT_DESC = "description";

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NAME_REGION = "region";
    private static final String COLUMN_NAME_TIMEPERIOD = "time_period";
    private static final String COLUMN_NAME_GENDER = "gender";

    private static final String COLUMN_NAMELIST_PROJECT_ID = "project_id";
    private static final String COLUMN_NAMELIST_NAME_ID1 = "name_id1";
    private static final String COLUMN_NAMELIST_NAME_ID2 = "name_id2";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 1, DATABASE_VERSION);
        addNamesToDatabase(context, db);
    }
    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PROJECTS_TABLE = "CREATE TABLE "
                + TABLE_PROJECTS + "("
                + COLUMN_PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PROJECT_NAME + " TEXT, "
                + COLUMN_PROJECT_DESC + " TEXT)";

        db.execSQL(CREATE_PROJECTS_TABLE);

        String CREATE_NAMES_TABLE = "CREATE TABLE "
                + TABLE_NAMES + "("
                + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_NAME_REGION + " TEXT, "
                + COLUMN_NAME_TIMEPERIOD + " TEXT, "
                + COLUMN_NAME_GENDER + " TEXT)";

        db.execSQL(CREATE_NAMES_TABLE);

        String CREATE_NAMELIST_TABLE = "CREATE TABLE "
                + TABLE_NAMELIST + "("
                + COLUMN_NAMELIST_PROJECT_ID + " INTEGER NOT NULL, "
                + COLUMN_NAMELIST_NAME_ID1 + " INTEGER NOT NULL, "
                + COLUMN_NAMELIST_NAME_ID2 + " INTEGER NOT NULL, "
                + "FOREIGN KEY (" + COLUMN_NAMELIST_PROJECT_ID + ") REFERENCES " + TABLE_PROJECTS
                        + "(" + COLUMN_PROJECT_ID + "), "
                + "FOREIGN KEY (" + COLUMN_NAMELIST_NAME_ID1 + ") REFERENCES " + TABLE_NAMES
                        + "(" + COLUMN_NAME_ID + "), "
                + "FOREIGN KEY (" + COLUMN_NAMELIST_NAME_ID2 + ") REFERENCES " + TABLE_NAMES
                + "(" + COLUMN_NAME_ID + "))";

        db.execSQL(CREATE_NAMELIST_TABLE);
        Log.d("CSVParser", "Tables created");



    }

    public void addNamesToDatabase (Context context, SQLiteDatabase db) {

        String mCSVFile = "names_database.csv";
        AssetManager manager = context.getAssets();
        InputStream inStream = null;

        try {
            inStream = manager.open(mCSVFile);
            Log.d("CSVParser", "CSVFile opened");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
        String line;
        db.beginTransaction();

        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length != 5) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_NAME_ID, columns[0].trim());
                cv.put(COLUMN_NAME, columns[1].trim());
                cv.put(COLUMN_NAME_REGION, columns[2].trim());
                cv.put(COLUMN_NAME_TIMEPERIOD, columns[3].trim());
                cv.put(COLUMN_NAME_GENDER, columns[4].trim());
                Log.d("CSVParser", columns[1]);
                db.insert(TABLE_NAMES, null, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES);
        String CREATE_NAMES_TABLE = "CREATE TABLE "
                + TABLE_NAMES + "("
                + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_NAME_REGION + " TEXT, "
                + COLUMN_NAME_TIMEPERIOD + " TEXT, "
                + COLUMN_NAME_GENDER + " TEXT)";

        db.execSQL(CREATE_NAMES_TABLE);
    }

    public ArrayList<String> getRegion() {

        SQLiteDatabase db = this.getReadableDatabase();
        Set<String> regionSet = new TreeSet<>();
        Cursor cursor;

        cursor = db.query(true, TABLE_NAMES, null, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String region = cursor.getString(cursor.getColumnIndex("region"));
            regionSet.add(region);
        }

        ArrayList<String> regionArrayList = new ArrayList<>(regionSet);
        return regionArrayList;
    }

    public ArrayList<String> getTimePeriod() {

        SQLiteDatabase db = getReadableDatabase();
        Set<String> timeSet = new TreeSet<>();
        Cursor cursor;

        cursor = db.query(true, TABLE_NAMES, null, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String timePeriod = cursor.getString(cursor.getColumnIndex("time_period"));
            timeSet.add(timePeriod);
        }

        ArrayList<String> timeArrayList = new ArrayList<>(timeSet);
        return timeArrayList;
    }

    public ArrayList<String> getNamesFromDatabase(String region, String time, String gender) {

        ArrayList<String> allNames = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String[] columnsToReturn = new String[] {COLUMN_NAME};

        cursor = db.query(true, TABLE_NAMES, columnsToReturn, COLUMN_NAME_REGION + " = '" + region + "' AND " + COLUMN_NAME_TIMEPERIOD + " = '" + time
                            + "' AND " + COLUMN_NAME_GENDER + " = '" + gender + "'", null, null, null, null, null);


        while (cursor.moveToNext()) {
            allNames.add(cursor.getString(0));
        }

        return allNames;
    }

    public void addProjectToDatabase(String projectName, String projectDescription) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_PROJECT_NAME, projectName);
        values.put(COLUMN_PROJECT_DESC, projectDescription);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PROJECTS, null, values);
        db.close();
    }

    public ArrayList<ProjectObject> getProjectsFromDatabase() {
        ArrayList<ProjectObject> allProjects = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor;

        cursor = db.query(true, TABLE_PROJECTS, null, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            ProjectObject projectObject = new ProjectObject();
            String projectName = cursor.getString(cursor.getColumnIndex("name"));
            String projectDescription = cursor.getString(cursor.getColumnIndex("description"));
            projectObject.setProjectName(projectName);
            projectObject.setProjectDescription(projectDescription);
            allProjects.add(projectObject);
            db.close();
        }

        return allProjects;
    }

    public int getProjectId(String projectName) {
        int projectID = 0;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor;
        String[] columnsToReturn = new String[] {COLUMN_PROJECT_ID};
        String[] projectNames = new String[] {projectName};

        cursor = db.query(true, TABLE_PROJECTS, columnsToReturn, COLUMN_PROJECT_NAME + " = ?", projectNames, null, null, null,null);


        if (cursor != null) {
            if (cursor.moveToFirst()) {
                projectID = cursor.getInt(cursor.getColumnIndex("id"));
            }
        }

        cursor.close();
        db.close();

        return projectID;
    }

    public int getNameId(String name) {
        int nameID = 0;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor;
        String[] columnsToReturn = new String[] {COLUMN_NAME_ID};
        String[] names = new String[] {name};

        cursor = db.query(true, TABLE_NAMES, columnsToReturn, COLUMN_NAME + " = ?", names, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                nameID = cursor.getInt(cursor.getColumnIndex("id"));
            }
        }

      //  Log.d("TEST", name);
        cursor.close();
        db.close();
        return nameID;
    }

    public void addNameListToDatabase(int projectID, int firstNameID, int middleNameID) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAMELIST_PROJECT_ID, projectID);
        values.put(COLUMN_NAMELIST_NAME_ID1, firstNameID);
        values.put(COLUMN_NAMELIST_NAME_ID2, middleNameID);


        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAMELIST, null, values);
        db.close();

    }

    public ArrayList<NameListObject> getNameListFromDatabase(int projectID) {
        ArrayList<NameListObject> nameListObjects = new ArrayList<>();
        int nameID1;
        int nameID2;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String[] project = new String[] {String.valueOf(projectID)};
        cursor = db.query(true, TABLE_NAMELIST, null, COLUMN_NAMELIST_PROJECT_ID + " = ?", project, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                nameID1 = cursor.getInt(1);
                nameID2 = cursor.getInt(2);
                Log.d("derp", String.valueOf(nameID1));

                NameListObject nameListObject = new NameListObject();
                Cursor cursor1;
                String nameIDOne[] = new String[] {String.valueOf(nameID1)};
                cursor1 = db.query(true, TABLE_NAMES, null, COLUMN_NAME_ID + " = ?", nameIDOne, null, null, null, null);

                if (cursor1 != null) {
                    if (cursor1.moveToFirst()) {
                        String firstName = cursor1.getString(1);
                        String region = cursor1.getString(2);
                        String timePeriod = cursor1.getString(3);
                        String gender = cursor1.getString(4);

                        nameListObject.setmFirstName(firstName);
                        nameListObject.setmFirstNameRegion(region);
                        nameListObject.setmFirstNameTimePeriod(timePeriod);
                        nameListObject.setmFirstNameGender(gender);
                    }
                }
                cursor1.close();
                Cursor cursor2;
                String nameIDTwo[] = new String[] {String.valueOf(nameID2)};
                cursor2 = db.query(true, TABLE_NAMES, null, COLUMN_NAME_ID + " = ?", nameIDTwo, null, null, null, null);

                if (cursor2 != null) {
                    if (cursor2.moveToFirst()) {
                        String middleName = cursor2.getString(1);
                        String region = cursor2.getString(2);
                        String timePeriod = cursor2.getString(3);
                        String gender = cursor2.getString(4);

                        nameListObject.setmLastName(middleName);
                        nameListObject.setmLastNameRegion(region);
                        nameListObject.setmLastNameTimePeriod(timePeriod);
                        nameListObject.setmLastNameGender(gender);
                    }
                }
                cursor2.close();
                nameListObjects.add(nameListObject);
            }
        }
        cursor.close();
        db.close();
        return nameListObjects;
    }

    public void deleteNamesListFromDatabase(int projectID, int nameID1, int nameID2) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_NAMELIST, COLUMN_NAMELIST_PROJECT_ID + " = " + projectID + " AND " + COLUMN_NAMELIST_NAME_ID1 + " = " + nameID1
                    + " AND " + COLUMN_NAMELIST_NAME_ID2 + " = " + nameID2, null);
        db.close();
        Log.d("derp", "deleted" + String.valueOf(nameID1));

    }

    public void deleteProjectFromDatabase (int projectID) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PROJECTS, COLUMN_PROJECT_ID + " = " + projectID, null);
        db.close();

        Log.d("derp", "deleted" + String.valueOf(projectID));
    }

    public void reAddProjectToDatabase(int projectID, String projectName, String projectDescription) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_PROJECT_ID, projectID);
        values.put(COLUMN_PROJECT_NAME, projectName);
        values.put(COLUMN_PROJECT_DESC, projectDescription);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PROJECTS, null, values);
        db.close();

        Log.d("derp", "readded" + String.valueOf(projectName));
    }


    public ArrayList<ProjectObject> getProjectsForDrawerList() {
        ArrayList<ProjectObject> allProjects = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String orderBy = "ID desc";
        String limit = "5";

        cursor = db.query(true,TABLE_PROJECTS, null, null, null, null, null, orderBy, limit);

        while (cursor.moveToNext()) {
            ProjectObject projectObject = new ProjectObject();
            String projectName = cursor.getString(cursor.getColumnIndex("name"));
            String projectDescription = cursor.getString(cursor.getColumnIndex("description"));
            projectObject.setProjectName(projectName);
            projectObject.setProjectDescription(projectDescription);
            allProjects.add(projectObject);
            db.close();
        }

        return allProjects;
    }
}
