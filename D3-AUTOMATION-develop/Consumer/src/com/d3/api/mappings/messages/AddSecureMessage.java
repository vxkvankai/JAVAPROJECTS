package com.d3.api.mappings.messages;

import com.d3.datawrappers.messages.SecureMessage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AddSecureMessage {

    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("issue")
    @Expose
    private String issue;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("messageDetails")
    @Expose
    private List<MessageDetail> messageDetails = null;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<MessageDetail> getMessageDetails() {
        return messageDetails;
    }

    public void setMessageDetails(List<MessageDetail> messageDetails) {
        this.messageDetails = messageDetails;
    }

    public AddSecureMessage(SecureMessage message) {
        this.topic = message.getTopic();
        this.issue = message.getIssue();
        this.messageDetails = new ArrayList<>();
        this.messageDetails.add(MessageDetail.messageBody(message));
        this.subject = message.getSubject();
    }

}
