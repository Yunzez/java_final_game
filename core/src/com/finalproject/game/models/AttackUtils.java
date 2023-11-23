package com.finalproject.game.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.utils.SortedIntList.Iterator;

import java.util.Collections;

public class AttackUtils {

    private static final Random random = new Random();

    public static ArrayList<Attack> generateRandomAttacks(int numberOfAttacks, String name) {
        ArrayList<Attack> availableAttacks = Attack.getAttacksExcludingSpecial();
        Collections.addAll(availableAttacks, Attack.values());
        ArrayList<Attack> assignedAttacks = new ArrayList<>();

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
                assignedAttacks.add(Attack.getAttacksByType(AttackType.SPECIAL_RELX).get(0));
                break;

            case "Mark Zucks":
                assignedAttacks.add(Attack.getAttacksByType(AttackType.SPECIAL_ZUCK).get(0));
                break;
            
            case "Mikado Sun":
                assignedAttacks.add(Attack.getAttacksByType(AttackType.SPECIAL_SUN).get(0));
                break;
            
            case "Beff Jezos":
                assignedAttacks.add(Attack.getAttacksByType(AttackType.SPECIAL_BEFF).get(0));
                break;
            
            case "Jezos Beffs": 
                assignedAttacks.add(Attack.getAttacksByType(AttackType.SPECIAL_JEZO).get(0));
                break;
            default:
                break;
        }

        return assignedAttacks;
    }

}
