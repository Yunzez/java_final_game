package com.finalproject.game.models;

import com.badlogic.gdx.graphics.Texture;

public enum Item {
    RELX_V5(ItemType.WEAPON, "RELX V5", 10,
            "A weapon that can relax you and your enenmy, enenmy attacks drop by 10 next 2 rounds.", "items/relx.png",
            new Buff("Relax", BuffType.MONSTER_DAMAGE, 10, 2)),
    SWORD_OF_TRUTH(ItemType.WEAPON, "Sword of Truth", 30,
            "A legendary blade that reveals the truth, cutting through deception. Give enenmy 30 damange",
            "items/sword.png", new Buff("Sword of Truth", BuffType.PLAYER_DAMAGE, 30, 1)),
    DRAGONSCALE_ARMOR(ItemType.ARMOR, "Dragonscale Armor", 20,
            "Near-impenetrable armor crafted from the scales of a dragon. Offers immense protection, plus 20 HP.",
            "items/armor.png", new Buff("Armor", BuffType.DEFENSE, 20, 1)),
    ELIXIR_OF_HEALTH(ItemType.POTION, "Elixir of Health", 20, "A rejuvenating potion that restores health of 20 hp.",
            "items/potion.png", new Buff("Elixir", BuffType.HEALTH, 20, 1)),
    BUTTBOOK(ItemType.WEAPON, "Buttbook", 40,
            "A weapon that can make opponent lost all their personal data, +20 damanges for next 2 rounds.",
            "items/buttbook.png",
            new Buff("Buttbook", BuffType.PLAYER_DAMAGE, 20, 2)),
    BANANA_CAT(ItemType.POTION, "Banana Cat", 50,
            "A potion that heal 50 points of your hp with the cuteness of banana cat.", "items/banana_cat.png",
            new Buff("Banana Cat", BuffType.HEALTH, 50, 1)),
    GIGITTY_GUN(ItemType.WEAPON, "Gigity Gun", 10,
            "A weapon that makes noise of gigity gigity goo, +10 damange for the next 2 rounds.", "items/Gigitty.png",
            new Buff("Gigitty Gun", BuffType.PLAYER_DAMAGE, 10, 2)),
            ;

    private final ItemType type;
    private final String name;
    private final int value;
    private final String description;
    private final String iconPath;
    private final Texture icon;
    private final int id;
    private static int idCounter = 0;
    private final Buff buff;

    private Item(ItemType type, String name, int value, String description, String iconPath, Buff buff) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.description = description;
        this.iconPath = iconPath;
        this.icon = new Texture(iconPath);
        this.id = generateId();
        this.buff = buff;
    }

    private static int generateId() {
        return idCounter++;
    }

    public ItemType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getIconPath() {
        return iconPath;
    }

    public Texture getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return name + " (" + type.toString() + "): " + value + " effects";
    }

    public Buff getBuff() {
        return buff;
    }

}
