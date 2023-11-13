package com.finalproject.game.models;

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
    OFF_KEY("Off Key", AttackType.KARAOKE, 10, "Blasts a note so off-key, it disrupts the enemy's focus.");

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
}