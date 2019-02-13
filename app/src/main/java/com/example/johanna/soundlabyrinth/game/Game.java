package com.example.johanna.soundlabyrinth.game;

import android.content.Context;

import java.util.Random;

public class Game {

    private LabyrinthMap map;
    private SoundSource sound;
    private Player player;

    private int dimension = 5;

    public Game() {
        //init map, sound and player
        init();

    }

    private void init() {
        Random random = new Random();

        //TODO: init map
        map = new LabyrinthMap();

        System.out.println("Build map with dimension " + map.getDimension());
        sound = new SoundSource();
        sound.setPosition(random.nextInt(map.getDimension()), random.nextInt(map.getDimension()));
        System.out.println("Set Sound on Position x: " + sound.getX() + ", y: " + sound.getY());

        //TODO: set sound
        player = new Player();
        player.setPosition(random.nextInt(map.getDimension()), random.nextInt(map.getDimension()));
        System.out.println("Set Player on Position x: " + player.getX() + ", y: " + player.getY());


        if (!findWayBetween()) {
            init();
        }
    }

    public void start(Context activity) {
        //TODO: start sound
        sound.startAudio(activity);
    }

    /**
     * @param direction Direction of movement in map
     */
    public void movePlayer(Direction direction) {
        player.move(direction);
        changeSoundDirection();
    }

    public boolean checkWin() {

        if (player.position.equals(sound.position)){
            sound.stopAudio();
            return true;
        }
        else return false;

        //or better trigger event?
    }

    public PuzzlePiece getCurrentPuzzlePiece(Mode gamemode, Direction dir) {
        PuzzlePiece puzzlePiece = new PuzzlePiece().buildRandom();

        if (gamemode == Mode.MAP)
        {
//            puzzlePiece = map.getPuzzlePiece(player.getX(), player.getY());
            return puzzlePiece;
        }
        else if (gamemode == Mode.RANDOM) {
            if (gamemode == Mode.RANDOM) {
                switch (dir) {
                    case LEFT:
                        puzzlePiece = new PuzzlePiece().build(Direction.RIGHT);
                        break;
                    case UP:
                        puzzlePiece = new PuzzlePiece().build(Direction.DOWN);
                        break;
                    case RIGHT:
                        puzzlePiece = new PuzzlePiece().build(Direction.LEFT);
                        break;
                    case DOWN:
                        puzzlePiece = new PuzzlePiece().build(Direction.UP);
                        break;
                }
                return puzzlePiece;
            }
        } else if (gamemode == Mode.STRAIGHT) {

            if (player.getX() < 0)
                puzzlePiece = new PuzzlePiece().build(false, false, true, false);
            else if (player.getY() >= dimension)
                puzzlePiece = new PuzzlePiece().build(false, false, false, true);
            else if (player.getX() >= dimension)
                puzzlePiece = new PuzzlePiece().build(true, false, false, false);
            else if (player.getY() < 0)
                puzzlePiece = new PuzzlePiece().build(false, true, false, false);
            else
                puzzlePiece = new PuzzlePiece().build(true, true, true, true);
            return puzzlePiece;
        }
        return new PuzzlePiece().buildRandom();
    }


    private void changeSoundDirection() {
        sound.updateDirection(player.getX(), player.getY(), map.getDimension());
    }

    private boolean findWayBetween() {
        //return true if a way between player and sound exists
        return true;
    }

    public float getCurrentSoundDirection() {
        return sound.getCurrentAngle();
    }

}
