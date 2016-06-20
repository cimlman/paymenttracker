package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class App {
    private final USDRateParser usdRateParser = new USDRateParser();
    private final PaymentParser paymentParser = new PaymentParser();
    private final PaymentSummarizer paymentSummarizer = new PaymentSummarizer();

    private App() {
    }

    public static void main(final String[] args) {
        final AppArguments appArguments;
        try {
            appArguments = new AppArgumentParser().parse(args);
        } catch (final IllegalArgumentException e) {
            System.err.println(AppArgumentParser.USAGE);
            System.exit(1);
            throw new IllegalStateException("This point should never be reached"); // just to prevent compiler error
        }

        new App().run(appArguments);
    }

    private void run(final AppArguments appArguments) {
        if (appArguments.getExchangeRateFilename() != null) {
            readExchangeRateFile(appArguments.getExchangeRateFilename());
        }

        if (appArguments.getInputFilename() != null) {
            readInputFile(appArguments.getInputFilename());
        }

        final PrintThread printThread = new PrintThread(paymentSummarizer);
        printThread.start();
        try {
            readStandardInput();
        } finally {
            printThread.die();
        }
    }

    private void readExchangeRateFile(final String filename) {
        final List<USDRate> usdRates;
        try {
            usdRates = usdRateParser.parseFile(filename);
        } catch (final IOException | USDRateParseException e) {
            System.err.println(
                    "I/O error occurred when reading from exchange rate file '" + filename + "': " + e.getMessage());
            System.exit(1);
            throw new IllegalStateException("This point should never be reached"); // just to prevent compiler error
        }
        for (final USDRate usdRate : usdRates) {
            paymentSummarizer.setUSDRate(usdRate);
        }
    }

    private void readInputFile(final String filename) {
        final List<Payment> payments;
        try {
            payments = paymentParser.parseFile(filename);
        } catch (final IOException | PaymentParseException e) {
            System.err.println("I/O error occurred when reading from file '" + filename + "': " + e.getMessage());
            System.exit(1);
            throw new IllegalStateException("This point should never be reached"); // just to prevent compiler error
        }
        for (final Payment payment : payments) {
            paymentSummarizer.addPayment(payment);
        }
    }

    private void readStandardInput() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null && !StringUtils.strip(line).equalsIgnoreCase("quit")) {
                try {
                    paymentSummarizer.addPayment(paymentParser.parse(line));
                } catch (final PaymentParseException e) {
                    System.err.println("Cannot parse '" + line + "'");
                }
            }
        } catch (final IOException e) {
            System.err.println("I/O error occurred when reading from standard input");
            System.exit(1);
        }
    }
}
