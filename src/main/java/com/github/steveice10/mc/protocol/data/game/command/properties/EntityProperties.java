package com.github.steveice10.mc.protocol.data.game.command.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntityProperties implements CommandProperties {
    private final boolean singleTarget;
    private final boolean playersOnly;
}
