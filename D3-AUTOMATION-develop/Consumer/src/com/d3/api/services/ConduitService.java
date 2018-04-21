package com.d3.api.services;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ConduitService {
    @POST("d3conduitapp/intraday")
    Call<ResponseBody> sendConduitFile(@Body RequestBody conduitFile);
}
