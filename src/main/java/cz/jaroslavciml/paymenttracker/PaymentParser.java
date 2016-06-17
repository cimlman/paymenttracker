package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Class with stateless instances capable of parsing a {@link String} into a {@link Payment} instances.
 */
public class PaymentParser {
    private static final Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]{3}");

    /**
     * Parses a {@link String} with the format {@code CCC a} where {@code CCC} is a three uppercase letter abbreviation
     * of currency and {@code a} is a (possibly negative) decimal number with arbitrary precision. Currency and amount
     * are separated by any whitespace characters.
     */
    public Payment parse(final String s) throws IllegalArgumentException {
        Validate.notNull(s);

        final String[] elements = StringUtils.split(s);
        if (elements.length != 2) {
            throw new IllegalArgumentException("Currency and amount separated by whitespace expected");
        }

        final String currency = elements[0];
        if (!CURRENCY_PATTERN.matcher(currency).matches()) {
            throw new IllegalArgumentException("Invalid currency: " + currency);
        }

        final BigDecimal amount;
        try {
            amount = new BigDecimal(elements[1]);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount: " + elements[1], e);
        }

        return new Payment(currency, amount);
    }
}
