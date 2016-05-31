package com.id11236662.gokeigo.util;

import com.id11236662.gokeigo.model.EntriesResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Endpoints of the API webservice is defined here.
 */
public interface JishoService {
    @GET("words")
    Call<EntriesResponse> getEntries(@Query("keyword") String keyword);

    @GET("words")
    Call<ResponseBody> getJSON(@Query("keyword") String keyword);

}
