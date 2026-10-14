package org.geysermc.mcprotocollib.protocol.data.game.chat.numbers;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.format.Style;
import org.cloudburstmc.nbt.NbtMap;

@Data
@AllArgsConstructor
public class StyledFormat implements NumberFormat {

    /**
     * Serialized {@link Style}
     */
    private final NbtMap style;
}
