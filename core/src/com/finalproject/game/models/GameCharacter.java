package com.finalproject.game.models;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

public class GameCharacter {
    private int health;
    private int attack;
    private int defence;
    private int speed;
    private int level;
    private String name;
    private int experience = 0;
    private ArrayList<Item> inventory = new ArrayList<Item>();
    private ArrayList<Buff> buffs = new ArrayList<Buff>();
    private Texture characterCard;
    private String characterCardPath;
    private int id;
    private static int autoId = 0;

    public GameCharacter(int id, int health, int attack, int defence, int speed, int level, String name,
            String characterCardPath) {
        this.id = id;
        this.health = health;
        this.attack = attack;
        this.defence = defence;
        this.speed = speed;
        this.level = level;
        this.name = name;
        this.characterCardPath = characterCardPath;
        this.characterCard = new Texture(characterCardPath);
    }

    public GameCharacter(int health, int attack, int defence, int speed, int level, String name,
            String characterCardPath) {
        // Add validation here if needed
        this.id = autoId++;
        this.health = health;
        this.attack = attack;
        this.defence = defence;
        this.speed = speed;
        this.level = level;
        this.name = name;
        this.characterCardPath = characterCardPath;
        this.characterCard = new Texture(characterCardPath);
    }

    public GameCharacter() {
        // Default values
        this(100, 10, 10, 10, 1, "Unknown Character", "charactors/xiaochuan.png");
    }

    public void levelUp() {
        this.level++;
        setAttack(attack + 5);
        setDefence(defence + 5);
        setHealth(health + 10);
    }

    public void addExperience(int experience) {
        this.experience += experience;
        if (this.experience >= 100) {
            levelUp();
            this.experience = 0;
        }
    }

    public int getId() {
        return id;
    }

    // create all setters and getters
    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }

    public int getSpeed() {
        return speed;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public Texture getImageTexture() {
        return characterCard;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }
}
