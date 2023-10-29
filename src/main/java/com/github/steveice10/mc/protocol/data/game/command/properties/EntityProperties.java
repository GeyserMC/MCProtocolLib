package com.github.steveice10.mc.protocol.data.game.command.properties;

public record EntityProperties(boolean singleTarget, boolean playersOnly) implements CommandProperties {
}
