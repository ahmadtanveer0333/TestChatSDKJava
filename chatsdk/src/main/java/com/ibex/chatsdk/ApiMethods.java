package com.ibex.chatsdk;

import com.ibex.chatsdk.model.ImageSendResponse;
import com.ibex.chatsdk.model.SaveVisitorChat;
import com.ibex.chatsdk.model.SaveVisitorModel;
import com.ibex.chatsdk.model.VisitorMessageDetail;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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



    @POST("UploadFiles?")
    @Multipart
    Call<ImageSendResponse> uploadFiles(@Query("sessionId") int  sessionId,
                                        @Query("visitorId") int visitorId,
                                        @Part MultipartBody.Part image);


}

