package cz.jaroslavciml.paymenttracker;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An instance of this class keeps sums of payments added by the method {@link #add(Payment)}. An extra amount sum is
 * hold for each currency. If no {@link Payment} has been added for a particular currency or if all the payments with a
 * particular currency result in zero amount sum, such sum is not stored at all. The current sums for all currencies can
 * be printed by the method {@link #print(Writer)}. An instance of this class is thread safe.
 */
public class PaymentSummarizer {
    private final Object lock = new Object();
    private final Map<Currency, BigDecimal> sums = new LinkedHashMap<>();

    public void add(final Payment payment) {
        synchronized(lock) {
            BigDecimal oldSum = sums.get(payment.getCurrency());
            if (oldSum == null) {
                oldSum = BigDecimal.ZERO;
            }

            final BigDecimal newSum = oldSum.add(payment.getAmount()).stripTrailingZeros();

            if (newSum.compareTo(BigDecimal.ZERO) != 0) {
                sums.put(payment.getCurrency(), newSum);
            } else {
                sums.remove(payment.getCurrency());
            }
        }
    }

    public void print(final Writer writer) throws IOException {
        synchronized (lock) {
            if (!sums.isEmpty()) {
                writer.write("\n");
                for (final Map.Entry<Currency, BigDecimal> sumEntry : sums.entrySet()) {
                    writer.write(sumEntry.getKey().getCode() + " " + sumEntry.getValue().toPlainString() + "\n");
                }
                writer.write("\n");
            }
        }
    }
}
