package com.github.steveice10.mc.protocol.data.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HoverEvent {
    private final HoverAction action;
    private final Message value;
}
