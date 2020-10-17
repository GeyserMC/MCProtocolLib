package com.github.steveice10.mc.protocol.data.message.style;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ClickEvent {
    @NonNull
    private final ClickAction action;
    @NonNull
    private final String value;

    public ClickEvent(@NonNull ClickAction action, @NonNull String value) {
        this.action = action;
        this.value = value;
    }

    public ClickAction getAction() {
        return this.action;
    }

    public String getValue() {
        return this.value;
    }
}
