package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
public abstract class Message {
    public abstract static class Builder<B extends Builder<?, M>, M extends Message> {
        @NonNull
        protected MessageStyle style = MessageStyle.DEFAULT;
        @NonNull
        protected List<Message> extra = new ArrayList<>();

        public B style(@NonNull MessageStyle style) {
            this.style = style;
            return (B) this;
        }

        public B extra(@NonNull Message... extra) {
            return this.extra(Arrays.asList(extra));
        }

        public B extra(@NonNull Collection<Message> extra) {
            this.extra.addAll(extra);
            return (B) this;
        }

        public B copy(@NonNull M message) {
            this.style = message.getStyle();
            this.extra = new ArrayList<>(message.getExtra());
            return (B) this;
        }

        public abstract M build();
    }

    private final MessageStyle style;
    private final List<Message> extra;

    protected Message(MessageStyle style, List<Message> extra) {
        this.style = style;
        this.extra = Collections.unmodifiableList(extra);
    }

    public MessageStyle getStyle() {
        return this.style;
    }

    public List<Message> getExtra() {
        return this.extra;
    }

    public abstract Builder<?, ?> toBuilder();

    @Override
    public String toString() {
        return MessageSerializer.toJsonString(this);
    }
}
