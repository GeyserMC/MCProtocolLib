package com.github.steveice10.mc.protocol.data.game.entity.object;

import lombok.Data;

@Data
public class WardenData implements ObjectData {
    private final boolean emerging;

    public WardenData(int data) {
        this.emerging = (data == 1);
    }
}
