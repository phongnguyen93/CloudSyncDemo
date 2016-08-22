package com.phongnguyen.cloudsyncdemo.upload_task.models;

/**
 * Created by phongnguyen on 8/23/16.
 */
public class ApiResponse {
    private int error;
    private String message;
    private String data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
