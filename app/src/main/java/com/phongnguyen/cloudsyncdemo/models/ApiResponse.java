package com.phongnguyen.cloudsyncdemo.models;

/**
 * Created by phongnguyen on 8/23/16.
 */
public class ApiResponse {
    private int error;
    private String status;
    private String data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return status;
    }

    public void setMessage(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
