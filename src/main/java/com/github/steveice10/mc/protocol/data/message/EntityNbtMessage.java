package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class EntityNbtMessage extends NbtMessage {
    public static class Builder extends NbtMessage.Builder<Builder, EntityNbtMessage> {
        @NonNull
        private String selector = "";

        public Builder selector(@NonNull String selector) {
            this.selector = selector;
            return this;
        }

        @Override
        public Builder copy(@NonNull EntityNbtMessage message) {
            super.copy(message);
            this.selector = message.getSelector();
            return this;
        }

        @Override
        public EntityNbtMessage build() {
            return new EntityNbtMessage(this.style, this.extra, this.path, this.interpret, this.selector);
        }
    }

    private final String selector;

    private EntityNbtMessage(MessageStyle style, List<Message> extra, String path, boolean interpret, String selector) {
        super(style, extra, path, interpret);
        this.selector = selector;
    }

    public String getSelector() {
        return this.selector;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().copy(this);
    }
}
