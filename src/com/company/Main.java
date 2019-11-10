package com.company;

import java.util.Random;

public class Main {

    public static int bossHealth = 2000;
    public static int bossAttack = 50;
    public static String bossDefenceType = "";

    public static int[] heroesHealths = {250, 250, 250, 250, 500, 250, 250,250};
    public static int[] heroesAttacks = {10, 20, 30, 0, 10, 10, 10,0};
    public static String[] heroesAttackTypes = {"Physical", "Magical", "Kinetic", "Medical", "Tank", "Dodger", "Berserk", "Torus"};
    public static int luckHero;
    public static boolean canBossHit=true; // Torus can make Boss cant hit for one round

    public static void main(String[] args) {
        // write your code here
        printStatistics();
        while (!isFinished()) {
            round();
        }
    }

    public static void round() {
        changeBossDefence();
        changeLuckHero();
        heroesHit();
        if (bossHealth > 0) {
            if(canBossHit) {
                bossHit();
            }
        }
        if (heroesHealths[3] > 0) { // if medical hero is alive
            medicalHelpToHeroes();
        }
        printStatistics();
    }

    public static void medicalHelpToHeroes() {
        for (int i = 0; i < heroesAttackTypes.length; i++) {
            if (heroesHealths[i] > 0) {
                int medicalHealth = new Random().nextInt(5); // random health increase
                heroesHealths[i] = heroesHealths[i] + medicalHealth;
            }
        }
    }

    public static void printStatistics() {
        System.out.println("______________");
        System.out.println("Boss health:" + bossHealth);

        for (int i = 0; i < heroesAttackTypes.length; i++) {
            System.out.println(heroesAttackTypes[i] + " health:" + heroesHealths[i]);
        }
        System.out.println("______________");
    }

    public static void changeBossDefence() {
        int randomIndex = new Random().nextInt(heroesAttackTypes.length);
        bossDefenceType = heroesAttackTypes[randomIndex];
        if (bossDefenceType.equals("Medical")) {
            changeBossDefence();
        } else {
            System.out.println("Boss choosed: " + bossDefenceType);
        }
    }

    public static void changeLuckHero() {
        luckHero = new Random().nextInt(heroesAttackTypes.length);
        String luckHeroType = heroesAttackTypes[luckHero];

        while((luckHeroType.equals(bossDefenceType) || heroesAttackTypes[luckHero].equals("Medical"))) {
            luckHero = new Random().nextInt(heroesAttackTypes.length);
            luckHeroType = heroesAttackTypes[luckHero];
        }
        System.out.println("Luck hero: " + luckHeroType);
    }

    public static boolean isFinished() {

        if (bossHealth <= 0) {
            System.out.println("Heroes won!");
            return true;
        }

        boolean finished = true;

        for (int i = 0; i < heroesHealths.length; i++) {
            if (heroesHealths[i] > 0) {
                finished = false;
            }
        }
        if (finished) {
            System.out.println("Boss won!");
            return true;
        }
        return false;
    }

    public static void bossHit() {
        for (int i = 0; i < heroesHealths.length; i++) {
            if (heroesHealths[i] > 0) {
                if (heroesAttackTypes[i].equals("Dodger")) //ловкача, имеет шанс уклонения от ударов босса
                {
                    boolean chance = new Random().nextBoolean();
                    if (chance) {
                        break;
                    }
                } else if (heroesAttackTypes[i].equals("Berserk")) //берсерк, блокирует часть удара босса по себе и прибавляет заблокированный урон к своему урону и возвращает боссу
                {
                    int attackPart = new Random().nextInt(100);
                    heroesHealths[i] = heroesHealths[i] - bossAttack * attackPart / 100;
                    bossHealth = bossHealth - (bossAttack * (100 - attackPart) / 100 + heroesAttacks[i]);
                }
                if (heroesHealths[4] > 0) {
                    int chanceAttack = new Random().nextInt(100);
                    heroesHealths[i] = heroesHealths[i] - bossAttack * chanceAttack / 100;
                    heroesHealths[4] = heroesHealths[4] - bossAttack * (100 - chanceAttack) / 100;
                } else {
                    heroesHealths[i] = heroesHealths[i] - bossAttack;
                }
            }
            if (heroesHealths[i] < 0) {
                heroesHealths[i] = 0;
            }
        }
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesAttacks.length; i++) {
            if (heroesHealths[i] > 0) {

                if(heroesAttackTypes[i].equals("Torus")) //тор, удар по боссу имеет шанс оглушить босса на раунд, вследствие чего босс пропускает раунд и не бьёт
                {
                    if(canBossHit) {
                        canBossHit = new Random().nextBoolean();
                    }
                    else
                    {
                        canBossHit=true;
                    }
                    continue;
                }

                if (!bossDefenceType.equals(heroesAttackTypes[i])) {
                    if (luckHero == i) {
                        int coefficient = new Random().nextInt(9) + 2;
                        System.out.println(heroesAttackTypes[i] + " hits " + heroesAttacks[i] * coefficient);
                        bossHealth = bossHealth - heroesAttacks[i] * coefficient;
                    } else {
                        bossHealth = bossHealth - heroesAttacks[i];
                    }
                    if (bossHealth < 0) {
                        bossHealth = 0;
                        break;
                    }
                }
            }
        }
    }

}
