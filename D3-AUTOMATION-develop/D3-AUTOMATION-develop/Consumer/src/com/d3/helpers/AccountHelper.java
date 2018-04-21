package com.d3.helpers;

import com.d3.database.AuditDatabaseHelper;
import com.d3.database.DatabaseHelper;
import com.d3.datawrappers.company.JFairyCompany;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.datawrappers.user.D3User;
import com.d3.monitoring.audits.AuditAttribute;
import com.d3.monitoring.audits.Audits;
import com.d3.support.internal.Element;
import com.mifmif.common.regex.Generex;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.data.MapBasedDataMaster;
import io.codearte.jfairy.producer.company.CompanyProperties;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.person.PersonProvider;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

@Slf4j
public class AccountHelper {

    private static final List<String> ON_US_ROUTING_NUMBERS =
            new ArrayList<>(Arrays.asList("302075018", "012345678", "211370545", "011400071", "054001725", "031201360"));

    private AccountHelper() {
    }

    public static String getCompanyName() {
        String ymlFile = String.format("%s_%s.yml", "jfairy", Locale.ENGLISH.getLanguage());
        Random randomGenerator = new Random();
        Set<String> companyNames = new HashSet<>();
        MapBasedDataMaster dataMaster = new MapBasedDataMaster(Fairy.create().baseProducer());

        try {
            dataMaster.readResources(ymlFile);
        } catch (IOException e) {
            log.error("Error trying to read resources from {}: {}", ymlFile, e);
        }

        for (int x = 0; x < 100; x++) {
            JFairyCompany company = JFairyCompany.createNewCompany(CompanyProperties.CompanyProperty.withName(
                String.format("%s %s", dataMaster.getValuesOfType(PersonProvider.LAST_NAME, Person.Sex.MALE.toString()),
                    dataMaster.getRandomValue("companySuffixes"))));
            companyNames.add(company.getName());
        }

        //noinspection ConstantConditions
        companyNames.removeAll(DatabaseHelper.getExistingBusinessNames());
        int index = randomGenerator.nextInt(companyNames.size());
        return new ArrayList<>(companyNames).get(index);
    }

    public static String generatePassword() {
        Generex generex = new Generex("([0-9]{1,2}[a-z]{1,3}[A-Z]{1,3}[@!#$%^&*()+=]{1,2})");
        String randomStr = generex.random(7, 25);
        log.info("Generated Password: {}", randomStr);
        return randomStr;
    }

    public static String getHiddenAccountString(String accountNum) {
        return getHiddenAccountString(accountNum, 4);
    }

    public static String getHiddenAccountString(String accountNum, int charsToShow) {
        int maskedLength = accountNum.length() - charsToShow;
        return StringUtils.overlay(accountNum, StringUtils.repeat("*", maskedLength), 0, maskedLength);
    }

    public static String getFormattedPhoneNumber(String phone) {
        return String.format("(%s) %s-%s", phone.substring(0, 3), phone.substring(3, 6), phone.substring(6, 10));
    }

    public static boolean verifyAccounts(String account, List<Element> dropdown, Function<Element, String> textFunc) {
        ArrayList<String> accounts = new ArrayList<>();
        accounts.add(account);
        return verifyAccounts(accounts, dropdown, textFunc, false);
    }

    public static boolean verifyAccounts(List<String> userAccounts, List<Element> dropdown, Function<Element, String> textFunc) {
        return verifyAccounts(userAccounts, dropdown, textFunc, false);
    }

    @Step("Verify that the given accounts are the same as the expected ones")
    public static boolean verifyAccounts(List<String> userAccounts, List<Element> dropdown, Function<Element, String> textFunc, boolean onceOnly) {
        List<String> userAccountsCopy = new ArrayList<>(userAccounts);

        final List<String> ignoredAccounts = new ArrayList<>();
        ignoredAccounts.add("Select Account");

        String errorMsg = "Account option: {} should not be available to the user";
        for (Element account : dropdown) {
            String availableAccount = StringUtils.substringBefore(textFunc.apply(account), "$").trim();
            log.info("Looking for user account {}", availableAccount);

            if (onceOnly) {
                if (!userAccountsCopy.remove(availableAccount)) {
                    log.warn(errorMsg, availableAccount);
                    return false;
                }
            } else {
                if (!ignoredAccounts.contains(availableAccount) && !userAccountsCopy.contains(availableAccount)) {
                    log.warn(errorMsg, availableAccount);
                    return false;
                }
            }
        }

        if (!onceOnly || userAccountsCopy.isEmpty()) {
            return true;
        } else {
            log.warn("Not all the elements in the the user list were found in the dropdown: {}", userAccountsCopy);
            return false;
        }
    }

