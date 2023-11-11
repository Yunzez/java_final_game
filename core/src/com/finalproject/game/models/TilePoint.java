package com.finalproject.game.models;

public class TilePoint {

    public int x;
    public int y;

    public TilePoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    // Manhattan distance
    public int distance(TilePoint other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }
}
