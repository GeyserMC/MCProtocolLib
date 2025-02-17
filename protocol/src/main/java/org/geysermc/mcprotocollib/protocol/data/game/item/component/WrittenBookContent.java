package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.util.List;

@Data
@With
@Builder(toBuilder = true)
public class WrittenBookContent {
    private final Filterable<String> title;
    private final String author;
    private final int generation;
    private final List<Filterable<Component>> pages;
    private final boolean resolved;

    public WrittenBookContent(Filterable<String> title, String author, int generation, List<Filterable<Component>> pages, boolean resolved) {
        this.title = title;
        this.author = author;
        this.generation = generation;
        this.pages = List.copyOf(pages);
        this.resolved = resolved;
    }
}
