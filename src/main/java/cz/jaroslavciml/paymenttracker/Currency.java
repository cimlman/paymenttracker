package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.Validate;

import java.util.regex.Pattern;

/**
 * Instances of this class wrap a currency code. It can be any sequence of three uppercase letters (in contrast to
 * {@link java.util.Currency}). Instances are immutable.
 */
public final class Currency {
    private static final Pattern CODE_PATTERN = Pattern.compile("[A-Z]{3}");

    private final String code;

    public Currency(final String code) throws IllegalArgumentException {
        Validate.notNull(code);
        Validate.isTrue(CODE_PATTERN.matcher(code).matches());

        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(final Object anotherObject) {
        if (this == anotherObject) return true;
        if (anotherObject == null || getClass() != anotherObject.getClass()) return false;

        final Currency anotherCurrency = (Currency) anotherObject;

        return this.code.equals(anotherCurrency.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
