package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class BlockNbtMessage extends NbtMessage {
    public static class Builder extends NbtMessage.Builder<Builder, BlockNbtMessage> {
        @NonNull
        private String pos = "";

        public Builder pos(@NonNull String pos) {
            this.pos = pos;
            return this;
        }

        @Override
        public Builder copy(@NonNull BlockNbtMessage message) {
            super.copy(message);
            this.pos = message.getPos();
            return this;
        }

        @Override
        public BlockNbtMessage build() {
            return new BlockNbtMessage(this.style, this.extra, this.path, this.interpret, this.pos);
        }
    }

    private final String pos;

    private BlockNbtMessage(MessageStyle style, List<Message> extra, String path, boolean interpret, String pos) {
        super(style, extra, path, interpret);
        this.pos = pos;
    }

    public String getPos() {
        return this.pos;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().copy(this);
    }
}
