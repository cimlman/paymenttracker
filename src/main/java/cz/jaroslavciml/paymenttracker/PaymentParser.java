package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Class with stateless instances capable of parsing a {@link String} into a {@link Payment} instance.
 */
public class PaymentParser {

    /**
     * Parses a {@link String} with the format {@code CCC amount} where {@code CCC} is a three uppercase letter code of
     * currency and {@code amount} is a (possibly negative) decimal number with arbitrary precision. Currency and amount
     * are separated by any whitespace characters.
     */
    public Payment parse(final String s) throws PaymentParseException {
        Validate.notNull(s);

        final String[] elements = StringUtils.split(s);
        if (elements.length != 2) {
            throw new PaymentParseException("Currency and amount separated by whitespace expected in '" + s + "'");
        }

        final Currency currency;
        try {
            currency = new Currency(elements[0]);
        } catch (final IllegalArgumentException e) {
            throw new PaymentParseException("Invalid currency: " + elements[0], e);
        }

        final BigDecimal amount;
        try {
            amount = new BigDecimal(elements[1]);
        } catch (final NumberFormatException e) {
            throw new PaymentParseException("Invalid amount: " + elements[1], e);
        }

        return new Payment(currency, amount);
    }

    /**
     * Parses a whole file into a list of {@link Payment} instances. Each line in the file corresponds to one payment.
     * See {@link #parse(String)} for the line format.
     */
    public List<Payment> parseFile(final String filename) throws IOException, PaymentParseException {
        Validate.notNull(filename);

        final List<Payment> payments = new ArrayList<>();

        final FileReader fileReader = new FileReader(filename);

        try (final BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                payments.add(parse(line));
            }
        }

        return payments;
    }
}
