package com.finalproject.game.models;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;

public enum Attack {
    BACKHANDED_COMPLIMENT("Backhanded Compliment", AttackType.SARCASM, 15, "A compliment that's actually an insult. Leaves the target questioning their self-worth."),
    SCOPE_CREEP("Scope Creep", AttackType.ANGULAR, 20, "Slowly increases the complexity until the target is overwhelmed."),
    PUNISHMENT("Punishment", AttackType.PUN_INTENDED, 10, "A pun so terrible it inflicts pain."),
    MIXED_WORDS("Mixed Words", AttackType.SPOONERISM, 5, "Mixes up the target's words, causing confusion."),
    GROANER("Groaner", AttackType.DAD_JOKE, 8, "Makes the target groan and roll their eyes, slightly damaging their morale."),
    DISTRACTACAT("Distractacat", AttackType.CAT_VIDEO, 0, "So cute, the enemy forgets to attack."),
    PEPE_SLAM("Pepe Slam", AttackType.MEME_SLAP, 25, "Hits with the force of a thousand internet trolls."),
    AWKWARD_PAUSE("Awkward Pause", AttackType.CRINGE, 5, "Creates an awkward silence, stunning the enemy briefly."),
    LITERAL_LAUNCH("Literal Launch", AttackType.LITERALISM, 15, "Launches the target, because they asked for 'a lift'."),
    OFF_KEY("Off Key", AttackType.KARAOKE, 10, "Blasts a note so off-key, it disrupts the enemy's focus."),

    // Special attack
    
    POWER_OF_LITANG("Litang Power", AttackType.SPECIAL_RELX, 30, "Bring out the power of Litang! Ding's special attack! "),
    
    PRIVACY_INVASION("Privacy Invasion", AttackType.SPECIAL_ZUCK, 33, "Use collected data on ButtBook to invade the enemy's privacy!, Zuck's special attack!"),
    
    SPACE_WINER("Space Winer", AttackType.SPECIAL_JEZO, 35, "Use the Space Origin Rocket that looks like a sausage to hit the enemy!"),
    
    NO_TIME_TO_PEE("No Time To Pee", AttackType.SPECIAL_BEFF, 35, "Use the super short break interval to make the enemy have no time to pee!"),
    
    HELICOPTER("Helicopter", AttackType.SPECIAL_KOUBEI, 35, "Use the helicopter to hit the enemy!"),

    PORRIGE_PUNCH("Porridge Punch", AttackType.SPECIAL_SUN, 35, "Use the porridge to hit the enemy!");

    private final String name;
    private final AttackType type;
    private final int harm;
    private final String description;

    Attack(String name, AttackType type, int harm, String description) {
        this.name = name;
        this.type = type;
        this.harm = harm;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public AttackType getType() {
        return type;
    }

    public int getHarm() {
        return harm;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return name + " (" + type.toString() + "): " + harm + " damage";
    }

     // Method to get attacks by type
    public static ArrayList<Attack> getAttacksByType(AttackType type) {
        ArrayList<Attack> attacksOfType = new ArrayList<Attack>();
        for (Attack attack : Attack.values()) {
            if (attack.getType() == type) {
                System.out.println("add attack "+ attack.getType().name());
                attacksOfType.add(attack);
            }
        }
        return attacksOfType;
    }

    // Method to get attacks excluding types starting with SPECIAL
    public static ArrayList<Attack> getAttacksExcludingSpecial() {
        ArrayList<Attack> nonSpecialAttacks = new ArrayList<>();
        for (Attack attack : Attack.values()) {
            if (!attack.getType().name().startsWith("SPECIAL")) {
                System.out.println(attack.getType().name() + " add to non special");
                nonSpecialAttacks.add(attack);
            }
        }
        return nonSpecialAttacks;
    }
}
