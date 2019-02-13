package com.example.johanna.soundlabyrinth.game;

/**
 * This class represents one place in the Labyrinth. It is made out of 4 quarters. Ways in labyrinth can go to 4 directions.
 */
public class PuzzlePiece extends PointOnLabyrinth {

    private int upperLeftRes;
    private int upperRightRes;
    private int downRightRes;
    private int downLeftRes;

    private boolean wayUp = false;
    private boolean wayToLeft = false;
    private boolean wayDown = false;
    private boolean wayToRight = false;


    private QuartersResourcesIds upper_left = null;
    private QuartersResourcesIds upper_right = null;
    private QuartersResourcesIds down_right = null;
    private QuartersResourcesIds down_left = null;

    public enum WayState{
        CLOSED, OPEN, RANDOM
    }

    public PuzzlePiece(){
        super();
    }


    //** Random dann bei aufruf würfeln
    public PuzzlePiece build(boolean left, boolean up, boolean right, boolean down){

        QuartersResourcesIds u_l = QuartersResourcesIds.randomQuarter();
        QuartersResourcesIds u_r = QuartersResourcesIds.randomQuarter();
        QuartersResourcesIds d_l = QuartersResourcesIds.randomQuarter();
        QuartersResourcesIds d_r = QuartersResourcesIds.randomQuarter();

        //case: 4 directions
        if(left && up && right && down){
            upper_left = QuartersResourcesIds.BOTHOPEN;
            upper_right = QuartersResourcesIds.BOTHOPEN;
            down_right = QuartersResourcesIds.BOTHOPEN;
            down_left = QuartersResourcesIds.BOTHOPEN;
        }

        //case: 0 directions
        else if(!left && !up && !right && !down){
            upper_left = QuartersResourcesIds.BOTHCLOSED;
            upper_right = QuartersResourcesIds.BOTHCLOSED;
            down_right = QuartersResourcesIds.BOTHCLOSED;
            down_left = QuartersResourcesIds.BOTHCLOSED;
        }

        //case: 2 Directions straight
        else if((left && right && !up && !down) || (!left && !right && up && down)){
            u_l = QuartersResourcesIds.LEFTOPEN;
            u_r = QuartersResourcesIds.RIGHTOPEN;
            d_r = QuartersResourcesIds.LEFTOPEN;
            d_l = QuartersResourcesIds.RIGHTOPEN;

            if(left && right) rotateToRightAndSetPublic(0, u_l, u_r, d_r, d_l);
            if(up&&down) rotateToRightAndSetPublic(1, u_l, u_r, d_r, d_l);
        }

        //case: 2 Directions corner
        else if((left && up && !right && !down) || (!left && up && right && !down) || (!left && !up && right && down) || (left && !up && !right && down)) {
            u_l = QuartersResourcesIds.BOTHOPEN;
            u_r = QuartersResourcesIds.RIGHTOPEN;
            d_r = QuartersResourcesIds.BOTHCLOSED;
            d_l = QuartersResourcesIds.RIGHTOPEN;

            if(left && up) rotateToRightAndSetPublic(0, u_l, u_r, d_r, d_l);
            if(up && right) rotateToRightAndSetPublic(1, u_l, u_r, d_r, d_l);
            if(right && down) rotateToRightAndSetPublic(2, u_l, u_r, d_r, d_l);
            if(down && left) rotateToRightAndSetPublic(3, u_l, u_r, d_r, d_l);
        }

        //case: 1 direction
        else if((left && !up && !right && !down) || (!left && up && !right && !down) || (!left && !up && right && !down) || (!left && !up && !right && down)){
            u_l = QuartersResourcesIds.LEFTOPEN;
            u_r = QuartersResourcesIds.BOTHCLOSED;
            d_r = QuartersResourcesIds.BOTHCLOSED;
            d_l = QuartersResourcesIds.RIGHTOPEN;

            if(left) rotateToRightAndSetPublic(0, u_l, u_r, d_r, d_l);
            if(up) rotateToRightAndSetPublic(1, u_l, u_r, d_r, d_l);
            if(right) rotateToRightAndSetPublic(2, u_l, u_r, d_r, d_l);
            if(down) rotateToRightAndSetPublic(3, u_l, u_r, d_r, d_l);
        }

        //case: 3 directions
        else if((!left && up && right && down) || (left && !up && right && down) || (left && up && !right && down) || (left && up && right && !down)){
            u_l = QuartersResourcesIds.RIGHTOPEN;
            u_r = QuartersResourcesIds.BOTHOPEN;
            d_r = QuartersResourcesIds.BOTHOPEN;
            d_l = QuartersResourcesIds.LEFTOPEN;

            if(!left) rotateToRightAndSetPublic(0, u_l, u_r, d_r, d_l);
            if(!up) rotateToRightAndSetPublic(1, u_l, u_r, d_r, d_l);
            if(!right) rotateToRightAndSetPublic(2, u_l, u_r, d_r, d_l);
            if(!down) rotateToRightAndSetPublic(3, u_l, u_r, d_r, d_l);
        }

        matchQuatersToRes();
        findWayDirections();
        return this;
    }



