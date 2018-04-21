package com.d3.datawrappers.user.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public enum UserServices {
    BUDGET("nav.planning.budget", AccessLevel.VIEW_ONLY),
    CATEGORIES("nav.settings.categories", AccessLevel.FULL_ACCESS),
    EBILLS("nav.money.movement.ebills", AccessLevel.FULL_ACCESS),
    GOALS("nav.planning.goals", AccessLevel.VIEW_ONLY),
    RECIPIENTS("nav.money.movement.recipients", AccessLevel.FULL_ACCESS),
    RULES("nav.settings.rules", AccessLevel.FULL_ACCESS);

    private String l10nKey;
    private AccessLevel maxAccess;

    UserServices(String l10nKey, AccessLevel maxAccess) {
        this.l10nKey = l10nKey;
        this.maxAccess = maxAccess;
    }

    /**
     * Gets a List of UserServices that Secondary User would have access to
     *
     * @return List of All User Services
     */
    public static List<UserServices> getAllServices() {
        return Arrays.asList(UserServices.values());
    }

    /**
     * Gets a random List of UserServices Secondary User would have access to
     * Gets all UserServices, shuffles the list, then selects 2 random UserServices
     *
     * @return List 2 random UserServices
     */
    public static List<UserServices> getRandomServices() {
        List<UserServices> userServices = Arrays.stream(UserServices.values()).collect(Collectors.toList());
        Collections.shuffle(userServices);
        return userServices.stream().limit(2).collect(Collectors.toList());
    }

    /**
     * Gets an empty list so Secondary User would have No Access to any of the UserServices
     *
     * @return empty list
     */
    public static List<UserServices> noAccess() {
        return Collections.emptyList();
    }

    /**
     * List of Lists containing different UserServices
     *
     * @return List of Lists of different UserServices
     */
    public static List<List<UserServices>> listsOfAccountPermissions() {
        return Arrays.asList(getAllServices(), getRandomServices(), noAccess());
    }

    /**
     * Get a random List of UserServices to assign a secondary user (No Access, All Services, Random Services)
     *
     * @return Random list of UserServices
     */
    public static List<UserServices> getRandomListOfUserServices() {
        return listsOfAccountPermissions().get(ThreadLocalRandom.current().nextInt(listsOfAccountPermissions().size()));
    }

    public String getServiceL10nKey() {
        return l10nKey;
    }

    public AccessLevel getMaxAccess() {
        return maxAccess;
    }
}
