package com.example.reallifeproject.model;

public class InventoryModel {
    private String sword;
    private String shield;
    private String magicWand;

    public InventoryModel() {
    }

    public InventoryModel(String sword, String shield, String magicWand) {
        this.sword = sword;
        this.shield = shield;
        this.magicWand = magicWand;
    }

    public String getSword() {
        return sword;
    }

    public void setSword(String sword) {
        this.sword = sword;
    }

    public String getShield() {
        return shield;
    }

    public void setShield(String shield) {
        this.shield = shield;
    }

    public String getMagicWand() {
        return magicWand;
    }

    public void setMagicWand(String magicWand) {
        this.magicWand = magicWand;
    }
}
