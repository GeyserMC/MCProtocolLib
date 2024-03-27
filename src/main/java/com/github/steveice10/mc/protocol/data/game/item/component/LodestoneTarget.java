package com.github.steveice10.mc.protocol.data.game.item.component;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.GlobalPos;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LodestoneTarget {
    private final GlobalPos pos;
    private final boolean tracked;
}
