package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class VillagerData {
    private int type;
    private int profession;
    private int level;

    public VillagerData(int type, int profession, int level) {
        this.type = type;
        this.profession = profession;
        this.level = level;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getProfession() {
        return profession;
    }

    public void setProfession(int profession) {
        this.profession = profession;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof VillagerData)) return false;

        VillagerData that = (VillagerData) o;
        return this.type == that.type &&
                this.profession == that.profession &&
                this.level == that.level;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.type, this.profession, this.level);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
