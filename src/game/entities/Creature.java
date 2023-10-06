package game.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public abstract class Creature {
    private static final Logger LOGGER = LoggerFactory.getLogger(Creature.class);
    private static final int POSITIVE_NUMBERS_BOUNDARY = 0;
    private static final int MIN_ATTACK_DEFENSE_VALUE = 1;
    private static final int MAX_ATTACK_DEFENSE_VALUE = 30;
    private static final int MAX_HEALING_COUNT = 4;
    private static final double HEALING_FACTOR = 0.3;
    private static final String INCORRECT_CONSTRUCTOR_PARAMETER_MSG = "Incorrect constructor parameter.";
    private static final String ILLEGAL_CREATURE_MSG = "You can't attack this creature: {}";
    private static final int DICE_BOUND = 7;
    private static final List<Integer> SUCCESS_DICE_NUMBERS = List.of(5, 6);
    private static final String ATTACK_WAS_NOT_SUCCESSFUL_MSG = "Attack of {} on the {} was not successful.";
    private static final String ATTACK_WAS_SUCCESSFUL_MSG = "Attack of {} on the {} was successful! Damage: {}.";
    private static final String DEATH_AFTER_ATTACK_MSG = "%s was attacked and has died.";
    private static final String HEALING_MSG = "%s healed himself from %d to %d.";
    private static final int MIN_ATTACK_MODIFICATOR_VALUE = 1;
    public static final String LOGGER_EMPTY_MSG = "";
    private final int attack;
    private final int defense;
    private int health;
    private final int maxHealth;
    private int healingCount;
    private final int damageMin;
    private final int damageMax;
    private boolean isAlive;

    public Creature(int attack, int defense, int health, int damageMin, int damageMax) {
        if (attack < MIN_ATTACK_DEFENSE_VALUE || attack > MAX_ATTACK_DEFENSE_VALUE
                || defense < MIN_ATTACK_DEFENSE_VALUE || defense > MAX_ATTACK_DEFENSE_VALUE
                || health < POSITIVE_NUMBERS_BOUNDARY || damageMin < POSITIVE_NUMBERS_BOUNDARY
                || damageMax < POSITIVE_NUMBERS_BOUNDARY || damageMax <= damageMin) {
            throw new IllegalArgumentException(INCORRECT_CONSTRUCTOR_PARAMETER_MSG);
        }
        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.maxHealth = health;
        this.healingCount = MAX_HEALING_COUNT;
        this.damageMin = damageMin;
        this.damageMax = damageMax;
        this.isAlive = true;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public String getMainInfo() {
        return this + "{" +
                "attack=" + attack +
                ", defense=" + defense +
                ", health=" + health +
                ", healingCount=" + healingCount +
                ", damageMin=" + damageMin +
                ", damageMax=" + damageMax +
                '}';
    }

    public void doAttack(Creature creature) {
        if (creature == null || !creature.isAlive || !this.isAlive) {
            throw new IllegalArgumentException(ILLEGAL_CREATURE_MSG + creature);
        }
        int attackModificator = Math.max(attack - creature.defense + MIN_ATTACK_DEFENSE_VALUE,
                MIN_ATTACK_MODIFICATOR_VALUE);
        boolean isSuccessful = false;
        for (int i = POSITIVE_NUMBERS_BOUNDARY; i < attackModificator; i++) {
            int res = new Random().nextInt(DICE_BOUND);
            if (SUCCESS_DICE_NUMBERS.contains(res)) {
                isSuccessful = true;
                break;
            }
        }
        if (!isSuccessful) {
            LOGGER.info(ATTACK_WAS_NOT_SUCCESSFUL_MSG, this, creature);
            return;
        }
        int tmpDamage = new Random().nextInt(damageMin, damageMax);
        String loggerMsg = creature.getAttacked(tmpDamage);
        LOGGER.info(ATTACK_WAS_SUCCESSFUL_MSG, this, creature, tmpDamage);
        if (!loggerMsg.equals(LOGGER_EMPTY_MSG)) {
            LOGGER.info(loggerMsg);
        }
    }

    private String getAttacked(int damage) {
        StringBuilder loggerMsg = new StringBuilder();
        health -= damage;
        if (health <= POSITIVE_NUMBERS_BOUNDARY) {
            isAlive = false;
            return String.format(DEATH_AFTER_ATTACK_MSG, this);
        }
        while (health < maxHealth && healingCount > POSITIVE_NUMBERS_BOUNDARY) {
            int tmpHealth = health;
            heal();
            loggerMsg.append(String.format(HEALING_MSG, this, tmpHealth, health));
        }
        return loggerMsg.toString();
    }

    private void heal() {
        health = (int) Math.min(health + maxHealth * HEALING_FACTOR, maxHealth);
        healingCount--;
    }
}