    public static boolean verifyRecipients(List<String> recipients, List<Element> recipientsDisplayed) {
        recipients.addAll(Arrays.asList("Select Recipient", "Select Account"));
        String errorMsg = "Recipient with name: {} should not be available to the user";
        for (Element recipient : recipientsDisplayed) {
            String availableRecipient = StringUtils.substringBefore(recipient.getText(), "Recipient Type").trim();
            log.info("Looking for user recipient {}", availableRecipient);
            if (!recipients.contains(availableRecipient)) {
                log.warn(errorMsg, availableRecipient);
                return false;
            }
        }
        return true;
    }

    /**
     * Compares the map(post-action list - pre-action list) to a list of expected values (Action here meaning include/exclude account, etc)
     *
     * @param oldInfoList Pre-action list of Doubles
     * @param currentInfoList Post-action list of Doubles
     * @param expectedList List of expected values (BigDecimal)
     */
    public static boolean compareDashboardLists(List<BigDecimal> oldInfoList, List<BigDecimal> currentInfoList, List<BigDecimal> expectedList) {
        for (int i = 0; i < oldInfoList.size(); ++i) {
            BigDecimal actual = currentInfoList.get(i).subtract(oldInfoList.get(i)).stripTrailingZeros();
            BigDecimal expected = expectedList.get(i).stripTrailingZeros();
            if (!DateAndCurrencyHelper.compareCurrency(actual, expected, BigDecimal.valueOf(1))) {
                log.error("Actual: {}, expected: {}", actual, expectedList.get(i).stripTrailingZeros());
                return false;
            }
        }
        return true;
    }

    /**
     * Compares the Net Worth/Current month status totals before and after unhide accounts and verify if it change
     *
     * @param oldInfoList Pre-action list of Doubles
     * @param currentInfoList Post-action list of Doubles
     */
    public static boolean haveTotalsChanged(List<BigDecimal> oldInfoList, List<BigDecimal> currentInfoList) {
        for (int i = 0; i < oldInfoList.size(); ++i) {
            BigDecimal actual = oldInfoList.get(i).stripTrailingZeros();
            log.info("Actual: {}, expected: {}", actual, currentInfoList.get(i).stripTrailingZeros());

            BigDecimal expected = currentInfoList.get(i).stripTrailingZeros();
            if (!actual.equals(expected)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a random On-Us routing number from the list of static routing numbers
     */
    public static String getRandomOnUsRoutingNumber() {
        return RandomHelper.getRandomElementFromList(ON_US_ROUTING_NUMBERS);
    }
    

    /*
    * This method return the string value of the status for transaction scheduled  for future dates date
    * @Param D3User, ProviderOption
    * @Return String PROCESSED  or  PENDING
    * NOTE:
    * Since the Bill pay transactions cannot be scheduled to the current date it will be treated as 
    * scheduled for future date
    */
    public static String getAuditFutureTransactionStatus(D3User user,  ProviderOption providerOption) {
        switch (providerOption) {
            case CORE_ON_US_TRANSFER:
                return AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.ON_US_TRANSFER_SCHEDULED)
                        .getOrDefault(AuditAttribute.STATUS_CODE.getAttributeName(), "");
            case FEDWIRE_TRANSFER:
                return AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.FEDWIRE_TRANSFER_SCHEDULED)
                        .getOrDefault(AuditAttribute.STATUS_CODE.getAttributeName(), "");
            case ACH_TRANSFER:
                return AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.ACH_TRANSFER_SCHEDULED)
                        .getOrDefault(AuditAttribute.STATUS_CODE.getAttributeName(), "");
            default:
                return AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.PAYMENT_SCHEDULED)
                        .getOrDefault(AuditAttribute.STATUS_CODE.getAttributeName(), "");
        }
    }

    /**
     * Get the list of on-us routing numbers
     */
    public static List<String> getOnUsRoutingNumbers() {
        return new ArrayList<>(ON_US_ROUTING_NUMBERS);
    }
}
