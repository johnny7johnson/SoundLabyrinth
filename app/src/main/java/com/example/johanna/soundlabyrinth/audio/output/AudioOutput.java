package com.example.johanna.soundlabyrinth.audio.output;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;

//import com.example.johanna.audioprocessing.TextReader;
import com.example.johanna.soundlabyrinth.LabyrinthActivity;
import com.example.johanna.soundlabyrinth.db.DataBaseHelper;
import com.example.johanna.soundlabyrinth.db.DbFetcher;

import java.util.Observable;
import java.util.Observer;



public class AudioOutput implements Observer, SampleFetcher {


    private AudioThread audioThread;
    private Context context;
    private double degree;
    DbFetcher dbFetcher;

    public AudioOutput(Context context, double degree) {
        this.context = context;
        this.degree = degree;
        //TODO init audiotrack
    }

    private void createOutputThread() {
        if (audioThread != null) {
            audioThread.cancel(true);
            audioThread.stop();


        }
        //TEST
        //TODO: process crossfading to next fileter
//        (new RealtimeProcessingNaiv()).processNextFilter(this.getBaseAudioSamples(), this.getFilter(60));


        float[] samples = this.getMergedAudioSamples();
        audioThread = new AudioThread(samples, this);


    }

    public void startCurrentOutputThread() {

        boolean test = true;

        if (!test) {
            createOutputThread();
        } else {
            startFetchingAudioSamples();
            audioThread = new AudioThread(new float[]{0, 0, 0, 0}, this);
        }

        if (test)
            audioThread.execute();
    }

    public void stopAudioOutputThread() {
        if (audioThread != null) {
            audioThread.cancel(true);
            audioThread.stop();
        }
    }


    private float[] getMergedAudioSamples() {
        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(this.context);
        try {

            myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
        float[] audioFile = myDbHelper.getMergedWaveForDeg(60);
        myDbHelper.close();
        return audioFile;
    }

    //controlling Methods:


    public DbFetcher startFetchingAudioSamples() {


        dbFetcher = new DbFetcher(context, degree);
//        ((LabyrinthActivity) context).getSupportLoaderManager().initLoader(dbFetcher.getIdOfWorkerThread(), null, dbFetcher).forceLoad(); //register dbfetcher as listener to loader

        dbFetcher.startFetching();


        //TODO
//        dbFetcher.addObserver(this);
        return dbFetcher;
    }


    public float[] getNextBlock(){
        float[] block = dbFetcher.getNextOutputBlock();
        while (block == null || block.length == 0 ){
            block = dbFetcher.getNextOutputBlock();
        }
        return block;
//        return dbFetcher.getNextOutputBlock();
    }


    @Override
    public void update(Observable observable, Object data) {
//        float[] fetchedBlock = (float[]) data;
//        this.writeNextIntoThread(fetchedBlock);
    }

    public void writeNextIntoThread(float[] nextBlock) {
        audioThread.writeNext(nextBlock);
    }

    public void changeDirection(double dir){
        dbFetcher.changeDirection(dir);
    }

}
