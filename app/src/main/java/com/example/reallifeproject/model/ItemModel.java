package com.example.reallifeproject.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemModel implements Parcelable {
    private int id;
    private String name;
    private int money;
    private int attack;
    private int defense;
    private int magic;
    private int resistance;
    private int agility;
    private String asked;
    private String describe;
    private String gender;
    private boolean isBuy;
    private String type;
    private String kind;
    private String common;
    private int imageId;

    public ItemModel() {
    }

    public ItemModel(int id, String name, int money, int attack, int defense, int magic, int resistance, int agility, String asked, String describe, String gender, boolean isBuy, String type, String kind, String common, int imageId) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.attack = attack;
        this.defense = defense;
        this.magic = magic;
        this.resistance = resistance;
        this.agility = agility;
        this.asked = asked;
        this.describe = describe;
        this.gender = gender;
        this.isBuy = isBuy;
        this.type = type;
        this.kind = kind;
        this.common = common;
        this.imageId = imageId;
    }

    protected ItemModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        money = in.readInt();
        attack = in.readInt();
        defense = in.readInt();
        magic = in.readInt();
        resistance = in.readInt();
        agility = in.readInt();
        asked = in.readString();
        describe = in.readString();
        gender = in.readString();
        isBuy = in.readByte() != 0;
        type = in.readString();
        kind = in.readString();
        common = in.readString();
        imageId = in.readInt();
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public String getAsked() {
        return asked;
    }

    public void setAsked(String asked) {
        this.asked = asked;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(money);
        dest.writeInt(attack);
        dest.writeInt(defense);
        dest.writeInt(magic);
        dest.writeInt(resistance);
        dest.writeInt(agility);
        dest.writeString(asked);
        dest.writeString(describe);
        dest.writeString(gender);
        dest.writeByte((byte) (isBuy ? 1 : 0));
        dest.writeString(type);
        dest.writeString(kind);
        dest.writeString(common);
        dest.writeInt(imageId);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
