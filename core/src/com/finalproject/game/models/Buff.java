package com.finalproject.game.models;



public class Buff {
    public enum BuffType {
        ATTACK,
        DEFENCE,
        HEALTH
    }

    private String buffName;
    private String buffDescription;
    private boolean isDebuff;
    private BuffType buffType;
    private int amount;  // could be positive or negative

    public Buff(String buffName, String buffDescription, boolean isDebuff, BuffType buffType, int amount) {
        this.buffName = buffName;
        this.buffDescription = buffDescription;
        this.isDebuff = isDebuff;
        this.buffType = buffType;
        this.amount = amount;
    }

    // create all setters and getters
    public String getBuffName() {
        return buffName;
    }

    public String getBuffDescription() {
        return buffDescription;
    }

    public boolean isDebuff() {
        return isDebuff;
    }

    public BuffType getBuffType() {
        return buffType;
    }

    public int getAmount() {
        return amount;
    }

    public void setBuffName(String buffName) {
        this.buffName = buffName;
    }

    public void setBuffDescription(String buffDescription) {
        this.buffDescription = buffDescription;
    }

    public void setIsDebuff(boolean isDebuff) {
        this.isDebuff = isDebuff;
    }

    public void setBuffType(BuffType buffType) {
        this.buffType = buffType;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void applyBuff(GameCharacter character) {
        switch (buffType) {
            case ATTACK:
                character.setAttack(character.getAttack() + amount);
                break;
            case DEFENCE:
                character.setDefence(character.getDefence() + amount);
                break;
            case HEALTH:
                character.setHealth(character.getHealth() + amount);
                break;
        }
    }
}
