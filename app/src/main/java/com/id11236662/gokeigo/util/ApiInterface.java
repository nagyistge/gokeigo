package com.id11236662.gokeigo.util;

import com.id11236662.gokeigo.data.EntriesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Endpoints of the API webservice is defined here.
 */
public interface ApiInterface {
    @GET("words")
    Call<EntriesResponse> getEntries(@Query("keyword") String keyword);
}
