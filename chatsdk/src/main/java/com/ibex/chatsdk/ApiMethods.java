package com.ibex.chatsdk;

import com.ibex.chatsdk.model.SaveVisitorChat;
import com.ibex.chatsdk.model.SaveVisitorModel;
import com.ibex.chatsdk.model.VisitorMessageDetail;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiMethods {

    @FormUrlEncoded
    @POST("SaveVisitor")
    Call<SaveVisitorModel> saveVisitor(@Field("name") String name,
                                       @Field("email") String email,
                                       @Field("PhoneNumber") String cellNumber);


    @FormUrlEncoded
    @POST("SaveVisitorChat")
    Call<SaveVisitorChat> saveVisitorChat(@Field("WcVisitorSessionId") int sessionId,
                                          @Field("wcVisitorId") int visitorId,
                                          @Field("message") String message);
    @FormUrlEncoded
    @POST("GetDetailOfVisitorChatSessionWise")
    Call<VisitorMessageDetail> visitorChatDetail(@Field("VisitorId") int visitorId,
                                                 @Field("SessionId") int sessionId);

}

