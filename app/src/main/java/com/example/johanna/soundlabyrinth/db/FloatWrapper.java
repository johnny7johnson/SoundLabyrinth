package com.example.johanna.soundlabyrinth.db;

import java.io.Serializable;

public class FloatWrapper implements Serializable {
    float[] array;

    public FloatWrapper(float[] arrayToWrap){
        this.array = arrayToWrap;
    }

    public float[] getFloatArray(){
        return  array;
    }

    public void setFloatArray(float[] floatArray){
        this.array = floatArray;
    }
}
