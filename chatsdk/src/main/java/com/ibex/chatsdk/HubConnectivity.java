package com.ibex.chatsdk;

import static com.ibex.chatsdk.Constatnts.AGENT_CONNECTED_Alert;
import static com.ibex.chatsdk.Constatnts.AGENT_ClOSED_CHAT_Alert;
import static com.ibex.chatsdk.Constatnts.AGENT_TYPING_Alert;
import static com.ibex.chatsdk.Constatnts.AUTHENTICATION_TOKEN;
import static com.ibex.chatsdk.Constatnts.CLOSED_WINDOW_MESSAGE;
import static com.ibex.chatsdk.Constatnts.CRM_INVOKE_METHOD_NAME;
import static com.ibex.chatsdk.Constatnts.SERVER_IMAGE_RECIEVING_METHODNAME;
import static com.ibex.chatsdk.Constatnts.SERVER_MESSAGERECIEVING_METHODNAME;
import static com.ibex.chatsdk.Constatnts.SESSION_ID;
import static com.ibex.chatsdk.Constatnts.URL;
import static com.ibex.chatsdk.Constatnts.VISITOR_CLOSED_WINDOW;
import static com.ibex.chatsdk.Constatnts.VISITOR_ID;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

public class HubConnectivity {

    HubProxy hubProxy;
    HubConnection hubConnection;
    Context context;
    CallBack callBack;
    public static final String HUB_CONNECTION_URL = Constatnts.URL;
    public static final String HUB_Name = Constatnts.HUB_Name;
    public static final String TAG = "hubStatus";
    PrefConfig prefConfig;
    int visitorId , sessionId;
    Logger logger = new Logger() {
        @Override
        public void log(String message, LogLevel level) {
            //   System.out.println(message);
        }
    };
    ServiceManager serviceManager;

    public HubConnectivity(Context context, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        prefConfig = new PrefConfig(context);
    }

    private void createHubConnection(String name, String email, String cellNumber){
        //  hubConnection = new HubConnection("http://swiftr.azurewebsites.net","", true ,logger);

        serviceManager = new ServiceManager(context, callBack);
        hubConnection = new HubConnection(HUB_CONNECTION_URL,"", true ,logger);
        createHubProxy();
        HubConnected();
        HubStart(name, email,cellNumber);
        HubMessageRecieved();
        HubConnectionSlow();
        HubReconnecting();
        HubReconnected();
        HubClosed();




    }

    private void HubConnected(){
        hubConnection.connected(new Runnable() {
            @Override
            public void run() {
                callBack.hubConnectionCallBack(HubConnectionResponse.CONNECTED);
                Log.d(TAG, "connected: ");

            }
        });
    }

    private void HubStart(String name, String email, String cellNumber){
        hubConnection.start()
                .done(new Action<Void>() {
                    @Override
                    public void run(Void obj) throws Exception {
                        Log.d("hubStatus", "start: ");

                        Log.d("hubStatus", "start: visitorId"+ prefConfig.getData(VISITOR_ID));
                        Log.d("hubStatus", "start: sessionId"+ prefConfig.getData(SESSION_ID));
                         visitorId = prefConfig.getData(VISITOR_ID);
                         sessionId = prefConfig.getData(SESSION_ID);

                        if (visitorId != 0 && visitorId > 0) {
                            Log.d("hubStatus", "start: visitorChatDetail");
                        //    callBack.idCallBack(visitorId , sessionId);
                            serviceManager.retrievePreviousChat();
                        } else {
                            Log.d("hubStatus", "start: saveVisitor");
                            serviceManager.saveVisitor(context, name, email, cellNumber);
                        }
                        callBack.hubConnectionCallBack(HubConnectionResponse.START);
                    }
                })
                .onError(new ErrorCallback() {
                    @Override
                    public void onError(Throwable throwable) {

                    }
                });

    }

