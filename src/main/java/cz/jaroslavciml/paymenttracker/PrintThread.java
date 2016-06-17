package cz.jaroslavciml.paymenttracker;

import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * A thread runs in a loop and prints payment sums once a minute. The payment sum printing is delegated to
 * {@link PaymentSummarizer} passed as a constructor argument. Call {@link #die()} to end the loop.
 */
public class PrintThread extends Thread {
    private static final long PRINT_INTERVAL_MILLIS = 10L * 1000L; //TODO JC should be 1 minute

    private final Object conditionVariable = new Object();
    private boolean dieFlag;
    private final PaymentSummarizer paymentSummarizer;

    public PrintThread(PaymentSummarizer paymentSummarizer) {
        Validate.notNull(paymentSummarizer);

        this.paymentSummarizer = paymentSummarizer;
    }

    @Override
    public void run() {
        final Writer writer = new OutputStreamWriter(System.out);

        while (true) {
            try {
                paymentSummarizer.print(writer);
                writer.flush();
            } catch (final IOException e) {
                System.err.println("I/O error occurred when writing to standard output");
            }

            synchronized (conditionVariable) {
                if (dieFlag) {
                    break;
                }
                try {
                    conditionVariable.wait(PRINT_INTERVAL_MILLIS);
                } catch (final InterruptedException ignored) {
                }
            }
        }
    }

    public void die() {
        synchronized (conditionVariable) {
            dieFlag = true;
            conditionVariable.notifyAll();
        }
    }
}
