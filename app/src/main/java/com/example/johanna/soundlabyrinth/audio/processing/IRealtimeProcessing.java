package com.example.johanna.soundlabyrinth.audio.processing;

public interface IRealtimeProcessing {

    public float[] fadeToNextOutput(float[] first, float[] second);

}
