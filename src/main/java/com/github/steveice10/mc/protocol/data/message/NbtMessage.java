package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public abstract class NbtMessage extends Message {
    public abstract static class Builder<B extends Builder<?, M>, M extends NbtMessage> extends Message.Builder<B, M> {
        @NonNull
        protected String path = "";
        protected boolean interpret = false;

        public B path(@NonNull String path) {
            this.path = path;
            return (B) this;
        }

        public B interpret(boolean interpret) {
            this.interpret = interpret;
            return (B) this;
        }

        @Override
        public B copy(@NonNull M message) {
            super.copy(message);
            this.path = message.getPath();
            this.interpret = message.shouldInterpret();
            return (B) this;
        }

        @Override
        public abstract M build();
    }

    private final String path;
    private final boolean interpret;

    protected NbtMessage(MessageStyle style, List<Message> extra, String path, boolean interpret) {
        super(style, extra);
        this.path = path;
        this.interpret = interpret;
    }

    public String getPath() {
        return this.path;
    }

    public boolean shouldInterpret() {
        return this.interpret;
    }
}
