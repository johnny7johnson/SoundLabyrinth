package com.example.johanna.soundlabyrinth.audio.output;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;

public class AudioThread extends AsyncTask {

    private static final String TAG = "MyAudioThread";

    private AudioTrack player;
    private float[] audioFile;
    private float[] buffer = null;
    private boolean running;
    private Boolean playFinished = true;

    private SampleFetcher mom;

    private boolean isNew = true;
    private int notificationMarkerPosition = -1;

    public AudioThread(float[] mergedAudioFile, SampleFetcher parent) {
        this(parent);
        this.audioFile = mergedAudioFile;
        buffer = mergedAudioFile;
    }


    private AudioThread(SampleFetcher parent) {
        super();
        this.mom = parent;
        int outputBufferSize = AudioTrack.getMinBufferSize(48000,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        player = new AudioTrack(AudioManager.STREAM_MUSIC, 48000
                , AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_FLOAT
                , outputBufferSize, AudioTrack.MODE_STREAM);
//        player.setNotificationMarkerPosition(-1);
        player.setNotificationMarkerPosition(10000);
        player.setPlaybackHeadPosition(notificationMarkerPosition);     //TEST
        player.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {

            @Override
            public void onMarkerReached(AudioTrack audioTrack) {
                System.out.println("Finished audioOutput");

                // Do something
                // If the condition is true, do the following:
//                synchronized (playFinished) {
                playFinished = true;
//                    playFinished.notify();
//                }
            }

            @Override
            public void onPeriodicNotification(AudioTrack audioTrack) {

            }
        });

    }


    @Override
    protected Object doInBackground(Object[] objects) {
        //TODO

        long starttime = System.currentTimeMillis();
        while (System.currentTimeMillis() - starttime < 800);


        System.out.println("Background worker started.");
        running = true;

        rotateBuffer();
        rotateBuffer();

        //Output
        while (running) {

            if (playFinished) {
                player.flush();
                if (isNew) playOutAudio_selfFetching();
                else playOutAudio();
                Log.v(TAG, "Wait for audio to finish from now.");
            }

        }
        System.out.println("Background worker broke up.");
        return null;
    }

    private void playOutAudio() {
        //synchronized (playFinished) {

        if (running && playFinished) {
            System.out.println("Init audioOutput");

            playFinished = false;
            player.play();          //is it waiting here?
            player.flush();


            player.setNotificationMarkerPosition(player.getNotificationMarkerPosition() + 48000 * 10 - 1);

//            player.setNotificationMarkerPosition(player.getNotificationMarkerPosition() + audioFile.length - 1);

            if (this.audioFile != null && this.audioFile.length != 0) {
                player.write(audioFile, 0, audioFile.length, AudioTrack.WRITE_BLOCKING);
            }

            System.out.println("Start audioOutput");
        }
        //playFinished.notify();
        //}
    }


    private void playOutAudio_selfFetching() {
        //synchronized (playFinished) {

        if (running && playFinished) {
            System.out.println("Init audioOutput");

            playFinished = false;
            player.play();          //is it waiting here?
            player.flush();

            notificationMarkerPosition = player.getNotificationMarkerPosition();
            notificationMarkerPosition = notificationMarkerPosition + audioFile.length / 2 - 1;

            player.setPlaybackHeadPosition(notificationMarkerPosition);     //TEST
            player.setNotificationMarkerPosition(notificationMarkerPosition - 1);

            if (this.audioFile != null && this.audioFile.length != 0) {
                player.write(audioFile, 0, audioFile.length, AudioTrack.WRITE_BLOCKING);

                rotateBuffer();
            }

            System.out.println("Start audioOutput");
        }
    }

    private void rotateBuffer() {
        audioFile = buffer;
        buffer = null;
        buffer = mom.getNextBlock();
    }

    public void stop() {
        running = false;
        Log.v(TAG, "Stop running.");
    }

    public boolean writeNext(float[] nextBlock) {
        if (buffer != null) {
            return false;
        } else {
            buffer = nextBlock;
            return true;
        }
    }


}
