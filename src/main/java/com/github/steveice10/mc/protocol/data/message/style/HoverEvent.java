package com.github.steveice10.mc.protocol.data.message.style;

import com.github.steveice10.mc.protocol.data.message.Message;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class HoverEvent {
    @NonNull
    private final HoverAction action;
    @NonNull
    private final Message value;
    
    public HoverEvent(@NonNull HoverAction action, @NonNull Message value) {
        this.action = action;
        this.value = value;
    }
    
    public HoverAction getAction() {
        return this.action;
    }
    
    public Message getValue() {
        return this.value;
    }
}
