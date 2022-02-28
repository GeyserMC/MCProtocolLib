package com.github.steveice10.mc.protocol.data.game.command.properties;

import com.github.steveice10.mc.protocol.data.game.Identifier;
import lombok.Data;

@Data
public class ResourceProperties implements CommandProperties {
    private final String registryKey;

    public ResourceProperties(String registryKey) {
        this.registryKey = Identifier.formalize(registryKey);
    }
}
