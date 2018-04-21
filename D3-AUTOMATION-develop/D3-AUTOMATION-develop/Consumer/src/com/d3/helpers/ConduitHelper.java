package com.d3.helpers;

import com.d3.conduit.ConduitBuilder;
import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Slf4j
public class ConduitHelper {

    private ConduitHelper() {}

    public static ConduitBuilder getConduitForUser(D3User user) throws ConduitException {
        return new ConduitBuilder()
                .addAccounts(user.getAccounts())
                .addUsers(user);
    }

    public static String getConduitForUserStr(D3User user) throws ConduitException {
        return getConduitForUser(user).getDocument();
    }

    /**
     * Send a user information via conduit to the server and wait for that user to be created
     * @param user user to create
     * @param conduitApiUrl Api of conduit
     * @param apiVersion version of the api to use
     * @return True if successful, false otherwise
     */
    public static boolean sendUserToServerAndWait(@Nonnull D3User user, String conduitApiUrl, String apiVersion) {
        ConduitBuilder conduitInfo = sendUserToServer(user, conduitApiUrl, apiVersion);

        if (conduitInfo == null) {
            return false;
        }

        int userId = UserDatabaseHelper.waitForUserToInDB(user);

        if (userId == -1) {
            log.error("Error uploading the user, see the logs/or conduit file");
            saveConduitFile(conduitInfo);
            return false;
        } else {
            // wait until user account is in the db
            UserDatabaseHelper.waitForUserAccountId(user, user.getFirstAccount());
            return true;
        }
    }

    /**
     * Send the user conduit file to the server
     *
     * @param user user to add
     * @param conduitApiUrl conduit api url to use (will not have version information in it)
     * @return The Conduit information if successful, null otherwise
     */
    @CheckForNull
    public static ConduitBuilder sendUserToServer(@Nonnull D3User user, String conduitApiUrl, String apiVersion) {
        log.info("Creating user {}", user);
        ConduitBuilder builder = null;
        try {
            builder = getConduitForUser(user);
            builder.uploadFileToApi(user.getCuID(), conduitApiUrl, apiVersion);
            return builder;
        } catch (ConduitException e) {
            log.error("Error with the conduit file", e);
            saveConduitFile(builder);
            return null;
        }
    }

    private static void saveConduitFile(@Nullable ConduitBuilder builder) {
        String localConduitFilepath = builder != null ? builder.saveConduitFileLocally() : null;
        if (localConduitFilepath != null) {
            log.error("User has not yet been created. Check conduit to see what went wrong: {}", localConduitFilepath);
        } else {
            log.error("User has not yet been created. Tried to save conduit file locally but failed");
        }
    }
}
