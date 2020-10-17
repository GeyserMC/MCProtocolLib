package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.google.gson.JsonElement;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class ItemHoverMessage extends Message {
    public static class Builder extends Message.Builder<Builder, ItemHoverMessage> {
        @NonNull
        private String id = "";
        private int count;
        private JsonElement tag;

        public Builder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public Builder tag(JsonElement tag) {
            this.tag = tag;
            return this;
        }

        @Override
        public Builder copy(@NonNull ItemHoverMessage message) {
            super.copy(message);
            this.id = message.getId();
            this.count = message.getCount();
            this.tag = message.getTag();
            return this;
        }

        @Override
        public ItemHoverMessage build() {
            return new ItemHoverMessage(this.style, this.extra, this.id, this.count, this.tag);
        }
    }

    private final String id;
    private final int count;
    private final JsonElement tag;

    private ItemHoverMessage(MessageStyle style, List<Message> extra, String id, int count, JsonElement tag) {
        super(style, extra);
        this.id = id;
        this.count = count;
        this.tag = tag;
    }

    public String getId() {
        return this.id;
    }

    public int getCount() {
        return this.count;
    }

    public JsonElement getTag() {
        return this.tag;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().copy(this);
    }
}
