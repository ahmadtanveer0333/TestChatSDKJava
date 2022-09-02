package com.ibex.chatsdk;

public interface CallBack {
    public void messageCallBack( Object object , boolean isSaveVisitorChat);
    public void idCallBack(int visitorId, int sessionid);
    public void hubConnectionCallBack(HubConnectionResponse status);



}
