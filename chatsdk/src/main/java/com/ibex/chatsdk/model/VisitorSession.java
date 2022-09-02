package com.ibex.chatsdk.model;

public class VisitorSession {
    private int Id;
    private String Name;
    private Object CreatedOn;
    private Object ConnectedWithId;
    private Object WcVisitorId;
    private Object AgentName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Object getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(Object createdOn) {
        CreatedOn = createdOn;
    }

    public Object getConnectedWithId() {
        return ConnectedWithId;
    }

    public void setConnectedWithId(Object connectedWithId) {
        ConnectedWithId = connectedWithId;
    }

    public Object getWcVisitorId() {
        return WcVisitorId;
    }

    public void setWcVisitorId(Object wcVisitorId) {
        WcVisitorId = wcVisitorId;
    }

    public Object getAgentName() {
        return AgentName;
    }

    public void setAgentName(Object agentName) {
        AgentName = agentName;
    }
}
