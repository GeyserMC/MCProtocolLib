package org.geysermc.mcprotocollib.protocol.data.game.command.properties;

import lombok.Data;
import net.kyori.adventure.key.Key;

@Data
public class ResourceProperties implements CommandProperties {
    private final Key registryKey;

    public ResourceProperties(String registryKey) {
        this.registryKey = Key.key(registryKey);
    }
}
