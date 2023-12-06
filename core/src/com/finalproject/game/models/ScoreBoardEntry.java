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
}