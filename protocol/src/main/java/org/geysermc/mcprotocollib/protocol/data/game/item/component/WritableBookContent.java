package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Data;

import java.util.List;

@Data
public class WritableBookContent {
    private final List<Filterable<String>> pages;

    public WritableBookContent(List<Filterable<String>> pages) {
        this.pages = List.copyOf(pages);
    }
}
