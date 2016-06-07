package com.id11236662.gokeigo.util;

import com.id11236662.gokeigo.model.DataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Endpoints of the Jisho webservice is defined here.
 */

public interface JishoService {
    @GET("words")
    Call<DataResponse> getData(@Query("keyword") String keyword);
}
