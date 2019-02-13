package com.example.johanna.soundlabyrinth.game;

public enum Direction {
    LEFT(0), UP(1), RIGHT(2), DOWN(3);
    private final int ori;
    Direction(int o){this.ori = o;};
    public int getValue() { return ori; }
}
