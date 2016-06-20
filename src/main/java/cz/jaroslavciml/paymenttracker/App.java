package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    private static final USDRateParser usdRateParser = new USDRateParser();
    private static final PaymentParser paymentParser = new PaymentParser();
    private static final PaymentSummarizer paymentSummarizer = new PaymentSummarizer();

    public static void main(final String[] args) {
        int argIndex = 0;
        final String exchangeRateFilename;
        if (argIndex < args.length && args[argIndex].equals("-e")) {
            argIndex++;
            if (argIndex >= args.length) {
                //TODO JC print usage
                System.exit(1);
            }
            exchangeRateFilename = args[argIndex];
            argIndex++;
        } else {
            exchangeRateFilename = null;
        }
        if (args.length > argIndex + 1) {
            System.err.println("Supported arguments: [input_filename]");
            System.exit(1);
        }
        final String inputFilename = argIndex < args.length ? args[argIndex] : null;

        if (exchangeRateFilename != null) {
            readExchangeRateFile(exchangeRateFilename);
        }
        if (inputFilename != null) {
            readInputFile(inputFilename);
        }
        final PrintThread printThread = new PrintThread(paymentSummarizer);
        printThread.start();
        try {
            readStandardInput();
        } finally {
            printThread.die();
        }
    }

    private static void readExchangeRateFile(final String filename) {
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

    private static void processExchangeRateLine(final String line) {
        try {
            usdRateParser.parse(line);
        } catch (final IllegalArgumentException e) {
            System.err.println("Cannot parse '" + line + "'");
            return;
        }

        //TODO JC store to payment summarizer
    }

    private static void readInputFile(final String filename) {
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

    private static void readStandardInput() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            readInput(reader, true);
        } catch (final IOException e) {
            System.err.println("I/O error occurred when reading from standard input");
            System.exit(1);
        }
    }

    private static void readInput(final BufferedReader reader, final boolean supportQuit) throws IOException {
        String line;
        while ((line = reader.readLine()) != null)
        {
            if (supportQuit && StringUtils.strip(line).equalsIgnoreCase("quit")) {
                break;
            }
            processPaymentLine(line);
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
