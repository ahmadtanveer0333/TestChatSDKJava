package com.ibex.chatsdk.model;

public class Message {
    String Message;
    boolean IsReply;
    boolean IsMediaFile;
    String Url;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isReply() {
        return IsReply;
    }

    public void setReply(boolean reply) {
        IsReply = reply;
    }

    public boolean isMediaFile() {
        return IsMediaFile;
    }

    public void setMediaFile(boolean mediaFile) {
        IsMediaFile = mediaFile;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}