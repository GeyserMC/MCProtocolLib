package org.spacehq.mc.protocol.data.message;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public abstract class Message implements Cloneable {

    private MessageStyle style = new MessageStyle();
    private List<Message> extra = new ArrayList<Message>();

    public abstract String getText();

    public String getFullText() {
        StringBuilder build = new StringBuilder(this.getText());
        for(Message msg : this.extra) {
            build.append(msg.getFullText());
        }

        return build.toString();
    }

    public MessageStyle getStyle() {
        return this.style;
    }

    public List<Message> getExtra() {
        return new ArrayList<Message>(this.extra);
    }

    public Message setStyle(MessageStyle style) {
        this.style = style;
        return this;
    }

    public Message setExtra(List<Message> extra) {
        this.extra = new ArrayList<Message>(extra);
        for(Message msg : this.extra) {
            msg.getStyle().setParent(this.style);
        }

        return this;
    }

    public Message addExtra(Message message) {
        this.extra.add(message);
        message.getStyle().setParent(this.style);
        return this;
    }

    public Message removeExtra(Message message) {
        this.extra.remove(message);
        message.getStyle().setParent(null);
        return this;
    }

    public Message clearExtra() {
        for(Message msg : this.extra) {
            msg.getStyle().setParent(null);
        }

        this.extra.clear();
        return this;
    }

    @Override
    public String toString() {
        return this.getFullText();
    }

    @Override
    public abstract Message clone();

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if(!extra.equals(message.extra)) return false;
        if(!style.equals(message.style)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = style.hashCode();
        result = 31 * result + extra.hashCode();
        return result;
    }

    public String toJsonString() {
        return this.toJson().toString();
    }

    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("color", this.style.getColor().toString());
        for(ChatFormat format : this.style.getFormats()) {
            json.addProperty(format.toString(), true);
        }

        if(this.style.getClickEvent() != null) {
            JsonObject click = new JsonObject();
            click.addProperty("action", this.style.getClickEvent().getAction().toString());
            click.addProperty("value", this.style.getClickEvent().getValue());
            json.add("clickEvent", click);
        }

        if(this.style.getHoverEvent() != null) {
            JsonObject hover = new JsonObject();
            hover.addProperty("action", this.style.getHoverEvent().getAction().toString());
            hover.add("value", this.style.getHoverEvent().getValue().toJson());
            json.add("hoverEvent", hover);
        }

        if(this.style.getInsertion() != null) {
            json.addProperty("insertion", this.style.getInsertion());
        }

        if(this.extra.size() > 0) {
            JsonArray extra = new JsonArray();
            for(Message msg : this.extra) {
                extra.add(msg.toJson());
            }

            json.add("extra", extra);
        }

        return json;
    }

    public static Message fromString(String str) {
        try {
            return fromJson(new JsonParser().parse(str));
        } catch(Exception e) {
            return new TextMessage(str);
        }
    }

    public static Message fromJson(JsonElement e) {
        if(e.isJsonPrimitive()) {
            return new TextMessage(e.getAsString());
        } else if(e.isJsonObject()) {
            JsonObject json = e.getAsJsonObject();
            Message msg = null;
            if(json.has("text")) {
                msg = new TextMessage(json.get("text").getAsString());
            } else if(json.has("translate")) {
                Message with[] = new Message[0];
                if(json.has("with")) {
                    JsonArray withJson = json.get("with").getAsJsonArray();
                    with = new Message[withJson.size()];
                    for(int index = 0; index < withJson.size(); index++) {
                        JsonElement el = withJson.get(index);
                        if(el.isJsonPrimitive()) {
                            with[index] = new TextMessage(el.getAsString());
                        } else {
                            with[index] = Message.fromJson(el.getAsJsonObject());
                        }
                    }
                }

                msg = new TranslationMessage(json.get("translate").getAsString(), with);
            } else {
                throw new IllegalArgumentException("Unknown message type in json: " + json.toString());
            }

            MessageStyle style = new MessageStyle();
            if(json.has("color")) {
                style.setColor(ChatColor.byName(json.get("color").getAsString()));
            }

            for(ChatFormat format : ChatFormat.values()) {
                if(json.has(format.toString()) && json.get(format.toString()).getAsBoolean()) {
                    style.addFormat(format);
                }
            }

            if(json.has("clickEvent")) {
                JsonObject click = json.get("clickEvent").getAsJsonObject();
                style.setClickEvent(new ClickEvent(ClickAction.byName(click.get("action").getAsString()), click.get("value").getAsString()));
            }

            if(json.has("hoverEvent")) {
                JsonObject hover = json.get("hoverEvent").getAsJsonObject();
                style.setHoverEvent(new HoverEvent(HoverAction.byName(hover.get("action").getAsString()), Message.fromJson(hover.get("value"))));
            }

            if(json.has("insertion")) {
                style.setInsertion(json.get("insertion").getAsString());
            }

            msg.setStyle(style);
            if(json.has("extra")) {
                JsonArray extraJson = json.get("extra").getAsJsonArray();
                List<Message> extra = new ArrayList<Message>();
                for(int index = 0; index < extraJson.size(); index++) {
                    JsonElement el = extraJson.get(index);
                    if(el.isJsonPrimitive()) {
                        extra.add(new TextMessage(el.getAsString()));
                    } else {
                        extra.add(Message.fromJson(el.getAsJsonObject()));
                    }
                }

                msg.setExtra(extra);
            }

            return msg;
        } else {
            throw new IllegalArgumentException("Cannot convert " + e.getClass().getSimpleName() + " to a message.");
        }
    }

}
