package com.phongnguyen.cloudsyncdemo.upload_task.interfaces;


import com.phongnguyen.cloudsyncdemo.upload_task.models.ApiResponse;
import com.phongnguyen.cloudsyncdemo.upload_task.models.Session;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    /*
        Request new upload session
     */

    @GET("/chunked_upload/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Session> requestUploadSession(@Header("Authorization") String authorization
            , @Query("name") String fileName
            , @Query("dest") String fileDest
            , @Query("reported_total_size") Long totalSize
            , @Query("chunks") int totalChunk);

    /*
        Upload
     */
    @POST("/chunked_upload/")
    @Multipart
    Call<ApiResponse> uploadFile(@Header("Authorization") String authorization
            , @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);
}