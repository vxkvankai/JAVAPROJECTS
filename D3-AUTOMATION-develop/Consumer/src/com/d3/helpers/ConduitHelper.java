package com.d3.helpers;

import com.d3.conduit.ConduitBuilder;
import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

        int userId = DatabaseUtils.waitForUserToInDB(user);

        if (userId == -1) {
            LoggerFactory.getLogger(ConduitHelper.class).error("Error uploading the user, see the logs/or conduit file");
            saveConduitFile(conduitInfo);
            return false;
        } else {
            // wait until user account is in the db
            DatabaseUtils.waitForUserAccountId(user, user.getFirstAccount());
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
        Logger logger = LoggerFactory.getLogger(ConduitHelper.class);
        logger.info("Creating user {}", user);
        ConduitBuilder builder = null;
        try {
            builder = getConduitForUser(user);
            builder.uploadFileToApi(user.getCuID(), conduitApiUrl, apiVersion);
            return builder;
        } catch (D3ApiException e) {
            logger.error("Error uploading file: ", e);
            saveConduitFile(builder);
            return null;
        } catch (ConduitException e) {
            logger.error("Error with the conduit file", e);
            saveConduitFile(builder);
            return null;
        }
    }

    private static void saveConduitFile(@Nullable ConduitBuilder builder) {
        String localConduitFilepath = builder != null ? builder.saveConduitFileLocally() : null;
        if (localConduitFilepath != null) {
            LoggerFactory.getLogger(ConduitHelper.class).error("User has not yet been created. Check conduit to see what went wrong: {}", localConduitFilepath);
        } else {
            LoggerFactory.getLogger(ConduitHelper.class).error("User has not yet been created. Tried to save conduit file locally but failed");
        }
    }
}
