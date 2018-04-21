package com.d3.database;

import com.d3.datawrappers.messages.SecureMessage;
import com.d3.exceptions.D3DatabaseException;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import javax.annotation.CheckForNull;

/**
 * Helper class that does actions and queries against the database related to Messages
 */
@Slf4j
public class MessageDatabaseHelper {

    private MessageDatabaseHelper() {
    }

    /**
     * This method will get the id for the specified secure message
     *
     * @param message Secure Message to get the id for
     * @return int of secure message id
     */
    @CheckForNull
    @Step("Get the secure {message}'s id value")
    public static Integer getSecureMessageId(SecureMessage message) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT id FROM secure_message WHERE subject = ?";
            String result = utils.getDataFromSelectQuery("id", query, message.getSubject());
            if (result == null) {
                log.error("Error getting secure message id, came back null, {}", query);
                return null;
            }
            return Integer.valueOf(result);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error retrieving id of secure message", e);
            return null;
        }
    }
}
