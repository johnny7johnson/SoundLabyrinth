package com.example.johanna.soundlabyrinth.game;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This class contains the Map for one Game.
 */
public class LabyrinthMap {

    HashMap<PointOnLabyrinth, PuzzlePiece> map;
    private int dimension = 5;

    public LabyrinthMap() {

    }

    public void build(int dimension) {
        this.dimension = dimension;
        map = new HashMap<>(dimension * dimension);
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; y++) {
                PuzzlePiece piece = new PuzzlePiece();
                piece.setPosition(x, y);
                PointOnLabyrinth p = new PointOnLabyrinth(piece.getX(), piece.getY());
                //TODO: find logic for this
                piece.build((new Random()).nextBoolean(), (new Random()).nextBoolean(), (new Random()).nextBoolean(), (new Random()).nextBoolean());
                map.put(p, piece);
            }
        }
        //TODO: fill
    }

    public PuzzlePiece getPuzzlePiece(int x, int y) {

        if (x >= dimension) {
            return createBorderPiece(x, y, Direction.LEFT);
        } else if (x < 0) {
            return createBorderPiece(x, y, Direction.RIGHT);
        } else if (y < 0) {
            return createBorderPiece(x, y, Direction.UP);
        } else if (y >= dimension) {
            return createBorderPiece(x, y, Direction.DOWN);
        }

        PuzzlePiece selectedPiece = this.map.get(new PointOnLabyrinth(x, y));
        return selectedPiece;
    }

    private PuzzlePiece createBorderPiece(int x, int y, Direction dir) {
        PuzzlePiece borderPiece = new PuzzlePiece();
        borderPiece.setPosition(x, y);
        switch (dir) {
            case LEFT:
                borderPiece.build(true, false, false, false);
                break;
            case UP:
                borderPiece.build(false, true, false, false);
                break;
            case RIGHT:
                borderPiece.build(false, false, true, false);
                break;
            case DOWN:
                borderPiece.build(false, false, false, true);
                break;
        }
        return borderPiece;

    }

    public int getDimension() {
        return dimension;
    }
}
