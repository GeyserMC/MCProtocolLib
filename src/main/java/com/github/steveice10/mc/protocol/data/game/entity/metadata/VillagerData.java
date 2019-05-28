package com.github.steveice10.mc.protocol.data.game.entity.metadata;

public class VillagerData {

    private int villagerType;
    private int villagerProfession;
    private int level;

    public VillagerData() {
    }

    public VillagerData(int villagerType, int villagerProfession, int level) {
        this.villagerType = villagerType;
        this.villagerProfession = villagerProfession;
        this.level = level;
    }

    public int getVillagerType() {
        return villagerType;
    }

    public void setVillagerType(int villagerType) {
        this.villagerType = villagerType;
    }

    public int getVillagerProfession() {
        return villagerProfession;
    }

    public void setVillagerProfession(int villagerProfession) {
        this.villagerProfession = villagerProfession;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
