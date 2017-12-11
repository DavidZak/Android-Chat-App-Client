package com.example.mradmin.androidnavapp.Entities.Messages.MessageParts;

/**
 * Created by mrAdmin on 27.09.2017.
 */

public class Body {

    private String content;
    private String type;

    public Body(String content) {
        this.content = content;
    }

    public Body() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
