package com.github.steveice10.mc.protocol.data.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Setter(AccessLevel.NONE)
public class MessageStyle implements Cloneable {
    private static final MessageStyle DEFAULT = new MessageStyle();

    private ChatColor color = ChatColor.NONE;
    private List<ChatFormat> formats = new ArrayList<ChatFormat>();
    private ClickEvent clickEvent;
    private HoverEvent hoverEvent;
    private String insertion;
    private MessageStyle parent = DEFAULT;

    public boolean isDefault() {
        return this.equals(DEFAULT);
    }

    public boolean hasColor() {
        return color != ChatColor.NONE;
    }

    public MessageStyle setColor(ChatColor color) {
        this.color = color;
        return this;
    }

    public MessageStyle setFormats(List<ChatFormat> formats) {
        this.formats = new ArrayList<ChatFormat>(formats);
        return this;
    }

    public MessageStyle setClickEvent(ClickEvent event) {
        this.clickEvent = event;
        return this;
    }

    public MessageStyle setHoverEvent(HoverEvent event) {
        this.hoverEvent = event;
        return this;
    }

    public MessageStyle setInsertion(String insertion) {
        this.insertion = insertion;
        return this;
    }

    protected MessageStyle setParent(MessageStyle parent) {
        if(parent == null) {
            parent = DEFAULT;
        }

        this.parent = parent;
        return this;
    }

    public MessageStyle addFormat(ChatFormat format) {
        this.formats.add(format);
        return this;
    }

    public MessageStyle removeFormat(ChatFormat format) {
        this.formats.remove(format);
        return this;
    }

    public MessageStyle clearFormats() {
        this.formats.clear();
        return this;
    }

    @Override
    public MessageStyle clone() {
        return new MessageStyle()
                .setParent(this.parent)
                .setColor(this.color)
                .setFormats(this.formats)
                .setClickEvent(this.clickEvent)
                .setHoverEvent(this.hoverEvent)
                .setInsertion(this.insertion);
    }
}