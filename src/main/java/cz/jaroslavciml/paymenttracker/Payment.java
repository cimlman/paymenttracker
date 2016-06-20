package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;

/**
 * The class represents a single payment, i. e. it wraps a tuple {@code [currency, amount]}. Currency is an instance of
 * {@link Currency}, amount is a number (possibly negative) with arbitrary precision. Instances of the class are
 * immutable.
 */
public final class Payment {
    private final Currency currency;
    private final BigDecimal amount;

    public Payment(final Currency currency, final BigDecimal amount) {
        Validate.notNull(currency);
        Validate.notNull(amount);

        this.currency = currency;
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
