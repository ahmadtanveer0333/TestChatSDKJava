package com.ibex.chatsdk;

import static com.ibex.chatsdk.Constatnts.SESSION_ID;
import static com.ibex.chatsdk.Constatnts.URL;
import static com.ibex.chatsdk.Constatnts.VISITOR_ID;

import android.content.Context;
import android.util.Log;

import com.ibex.chatsdk.model.ImageSendResponse;
import com.ibex.chatsdk.model.Message;
import com.ibex.chatsdk.model.SaveVisitorChat;
import com.ibex.chatsdk.model.SaveVisitorModel;
import com.ibex.chatsdk.model.VisitorMessageDetail;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    ArrayList<Message> messageList = new ArrayList<>();
    PrefConfig prefConfig;
    int visitorId ;
    int sessionId;


    public ServiceManager(Context context, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        prefConfig = new PrefConfig(context);
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

                prefConfig.save(VISITOR_ID,saveVisitorModel.getModel().getId());
                prefConfig.save(SESSION_ID,saveVisitorModel.getModel().getVisitorSession().getId());
      //          callBack.messageCallBack(saveVisitorModel, false, false);
                callBack.onSaveVisitor(response.message(),saveVisitorModel.getModel().getId(), saveVisitorModel.getModel().getVisitorSession().getId());
       //         callBack.idCallBack(saveVisitorModel.getModel().getId(),saveVisitorModel.getModel().getVisitorSession().getId());

            }

            @Override
            public void onFailure(Call<SaveVisitorModel> call, Throwable t) {
                Log.d("apiresponse", "onResponse: " + t.getMessage());
                callBack.onSaveVisitor(t.getMessage(),0, 0);


            }
        });
    }

    public void sendMessage(String message){
        try {
            this.visitorId = prefConfig.getData(VISITOR_ID);
            this.sessionId = prefConfig.getData(SESSION_ID);
            Call<SaveVisitorChat> call = apiMethods.saveVisitorChat(sessionId, visitorId, message);
            call.enqueue(new Callback<SaveVisitorChat>() {
                @Override
                public void onResponse(Call<SaveVisitorChat> call, Response<SaveVisitorChat> response) {
                    SaveVisitorChat saveVisitorChat = response.body();
                    callBack.messageCallBack(saveVisitorChat, true, false);
                    Log.d("apiresponse", "saveVisitorChat: sessionID == " + sessionId);
                    Log.d("apiresponse", "saveVisitorChat: visitorID == " + visitorId);
                    //      Log.d("apiresponse", "saveVisitorChat: " + saveVisitorChat.getWcVisitorId());
                    //  callBack.messageCallBack(saveVisitorChat);

                    callBack.onsendMessage(response.message());

                }

                @Override
                public void onFailure(Call<SaveVisitorChat> call, Throwable t) {
                    Log.d("apiresponse", "onResponse: " + t.getMessage());
                    callBack.onsendMessage(t.getMessage());

                }
            });
        }catch (Exception e ){
            callBack.onsendMessage(e.getMessage());
        }

    }

    public void retrievePreviousChat(){
        try {
             visitorId = prefConfig.getData(VISITOR_ID);
             sessionId = prefConfig.getData(SESSION_ID);
            Call<VisitorMessageDetail> call = apiMethods.visitorChatDetail(visitorId, sessionId);
            call.enqueue(new Callback<VisitorMessageDetail>() {
                @Override
                public void onResponse(Call<VisitorMessageDetail> call, Response<VisitorMessageDetail> response) {
                    VisitorMessageDetail messageDetail = response.body();
                    //     callBack.messageCallBack(messageDetail, false, true);
                    Log.d("apiresponse", "visitorChatDetail: " + response.code());
                    if (messageDetail.getWebChatDetialVisitor().size() > 0) {

                        for (int i = 0; i < messageDetail.getWebChatDetialVisitor().size(); i++) {
                            Message message = new Message();
                            //        Log.d("apiresponse", "messages: " + messageDetail.getWebChatDetialVisitor().get(i).getMessage());
                            message.setMessage(messageDetail.getWebChatDetialVisitor().get(i).getMessage());
                            message.setReply(messageDetail.getWebChatDetialVisitor().get(i).getReply());
                            message.setMediaFile(messageDetail.getWebChatDetialVisitor().get(i).getMediaFile());
                            message.setUrl(messageDetail.getWebChatDetialVisitor().get(i).getUrl());
                            messageList.add(message);
                        }
                        callBack.onSaveVisitor(response.message(), visitorId, sessionId);
                     //   callBack.messageCallBack(messageList, false, true);
                        callBack.onretrievePreviousChat(response.message(), messageList);

                    }

                }

                @Override
                public void onFailure(Call<VisitorMessageDetail> call, Throwable t) {
                    Log.d("apiresponse", "visitorChatDetail: " + t.getMessage());
                    callBack.onretrievePreviousChat(t.getMessage(), null);

                }
            });


        }catch (Exception e){
            callBack.onretrievePreviousChat(e.getMessage(), null);
        }
    }

    public void sendFile( File file){

        try {
            visitorId = prefConfig.getData(VISITOR_ID);
            sessionId = prefConfig.getData(SESSION_ID);
            // RequestBody  requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            Call<ImageSendResponse> call = apiMethods.uploadFiles(sessionId, visitorId, body);
            call.enqueue(new Callback<ImageSendResponse>() {
                @Override
                public void onResponse(Call<ImageSendResponse> call, Response<ImageSendResponse> response) {
                    Log.d("apiresponse", "sendImage: " + response.body().getUrl());
                    Log.d("apiresponse", "sendImage: " + response.message());
                    callBack.onFileSent(response.message(),URL+ response.body().getUrl());
                }

                @Override
                public void onFailure(Call<ImageSendResponse> call, Throwable t) {
                    Log.d("apiresponse", "sendImage: onFailure" + t.getMessage());
                    callBack.onFileSent(t.getMessage(), null);
                }


            });
        }catch (Exception e ){
            callBack.onFileSent(e.getMessage(), null);
        }

    }






}
