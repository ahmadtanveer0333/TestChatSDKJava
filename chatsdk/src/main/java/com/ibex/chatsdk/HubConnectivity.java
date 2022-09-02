package com.ibex.chatsdk;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
    }

    public void createHubConnection(String name, String email, String cellNumber){
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

    public void HubConnected(){
        hubConnection.connected(new Runnable() {
            @Override
            public void run() {
                callBack.hubConnectionCallBack(HubConnectionResponse.CONNECTED);
                Log.d(TAG, "connected: ");

            }
        });

    }

    public void HubStart(String name, String email, String cellNumber){
        hubConnection.start()
                .done(new Action<Void>() {
                    @Override
                    public void run(Void obj) throws Exception {
                        Log.d("hubStatus", "start: ");
                        serviceManager.saveVisitor(context,name , email, cellNumber);

                    }
                })
                .onError(new ErrorCallback() {
                    @Override
                    public void onError(Throwable throwable) {

                    }
                });

    }

    public void HubMessageRecieved(){
        hubConnection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(JsonElement jsonElement) {
                Log.d(TAG, "onMessageReceived: " + jsonElement.toString());
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonElement message = jsonElement.getAsJsonObject().get("A");



            }
        });
    }

    public void HubConnectionSlow(){
        hubConnection.connectionSlow(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "connectionSlow: ");
            }
        });
    }

    public void HubReconnecting(){
        hubConnection.reconnecting(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "reconnecting: ");
            }
        });
    }

    public void HubReconnected(){
        hubConnection.reconnected(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "reconnected: ");
            }
        });
    }

    public void HubClosed(){
        hubConnection.closed(new Runnable() {
            @Override
            public void run() {
                Log.d("hubStatus", "closed: ");
            }
        });
    }

    public void createHubProxy(){
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

    public void HubProxyInvoke(int visitorId, String visitorName){
        hubProxy.invoke(Constatnts.CRM_INVOKE_METHOD_NAME ,visitorId,visitorName)
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

}
