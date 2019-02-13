package com.example.johanna.soundlabyrinth.game;

public class Player extends PointOnLabyrinth {


    private Direction orientation;


    public Player(){
        super();
        this.orientation = Direction.UP;
    }


    public void move(Direction dir){
        int curX = this.getX();
        int curY = this.getY();

        switch (dir){
            case LEFT:
                this.setPosition(curX - 1, curY);
                break;
            case UP:
                this.setPosition(curX, curY + 1);
                break;
            case RIGHT:
                this.setPosition(curX + 1, curY);
                break;
            case DOWN:
                this.setPosition(curX, curY - 1);
                break;
        }
        System.out.println("Moved Player to Position: x: " + this.getX() + " y: " + this.getY());
    }

    public Direction getOrientation() {
        return orientation;
    }

    public void setOrientation(Direction orientation) {
        this.orientation = orientation;
    }
}
