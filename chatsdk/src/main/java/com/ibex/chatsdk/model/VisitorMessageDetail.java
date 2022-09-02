package com.ibex.chatsdk.model;

import java.util.ArrayList;

public class VisitorMessageDetail {
    private Integer VisitorId;
    private Integer VisitorSession;
    private String VisitorSessionName;
    private ArrayList<WebChatDetialVisitor> WebChatDetialVisitor = null;

    public Integer getVisitorId() {
        return VisitorId;
    }

    public void setVisitorId(Integer visitorId) {
        VisitorId = visitorId;
    }

    public Integer getVisitorSession() {
        return VisitorSession;
    }

    public void setVisitorSession(Integer visitorSession) {
        VisitorSession = visitorSession;
    }

    public String getVisitorSessionName() {
        return VisitorSessionName;
    }

    public void setVisitorSessionName(String visitorSessionName) {
        VisitorSessionName = visitorSessionName;
    }

    public ArrayList<WebChatDetialVisitor> getWebChatDetialVisitor() {
        return WebChatDetialVisitor;
    }

    public void setWebChatDetialVisitor(ArrayList<WebChatDetialVisitor> webChatDetialVisitor) {
        WebChatDetialVisitor = webChatDetialVisitor;
    }
}

