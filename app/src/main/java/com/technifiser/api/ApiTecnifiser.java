package com.technifiser.api;

import android.content.Context;

import com.technifiser.R;
import com.technifiser.TecnifiserApplication;
import com.technifiser.api.result.FourSquareResult;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by guerra on 11/08/17.
 * Util to call the foursquare service.
 */

public class ApiTecnifiser {

    private Context context;
    private Retrofit retrofit;

    private String foursquareClientID;
    private String foursquareClientSecret;
    private String version;

    public ApiTecnifiser() {
        if (retrofit == null) {
            context = TecnifiserApplication.getInstance();
            retrofit = ApiClient.retrofitAdapterFourSquare();
        }
        foursquareClientID = context.getString(R.string.foursquare_client_id);
        foursquareClientSecret = context.getString(R.string.foursquare_client_secret);
        version = context.getString(R.string.foursquare_version);
    }

    public void search(String query, String latLng, double userLLAcc,
                       final SearchByQueryCallback searchByQueryCallback) {
        ApiService foursquare = retrofit.create(ApiService.class);
        Call<FourSquareResult> sushiCall = foursquare.searchByQuery(
                foursquareClientID,
                foursquareClientSecret,
                version,
                query,
                latLng,
                userLLAcc);

        sushiCall.enqueue(new Callback<FourSquareResult>() {
            @Override
            public void onResponse(Call<FourSquareResult> call, Response<FourSquareResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMeta().getCode() == 200) {
                        searchByQueryCallback.searchOnSuccess(response.body().getResponse());
                    } else {
                        try {
                            searchByQueryCallback.searchError(response.body().getMeta().getCode(), response.errorBody().string());
                        } catch (IOException e) {
                            searchByQueryCallback.searchError(response.body().getMeta().getCode(), "error");
                            e.printStackTrace();
                        }
                    }
                } else {
                    searchByQueryCallback.searchError(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<FourSquareResult> call, Throwable t) {
                searchByQueryCallback.searchError(-1, t.getMessage());
            }
        });
    }

}
