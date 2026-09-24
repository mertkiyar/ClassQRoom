package com.mrtkyr.classqroom;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public final class ApiErrorReader {
    private ApiErrorReader() {
    }

    public static String message(Response<?> response, String fallback) {
        ResponseBody errorBody = response.errorBody();
        if (errorBody == null) {
            return fallback;
        }
        try (ResponseBody body = errorBody) {
            String message = new JSONObject(body.string()).optString("errorMessage", "");
            return message.trim().isEmpty() ? fallback : message;
        } catch (IOException | org.json.JSONException exception) {
            return fallback;
        }
    }
}
