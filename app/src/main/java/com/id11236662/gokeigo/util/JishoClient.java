package com.id11236662.gokeigo.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This singleton helper will create a client to access the web service and convert the responses
 * into POJOs.
 */
public class JishoClient {
    private static final String BASE_URL = "http://jisho.org/api/v1/search/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
