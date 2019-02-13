package com.example.johanna.soundlabyrinth.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;


public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private String DB_PATH = "";

    private static String DB_NAME = "guitarwave.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
    }


    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CREATOR ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

            String myPath = DB_PATH + DB_NAME;
        try {
            //checkDB = this.getReadableDatabase();
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY); //HERE
            checkDB.close();
        } catch (SQLiteException e) {

            //database does't exist yet.
            System.out.println("Database doesn't exist yet.");
        }

        File file = new File(myPath);

        if (checkDB != null && file.exists()) {

            checkDB.close();

        }

         if(!file.exists()) return false;

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ FETCHER ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓


    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }


    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.



    public float[] getMergedWaveForDeg(double deg, int startsampleIndex, int endSampleIndex) {

        int length = 0;
        String selectQuery = "SELECT left, right FROM " + generateTableName(deg)
                + " WHERE _id >= " + startsampleIndex + " AND _id <= " + endSampleIndex + ";";
        try {
            this.close();
            this.openDataBase();
        } catch (SQLException e) {
            System.out.println("damn error");
        }
        myDataBase = this.getReadableDatabase();
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        float[] sampleArray = extractFloatArrayFromCursor(cursor);

        myDataBase.close();
        return sampleArray;

    }


    public int getSampleCountForDeg(double deg) {
        int count = 0;
        String selectQuery = "SELECT count(_id) FROM " + generateTableName(deg) + ";";
        try {
            this.close();
            this.openDataBase();
        } catch (SQLException e) {
            System.out.println("damn error");
        }
        myDataBase = this.getReadableDatabase();
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);

        }

        myDataBase.close();
        return count;
    }


    public float[] getMergedWaveForDeg(double deg) {

        long startTime = System.currentTimeMillis();
        int length = 0;
        String selectQuery = "SELECT left, right FROM " + generateTableName(deg) + ";";
        try {
            this.close();
            this.openDataBase();
        } catch (SQLException e) {
            System.out.println("damn error");
        }
        myDataBase = this.getReadableDatabase();
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        long fetch = System.currentTimeMillis() - startTime;
        long difSec = fetch / 1000;
        long fetchTime = System.currentTimeMillis();
        System.out.println("Time to fetch from DB: " + difSec + " s.");

        length = cursor.getCount();
        float[] sampleArray = new float[length * 2];


        int linecounter = 0;
        //if TABLE has rows
        if (cursor.moveToFirst()) {
            //Loop through the table rows
            do {
                sampleArray[linecounter * 2] = cursor.getFloat(0);
                sampleArray[linecounter * 2 + 1] = cursor.getFloat(1);

                linecounter++;
            } while (cursor.moveToNext());
        }
        myDataBase.close();
        long loop = System.currentTimeMillis() - fetchTime;
        difSec = loop / 1000;
        System.out.println("Time to fetch from DB: " + difSec + " s.");
        return sampleArray;
    }


    private float[] extractFloatArrayFromCursor(Cursor cursor) {
        int length = cursor.getCount();
        float[] sampleArray = new float[length * 2];
        int linecounter = 0;
        if (cursor.moveToFirst()) {
            do {
                sampleArray[linecounter * 2] = cursor.getFloat(0);
                sampleArray[linecounter * 2 + 1] = cursor.getFloat(1);

                linecounter++;
            } while (cursor.moveToNext());
        }
        return sampleArray;
    }


    private String generateTableName(double deg) {
        double rounded = Math.round(deg / 10.0) * 10;
        if (rounded > 180) rounded = rounded - 360; //to match to database sound angles -170 to 180
        String name = "";
        if (rounded < 0) {
            name = "minus" + (int) -rounded;
        } else if (rounded > 0) {
            name = "plus" + (int) rounded;
        } else {
            name = "zero";
        }
        return name;
    }

}