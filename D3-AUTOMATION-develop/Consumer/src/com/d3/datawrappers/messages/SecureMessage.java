package com.d3.datawrappers.messages;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import com.d3.datawrappers.user.D3User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class SecureMessage extends D3Message {

    private static final String FORMATTED_MESSAGE_BODY = "Message body for %s %s from %s";
    private static final String FORMATTED_MESSAGE_SUBJECT = "%s %s from %s";

    public SecureMessage(String topic, String issue, String subject, String messageBody) {
        super(topic, issue, subject, messageBody);
    }

    /**
     * Creates a random Secure Message
     *
     * @param user User to write a secure message for
     */
    public static SecureMessage createRandomMessage(D3User user) {
        String topic = Topic.getRandom();
        String issue = Issue.getRandom();
        String subject = String.format(FORMATTED_MESSAGE_SUBJECT, topic, issue, user.getLogin());
        String messageBody = String.format(FORMATTED_MESSAGE_BODY, topic, issue, user.getLogin());
        return new SecureMessage(topic, issue, subject, messageBody);
    }


    /**
     * Creates multiple random Secure Messages
     *
     * @param user User to create messages for
     * @param numberOfMessages Total number of messages to create
     * @return List<SecureMessage>
     */
    public static List<SecureMessage> createMultipleSecureMessages(D3User user, int numberOfMessages) {
        List<SecureMessage> messages = new ArrayList<>();
        for (int i = 0; i < numberOfMessages; i++) {
            messages.add(createRandomMessage(user));
        }

        // Filter created Secure Messages and compare by getSubject to avoid duplicate messages created
        return messages.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(SecureMessage::getSubject))), ArrayList::new));
    }
}
