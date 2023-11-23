package com.finalproject.game.models;

public class UserGameCharacters extends GameCharacter{
    private int monsterKilled;
    private int points;
    private String savingName;
    // Constructor
    public UserGameCharacters() {
        super();
        // Initialize fields if necessary
        this.monsterKilled = 0;
        this.points = 0;

    }

    // Additional constructor to initialize all fields
    public UserGameCharacters(int health, int strength, int defense, int speed, int level, String name, String imagePath, int monsterKilled, int points, String savingName) {
        super(health, strength, defense, speed, level, name, imagePath); // Assuming these are the fields of GameCharacter
        this.monsterKilled = monsterKilled;
        this.points = points;
        this.savingName = savingName;
    }

    // Getters and setters for monsterKilled
    public int getMonsterKilled() {
        return monsterKilled;
    }

    public void setMonsterKilled(int monsterKilled) {
        this.monsterKilled = monsterKilled;
    }

    // Getters and setters for points
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getSavingName() {
        return savingName;
    }

    public void setSavingName(String savingName) {
        this.savingName = savingName;
    }

}   
