package com.example.johanna.soundlabyrinth.db;

import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.example.johanna.soundlabyrinth.LabyrinthActivity;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Observable;

public class DbFetcher implements LoaderManager.LoaderCallbacks<FloatWrapper> {

    private static int BLOCKSIZE = 10240 * 4;

    private boolean UPDATING = false;

    private Context context;
    private DatabaseThread dbTread;


    float[] soundData;
    int soundLength = 0;
    double degree = 0.0;
    int filledPointer = 0;
    int fetchedPointer = 0;

    int updatedPointer = 0;
    int updateSwitch = 0;
    int dbMax = 0;

    int dBPointer = 0;

    public DbFetcher(Context context, double degree) {
        this.context = context;
        this.degree = degree;
        dbTread = new DatabaseThread(this.context, BLOCKSIZE, 0, degree);
        dbTread.setDegree(this.degree);
        soundLength = dbTread.getSize(); //maybe *2?
        this.soundData = new float[soundLength * 2];
    }


    public void startFetching() {
        UPDATING = false;
        this.executeLoaderFetch();
    }

    private void startUpdate() {
        //1) dbPointer umsetzen
        UPDATING = true;

        //working fine if wanted to start on start
//        dBPointer = 0;
//        filledPointer = 0;

        dBPointer = fetchedPointer/2 + 1;
        updatedPointer = fetchedPointer;
        updateSwitch = fetchedPointer;
        dbMax = dbTread.getSize();
        //2) um onloadFinished kümmern //evtl sowas wie update und fetch -TAG?

        this.executeLoaderFetch();
        //3) this.executeLoaderFetch();
    }


    public void changeDirection(double degree) {
        this.degree = degree;
        this.dbTread.setDegree(this.degree);
        this.startUpdate();
    }


    private void incrementFetchedCounter() {
        fetchedPointer = fetchedPointer + BLOCKSIZE;
        if (fetchedPointer > soundData.length) fetchedPointer = fetchedPointer - soundData.length;
    }


    private void incrementUpdatedCounter() {
        updatedPointer = updatedPointer + BLOCKSIZE;
        if (updatedPointer > soundData.length) updatedPointer = updatedPointer - soundData.length;
    }

    @Override
    public android.support.v4.content.Loader<FloatWrapper> onCreateLoader(int i, Bundle bundle) {
        return new DatabaseThread(context, BLOCKSIZE, dBPointer, degree);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<FloatWrapper> loader, FloatWrapper data) {
        System.out.println("I WAS IN LOAD FINISHED. LOOK AT ME. I AM AMAZING. ");

        if (!UPDATING) {

            dBPointer = dBPointer + BLOCKSIZE;
            float[] floatdata = data.getFloatArray();
            System.arraycopy(floatdata, 0, soundData, filledPointer, floatdata.length);
            filledPointer = filledPointer + floatdata.length;

            if (filledPointer < soundData.length - 1) {
                this.executeLoaderFetch();  //thread is getting data from onCreateLoader
            }
            //here process my data

        } else if (UPDATING) {
            filledPointer = soundData.length-1;
            dBPointer = dBPointer + BLOCKSIZE;
            float[] floatdata = data.getFloatArray();
            System.arraycopy(floatdata, 0, soundData, updatedPointer, floatdata.length);
            int updatestart = updatedPointer;
            updatedPointer = updatedPointer + floatdata.length;
            int updateend = updatedPointer;

            if(updatedPointer >= soundData.length)
                updatedPointer = 0;

            if(dBPointer >= dbMax) {
                dBPointer = 0;
            }


//            if (updatestart< updatedPointer && updateend >= updatedPointer) {
                this.executeLoaderFetch();  //thread is getting data from onCreateLoader
//            }
        }

    }


    public float[] getNextOutputBlock() {
        float[] nextBlock = new float[BLOCKSIZE];

        int aimpointer = fetchedPointer + BLOCKSIZE;


        if (aimpointer < soundData.length) {
            //case: want to return something that isn't fetched from database yet
            if (aimpointer > filledPointer) {
                fetchedPointer = 0;
                aimpointer = fetchedPointer * BLOCKSIZE;
            }

            //case: normal fetch
            nextBlock = Arrays.copyOfRange(soundData, fetchedPointer, aimpointer);

        } else if (aimpointer >= soundData.length) {

            int dif = soundData.length - 1 - fetchedPointer;
            nextBlock = concatenate(
                    Arrays.copyOfRange(soundData, fetchedPointer, soundData.length - 1),
                    Arrays.copyOfRange(soundData, 0, BLOCKSIZE - dif)
            );
        }


        this.incrementFetchedCounter();


        return nextBlock;
    }


    private void executeLoaderFetch() {

        LoaderManager loaderManager = ((LabyrinthActivity) context).getSupportLoaderManager();
        // Get our Loader by calling getLoader and passing the ID we specified
        Loader<FloatWrapper> loader = loaderManager.getLoader(dbTread.getId());
        // If the Loader was null, initialize it. Else, restart it.
        if (loader == null) {
            loaderManager.initLoader(dbTread.getId(), null, this).forceLoad();
        } else {
            loaderManager.restartLoader(dbTread.getId(), null, this).forceLoad();
        }

    }

    private static <T> T concatenate(T a, T b) {
        if (!a.getClass().isArray() || !b.getClass().isArray()) {
            throw new IllegalArgumentException();
        }

        Class<?> resCompType;
        Class<?> aCompType = a.getClass().getComponentType();
        Class<?> bCompType = b.getClass().getComponentType();

        if (aCompType.isAssignableFrom(bCompType)) {
            resCompType = aCompType;
        } else if (bCompType.isAssignableFrom(aCompType)) {
            resCompType = bCompType;
        } else {
            throw new IllegalArgumentException();
        }

        int aLen = Array.getLength(a);
        int bLen = Array.getLength(b);

        @SuppressWarnings("unchecked")
        T result = (T) Array.newInstance(resCompType, aLen + bLen);
        System.arraycopy(a, 0, result, 0, aLen);
        System.arraycopy(b, 0, result, aLen, bLen);

        return result;
    }


    @Override
    public void onLoaderReset(android.support.v4.content.Loader<FloatWrapper> loader) {

    }


}
