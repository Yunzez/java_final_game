package com.finalproject.game.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.Collections;

public class AttackUtils {

    private static final Random random = new Random();

    public static ArrayList<Attack> generateRandomAttacks(int numberOfAttacks) {
        ArrayList<Attack> availableAttacks = new ArrayList<>();
        Collections.addAll(availableAttacks, Attack.values());
        ArrayList<Attack> assignedAttacks = new ArrayList<>();

        for (int i = 0; i < numberOfAttacks; i++) {
            if (!availableAttacks.isEmpty()) {
                int randomIndex = random.nextInt(availableAttacks.size());
                assignedAttacks.add(availableAttacks.remove(randomIndex));
            }
        }

        return assignedAttacks;
    }


}
