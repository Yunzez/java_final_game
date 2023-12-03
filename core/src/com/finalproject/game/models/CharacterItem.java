package com.finalproject.game.models;

public class CharacterItem {

    private Item item;
    private int count;

    public CharacterItem(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incrementCount() {
        this.count++;
    }

    public void decrementCount() {
        if (this.count > 0) {
            this.count--;
        }
    }

    @Override
    public String toString() {
        return item.getName() + " x" + count;
    }

    public Buff useItem() {
        if (this.count > 0) {
            this.count--;
            return item.getBuff();
        }
        return null;
    }
}


