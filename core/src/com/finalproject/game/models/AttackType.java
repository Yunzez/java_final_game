package com.finalproject.game.models;

public enum AttackType {
    SARCASM("Sarcasm", "Delivers a cutting remark that's hard to shake off."),
    ANGULAR("Angular", "Throws a complex framework at the enemy. Confusion ensues."),
    PUN_INTENDED("Pun Intended", "Hits with a pun so bad, it's good."),
    SPOONERISM("Spoonerism", "Swaps letters around to confusing and comedic effect."),
    DAD_JOKE("Dad Joke", "A joke so old and tired it loops back to being funny."),
    CAT_VIDEO("Cat Video", "Distracts with an endlessly looping feline mishap."),
    MEME_SLAP("Meme Slap", "The impact of a viral meme, both baffling and hilarious."),
    CRINGE("Cringe", "Makes everyone uncomfortable for a moment."),
    LITERALISM("Literalism", "Takes things too literally, leading to unexpected results."),
    KARAOKE("Karaoke", "Assaults the senses with off-key singing and dramatic gestures."),
    SPECIAL_RELX("Special", "A special attack that only RELX Ding can use."),
    SPECIAL_SUN("Special", "A special attack that only RELX Ding can use."),
    SPECIAL_ZUCK("Special", "A special attack that only Mark Zuck can use."),
    SPECIAL_BO("Special", "A special attack that only Bob can use."),
    SPECIAL_BEFF("Special", "A special attack that only Beff can use."),
    SPECIAL_JEZO("Special", "A special attack that only Jezo can use."),
    SPECIAL_KOUBEI("Special", "A special attack that only Koubei can use.");

    private final String name;
    private final String description;

    AttackType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return name;
    }
}
