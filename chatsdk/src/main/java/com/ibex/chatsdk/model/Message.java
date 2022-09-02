package com.ibex.chatsdk.model;

public class Message {
    String Message;
    boolean IsReply;

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
}