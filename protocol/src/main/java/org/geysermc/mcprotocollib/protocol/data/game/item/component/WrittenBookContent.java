package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;

import java.util.List;

@Data
@AllArgsConstructor
public class WrittenBookContent {
    private final Filterable<String> title;
    private final String author;
    private final int generation;
    private final List<Filterable<Component>> pages;
    private final boolean resolved;
}
