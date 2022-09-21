package com.ibex.chatsdk;

import com.ibex.chatsdk.model.Message;

import java.util.ArrayList;

public interface CallBack {

//    public void messageCallBack( Object object , boolean isSaveVisitorChat, boolean isVsitorChatDetail);
//    public void idCallBack(int visitorId, int sessionid);
    public void hubConnectionCallBack(HubConnectionResponse status);
    public void onRecieveMessage(String message);
    public void onAgentConnected(String agentName, String welcomeMessage);
    public void onChatClosedbyAgent(String agentName);
//  public void onAgentTyping(String message);
    public void onAuthentication(String status);
    public void onImageRecieving(String imageUrl);
    public void onFileSent(String status ,String fileUrl);
    public void onSaveVisitor(String status ,int visitorId, int sessionId);
    public void onsendMessage(String status);
    public void onretrievePreviousChat(String Status , ArrayList<Message> chatList);


}
