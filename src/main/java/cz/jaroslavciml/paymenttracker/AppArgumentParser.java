package cz.jaroslavciml.paymenttracker;

/**
 * An instance of this class (that is stateless) parses command line arguments of the application into an instance of
 * {@link AppArguments}.
 */
public class AppArgumentParser {
    public static final String USAGE = "Supported arguments: [-e exchange_rate_filename] [input_filename]";

    public AppArguments parse(final String[] args) throws IllegalArgumentException {
        int argIndex = 0;
        final String exchangeRateFilename;
        if (argIndex < args.length && args[argIndex].equals("-e")) {
            argIndex++;
            if (argIndex >= args.length) {
                throw new IllegalArgumentException();
            }
            exchangeRateFilename = args[argIndex];
            argIndex++;
        } else {
            exchangeRateFilename = null;
        }

        final String inputFilename;
        if (argIndex < args.length) {
            inputFilename = args[argIndex];
            argIndex++;
        } else {
            inputFilename = null;
        }

        if (argIndex < args.length) {
            throw new IllegalArgumentException();
        }

        return new AppArguments(exchangeRateFilename, inputFilename);
    }
}
