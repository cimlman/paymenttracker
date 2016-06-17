package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    private static final PaymentParser paymentParser = new PaymentParser();
    private static final PaymentSummarizer paymentSummarizer = new PaymentSummarizer();

    public static void main(final String[] args) {
        readInput();
    }

    private static void readInput() {
        try (final BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = inputReader.readLine()) != null)
            {
                if (StringUtils.strip(line).equalsIgnoreCase("quit")) {
                    break;
                }
                processPaymentLine(line);
            }
        } catch (final IOException e) {
            System.err.println("I/O error occurred when reading from standard input");
            System.exit(1);
        }
    }

    private static void processPaymentLine(final String line) {
        final Payment payment;
        try {
            payment = paymentParser.parse(line);
        } catch (final IllegalArgumentException e) {
            System.err.println("Cannot parse '" + line + "'");
            return;
        }

        paymentSummarizer.add(payment);
    }
}
