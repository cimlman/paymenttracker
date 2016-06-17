package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;

/**
 * The class represents a single payment, i. e. it wraps a tuple [currency, amount]. Currency is expected to be three
 * uppercase letter string (this is not checked in constructor however), amount is a number (possibly negative) with
 * arbitrary precision. Instances of the class are immutable.
 */
public final class Payment {
    private final String currency;
    private final BigDecimal amount;

    public Payment(final String currency, final BigDecimal amount) {
        Validate.notNull(currency);
        Validate.notNull(amount);

        this.currency = currency;
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
