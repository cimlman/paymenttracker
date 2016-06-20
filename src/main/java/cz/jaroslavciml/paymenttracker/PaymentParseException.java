package cz.jaroslavciml.paymenttracker;

/**
 * Thrown when a String cannot be parsed into an instance of {@link Payment} due to invalid format.
 */
public class PaymentParseException extends Exception {

    public PaymentParseException(String message) {
        super(message);
    }

    public PaymentParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
