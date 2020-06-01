package com.github.steveice10.mc.protocol.data.message.style;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class MessageStyle {
    public static class Builder {
        @NonNull
        private ChatColor color = ChatColor.NONE;
        @NonNull
        private List<ChatFormat> formats = new ArrayList<>();
        private ClickEvent clickEvent;
        private HoverEvent hoverEvent;
        private String insertion;

        public Builder color(@NonNull ChatColor color) {
            this.color = color;
            return this;
        }

        public Builder formats(@NonNull ChatFormat... formats) {
            return this.formats(Arrays.asList(formats));
        }

        public Builder formats(@NonNull Collection<ChatFormat> formats) {
            this.formats.addAll(formats);
            return this;
        }

        public Builder clickEvent(ClickEvent clickEvent) {
            this.clickEvent = clickEvent;
            return this;
        }

        public Builder hoverEvent(HoverEvent hoverEvent) {
            this.hoverEvent = hoverEvent;
            return this;
        }

        public Builder insertion(String insertion) {
            this.insertion = insertion;
            return this;
        }

        public Builder copy(MessageStyle style) {
            this.color = style.getColor();
            this.formats = new ArrayList<>(style.getFormats());
            this.clickEvent = style.getClickEvent();
            this.hoverEvent = style.getHoverEvent();
            this.insertion = style.getInsertion();
            return this;
        }

        public MessageStyle build() {
            return new MessageStyle(this.color, this.formats, this.clickEvent, this.hoverEvent, this.insertion);
        }
    }

    public static final MessageStyle DEFAULT = new MessageStyle.Builder().build();

    private final ChatColor color;
    private final List<ChatFormat> formats;
    private final ClickEvent clickEvent;
    private final HoverEvent hoverEvent;
    private final String insertion;

    private MessageStyle(ChatColor color, List<ChatFormat> formats, ClickEvent clickEvent, HoverEvent hoverEvent, String insertion) {
        this.color = color;
        this.formats = Collections.unmodifiableList(formats);
        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;
        this.insertion = insertion;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public List<ChatFormat> getFormats() {
        return this.formats;
    }

    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }

    public HoverEvent getHoverEvent() {
        return this.hoverEvent;
    }

    public String getInsertion() {
        return this.insertion;
    }
}