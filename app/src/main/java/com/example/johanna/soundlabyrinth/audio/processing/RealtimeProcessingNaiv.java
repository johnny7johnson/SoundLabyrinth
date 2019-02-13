package com.example.johanna.soundlabyrinth.audio.processing;

import android.util.Log;


//import com.example.johanna.audioprocessing.AudioProcessing.IRealtimeProcessing;

import java.util.ArrayList;
import java.util.Arrays;

//import be.tarsos.dsp.util.fft.FFT;
//import be.tarsos.dsp.util.fft.FloatFFT;

public class RealtimeProcessingNaiv implements IRealtimeProcessing {

    private static final String TAG = "NaivRealtimeProcessing";

    @Override
    public float[] fadeToNextOutput(float[] first, float[] second) {

        float[] faded = new float[first.length];
        if (first.length == second.length) {
            int length = first.length;
            for (int i = 0; i < length; i++) {

                faded[i] = (first[i] * ((length - i) / length)) + (second[i] * ((i) / length));

            }
            return faded;
        }
        return new float[0];
    }
}
