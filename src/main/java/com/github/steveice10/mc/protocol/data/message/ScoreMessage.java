package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class ScoreMessage extends Message {
    public static class Builder extends Message.Builder<Builder, ScoreMessage> {
        @NonNull
        private String name = "";
        @NonNull
        private String objective = "";
        private String value = null;

        public Builder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        public Builder objective(@NonNull String objective) {
            this.objective = objective;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        @Override
        public Builder copy(@NonNull ScoreMessage message) {
            super.copy(message);
            this.name = message.getName();
            this.objective = message.getObjective();
            this.value = message.getValue();
            return this;
        }

        @Override
        public ScoreMessage build() {
            return new ScoreMessage(this.style, this.extra, this.name, this.objective, this.value);
        }
    }

    private final String name;
    private final String objective;
    private final String value;

    private ScoreMessage(MessageStyle style, List<Message> extra, String name, String objective, String value) {
        super(style, extra);
        this.name = name;
        this.objective = objective;
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getObjective() {
        return this.objective;
    }
    
    public String getValue() {
        return this.value;
    }
}
