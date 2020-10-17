package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class KeybindMessage extends Message {
    public static class Builder extends Message.Builder<Builder, KeybindMessage> {
        @NonNull
        private String keybind = "";

        public Builder keybind(@NonNull String keybind) {
            this.keybind = keybind;
            return this;
        }

        @Override
        public Builder copy(@NonNull KeybindMessage message) {
            super.copy(message);
            this.keybind = message.getKeybind();
            return this;
        }

        @Override
        public KeybindMessage build() {
            return new KeybindMessage(this.style, this.extra, this.keybind);
        }
    }

    private final String keybind;

    private KeybindMessage(MessageStyle style, List<Message> extra, String keybind) {
        super(style, extra);
        this.keybind = keybind;
    }

    public String getKeybind() {
        return this.keybind;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().copy(this);
    }
}
