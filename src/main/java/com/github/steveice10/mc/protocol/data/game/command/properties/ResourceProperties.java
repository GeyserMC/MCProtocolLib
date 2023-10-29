package com.github.steveice10.mc.protocol.data.game.command.properties;

import com.github.steveice10.mc.protocol.data.game.Identifier;

public record ResourceProperties(String registryKey) implements CommandProperties {
    public ResourceProperties {
        registryKey = Identifier.formalize(registryKey);
    }
}
