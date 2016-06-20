package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;

/**
 * The class represents an exchange rate of a particular currency and USD. An instance is an immutable object that
 * contains a currency abbreviation (three uppercase letters, e.g. {@code GBP}) and a USD amount (positive number with
 * arbitrary precision). Example: {@code [GBP, 1.46]} represents exchange rate {@code 1 GBP = 1.46 USD}.
 */
public class USDRate {
    private final String currency;
    private final BigDecimal usdAmount;

    public USDRate(final String currency, final BigDecimal usdAmount) {
        Validate.notNull(currency);
        Validate.notNull(usdAmount);

        this.currency = currency;
        this.usdAmount = usdAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getUsdAmount() {
        return usdAmount;
    }
}
