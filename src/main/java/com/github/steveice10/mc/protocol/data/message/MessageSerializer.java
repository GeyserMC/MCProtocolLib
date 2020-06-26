package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.style.ChatColor;
import com.github.steveice10.mc.protocol.data.message.style.ChatFormat;
import com.github.steveice10.mc.protocol.data.message.style.ClickAction;
import com.github.steveice10.mc.protocol.data.message.style.ClickEvent;
import com.github.steveice10.mc.protocol.data.message.style.HoverAction;
import com.github.steveice10.mc.protocol.data.message.style.HoverEvent;
import com.github.steveice10.mc.protocol.data.message.style.MessageStyle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

public class MessageSerializer {
    public static Message fromString(String str) {
        try {
            return fromJson(new JsonParser().parse(str));
        } catch(Exception e) {
            return new TextMessage.Builder().text(str).build();
        }
    }

    public static Message fromJson(JsonElement e) {
        return builderFromJson(e).build();
    }

    private static Message.Builder<?, ?> builderFromJson(JsonElement e) {
        if(e.isJsonPrimitive()) {
            return new TextMessage.Builder().text(e.getAsString());
        } else if(e.isJsonArray()) {
            JsonArray array = e.getAsJsonArray();
            if(array.size() == 0) {
                return new TextMessage.Builder().text("");
            }

            Message.Builder<?, ?> msg = builderFromJson(array.get(0));
            for(int index = 1; index < array.size(); index++) {
                msg.extra(fromJson(array.get(index)));
            }

            return msg;
        } else if(e.isJsonObject()) {
            JsonObject json = e.getAsJsonObject();

            Message.Builder<?, ?> msg = dataFromJson(json);
            msg.style(styleFromJson(json));
            msg.extra(extraFromJson(json));
            return msg;
        } else {
            throw new IllegalArgumentException("Cannot convert JSON type " + e.getClass().getSimpleName() + " to a message.");
        }
    }

    public static String toJsonString(Message message) {
        return toJson(message).toString();
    }

    public static String toJsonAsString(Message message) {
        return toJson(message).getAsString();
    }

    public static JsonElement toJson(Message message) {
        if(message instanceof TextMessage && message.getStyle().equals(MessageStyle.DEFAULT) && message.getExtra().isEmpty()) {
            return new JsonPrimitive(((TextMessage) message).getText());
        }

        JsonObject json = new JsonObject();
        dataToJson(json, message);
        styleToJson(json, message.getStyle());
        extraToJson(json, message.getExtra());
        return json;
    }

    private static Message.Builder<?, ?> dataFromJson(JsonObject json) {
        if(json.has("text")) {
            return new TextMessage.Builder()
                    .text(json.get("text").getAsString());
        } else if(json.has("translate")) {
            List<Message> with = new ArrayList<>();
            if(json.has("with")) {
                JsonArray withJson = json.get("with").getAsJsonArray();
                for(int index = 0; index < withJson.size(); index++) {
                    with.add(fromJson(withJson.get(index)));
                }
            }

            return new TranslationMessage.Builder()
                    .key(json.get("translate").getAsString())
                    .with(with);
        } else if(json.has("keybind")) {
            return new KeybindMessage.Builder()
                    .keybind(json.get("keybind").getAsString());
        } else if(json.has("score")) {
            JsonObject score = json.get("score").getAsJsonObject();
            return new ScoreMessage.Builder()
                    .name(score.get("name").getAsString())
                    .objective(score.get("objective").getAsString())
                    .value(score.has("value") ? score.get("value").getAsString() : null);
        } else if(json.has("selector")) {
            return new SelectorMessage.Builder()
                    .selector(json.get("selector").getAsString());
        } else if(json.has("nbt")) {
            String path = json.get("nbt").getAsString();
            boolean interpret = json.has("interpret") && json.get("interpret").getAsBoolean();
            if(json.has("block")) {
                return new BlockNbtMessage.Builder()
                        .path(path)
                        .interpret(interpret)
                        .pos(json.get("block").getAsString());
            } else if(json.has("entity")) {
                return new EntityNbtMessage.Builder()
                        .path(path)
                        .interpret(interpret)
                        .selector(json.get("entity").getAsString());
            } else if(json.has("storage")) {
                return new StorageNbtMessage.Builder()
                        .path(path)
                        .interpret(interpret)
                        .id(json.get("storage").getAsString());
            } else {
                throw new IllegalArgumentException("Unknown NBT message type in json: " + json);
            }
        } else if(json.has("type")) {
            return new EntityHoverMessage.Builder()
                    .type(json.get("type").getAsString())
                    .id(json.get("id").getAsString())
                    .name(fromJson(json.get("name")));
        } else {
            throw new IllegalArgumentException("Unknown message type in json: " + json);
        }
    }

