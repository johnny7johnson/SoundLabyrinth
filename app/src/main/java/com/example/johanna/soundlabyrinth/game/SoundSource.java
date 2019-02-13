package com.example.johanna.soundlabyrinth.game;

import android.content.Context;

import com.example.johanna.soundlabyrinth.audio.output.AudioOutput;

public class SoundSource extends PointOnLabyrinth {

    private float currentAngle = 0;
    private AudioOutput audio;


    public SoundSource(){
        super();
    }

    public void updateDirection(int playerX, int playerY, int mapDimension){
        float newAngle = 0;
        //calculate angle the sound must come from
        if (this.getX()==playerX){
            if(this.getY() >= playerY) newAngle = 0;
            else  newAngle = 180;
        }
        else if(this.getY()==playerY){
            if(this.getX() > playerX) newAngle = 90;
            else newAngle = 270;
        }
        else {
            //player as central point in circle
//            double theta = Math.toDegrees(Math.atan2(this.getY() - playerY, this.getX() - playerX));
//            double theta = Math.toDegrees(Math.atan2(playerY - this.getY() ,  playerX -  this.getX())); //best :)

//            double theta = Math.toDegrees(-Math.sin(Math.atan2(playerY - this.getY() ,  playerX -  this.getX()))); //best :)
            double theta = Math.toDegrees(-Math.atan2(playerY - this.getY() ,  playerX -  this.getX())); //best :)


            // theta relative to x axis
            newAngle = (float) theta - 90;
            if(newAngle < 0) newAngle = newAngle + 360;


        }
        currentAngle = newAngle;
        System.out.println("New sound angle: " + newAngle);
        //Pythagoras: sin(alpha) = gegenkathete / ankathetet -> sin(alpha) = dist(y)/dist(x) -> alpha = arcsin(  dist(y)/dist(x))
        //TODO: change sound direction in AudioOutput

        audio.changeDirection(currentAngle);
    }

    public float getCurrentAngle(){
        return currentAngle;
    }


    public void startAudio(Context context) {
        audio = new AudioOutput(context, currentAngle);
        audio.startCurrentOutputThread();
    }

    public void stopAudio(){
        audio.stopAudioOutputThread();
    }
}
