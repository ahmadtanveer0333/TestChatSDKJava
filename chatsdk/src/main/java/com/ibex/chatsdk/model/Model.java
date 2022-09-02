package com.ibex.chatsdk.model;

public class Model {
    private int Id;
    private Object ConnectionId = null;
    private String Name;
    private String Email;
    private Object RegisterDate = null;
    private Object CreatedOn= null;
    private Object ModifiedOn= null;
    private VisitorSession VisitorSession;
    private Object ChatDetail= null;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Object getConnectionId() {
        return ConnectionId;
    }

    public void setConnectionId(Object connectionId) {
        ConnectionId = connectionId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Object getRegisterDate() {
        return RegisterDate;
    }

    public void setRegisterDate(Object registerDate) {
        RegisterDate = registerDate;
    }

    public Object getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(Object createdOn) {
        CreatedOn = createdOn;
    }

    public Object getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(Object modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public  VisitorSession getVisitorSession() {
        return VisitorSession;
    }

    public void setVisitorSession(VisitorSession visitorSession) {
        VisitorSession = visitorSession;
    }

    public Object getChatDetail() {
        return ChatDetail;
    }

    public void setChatDetail(Object chatDetail) {
        ChatDetail = chatDetail;
    }
}
