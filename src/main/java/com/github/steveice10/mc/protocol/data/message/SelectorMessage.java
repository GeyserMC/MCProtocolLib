package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class SelectorMessage extends Message {
    public static class Builder extends Message.Builder<Builder, SelectorMessage> {
        @NonNull
        private String selector = "";

        public Builder selector(@NonNull String selector) {
            this.selector = selector;
            return this;
        }

        @Override
        public Builder copy(@NonNull SelectorMessage message) {
            super.copy(message);
            this.selector = message.getSelector();
            return this;
        }

        @Override
        public SelectorMessage build() {
            return new SelectorMessage(this.style, this.extra, this.selector);
        }
    }

    private final String selector;

    private SelectorMessage(MessageStyle style, List<Message> extra, String selector) {
        super(style, extra);
        this.selector = selector;
    }

    public String getSelector() {
        return this.selector;
    }
}
