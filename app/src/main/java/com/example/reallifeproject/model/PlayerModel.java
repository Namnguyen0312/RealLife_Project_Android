package com.example.reallifeproject.model;

public class PlayerModel {
    private String gender;
    private String scene;
    private String playerId;
    private int money;
    private int day;
    private String event;
    private int heart;
    private int stress;
    private int strength;
    private int smart;
    private int attack;
    private int magic;
    private int defense;
    private int resistance;
    private int agility;
    private boolean isDead;


    public PlayerModel() {
    }

    public PlayerModel(String gender, String scene, String playerId, int money, int day, String event, int heart, int stress, int strength, int smart, int attack, int magic, int defense, int resistance, int agility, boolean isDead) {
        this.gender = gender;
        this.scene = scene;
        this.playerId = playerId;
        this.money = money;
        this.day = day;
        this.event = event;
        this.heart = heart;
        this.stress = stress;
        this.strength = strength;
        this.smart = smart;
        this.attack = attack;
        this.magic = magic;
        this.defense = defense;
        this.resistance = resistance;
        this.agility = agility;
        this.isDead = isDead;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getStress() {
        return stress;
    }

    public void setStress(int stress) {
        this.stress = stress;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getSmart() {
        return smart;
    }

    public void setSmart(int smart) {
        this.smart = smart;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }
}