    public PuzzlePiece build(Direction from){

        QuartersResourcesIds u_l = QuartersResourcesIds.randomQuarter();
        QuartersResourcesIds u_r = QuartersResourcesIds.randomQuarter();
        QuartersResourcesIds d_l = QuartersResourcesIds.randomQuarter();
        QuartersResourcesIds d_r = QuartersResourcesIds.randomQuarter();

        //puzzle the peaces clockwise beginning with upper left quarter from the beginning o
        while (!u_l.isLeftOpen()){
            u_l = QuartersResourcesIds.randomQuarter();
        }
        while (u_l.isRightOpen()!= u_r.isLeftOpen()) {
            u_r = QuartersResourcesIds.randomQuarter();
        }
        while (u_r.isRightOpen()!= d_r.isLeftOpen()) {
            d_r = QuartersResourcesIds.randomQuarter();
        }
        while (d_r.isRightOpen()!= d_l.isLeftOpen() || d_l.isRightOpen() != u_l.isLeftOpen()) {
            d_l = QuartersResourcesIds.randomQuarter();
        }


        // rotate puzzle piece if necessary
        switch (from) {
            case LEFT:
               rotateToRightAndSetPublic(0, u_l, u_r, d_r, d_l);
                break;
            case UP:
                rotateToRightAndSetPublic(1, u_l, u_r, d_r, d_l);
                break;
            case RIGHT:
                rotateToRightAndSetPublic(2, u_l, u_r, d_r, d_l);
                break;
            case DOWN:
                rotateToRightAndSetPublic(3, u_l, u_r, d_r, d_l);
                break;
        }

        matchQuatersToRes();
        findWayDirections();

        return this;
    }

    public void rotateToRightAndSetPublic(int steps, QuartersResourcesIds u_l, QuartersResourcesIds u_r, QuartersResourcesIds d_r, QuartersResourcesIds d_l){
        switch (steps%4){
            case 0:
                upper_left = u_l;
                upper_right = u_r;
                down_right = d_r;
                down_left = d_l;
                break;
            case 1:
                upper_left = d_l;
                upper_right = u_l;
                down_right = u_r;
                down_left = d_r;
                break;
            case 2:
                upper_left = d_r;
                upper_right = d_l;
                down_right = u_l;
                down_left = u_r;
                break;
            case 3:
                upper_left = u_r;
                upper_right = d_r;
                down_right = d_l;
                down_left = u_l;
                break;
        }

    }

    public  void matchQuatersToRes(){
        upperLeftRes = upper_left.getValue();
        upperRightRes = upper_right.getValue();
        downRightRes = down_right.getValue();
        downLeftRes = down_left.getValue();
    }

    public PuzzlePiece buildRandom(){

        upperLeftRes = QuartersResourcesIds.randomQuarter().getValue();
        upperRightRes = QuartersResourcesIds.randomQuarter().getValue();
        downRightRes = QuartersResourcesIds.randomQuarter().getValue();
        downLeftRes = QuartersResourcesIds.randomQuarter().getValue();
        return this;
    }


    public int getUpperLeftRes() {
        return upperLeftRes;
    }

    public int getUpperRightRes() {
        return upperRightRes;
    }

    public int getDownRightRes() {
        return downRightRes;
    }

    public int getDownLeftRes() {
        return downLeftRes;
    }


    private void findWayDirections() throws IllegalArgumentException{

        try {
            wayUp = checkWayBetweenLeftAndRight(upper_left, upper_right);
            wayToRight = checkWayBetweenLeftAndRight(upper_right, down_right);
            wayDown = checkWayBetweenLeftAndRight(down_right, down_left);
            wayToLeft = checkWayBetweenLeftAndRight(down_left, upper_left);
        }catch(IllegalArgumentException e){
            throw e;
        }
    }

    private boolean checkWayBetweenLeftAndRight(QuartersResourcesIds left, QuartersResourcesIds right){
        if( left == null || right == null)
            throw  new IllegalArgumentException("One QuartersResourcesIds which should be checked is null.");

        if(left.isRightOpen() && right.isLeftOpen())
            return true;
        else
           return false;
    }

    public boolean hasWayUp() {
        try{
            findWayDirections();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
        return wayUp;
    }

    public boolean hasWayToLeft() {
        try{
            findWayDirections();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
        return wayToLeft;
    }

    public boolean hasWayDown() {
        try{
            findWayDirections();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
        return wayDown;
    }

    public boolean hasWayToRight() {
        try{
            findWayDirections();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
        return wayToRight;
    }


}
