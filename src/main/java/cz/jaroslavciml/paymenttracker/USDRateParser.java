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
 * Class with stateless instances capable of parsing a {@link String} into a {@link USDRate} instance.
 */
public class USDRateParser {

    /**
     * Parses a {@link String} with the format {@code CCC usd_amount} where {@code CCC} is a three uppercase letter
     * code of currency and {@code usd_amount} is a positive decimal number with arbitrary precision. Currency
     * and USD amount are separated by any whitespace characters.
     */
    public USDRate parse(final String s) throws USDRateParseException {
        Validate.notNull(s);

        final String[] elements = StringUtils.split(s);
        if (elements.length != 2) {
            throw new USDRateParseException("Currency and USD amount separated by whitespace expected in '" + s + "'");
        }

        final Currency currency;
        try {
            currency = new Currency(elements[0]);
        } catch (final IllegalArgumentException e) {
            throw new USDRateParseException("Invalid currency: " + elements[0], e);
        }

        final BigDecimal usdAmount;
        try {
            usdAmount = new BigDecimal(elements[1]);
        } catch (final NumberFormatException e) {
            throw new USDRateParseException("Invalid USD amount: " + elements[1], e);
        }
        if (usdAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new USDRateParseException("Positive USD amount expected but " + elements[1] + " found");
        }

        return new USDRate(currency, usdAmount);
    }

    /**
     * Parses a whole file into a list of {@link USDRate} instances. Each line in the file corresponds to one
     * {@link USDRate} instance. See {@link #parse(String)} for the line format.
     */
    public List<USDRate> parseFile(final String filename) throws IOException, USDRateParseException {
        Validate.notNull(filename);

        final List<USDRate> usdRates = new ArrayList<>();

        final FileReader fileReader = new FileReader(filename);

        try (final BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!StringUtils.isBlank(line)) {
                    usdRates.add(parse(line));
                }
            }
        }

        return usdRates;
    }
}
