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

    private final Message contents;
    
    public HoverEvent(@NonNull HoverAction action, @NonNull Message contents) {
        this.action = action;
        this.contents = contents;
    }
    
    public HoverAction getAction() {
        return this.action;
    }
    
    public Message getContents() {
        return this.contents;
    }
}