    private static void dataToJson(JsonObject json, Message message) {
        if(message instanceof TextMessage) {
            json.addProperty("text", ((TextMessage) message).getText());
        } else if(message instanceof TranslationMessage) {
            TranslationMessage translationMessage = (TranslationMessage) message;
            json.addProperty("translate", translationMessage.getKey());

            List<Message> with = translationMessage.getWith();
            if(!with.isEmpty()) {
                JsonArray jsonWith = new JsonArray();
                for(Message msg : with) {
                    jsonWith.add(toJson(msg));
                }

                json.add("with", jsonWith);
            }
        } else if(message instanceof KeybindMessage) {
            json.addProperty("keybind", ((KeybindMessage) message).getKeybind());
        } else if(message instanceof ScoreMessage) {
            ScoreMessage scoreMessage = (ScoreMessage) message;

            JsonObject score = new JsonObject();
            score.addProperty("name", scoreMessage.getName());
            score.addProperty("objective", scoreMessage.getObjective());
            if(scoreMessage.getValue() != null) {
                score.addProperty("value", scoreMessage.getValue());
            }

            json.add("score", score);
        } else if(message instanceof SelectorMessage) {
            json.addProperty("selector", ((SelectorMessage) message).getSelector());
        } else if(message instanceof NbtMessage) {
            NbtMessage nbtMessage = (NbtMessage) message;

            json.addProperty("nbt", nbtMessage.getPath());
            json.addProperty("interpret", nbtMessage.shouldInterpret());

            if(message instanceof BlockNbtMessage) {
                json.addProperty("block", ((BlockNbtMessage) nbtMessage).getPos());
            } else if(message instanceof EntityNbtMessage) {
                json.addProperty("entity", ((EntityNbtMessage) nbtMessage).getSelector());
            } else if(message instanceof StorageNbtMessage) {
                json.addProperty("storage", ((StorageNbtMessage) nbtMessage).getId());
            }
        } else if(message instanceof EntityHoverMessage) {
            EntityHoverMessage entityHoverMessage = (EntityHoverMessage) message;
            json.addProperty("type", entityHoverMessage.getType());
            json.addProperty("id", entityHoverMessage.getId());
            json.add("name", toJson(entityHoverMessage.getName()));
        }
    }

    private static MessageStyle styleFromJson(JsonObject json) {
        MessageStyle.Builder style = new MessageStyle.Builder();
        if(json.has("color")) {
            style.color(json.get("color").getAsString());
        }

        for(ChatFormat format : ChatFormat.values()) {
            if(json.has(format.toString()) && json.get(format.toString()).getAsBoolean()) {
                style.formats(format);
            }
        }

        if(json.has("clickEvent")) {
            JsonObject click = json.get("clickEvent").getAsJsonObject();
            style.clickEvent(new ClickEvent(ClickAction.byName(click.get("action").getAsString()), click.get("value").getAsString()));
        }

        if(json.has("hoverEvent")) {
            JsonObject hover = json.get("hoverEvent").getAsJsonObject();
            style.hoverEvent(new HoverEvent(HoverAction.byName(hover.get("action").getAsString()), fromJson(hover.get("contents"))));
        }

        if(json.has("insertion")) {
            style.insertion(json.get("insertion").getAsString());
        }

        return style.build();
    }

    private static void styleToJson(JsonObject json, MessageStyle style) {
        if(style.getColor() != ChatColor.NONE) {
            json.addProperty("color", style.getColor().toString());
        }

        for(ChatFormat format : style.getFormats()) {
            json.addProperty(format.toString(), true);
        }

        if(style.getClickEvent() != null) {
            JsonObject click = new JsonObject();
            click.addProperty("action", style.getClickEvent().getAction().toString());
            click.addProperty("value", style.getClickEvent().getValue());
            json.add("clickEvent", click);
        }

        if(style.getHoverEvent() != null) {
            JsonObject hover = new JsonObject();
            hover.addProperty("action", style.getHoverEvent().getAction().toString());
            hover.add("contents", toJson(style.getHoverEvent().getContents()));
            json.add("hoverEvent", hover);
        }

        if(style.getInsertion() != null) {
            json.addProperty("insertion", style.getInsertion());
        }
    }

    private static List<Message> extraFromJson(JsonObject json) {
        List<Message> extra = new ArrayList<>();
        if(json.has("extra")) {
            JsonArray extraJson = json.get("extra").getAsJsonArray();
            for(int index = 0; index < extraJson.size(); index++) {
                extra.add(fromJson(extraJson.get(index)));
            }
        }

        return extra;
    }

    private static void extraToJson(JsonObject json, List<Message> extra) {
        if(!extra.isEmpty()) {
            JsonArray jsonExtra = new JsonArray();
            for(Message msg : extra) {
                jsonExtra.add(toJson(msg));
            }

            json.add("extra", jsonExtra);
        }
    }
}
