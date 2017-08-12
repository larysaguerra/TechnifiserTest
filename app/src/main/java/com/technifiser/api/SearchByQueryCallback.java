package com.technifiser.api;

import com.technifiser.api.result.Response;

/**
 * Created by guerra on 11/08/17.
 * Callback for response search by query
 */

public interface SearchByQueryCallback {
    void searchOnSuccess(Response response);
    void searchError(int code, String mjs );
}
