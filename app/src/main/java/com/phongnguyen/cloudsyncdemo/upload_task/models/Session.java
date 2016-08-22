package com.phongnguyen.cloudsyncdemo.upload_task.models;

public class Session {

    private int status;
    private String expiry_at;
    private String id;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExpiryDate() {
        return expiry_at;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiry_at = expiryDate;
    }

    public String getSessionId() {
        return id;
    }

    public void setSessionId(String sessionId) {
        this.id = sessionId;
    }
}
