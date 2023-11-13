package com.finalproject.game.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.utils.SortedIntList.Iterator;

import java.util.Collections;

public class AttackUtils {

    private static final Random random = new Random();

    public static ArrayList<Attack> generateRandomAttacks(int numberOfAttacks, String name) {
        ArrayList<Attack> availableAttacks = new ArrayList<>();
        Collections.addAll(availableAttacks, Attack.values());
        ArrayList<Attack> assignedAttacks = new ArrayList<>();

        // Filter out the SPECIAL attack if the character is not "Relx Ding"
        
        for (int i = 0; i< availableAttacks.size(); i++) {
            Attack attack = availableAttacks.get(i);
            if (attack.getType() == AttackType.SPECIAL) {
                availableAttacks.remove(i);
            }
        }
        // Generate the random attacks
        for (int i = 0; i < numberOfAttacks; i++) {
            if (!availableAttacks.isEmpty()) {
                int randomIndex = random.nextInt(availableAttacks.size());
                assignedAttacks.add(availableAttacks.remove(randomIndex));
            }
        }

        // add a switch statement to add the special attack
        switch (name) {
            case "Relx Ding":
                assignedAttacks.add(Attack.POWER_OF_LITANG);
                break;

            default:
                break;
        }

        return assignedAttacks;
    }

}
