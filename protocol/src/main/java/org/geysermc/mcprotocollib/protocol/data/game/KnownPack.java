package com.github.steveice10.mc.protocol.data.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KnownPack {
    private String namespace;
    private String id;
    private String version;
}
