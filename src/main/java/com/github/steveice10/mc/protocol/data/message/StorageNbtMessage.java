package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class StorageNbtMessage extends NbtMessage {
    public static class Builder extends NbtMessage.Builder<Builder, StorageNbtMessage> {
        @NonNull
        private String id = "";

        public Builder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder copy(@NonNull StorageNbtMessage message) {
            super.copy(message);
            this.id = message.getId();
            return this;
        }

        @Override
        public StorageNbtMessage build() {
            return new StorageNbtMessage(this.style, this.extra, this.path, this.interpret, this.id);
        }
    }

    private final String id;

    private StorageNbtMessage(MessageStyle style, List<Message> extra, String path, boolean interpret, String id) {
        super(style, extra, path, interpret);
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().copy(this);
    }
}
