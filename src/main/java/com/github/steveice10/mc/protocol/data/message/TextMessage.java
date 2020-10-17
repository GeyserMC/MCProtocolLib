package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class TextMessage extends Message {
    public static class Builder extends Message.Builder<Builder, TextMessage> {
        @NonNull
        private String text = "";

        public Builder text(@NonNull String text) {
            this.text = text;
            return this;
        }

        @Override
        public Builder copy(@NonNull TextMessage message) {
            super.copy(message);
            this.text = message.getText();
            return this;
        }

        @Override
        public TextMessage build() {
            return new TextMessage(this.style, this.extra, this.text);
        }
    }

    private final String text;

    private TextMessage(MessageStyle style, List<Message> extra, String text) {
        super(style, extra);
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().copy(this);
    }
}
