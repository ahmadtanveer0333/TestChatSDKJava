package com.ibex.chatsdk;

import android.content.Context;
import android.util.Log;

import com.ibex.chatsdk.model.SaveVisitorChat;
import com.ibex.chatsdk.model.SaveVisitorModel;
import com.ibex.chatsdk.model.VisitorMessageDetail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceManager {

    private static Retrofit retrofitInstance , retrofitToken;
    private static String BASE_URL= Constatnts.BASE_URL;
    String SERVER_PATH ,token;
    Context context;
    CallBack callBack;


    public ServiceManager(Context context, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public ServiceManager(Context context) {
        this.context = context;

    }

    public static Retrofit getRetrofitInstance(){
        if(retrofitInstance == null){
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofitInstance;
    }

    ApiMethods apiMethods = ServiceManager.getRetrofitInstance().create(ApiMethods.class);

    public void saveVisitor(Context context, String name , String email , String number){
        Call<SaveVisitorModel> call = apiMethods.saveVisitor(name,email,number);
        call.enqueue(new Callback<SaveVisitorModel>() {
            @Override
            public void onResponse(Call<SaveVisitorModel> call, Response<SaveVisitorModel> response) {
                SaveVisitorModel saveVisitorModel = response.body();
                //    Log.d("apiresponse", "onResponse: " + saveVisitorModel.getModel().getId());
                Log.d("apiresponse", "saveVisitor: visitorID == " + saveVisitorModel.getModel().getId());
                Log.d("apiresponse", "saveVisitor: sessionId == " + saveVisitorModel.getModel().getVisitorSession().getId());

                callBack.messageCallBack(saveVisitorModel, false);
                callBack.idCallBack(saveVisitorModel.getModel().getId(),saveVisitorModel.getModel().getVisitorSession().getId());

            }

            @Override
            public void onFailure(Call<SaveVisitorModel> call, Throwable t) {
                Log.d("apiresponse", "onResponse: " + t.getMessage());

            }
        });
    }

    public void saveVisitorChat(Context context , int sessionId , int visitorId , String message){
        Call<SaveVisitorChat> call = apiMethods.saveVisitorChat(sessionId,visitorId,message);
        call.enqueue(new Callback<SaveVisitorChat>() {
            @Override
            public void onResponse(Call<SaveVisitorChat> call, Response<SaveVisitorChat> response) {
                SaveVisitorChat saveVisitorChat = response.body();
                callBack.messageCallBack(saveVisitorChat, true);
                Log.d("apiresponse", "saveVisitorChat: sessionID == " + sessionId);
                Log.d("apiresponse", "saveVisitorChat: visitorID == " + visitorId);
                //      Log.d("apiresponse", "saveVisitorChat: " + saveVisitorChat.getWcVisitorId());
                //  callBack.messageCallBack(saveVisitorChat);

            }
            @Override
            public void onFailure(Call<SaveVisitorChat> call, Throwable t) {
                Log.d("apiresponse", "onResponse: " + t.getMessage());

            }
        });


    }

    public void visitorChatDetail(Context context, int visitorId, int sessionId ){
        Call<VisitorMessageDetail> call = apiMethods.visitorChatDetail(visitorId, sessionId);
        call.enqueue(new Callback<VisitorMessageDetail>() {
            @Override
            public void onResponse(Call<VisitorMessageDetail> call, Response<VisitorMessageDetail> response) {
                VisitorMessageDetail messageDetail = response.body();
                callBack.messageCallBack(messageDetail, false);
                Log.d("apiresponse", "visitorChatDetail: " + response.code());
//                Log.d("apiresponse", "visitorChatDetail: " + messageDetail.getVisitorId());
//                Log.d("apiresponse", "visitorChatDetail: " + messageDetail.getVisitorSession());
//                Log.d("apiresponse", "visitorChatDetail: " + messageDetail.getWebChatDetialVisitor().get(0).getMessage());
//                Log.d("apiresponse", "visitorChatDetail: " + messageDetail.getWebChatDetialVisitor().get(0).getReply());
//                Log.d("apiresponse", "visitorChatDetail: " + messageDetail.getWebChatDetialVisitor().get(1).getMessage());
//                Log.d("apiresponse", "visitorChatDetail: " + messageDetail.getWebChatDetialVisitor().get(1).getReply());
//                Log.d("apiresponse", "visitorChatDetail: " + messageDetail.getWebChatDetialVisitor().get(2).getMessage());
//                Log.d("apiresponse", "visitorChatDetail: " + messageDetail.getWebChatDetialVisitor().get(2).getReply());

            }

            @Override
            public void onFailure(Call<VisitorMessageDetail> call, Throwable t) {
                Log.d("apiresponse", "visitorChatDetail: " + t.getMessage());

            }
        });
    }




}