    private void HubMessageRecieved(){
        hubConnection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(JsonElement jsonElement) {
                Log.d(TAG, "onMessageReceived: " + jsonElement.toString());
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonElement methodName = jsonElement.getAsJsonObject().get("M");
                JsonArray message = (JsonArray) jsonElement.getAsJsonObject().get("A");
                Log.d(TAG, "onMessageReceived: A " + message.toString());
                Log.d(TAG, "onMessageReceived: sessionId " + sessionId);
                visitorId = prefConfig.getData(VISITOR_ID);
                sessionId = prefConfig.getData(SESSION_ID);
                if(methodName.getAsString().equals(AGENT_CONNECTED_Alert)){
                    if(message.get(0).getAsString().equals(String.valueOf(sessionId))){
                        welcomeMessage(message.get(2).getAsString());
                    }

                    Log.d(TAG, "onMessageReceived: agentconnected name == " + message.get(2).getAsString());
                }
                if(methodName.getAsString().equals(AGENT_ClOSED_CHAT_Alert)){
                   if(message.get(1).getAsString().equals(String.valueOf(visitorId)) && message.get(2).getAsString().equals(String.valueOf(sessionId))) {
                       callBack.onChatClosedbyAgent(message.get(0).getAsString());
                   }


                }
//                if(methodName.getAsString().equals(AGENT_TYPING_Alert)){
//                    callBack.onAgentTyping("Agent is typing....");
//                }
                if(methodName.getAsString().equals(SERVER_MESSAGERECIEVING_METHODNAME)){
                    Log.d("hubStatus", "start: visitorId"+ prefConfig.getData(VISITOR_ID));
                     visitorId = prefConfig.getData(VISITOR_ID);
                     sessionId = prefConfig.getData(SESSION_ID);
                    if(message.get(1).getAsString().equals(String.valueOf(visitorId))){
                        callBack.onRecieveMessage(message.get(2).getAsString());
                    }

                }
                if(methodName.getAsString().equals(SERVER_IMAGE_RECIEVING_METHODNAME)){
                    Log.d(TAG, "onMessageReceived: onImageRecieving " + URL+message.get(2).getAsString());
                    if(message.get(1).getAsString().equals(String.valueOf(visitorId))) {
                        callBack.onImageRecieving(URL + message.get(2).getAsString());
                    }
                }
            }
        });
    }

    private void welcomeMessage(String agentName) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            callBack.onAgentConnected(agentName,"Good Morning, my name is " + agentName + ", how may i help you ?" );
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            callBack.onAgentConnected(agentName,"Good Afternoon, my name is " + agentName + ", how may i help you ?" );
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            callBack.onAgentConnected(agentName,"Good Evening, my name is " + agentName + ", how may i help you ?" );
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            callBack.onAgentConnected(agentName,"Good Night, my name is " + agentName + ", how may i help you ?" );

        }

    }

    private void HubConnectionSlow(){
        hubConnection.connectionSlow(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "connectionSlow: ");
                callBack.hubConnectionCallBack(HubConnectionResponse.SLOW);
            }
        });
    }

    private void HubReconnecting(){
        hubConnection.reconnecting(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "reconnecting: ");
                callBack.hubConnectionCallBack(HubConnectionResponse.RECONNECTING);
            }
        });
    }

    private void HubReconnected(){
        hubConnection.reconnected(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "reconnected: ");
                callBack.hubConnectionCallBack(HubConnectionResponse.RECONNECTED);
            }
        });
    }

    private void HubClosed(){
        hubConnection.closed(new Runnable() {
            @Override
            public void run() {
                Log.d("hubStatus", "closed: ");
                callBack.hubConnectionCallBack(HubConnectionResponse.CLOSED);
            }
        });
    }

    private void createHubProxy(){
        hubProxy  = hubConnection.createHubProxy(HUB_Name);
        Log.d(TAG, "messageReceived: " + hubProxy.toString());
//        hubProxy.subscribe(new Object() {
//            @SuppressWarnings("unused")
//            public void messageReceived(String name, String message) {
//                Log.d(TAG, "messageReceived: " + message);
//            }
//        });
//        hubProxy.on("broadcastMessage", new SubscriptionHandler1<String>(){
//
//
//            @Override
//            public void run(String s) {
//                Log.d(TAG, "hubProxy.on:1 " + s  );
//
//            }
//        },String.class);
//        hubProxy.on("broadcastMessage", new SubscriptionHandler2<String, String>() {
//
//            @Override
//            public void run(String s, String s2) {
//                Log.d(TAG, "hubProxy.on:2 " + s + " " + s2 + " " );
//            }
//        },String.class,String.class);
//        hubProxy.on("broadcastMessage", new SubscriptionHandler3<String, String, String>() {
//
//            @Override
//            public void run(String s, String s2, String s3) {
//                Log.d(TAG, "hubProxy.on:3 " + s + " " + s2 + " " + s3 );
//            }
//        },String.class,String.class,String.class);
//        hubProxy.on("broadcastMessage", new SubscriptionHandler4<String, String, String, String>() {
//
//            @Override
//            public void run(String s, String s2, String s3, String s4) {
//                Log.d(TAG, "hubProxy.on:4 " + s + " " + s2 + " " + s3 );
//            }
//        },String.class,String.class,String.class,String.class);
//
//        //        hubProxy.on("broadcastMessage", new SubscriptionHandler5<String, String, String, String, String>() {
//
//            @Override
//            public void run(String s, String s2, String s3, String s4, String s5) {
//                Log.d(TAG, "hubProxy.on: 5" + s + " " + s2 + " " + s3 );
//            }
//        },String.class,String.class,String.class,String.class,String.class);


//        hubProxy.subscribe("broadcastMessage").addReceivedHandler(new Action<JsonElement[]>() {
//            @Override
//            public void run(JsonElement[] jsonElements) throws Exception {
//                Log.d(TAG, "hubProxy.subscribe: " + jsonElements.toString());
//            }
//        });

    }

    // Public Functions

    public void TypingAlertInvoke(String visitorName){
        visitorId = prefConfig.getData(VISITOR_ID);
        sessionId = prefConfig.getData(SESSION_ID);
        hubProxy.invoke(CRM_INVOKE_METHOD_NAME ,visitorId,visitorName)
                .done(new Action<Void>() {
                    @Override
                    public void run(Void obj) throws Exception {
                        Log.d("hubStatus", "TypingAlertInvoke: successfully");
                    }
                })
                .onError(new ErrorCallback() {
                    @Override
                    public void onError(Throwable error) {
                        // Error handling
                        Log.d("hubStatus", "invoke: " + error.getMessage());

                    }
                });
    }

    public  void VisitorClosedWindowInvoke(){
        visitorId = prefConfig.getData(VISITOR_ID);
        sessionId = prefConfig.getData(SESSION_ID);
        hubProxy.invoke(VISITOR_CLOSED_WINDOW, visitorId,CLOSED_WINDOW_MESSAGE )
                .done(new Action<Void>() {
                    @Override
                    public void run(Void unused) throws Exception {
                        Log.d("hubStatus", "VisitorClosedWindowInvoke: successfully");
                        prefConfig.clear();
                        try {
                            Log.d("hubStatus", "VisitorClosedWindowInvoke: sessionId " + prefConfig.getData(SESSION_ID));

                        }catch (Exception e){
                            Log.d("hubStatus", "Local Storage Cleared");

                        }
                    }
                }).onError(new ErrorCallback() {
                    @Override
                    public void onError(Throwable throwable) {

                    }
                });


    }

    public void HubProxyGroupInvoke(){
        hubProxy.invoke(Constatnts.CRM_GROUP_INVOKE_METHOD_NAME ,"Group1")
                .done(new Action<Void>() {

                    @Override
                    public void run(Void obj) throws Exception {
                        Log.d("hubStatus", "invoke: successfully");
                    }
                })
                .onError(new ErrorCallback() {

                    @Override
                    public void onError(Throwable error) {
                        // Error handling
                        Log.d("hubStatus", "invoke: " + error.getMessage());

                    }
                });

    }

    public void getAuthorizedAndMakeConenction(String token ,String name, String email, String cellNumber){
            if(token.equals(AUTHENTICATION_TOKEN)){

                createHubConnection(name , email, cellNumber);
                 callBack.onAuthentication("token");
            }else {
                callBack.onAuthentication("Your are not authorized");

            }
    }

}
