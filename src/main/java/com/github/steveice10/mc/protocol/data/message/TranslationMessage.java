package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Objects;

public class TranslationMessage extends Message {
    private String translationKey;
    private Message translationParams[];

    public TranslationMessage(String translationKey, Message... translationParams) {
        this.translationKey = translationKey;
        this.translationParams = translationParams;
        this.translationParams = this.getTranslationParams();
        for(Message param : this.translationParams) {
            param.getStyle().setParent(this.getStyle());
        }
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public Message[] getTranslationParams() {
        Message copy[] = Arrays.copyOf(this.translationParams, this.translationParams.length);
        for(int index = 0; index < copy.length; index++) {
            copy[index] = copy[index].clone();
        }

        return copy;
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
    public String getText() {
        return this.translationKey;
    }

    @Override
    public TranslationMessage clone() {
        return (TranslationMessage) new TranslationMessage(this.getTranslationKey(), this.getTranslationParams()).setStyle(this.getStyle().clone()).setExtra(this.getExtra());
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

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof TranslationMessage)) return false;

        TranslationMessage that = (TranslationMessage) o;
        return super.equals(o) &&
                Objects.equals(this.translationKey, that.translationKey) &&
                Arrays.equals(this.translationParams, that.translationParams);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(super.hashCode(), this.translationKey, this.translationParams);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
