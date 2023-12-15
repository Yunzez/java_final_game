package com.finalproject.game.models;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

public class GameCharacter {
    private int maxHealth;
    private int health;
    private int attack;
    private int defense;
    private int speed;
    private int level;
    private String name;
    private int experience = 0;
    private ArrayList<CharacterItem> inventory = new ArrayList<CharacterItem>();
    private Texture characterCard;
    private String characterCardPath;
    private String characterLostCardPath;
    private Texture characterLostCard;
    private int id;
    private static int autoId = 0;
    private String description = "This is a character";

    private ArrayList<Attack> attacks;

    // These attributes are for record
    private int monsterKilled;
    private int points;
    //

    public GameCharacter(int id, int health, int strength, int defense, int speed, int level, int monsterKilled,
            String name, String characterCardPath) {
        this.id = id;
        this.health = health;
        this.maxHealth = health;
        this.attack = strength;
        this.defense = defense;
        this.speed = speed;
        this.level = level;
        //
        this.monsterKilled = monsterKilled;
        this.points = monsterKilled * 20 + (level - 1) * 100;
        //
        this.name = name;
        this.characterCardPath = characterCardPath;
        this.characterCard = new Texture(characterCardPath);
        this.inventory = new ArrayList<CharacterItem>();
        initializeInventory();
    }

    public GameCharacter(int health, int strength, int defense, int speed, int level, int monsterKilled,
            String name, String characterCardPath, String characterLostCardPath) {
        // Add validation here if needed
        this.id = autoId++;
        this.health = health;
        this.maxHealth = health;
        this.attack = strength;
        this.defense = defense;
        this.speed = speed;
        this.level = level;
        //
        this.monsterKilled = monsterKilled;
        this.points = monsterKilled * 20 + (level - 1) * 100;
        //
        this.name = name;
        this.characterCardPath = characterCardPath;
        this.characterCard = new Texture(characterCardPath);

        this.characterLostCardPath = characterLostCardPath;
        this.characterLostCard = new Texture(characterLostCardPath);
        this.inventory = new ArrayList<CharacterItem>();
        initializeInventory();
    }

    public GameCharacter() {
        // Default values
        this(100, 10, 10, 10, 1, 0, "Unknown Character", "charactors/xiaochuan.png", "charactors/xiaochuanLost.png");
    }

    public void initializeInventory() {
        inventory.add(new CharacterItem(Item.RELX_V5, 0));
        inventory.add(new CharacterItem(Item.SWORD_OF_TRUTH, 0));
        inventory.add(new CharacterItem(Item.DRAGONSCALE_ARMOR, 0));
        inventory.add(new CharacterItem(Item.ELIXIR_OF_HEALTH, 2));
        inventory.add(new CharacterItem(Item.BUTTBOOK, 0));
        inventory.add(new CharacterItem(Item.GIGITTY_GUN, 0));
        inventory.add(new CharacterItem(Item.BANANA_CAT, 0));
    }

    public void levelUp() {
        this.level++;
        setAttack(attack + 5);
        setDefense(defense + 5);
        setMaxHealth(maxHealth + 10);
        setHealth(maxHealth);
        System.out.println("Level up!");
        System.out.println("Before points" + this.points);
        setPoints(this.points + 100);
        System.out.println("After points" + this.points);
    }

    public void addMonsterKilled() {
        this.monsterKilled++;
        System.out.println("Monster killed!");
        System.out.println("Before points" + this.points);
        setPoints(points + 20);
        System.out.println("After points" + this.points);
    }

    public void setImagePath(String imagePath) {
        this.characterCardPath = imagePath;
        this.characterCard = new Texture(imagePath);
    }

    public String getImagePath() {
        return characterCardPath;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
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
    public int getCurrentHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttack() {
        return attack;
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

    public String getName() {
        return name;
    }

    public ArrayList<Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(ArrayList<Attack> attacks) {
        this.attacks = attacks;
    }

    public Attack getRandomAttack() {
        return attacks.get((int) (Math.random() * attacks.size()));
    }

    public void assignRandomAttacks(int numberOfAttacks) {

        this.attacks = new ArrayList<>(AttackUtils.generateRandomAttacks(numberOfAttacks, this.name));

    }

    public Texture getImageTexture() {
        return characterCard;
    }

    public Texture getLostImageTexture() {
        return characterLostCard;
    }

    public void setHealth(int health) {
        if (health < 0) {
            this.health = 0;
        } else {
            this.health = health;
        }
    }

    public void setAttack(int attack) {
        this.attack = attack;
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

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CharacterItem> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<CharacterItem> inventory) {
        this.inventory = inventory;
    }

    public void addInventory(Item item, int count) {

        inventory.forEach((characterItem) -> {
            if (characterItem.getItem() == item) {
                characterItem.setCount(characterItem.getCount() + count);
            }
        });
    }

    public void removeInventory(CharacterItem item, int count) {
        inventory.forEach((characterItem) -> {
            if (characterItem.getItem() == item.getItem()) {
                characterItem.setCount(characterItem.getCount() - count == 0 ? 0 : characterItem.getCount() - count);
            }
        });

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // retunr the character as a new instance
    public GameCharacter clone() {
        GameCharacter clone = new GameCharacter(this.health, this.attack, this.defense, this.speed, this.level,
                this.monsterKilled,
                this.name, this.characterCardPath, this.characterLostCardPath);
        clone.attacks = this.attacks;
        return clone;
    }

    // These are for record
    public int getMonsterKilled() {
        return monsterKilled;
    }

    public void setMonsterKilled(int monsterKilled) {
        this.monsterKilled = monsterKilled;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
