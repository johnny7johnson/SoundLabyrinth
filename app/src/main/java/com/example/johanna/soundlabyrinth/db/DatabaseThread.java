package com.example.johanna.soundlabyrinth.db;

//import android.content.AsyncTaskLoader;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

public class DatabaseThread extends AsyncTaskLoader<FloatWrapper> {

    private DataBaseHelper dbHelper;
    private double degree = 0.0;
    int fetchPointer = 0;
    int blocksize = 0;


    public DatabaseThread(Context context) {
        super(context);
        this.dbHelper = new DataBaseHelper(context);
    }

    public DatabaseThread(Context context, int blockSize, int currentFetchPointer, double currentDegree) {     //maybe another constructor with fetchpointer too
        this(context);
        this.degree = currentDegree;
        this.blocksize = blockSize;
        this.fetchPointer = currentFetchPointer;
    }


//    @Override
//    public Object loadInBackground() {
//        return this.getNextBlock(degree);
//    }

    @Override
    public FloatWrapper loadInBackground() {

//        return this.getNextBlock(degree);
        return new FloatWrapper(this.getNextBlock(degree));
    }


    public float[] getNextBlock(double deg) {
        dbHelper.openDataBase();
        float[] samples = dbHelper.getMergedWaveForDeg(deg, fetchPointer, fetchPointer + blocksize -1);
        fetchPointer = fetchPointer + blocksize;
        dbHelper.close();
        if(fetchPointer > 495000){
            dbHelper.close();
        }
        return samples;
    }

    public int getSize() {
        return this.dbHelper.getSampleCountForDeg(degree);
    }


    public void setDegree(double deg) {
        this.degree = deg;
    }

    public int getCurrentFetchPointer(){
        return this.fetchPointer;
    }

    @Override
    public void deliverResult(FloatWrapper data){
        super.deliverResult(data);
    }



}
