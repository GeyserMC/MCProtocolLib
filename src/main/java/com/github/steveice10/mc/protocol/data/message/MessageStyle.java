package com.github.steveice10.mc.protocol.data.message;

import java.util.ArrayList;
import java.util.List;

public class MessageStyle implements Cloneable {
    private static final MessageStyle DEFAULT = new MessageStyle();

    private ChatColor color = ChatColor.WHITE;
    private List<ChatFormat> formats = new ArrayList<ChatFormat>();
    private ClickEvent click;
    private HoverEvent hover;
    private String insertion;
    private MessageStyle parent = DEFAULT;

    public boolean isDefault() {
        return this.equals(DEFAULT);
    }

    public ChatColor getColor() {
        return this.color;
    }

    public List<ChatFormat> getFormats() {
        return new ArrayList<ChatFormat>(this.formats);
    }

    public ClickEvent getClickEvent() {
        return this.click;
    }

    public HoverEvent getHoverEvent() {
        return this.hover;
    }

    public String getInsertion() {
        return this.insertion;
    }

    public MessageStyle getParent() {
        return this.parent;
    }

    public MessageStyle setColor(ChatColor color) {
        this.color = color;
        return this;
    }

    public MessageStyle setFormats(List<ChatFormat> formats) {
        this.formats = new ArrayList<ChatFormat>(formats);
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

    public MessageStyle setClickEvent(ClickEvent event) {
        this.click = event;
        return this;
    }

    public MessageStyle setHoverEvent(HoverEvent event) {
        this.hover = event;
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

    @Override
    public String toString() {
        return "MessageStyle{color=" + this.color + ",formats=" + this.formats + ",clickEvent=" + this.click + ",hoverEvent=" + this.hover + ",insertion=" + this.insertion + "}";
    }

    @Override
    public MessageStyle clone() {
        return new MessageStyle().setParent(this.parent).setColor(this.color).setFormats(this.formats).setClickEvent(this.click != null ? this.click.clone() : null).setHoverEvent(this.hover != null ? this.hover.clone() : null).setInsertion(this.insertion);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MessageStyle && this.color == ((MessageStyle) o).color && this.formats.equals(((MessageStyle) o).formats) && (this.click != null ? this.click.equals(((MessageStyle) o).click) : ((MessageStyle) o).click == null) && (this.hover != null ? this.hover.equals(((MessageStyle) o).hover) : ((MessageStyle) o).hover == null) && (this.insertion != null ? this.insertion.equals(((MessageStyle) o).insertion) : ((MessageStyle) o).insertion == null) && this.parent.equals(((MessageStyle) o).parent);
    }

    @Override
    public int hashCode() {
        int result = this.color.hashCode();
        result = 31 * result + this.formats.hashCode();
        result = 31 * result + (this.click != null ? this.click.hashCode() : 0);
        result = 31 * result + (this.hover != null ? this.hover.hashCode() : 0);
        result = 31 * result + (this.insertion != null ? this.insertion.hashCode() : 0);
        result = 31 * result + this.parent.hashCode();
        return result;
    }
}