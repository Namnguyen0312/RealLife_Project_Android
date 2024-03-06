package com.example.reallifeproject.model;

public class PlayerModel {
    private String gender;
    private String scene;
    private String playerId;
    private int money;
    private int age;
    private String event;
    private int heart;
    private int stress;
    private int strength;
    private int smart;


    public PlayerModel() {
    }

    public PlayerModel(String gender, String scene, String playerId, int money, int age, String event, int heart, int stress, int strength, int smart) {
        this.gender = gender;
        this.scene = scene;
        this.playerId = playerId;
        this.money = money;
        this.age = age;
        this.event = event;
        this.heart = heart;
        this.stress = stress;
        this.strength = strength;
        this.smart = smart;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

}
