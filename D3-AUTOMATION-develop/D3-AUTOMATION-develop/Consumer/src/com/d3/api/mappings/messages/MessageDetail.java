package com.d3.api.mappings.messages;

import com.d3.datawrappers.messages.SecureMessage;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDetail {
    private String body;

    public static MessageDetail messageBody(SecureMessage secureMessage) {
        MessageDetail details = new MessageDetail();
        details.setBody(secureMessage.getMessageBody());
        return details;
    }

}
