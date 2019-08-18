package com.github.steveice10.mc.protocol.data.message;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TranslationMessage extends Message {
    private final String translationKey;
    private final Message[] translationParams;

    public TranslationMessage(String translationKey, Message... translationParams) {
        this.translationKey = translationKey;
        this.translationParams = new Message[translationParams.length];
        for(int index = 0; index < this.translationParams.length; index++) {
            this.translationParams[index] = translationParams[index].clone();
            this.translationParams[index].getStyle().setParent(this.getStyle());
        }
    }

    @Override
    public String getText() {
        return this.translationKey;
    }

    @Override
    public Message setStyle(MessageStyle style) {
        super.setStyle(style);
        for(Message param : this.translationParams) {
            param.getStyle().setParent(this.getStyle());
        }

        return this;
    }

    @Override
    public TranslationMessage clone() {
        return (TranslationMessage) new TranslationMessage(this.translationKey, this.translationParams).setStyle(this.getStyle().clone()).setExtra(this.getExtra());
    }

    @Override
    public JsonElement toJson() {
        JsonElement e = super.toJson();
        if(e.isJsonObject()) {
            JsonObject json = e.getAsJsonObject();
            json.addProperty("translate", this.translationKey);
            JsonArray params = new JsonArray();
            for(Message param : this.translationParams) {
                params.add(param.toJson());
            }

            json.add("with", params);
            return json;
        } else {
            return e;
        }
    }
}
