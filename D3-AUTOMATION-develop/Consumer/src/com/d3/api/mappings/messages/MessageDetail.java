package com.d3.api.mappings.messages;

import com.d3.datawrappers.messages.SecureMessage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageDetail {

    @SerializedName("body")
    @Expose
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static MessageDetail messageBody(SecureMessage secureMessage) {
        MessageDetail details = new MessageDetail();
        details.setBody(secureMessage.getMessageBody());
        return details;
    }

}
