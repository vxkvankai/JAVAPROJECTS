package com.d3.api.mappings.messages;

import com.d3.datawrappers.messages.SecureMessage;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddSecureMessage {

    private String topic;
    private String issue;
    private String subject;
    private List<MessageDetail> messageDetails;

    public AddSecureMessage(SecureMessage message) {
        this.topic = message.getTopic();
        this.issue = message.getIssue();
        this.messageDetails = new ArrayList<>();
        this.messageDetails.add(MessageDetail.messageBody(message));
        this.subject = message.getSubject();
    }

}
