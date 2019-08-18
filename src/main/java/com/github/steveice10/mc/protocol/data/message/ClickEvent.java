package com.github.steveice10.mc.protocol.data.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClickEvent {
    private final ClickAction action;
    private final String value;
}
