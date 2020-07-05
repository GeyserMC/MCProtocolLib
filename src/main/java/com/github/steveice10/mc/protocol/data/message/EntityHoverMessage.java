package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class EntityHoverMessage extends Message {
    public static class Builder extends Message.Builder<Builder, EntityHoverMessage> {
        @NonNull
        private String type = "";
        @NonNull
        private String id = "";
        private Message name;

        public Builder type(@NonNull String type) {
            this.type = type;
            return this;
        }

        public Builder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        public Builder name(Message name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder copy(@NonNull EntityHoverMessage message) {
            super.copy(message);
            this.type = message.getType();
            this.id = message.getId();
            this.name = message.getName();
            return this;
        }

        @Override
        public EntityHoverMessage build() {
            return new EntityHoverMessage(this.style, this.extra, this.type, this.id, this.name);
        }
    }

    private final String type;
    private final String id;
    private final Message name;

    private EntityHoverMessage(MessageStyle style, List<Message> extra, String type, String id, Message name) {
        super(style, extra);
        this.type = type;
        this.id = id;
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public String getId() {
        return this.id;
    }

    public Message getName() {
        return this.name;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().copy(this);
    }
}
