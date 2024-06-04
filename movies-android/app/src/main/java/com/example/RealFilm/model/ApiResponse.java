package com.example.RealFilm.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {

    @SerializedName("code")
    private int code;

    @SerializedName("status")
    private Status status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;

    @SerializedName("accessToken")
    private String accessToken;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Status getStatus() {
        if (status == null) {
            return Status.ERROR;
        }
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
