package cz.jaroslavciml.paymenttracker;

/**
 * Parsed command line arguments of the application.
 */
public class AppArguments {
    private final String exchangeRateFilename;
    private final String inputFilename;

    public AppArguments(final String exchangeRateFilename, final String inputFilename) {
        this.exchangeRateFilename = exchangeRateFilename;
        this.inputFilename = inputFilename;
    }

    public String getExchangeRateFilename() {
        return exchangeRateFilename;
    }

    public String getInputFilename() {
        return inputFilename;
    }
}
