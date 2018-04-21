package com.d3.helpers;

import org.joda.time.DateTime;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;

public class RandomHelper {

    private RandomHelper() {
    }

    /**
     * Returns a random string of length len using the characters from chars
     *
     * @param len length of the wanted string
     * @param chars The characters used in selecting the random string
     * @return The random string
     */
    private static String getRandomString(int len, String chars) {
        StringBuilder sb = new StringBuilder(len);
        SecureRandom rnd = new SecureRandom();
        for (int i = 0; i < len; ++i) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * Returns a random number of type String.
     *
     * @param digits Length of the string
     * @param zeroIncluded Should zero be included in the randomly generated number
     * @return The random string of numbers
     */
    public static String getRandomNumberString(int digits, boolean zeroIncluded) {
        String numericChars = zeroIncluded ? "0123456789" : "123456789";
        return getRandomString(digits, numericChars);
    }


    /**
     * Returns a random double
     *
     * @param min Minimum the number will be
     * @param max Maximum the number will be
     */
    public static Double getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextDouble(min, max + 1.0);
    }

    public static Integer getRandomNumberInt(int min, int max) {
        return getRandomNumber(min, max).intValue();
    }

    /**
     * Returns a random string of length len
     *
     * @param len length of the wanted string
     * @return The random string
     */
    public static String getRandomString(int len) {
        final String alphaNumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        return getRandomString(len, alphaNumeric);
    }

    /**
     * Get a random date that's in the future (up to 1 year)
     */
    public static DateTime getRandomFutureDate() {
        DateTime today = DateTime.now();
        long todayMillis = today.getMillis();
        long oneYearLater = today.withYear(today.getYear() + 1).getMillis();
        long diff = oneYearLater - todayMillis;

        return new DateTime(todayMillis + (long) (Math.random() * diff));
    }

    /**
     * Get a random date that's in the past (up to 1 year)
     */
    public static DateTime getRandomPastDate() {
        DateTime today = DateTime.now();
        long todayMillis = today.getMillis();
        long oneYearBefore = today.withYear(today.getYear() - 1).getMillis();
        long diff = todayMillis - oneYearBefore;

        return new DateTime(oneYearBefore + (long) (Math.random() * diff));
    }

    /**
     * Creates a random valid routing number to use
     *
     * @return 9 digit string that contains a valid routing number
     */
    public static String getRandomRoutingNumber() {
        // TODO could probably use arrays here
        int first = getRandomNumberInt(0, 8);
        int second;
        if (first != 8) {
            second = getRandomNumberInt(0, 9);
        } else {
            second = 0;
        }

        int third = getRandomNumberInt(0, 9);
        int fourth = getRandomNumberInt(0, 9);
        int fifth = getRandomNumberInt(0, 9);
        int sixth = getRandomNumberInt(0, 9);
        int seventh = getRandomNumberInt(0, 9);
        int eigth = getRandomNumberInt(0, 9);

        // to find the checksum digit
        int total = (3 * (first + fourth + seventh) + 7 * (second + fifth + eigth) + third + sixth) % 10;
        int ninth = 10 - total;
        if (ninth >= 10) {
            ninth = 0;
        }

        return String.format("%d%d%d%d%d%d%d%d%d", first, second, third, fourth, fifth, sixth, seventh, eigth, ninth);
    }

    /**
     * Selects a random element from the given non null list
     *
     * @param list List to pull the random element from
     */
    public static <T> T getRandomElementFromList(@Nonnull List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    /**
     * Selects a random element from the given non null Array
     *
     * @param array Array to pull the random element from
     */
    public static <T> T getRandomElementFromArray(@Nonnull T[] array) {
        return array[(ThreadLocalRandom.current().nextInt(array.length))];
    }

    public static String getRandomCurrencyValue(int min, int max) {
        Double randomNum = getRandomNumber(min, max);
        return new DecimalFormat(".00").format(randomNum);
    }
}
