package com.example.johanna.soundlabyrinth.game;


import com.example.johanna.soundlabyrinth.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum QuartersResourcesIds {
    LEFTOPEN(R.mipmap.way_left), RIGHTOPEN(R.mipmap.way_right), BOTHOPEN(R.mipmap.way_both), BOTHCLOSED(R.mipmap.rare);
    private final int pic;
    QuartersResourcesIds(int o){this.pic = o;};
    public int getValue() { return pic; }

    private static final List<QuartersResourcesIds> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();


    public static QuartersResourcesIds randomQuarter()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    //always seen from center
    public boolean isLeftOpen(){
        switch (this){
            case LEFTOPEN: return true;
            case RIGHTOPEN: return false;
            case BOTHOPEN: return true;
            case BOTHCLOSED: return false;
            default: return false;
        }
    }

    public boolean isRightOpen(){
        switch (this){
            case LEFTOPEN: return false;
            case RIGHTOPEN: return true;
            case BOTHOPEN: return true;
            case BOTHCLOSED: return false;
            default: return false;
        }
    }
};