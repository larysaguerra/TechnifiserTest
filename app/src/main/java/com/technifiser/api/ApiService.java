package com.technifiser.api;


import com.technifiser.api.result.FourSquareResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by guerra on 11/08/17.
 * Search endpoint
 */

public interface ApiService {

    @GET("venues/explore")
    Call<FourSquareResult> searchByQuery(@Query("client_id") String clientID,
                                         @Query("client_secret") String clientSecret,
                                         @Query("v") String version,
                                         @Query("query") String query,
                                         @Query("ll") String ll,
                                         @Query("llAcc") double llAcc);

}
