package com.d3.datawrappers.messages;

import java.io.Serializable;

public abstract class D3Message implements Serializable {

    private String topic;
    private String issue;
    private String subject;
    private String messageBody;

    public String getTopic() {
        return topic;
    }

    public String getIssue() {
        return issue;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessageBody() {
        return messageBody;
    }


    public D3Message(String topic, String issue, String subject, String messageBody) {
        this.topic = topic;
        this.issue = issue;
        this.subject = subject;
        this.messageBody = messageBody;
    }
}
