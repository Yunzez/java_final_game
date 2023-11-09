package com.finalproject.game.components;

import java.util.Random;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.finalproject.game.models.TileType;
import com.finalproject.game.models.Room;
import com.finalproject.game.models.TilePoint;

import java.util.Collections;
import java.util.Comparator;

public class MapGenerator {
    private int width;
    private int height;
    private TileType[][] map; // Enum for different tile types (wall, floor, etc.)
    private int tileSize;
    private Random random = new Random();
    private ArrayList<Room> rooms; // List to hold rooms
    private TilePoint entranceLocation;
    private TilePoint exitLocation;

    public MapGenerator(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.map = new TileType[this.width][this.height];
        System.out.println(this.width + " " + this.height);

    }

    public TileType[][] generateMap() {
        // Initialize the map with walls
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = TileType.WALL;
            }
        }

        generateRoomsAndCorridors();

        return map;
    }

    public TilePoint getEntranceLocation() {
        return entranceLocation;
    }

    public TilePoint getExitLocation() {
        return exitLocation;
    }

    private void generateRoomsAndCorridors() {
        rooms = new ArrayList<Room>();
        int numberOfRooms = random.nextInt(4) + 3; // Number of rooms from 3 to 5

        for (int i = 0; i < numberOfRooms; i++) {
            int roomWidth = random.nextInt(3) + 3;
            int roomHeight = random.nextInt(3) + 2;
            int roomX = random.nextInt(width - roomWidth);
            int roomY = random.nextInt(height - roomHeight);

            Room room = new Room(roomX + roomWidth / 2, roomY + roomHeight / 2, roomWidth, roomHeight);
            rooms.add(room);
            createRoom(room.centerX, room.centerY, room.width, room.height);
        }

        // Sort rooms from left to right based on centerX
        Collections.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room r1, Room r2) {
                return Integer.compare(r1.centerX, r2.centerX);
            }
        });

        // Ensure there is an entrance on the left
        Room firstRoom = rooms.get(0);

        createCorridor(0, firstRoom.centerY, firstRoom.centerX, firstRoom.centerY);

        // Connect rooms with corridors
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room currentRoom = rooms.get(i);
            Room nextRoom = rooms.get(i + 1);
            createCorridor(currentRoom.centerX, currentRoom.centerY, nextRoom.centerX, currentRoom.centerY);
            createCorridor(nextRoom.centerX, currentRoom.centerY, nextRoom.centerX, nextRoom.centerY);
        }

        // Ensure there is an exit on the right
        Room lastRoom = rooms.get(rooms.size() - 1);
        createCorridor(lastRoom.centerX, lastRoom.centerY, width - 1, lastRoom.centerY);

        // Set entrance and exit locations
        entranceLocation = new TilePoint(0, firstRoom.centerY);
        exitLocation = new TilePoint((width - 1), lastRoom.centerY);
    }

    public void renderMap(SpriteBatch batch) {
        for (int x = 0; x < map.length; x++) { // Use map length, not width/height in pixels
            for (int y = 0; y < map[x].length; y++) { // Use map[x] length, not width/height in pixels
                TileType type = map[x][y];
                Texture texture = type.getTexture();
                batch.draw(texture, x * this.tileSize, y * this.tileSize, this.tileSize, this.tileSize); // Draw each
                                                                                                         // tile
            }
        }
    }

    public TileType getTile(int x, int y) {
        return map[x / tileSize][y / tileSize];
    }

    public boolean isWall(int x, int y) {
        int tileX = (int) (x / tileSize);
        int tileY = (int) (x / tileSize);

        // Ensure that the tile coordinates stay within the array bounds
        tileX = MathUtils.clamp(tileX, 0, width - 1);
        tileY = MathUtils.clamp(tileY, 0, height - 1);
        return getTile(x, y) == TileType.WALL;
    }

    private void createRoom(int centerX, int centerY, int roomWidth, int roomHeight) {
        for (int x = centerX - roomWidth / 2; x < centerX + roomWidth / 2; x++) {
            for (int y = centerY - roomHeight / 2; y < centerY + roomHeight / 2; y++) {
                map[x][y] = TileType.FLOOR;
            }
        }
    }

    private void createCorridor(int startX, int startY, int endX, int endY) {
        // Horizontal corridor
        for (int x = Math.min(startX, endX); x <= Math.max(startX, endX); x++) {
            map[x][startY] = TileType.FLOOR;
        }

        // Vertical corridor
        for (int y = Math.min(startY, endY); y <= Math.max(startY, endY); y++) {
            map[startX][y] = TileType.FLOOR;
        }
    }

    // Additional methods for generating specific rooms, corridors, and features...
}
