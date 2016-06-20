package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Class with stateless instances capable of parsing a {@link String} into a {@link USDRate} instance.
 */
public class USDRateParser {
    private static final Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]{3}");

    /**
     * Parses a {@link String} with the format {@code CCC usd_amount} where {@code CCC} is a three uppercase letter
     * abbreviation of currency and {@code usd_amount} is a positive decimal number with arbitrary precision. Currency
     * and USD amount are separated by any whitespace characters.
     */
    public USDRate parse(final String s) throws IllegalArgumentException {
        Validate.notNull(s);

        final String[] elements = StringUtils.split(s);
        if (elements.length != 2) {
            throw new IllegalArgumentException("Currency and USD amount separated by whitespace expected");
        }

        final String currency = elements[0];
        if (!CURRENCY_PATTERN.matcher(currency).matches()) {
            throw new IllegalArgumentException("Invalid currency: " + currency);
        }

        final BigDecimal usdAmount;
        try {
            usdAmount = new BigDecimal(elements[1]);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid USD amount: " + elements[1], e);
        }
        if (usdAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Positive USD amoutn expected but " + elements[1] + " found");
        }

        return new USDRate(currency, usdAmount);
    }
}
