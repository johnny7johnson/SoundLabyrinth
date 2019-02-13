package com.example.johanna.soundlabyrinth.game;

import android.graphics.Point;

public class PointOnLabyrinth{

    ;


    Point position;

    public PointOnLabyrinth(){
        this.position = new Point(0,0);
    }

    public PointOnLabyrinth(int x, int y){
        this();
        this.setPosition(x, y);
    }

    public int getX(){
        return position.x;
    }

    public void setX(int x){
        this.position.x = x;
    }

    public  int getY(){
        return position.y;
    }

    public void setY(int y){
        this.position.y = y;
    }

    public void setPosition(int x, int y) {
        this.position.set(x ,y);
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        PointOnLabyrinth other = (PointOnLabyrinth) o;
        return other.equals(this.getX(), this.getY());
    }

    public boolean equals(int x, int y){
        return position.equals(x,y);
    }

}
