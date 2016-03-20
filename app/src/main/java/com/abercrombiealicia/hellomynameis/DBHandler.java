package com.abercrombiealicia.hellomynameis;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
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

    private static final int DATABASE_VERSION = 1;
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
        onUpgrade(db, DATABASE_VERSION, DATABASE_VERSION);
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
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMELIST);
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

        Cursor cursor = db.rawQuery("SELECT * FROM names;", null);
        while (cursor.moveToNext()) {
            String region = cursor.getString(cursor.getColumnIndex("region"));
            regionSet.add(region);
        }

        ArrayList<String> regionArrayList = new ArrayList<>(regionSet);
        return regionArrayList;
    }

    public ArrayList<String> getTimePeriod() {

        SQLiteDatabase db = this.getReadableDatabase();
        Set<String> timeSet = new TreeSet<>();

        Cursor cursor = db.rawQuery("SELECT * FROM names;", null);
        while (cursor.moveToNext()) {
            String timePeriod = cursor.getString(cursor.getColumnIndex("time_period"));
            timeSet.add(timePeriod);
        }

        ArrayList<String> timeArrayList = new ArrayList<>(timeSet);
        return timeArrayList;
    }

    public ArrayList<String> getNamesFromDatabase(String region, String time, String gender) {

        ArrayList<String> allNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String[] columnsToReturn = new String[] {COLUMN_NAME};

        cursor = db.query(true, TABLE_NAMES, columnsToReturn, COLUMN_NAME_REGION + " = '" + region + "' AND " + COLUMN_NAME_TIMEPERIOD + " = '" + time
                            + "' AND " + COLUMN_NAME_GENDER + " = '" + gender + "'", null, null, null, null, null);


        while (cursor.moveToNext()) {
            allNames.add(cursor.getString(0));
        }

        return allNames;
    }
}
