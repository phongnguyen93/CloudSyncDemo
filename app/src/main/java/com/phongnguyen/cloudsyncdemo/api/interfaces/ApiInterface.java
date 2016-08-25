package com.phongnguyen.cloudsyncdemo.api.interfaces;


import com.phongnguyen.cloudsyncdemo.models.ApiResponse;
import com.phongnguyen.cloudsyncdemo.models.MyFile;
import com.phongnguyen.cloudsyncdemo.models.MyFolder;
import com.phongnguyen.cloudsyncdemo.models.Session;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
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


    /*
        Browse files
     */
    @POST("/browse/")
    Call<MyFolder> browse(@Header("Authorization") String authorization,
                          @Query("file_path") String directory);

    /*
        Create new folder
     */
    @POST("/cmd/{action}/")
    @Multipart
    Call<MyFile> folderAction(@Header("Authorization") String authorization, @Path("action") String action,
                              @PartMap Map<String,RequestBody> params);
}