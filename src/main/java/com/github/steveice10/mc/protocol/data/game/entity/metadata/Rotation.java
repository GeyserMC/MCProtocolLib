package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rotation {
    private final float pitch;
    private final float yaw;
    private final float roll;
}
