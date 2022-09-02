package com.ibex.chatsdk.model;

public class SaveVisitorChat {
    private int Id;
    private int WcVisitorId;
    private Object FromId;
    private Integer WcVisitorSessionId;
    private String FromName;
    private Integer ToId;
    private Object ToName;
    private String Message;
    private Object MessageDate;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getWcVisitorId() {
        return WcVisitorId;
    }

    public void setWcVisitorId(int wcVisitorId) {
        WcVisitorId = wcVisitorId;
    }

    public Object getFromId() {
        return FromId;
    }

    public void setFromId(Object fromId) {
        FromId = fromId;
    }

    public Integer getWcVisitorSessionId() {
        return WcVisitorSessionId;
    }

    public void setWcVisitorSessionId(Integer wcVisitorSessionId) {
        WcVisitorSessionId = wcVisitorSessionId;
    }

    public String getFromName() {
        return FromName;
    }

    public void setFromName(String fromName) {
        FromName = fromName;
    }

    public Integer getToId() {
        return ToId;
    }

    public void setToId(Integer toId) {
        ToId = toId;
    }

    public Object getToName() {
        return ToName;
    }

    public void setToName(Object toName) {
        ToName = toName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Object getMessageDate() {
        return MessageDate;
    }

    public void setMessageDate(Object messageDate) {
        MessageDate = messageDate;
    }
}
