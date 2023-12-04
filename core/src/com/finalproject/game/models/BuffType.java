package com.finalproject.game.models;

public enum BuffType {
    HEALTH("buffs/Health.png", "Increases health."),
    PLAYER_DAMAGE("buffs/Player_damage.png", "Increases player's damage."),
    MONSTER_DAMAGE("buffs/Monster_damage.png", "Decrease monster's damage."),
    DEFENSE("buffs/Defense.png", "Increases defense.");

    private final String iconPath;
    private final String description;

    BuffType(String iconPath, String description) {
        this.iconPath = iconPath;
        this.description = description;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getDescription() {
        return description;
    }
}
