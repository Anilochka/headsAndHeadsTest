package game;

import game.entities.Creature;
import game.entities.Monster;
import game.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        List<Creature> creatures = new ArrayList<>();
        Creature playerOne = new Player(10, 8, 20, 1, 6);
        creatures.add(playerOne);
        Creature playerTwo = new Player(20, 3, 12, 10, 25);
        creatures.add(playerTwo);
        Creature playerThree = new Player(4, 29, 40, 1, 30);
        creatures.add(playerThree);

        Creature monsterOne = new Monster(10, 8, 20, 1, 6);
        creatures.add(monsterOne);
        Creature monsterTwo = new Monster(20, 3, 12, 10, 25);
        creatures.add(monsterTwo);
        Creature monsterThree = new Monster(4, 29, 40, 1, 30);
        creatures.add(monsterThree);

        while (creatures.size() > 1) {
            Creature randomCreature = getRandomCreature(creatures);
            Creature randomCreatureToAttack = getRandomCreatureExceptingOne(creatures, randomCreature);
            randomCreature.doAttack(randomCreatureToAttack);
            if (!randomCreatureToAttack.isAlive()) {
                creatures.remove(randomCreatureToAttack);
            }
        }

        System.out.println("GAME OVER! THE WINNER IS " + creatures.get(0).getMainInfo());
    }

    private static Creature getRandomCreatureExceptingOne(List<Creature> creatures, Creature creatureToExcept) {
        Creature creature = null;
        while (creature == null) {
            Creature tmpCreature = getRandomCreature(creatures);
            if (!tmpCreature.equals(creatureToExcept)) {
                creature = tmpCreature;
            }
        }
        return creature;
    }

    private static Creature getRandomCreature(List<Creature> creatures) {
        return creatures.get(new Random().nextInt(creatures.size()));
    }
}