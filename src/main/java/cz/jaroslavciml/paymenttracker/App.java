package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        final FileReader fileReader;
        try {
            fileReader = new FileReader(filename);
        } catch (final FileNotFoundException e) {
            System.err.println("Cannot open '" + filename + "'");
            System.exit(1);
            throw new IllegalStateException("This point should never be reached"); // just to prevent compiler error
        }

        try (final BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            while ((line = reader.readLine()) != null)
            {
                processExchangeRateLine(line);
            }
        } catch (final IOException e) {
            System.err.println("I/O error occurred when reading from file '" + filename + "'");
            System.exit(1);
        }
    }

    private void processExchangeRateLine(final String line) {
        final USDRate usdRate;
        try {
            usdRate = usdRateParser.parse(line);
        } catch (final IllegalArgumentException e) {
            System.err.println("Cannot parse '" + line + "'");
            return;
        }

        paymentSummarizer.setUSDRate(usdRate);
    }

    private void readInputFile(final String filename) {
        final FileReader fileReader;
        try {
            fileReader = new FileReader(filename);
        } catch (final FileNotFoundException e) {
            System.err.println("Cannot open '" + filename + "'");
            System.exit(1);
            throw new IllegalStateException("This point should never be reached"); // just to prevent compiler error
        }

        try (final BufferedReader reader = new BufferedReader(fileReader)) {
            readInput(reader, false);
        } catch (final IOException e) {
            System.err.println("I/O error occurred when reading from file '" + filename + "'");
            System.exit(1);
        }
    }

    private void readStandardInput() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            readInput(reader, true);
        } catch (final IOException e) {
            System.err.println("I/O error occurred when reading from standard input");
            System.exit(1);
        }
    }

    private void readInput(final BufferedReader reader, final boolean supportQuit) throws IOException {
        String line;
        while ((line = reader.readLine()) != null)
        {
            if (supportQuit && StringUtils.strip(line).equalsIgnoreCase("quit")) {
                break;
            }
            processPaymentLine(line);
        }
    }

    private void processPaymentLine(final String line) {
        final Payment payment;
        try {
            payment = paymentParser.parse(line);
        } catch (final IllegalArgumentException e) {
            System.err.println("Cannot parse '" + line + "'");
            return;
        }

        paymentSummarizer.addPayment(payment);
    }
}
