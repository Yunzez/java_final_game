package com.finalproject.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum TileType {
    FLOOR("floor", true, "map/floor1.png", 100, 100),
    WALL("wall", false, "map/wall.png", 100, 100);

    private String tileName;
    private boolean walkable;
    private Texture texture;
    private int width;
    private int height;
    
    TileType(String tileName, boolean walkable, String texturePath, int width, int height) {
        this.tileName = tileName;
        this.walkable = walkable;
        this.texture = new Texture(texturePath);
        this.width = width;
        this.height = height;
    }
    
    public String getTileName() {
        return tileName;
    }
    
    public boolean isWalkable() {
        return walkable;
    }

    public Texture getTexture() {
        // Lazy loading of the texture
        if (texture == null) {
            throw new IllegalArgumentException("Texture for tile type " + tileName + " has not been loaded.");
        } 
        return texture;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    // Call this method when disposing of the enum's resources
    public void dispose() {
        texture.dispose();
    }
}
