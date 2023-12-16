package com.finalproject.game.models;

public class Buff {

        private String name;
        private BuffType type;
        private int magnitude;
        private float duration; // Use a negative value or a specific flag for permanent effects
    
        public Buff(String name, BuffType type, int magnitude, float duration) {
            this.name = name;
            this.type = type;
            this.magnitude = magnitude;
            this.duration = duration;
        }
    
        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BuffType getType() {
            return type;
        }

        public String getTypeIconPath(){
            return type.getIconPath();
        }

        public void setType(BuffType type) {
            this.type = type;
        }

        public int getMagnitude() {
            return magnitude;
        }

        public void setMagnitude(int magnitude) {
            this.magnitude = magnitude;
        }

        public float getDuration() {
            return duration;
        }

        public void setDuration(float duration) {
            this.duration = duration;
        }

        public String toString() {
            return name + " (" + type.toString() + "): " + magnitude + " for " + duration + " rounds";
        }
    
        // Additional methods to handle the buff logic
    
}
