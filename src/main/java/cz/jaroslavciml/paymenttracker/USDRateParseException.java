package cz.jaroslavciml.paymenttracker;

/**
 * Thrown when a String cannot be parsed into an instance of {@link USDRate} due to invalid format.
 */
public class USDRateParseException extends Exception {

    public USDRateParseException(String message) {
        super(message);
    }

    public USDRateParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
