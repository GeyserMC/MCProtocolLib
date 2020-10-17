package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class TranslationMessage extends Message {
    public static class Builder extends Message.Builder<Builder, TranslationMessage> {
        @NonNull
        private String key = "";
        @NonNull
        private List<Message> with = new ArrayList<>();

        public Builder key(@NonNull String key) {
            this.key = key;
            return this;
        }

        public Builder with(@NonNull Message... with) {
            return this.with(Arrays.asList(with));
        }

        public Builder with(@NonNull Collection<Message> with) {
            this.with.addAll(with);
            return this;
        }

        @Override
        public Builder copy(@NonNull TranslationMessage message) {
            super.copy(message);
            this.key = message.getKey();
            this.with = new ArrayList<>(message.getWith());
            return this;
        }

        @Override
        public TranslationMessage build() {
            return new TranslationMessage(this.style, this.extra, this.key, this.with);
        }
    }

    private final String key;
    private final List<Message> with;

    private TranslationMessage(MessageStyle style, List<Message> extra, String key, List<Message> with) {
        super(style, extra);
        this.key = key;
        this.with = Collections.unmodifiableList(with);
    }

    public String getKey() {
        return this.key;
    }

    public List<Message> getWith() {
        return this.with;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().copy(this);
    }
}
