package com.finalproject.game.models;


import com.badlogic.gdx.graphics.Texture;

public enum Item {
    RELX_V5(ItemType.WEAPON, "Relx V5", 10, "A weapon that can relax you and your enenmy.","icons/relx.png"),
    SWORD_OF_TRUTH(ItemType.WEAPON, "Sword of Truth", 30, "A legendary blade that reveals the truth, cutting through deception.","icons/sword.png"),
    DRAGONSCALE_ARMOR(ItemType.ARMOR, "Dragonscale Armor", 70, "Near-impenetrable armor crafted from the scales of a dragon. Offers immense protection.","icons/dragon.png"),
    ELIXIR_OF_HEALTH(ItemType.POTION, "Elixir of Health", 20, "A rejuvenating potion that restores health over time.","icons/elixir.png"),

    ;

    private final ItemType type;
    private final String name;
    private final int value;
    private final String description;
    private final String iconPath;
    private final Texture icon;

    private Item(ItemType type, String name, int value, String description, String iconPath) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.description = description;
        this.iconPath = iconPath;
        this.icon = new Texture(iconPath);
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

    public String toString() {
        return name;
    }

}
