package com.finalproject.game.models;

public class ScoreBoardEntry {
    String id;
    String savingName;
    int health;
    int strength;
    int defense;
    int speed;
    int level;
    String name;
    String imagePath;
    int monsterKilled;
    int points;
    String userId;

    public ScoreBoardEntry() {
        this.name = "";
        this.monsterKilled = 0;
        this.points = 0;
        this.defense = 0;
        this.health = 0;
        this.strength = 0;
        this.speed = 0;
        this.level = 0;
        this.imagePath = "";
        this.savingName = "";
        this.id = "";
        this.userId = "";

    }

    public ScoreBoardEntry(String id, String savingName, int health, int strength, int defense,
            int speed,
            int level,
            String name,
            String imagePath,
            int monsterKilled,
            int points,
            String userId) {
        this.name = name;
        this.monsterKilled = monsterKilled;
        this.points = points;
        this.defense = defense;
        this.health = health;
        this.strength = strength;
        this.speed = speed;
        this.level = level;
        this.imagePath = imagePath;
        this.savingName = savingName;
        this.id = id;
        this.userId = userId;

    }

    // Getters
    public String getName() {
        return name;
    }

    public int getMonsterKilled() {
        return monsterKilled;
    }

    public int getPoints() {
        return points;
    }

    public String getId() {
        return id;
    }

    public String getSavingName() {
        return savingName;
    }

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return strength;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getLevel() {
        return level;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getUserId() {
        return userId;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setMonsterKilled(int monsterKilled) {
        this.monsterKilled = monsterKilled;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSavingName(String savingName) {
        this.savingName = savingName;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}