package com.magarex.easyly.Models;

/**
 * Created by HP on 12/23/2017.
 */

public class ChatModel {

    public String message;
    public Boolean isSend;

    public ChatModel(String message, Boolean isSend) {
        this.message = message;
        this.isSend = isSend;
    }

    public ChatModel() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSend() {
        return isSend;
    }

    public void setSend(Boolean send) {
        isSend = send;
    }
}
