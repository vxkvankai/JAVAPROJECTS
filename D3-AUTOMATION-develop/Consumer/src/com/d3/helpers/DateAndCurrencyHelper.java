package com.d3.helpers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.annotation.CheckForNull;

public class DateAndCurrencyHelper {

    private DateAndCurrencyHelper() {
    }

    /**
     * Gets the current date formatted in HHmmss-MM-dd-yyyy
     *
     * @return String with the format "HHmmss-MM-dd-yyyy"
     */
    public static String getDateWithDashes() {
        return new SimpleDateFormat("HHmmss-MM-dd-yyyy").format(new Date());
    }

    /**
     * Converts the given money values subtracted with each other equal the expected value
     *
     * @param number1 The currency value to subtract from
     * @param number2 The currency value to subtract
     * @param equals The expected value
     * @throws ParseException Thrown if the parameters cannot be converted to a monetary value
     */
    public static boolean convertMoneyAndSubtract(String number1, String number2, String equals) throws ParseException {

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        List<Double> dds = new ArrayList<>();
        List<BigDecimal> ggs = new ArrayList<>();

        int multiply = 1;
        String number1Replace = number1;
        if (number1.contains("-")) {
            number1Replace = number1.replace("-", "");
            multiply = -1;
        }
        dds.add(nf.parse(number1Replace).doubleValue() * multiply);
        multiply = 1;

        String number2Replace = number2;
        if (number2.contains("-")) {
            number2Replace = number2.replace("-", "");
            multiply = -1;
        }

        dds.add(nf.parse(number2Replace).doubleValue() * multiply);
        multiply = 1;

        String equalsReplace = equals;
        if (equals.contains("-")) {
            equalsReplace = equals.replace("-", "");
            multiply = -1;
        }

        dds.add(nf.parse(equalsReplace).doubleValue() * multiply);

        for (Double dd : dds) {
            ggs.add(new BigDecimal(dd).setScale(2, RoundingMode.HALF_UP));
        }

        BigDecimal answer;
        MathContext mc = new MathContext(11);
        answer = ggs.get(0).subtract(ggs.get(1), mc);
        return answer.equals(ggs.get(2));
    }

    /**
     * Parse a String value (can be negative) into its currency value e.g. -$1,000.35 -> -1000.35d
     *
     * @param value Currency value (e.g. $1,000)
     * @return BigDecimal of the parsed value, null if there was an error
     */
    @CheckForNull
    public static BigDecimal parseCurrency(String value) {
        boolean negative = value.contains("-");
        String newValue = value.replaceAll("[$,-]", "").trim();

        BigDecimal doubleVal = new BigDecimal(newValue);
        return negative ? doubleVal.negate() : doubleVal;
    }


    /**
     * Returns true if actual is within 1 cent of the expected value
     */
    public static boolean compareCurrency(BigDecimal actual, BigDecimal expected) {
        return compareCurrency(actual, expected, BigDecimal.valueOf(.01));
    }

    /**
     * Returns true if actual is within the rounding error, false otherwise
     *
     * @param actual Actual number to compare against
     * @param expected Expected value
     * @param roundingError Value to add/subtract to the actual number to see if the expected value falls within the range
     */
    public static boolean compareCurrency(BigDecimal actual, BigDecimal expected, BigDecimal roundingError) {
        LoggerFactory.getLogger(DateAndCurrencyHelper.class).info("Comparing {} to {} with rounding error: {}", actual, expected, roundingError);
        return actual.compareTo(expected) == 0
                || actual.add(roundingError).compareTo(expected) == 0
                || actual.subtract(roundingError).compareTo(expected) == 0
                || (actual.add(roundingError).compareTo(expected) > 0 && actual.subtract(roundingError).compareTo(expected) < 0);
    }

    /**
     * Get the string value and add it a $ so it can be used as an amount e.g. 10.00 -> $10.00 If it's a negative value, it will format it to replace
     * the -
     *
     * @param value = the string before adding the currency format
     * @return String with the format "$#.#"
     */
    public static String getAmountWithDollarSign(String value) {
        return value.contains("-") ? String.format("-$%s", value.replaceAll("-", "")) : String.format("$%s", value);
    }

    /**
     * Returns a date later than the entered date
     */
    @CheckForNull
    public static Date getEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 2);
        return cal.getTime();
    }


    /**
     * Method will format DateTime date into the specified pattern
     *
     * @param pattern pattern to format DateTime in
     * @param date DateTime date
     * @return String value of formatted DateTime
     */
    //TODO update other instances to use this method if possible
    public static String formatDateTime(String pattern, DateTime date) {
        return DateTimeFormat.forPattern(pattern).print(date);
    }

}
