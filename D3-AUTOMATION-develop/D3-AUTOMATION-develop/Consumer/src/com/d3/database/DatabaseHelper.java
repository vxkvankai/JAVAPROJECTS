package com.d3.database;

import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.exceptions.D3DatabaseException;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.List;
import javax.annotation.CheckForNull;

/**
 * A Helper class that gets various information from the database
 */
@Slf4j
public class DatabaseHelper {

    private DatabaseHelper() {
    }

    /**
     * Get the all the business names from the user_profile table
     *
     * @return List of all the business names from the db, null if error
     */
    @CheckForNull
    @Step("Get the existing business names from the db")
    public static List<String> getExistingBusinessNames() {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT business_name FROM user_profile WHERE business_name IS NOT NULL";
            return utils.getAllDataFromSelectQuery("business_name", query);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue get list of business names that have already been created", e);
            return null;
        }
    }


    /**
     * Gets local_value string from the l10n_text_resource table at the system level based on the L10N name value given
     * NOTE: Defaults to English Locale
     *
     * @param l10n name value of L10N enum
     * @return The l10n value for the given key
     * @throws D3DatabaseException If a sql error or connection error happens
     */
    @Step("Get {l10n}'s value")
    public static String getL10nValueString(String l10n) throws D3DatabaseException {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT local_value FROM l10n_text_resource WHERE name = ? AND company_id = "
                + "(SELECT id FROM company WHERE bank_structure = 'ROOT')"
                + " AND locale_id = (SELECT id FROM l10n_locale WHERE language_code = 'en') "
                + "ORDER BY local_value ASC";
            String returnString = utils.getDataFromSelectQuery("local_value", query, l10n);

            if (returnString == null) {
                throw new D3DatabaseException("Error getting l10n value");
            } else {
                return returnString;
            }

        } catch (SQLException e) {
            log.error("Error running SQL", e);
            throw new D3DatabaseException("Error getting l10n value");
        }
    }

    /**
     * This method will enable the specified Scheduled job and set it to run every second (* * * * * ?)
     * NOTE: Will set fire_count to 0 before running the job
     *
     * @param job Schedule job to trigger
     */
    @Step("Run the job: {job}")
    public static void runScheduledJob(D3ScheduledJobs job) {
        @Language("SQL") String resetFireCount = "UPDATE job_trigger SET fire_count = 0 WHERE job_name = ?";
        @Language("SQL") String query = "UPDATE job_trigger SET cron_expression ='* * * * * ?', STATUS = 3 WHERE job_name = ?";

        try (DatabaseUtils utils = new DatabaseUtils()) {
            utils.executeUpdateQuery(resetFireCount, job.getJobName());
            utils.executeUpdateQuery(query, job.getJobName());
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error running scheduled job", e);
        }
    }

    /**
     * This method will disable a scheduled job and set the cron expression to (1 1 1 1 1 1)
     *
     * @param job Scheduled job to stop
     */
    @Step("Stop the job: {job}")
    public static void stopScheduledJob(D3ScheduledJobs job) {
        @Language("SQL") String query = "UPDATE job_trigger SET cron_expression ='1 1 1 1 1 1', STATUS = '2' WHERE job_name = ?";
        try (DatabaseUtils utils = new DatabaseUtils()) {
            utils.executeUpdateQuery(query, job.getJobName());
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error stopping scheduled job sql", e);
        }
    }

    /**
     * This method gets the fire_count from the job_trigger table for the specified Scheduled job
     * NOTE: D3ScheduledJobs.MASTER shouldn't use this, since the fire_count will always be 0
     *
     * @param job Schedule job
     * @return true if the fire_count is greater than 0, false otherwise
     */
    @Step("Wait for schedule job: {job} to run")
    public static boolean waitForJobToRun(D3ScheduledJobs job) {
        int currentFireCount;
        @Language("SQL") String query = "SELECT fire_count FROM job_trigger WHERE job_name = ?";
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String data = utils.getDataFromSelectQuery("fire_count", query, job.getJobName());
            if (data == null) {
                log.error("Error getting fire_count for {}, came back null", job.getJobName());
                stopScheduledJob(job);
                return false;
            } else {
                int counter = 0;
                do {
                    currentFireCount = Integer.parseInt(utils.getDataFromSelectQuery("fire_count", query, job.getJobName()));
                    log.info("Waiting for fire_count to be greater than 0 for scheduled job {}. Currently has count of {}", job.getJobName(), currentFireCount);
                    counter++;
                    Thread.sleep(100);
                } while (currentFireCount == 0 && counter < 1000);
                if (counter >= 1000) {
                    log.error("Scheduled Job: {} was never triggered. fire_count never was never greater than 0", job.getJobName());
                    stopScheduledJob(job);
                    return false;
                }
                log.info("Scheduled job {} has been run", job.getJobName());
            }
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting job fire_count", e);
            stopScheduledJob(job);
        } catch (InterruptedException e) {
            log.error("Error sleeping Thread", e);
            Thread.currentThread().interrupt();
        }

        return true;
    }
}

