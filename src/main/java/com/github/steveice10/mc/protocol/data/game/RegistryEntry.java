package com.github.steveice10.mc.protocol.data.game;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistryEntry {
    private final String id;
    private final CompoundTag data;
}
