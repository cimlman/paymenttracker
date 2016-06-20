package cz.jaroslavciml.paymenttracker;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>An instance of this class keeps sums of payments added by the method {@link #addPayment(Payment)}. An extra amount
 * sum is hold for each currency. If no {@link Payment} has been added for a particular currency or if all the payments
 * with a particular currency result in zero amount sum, such sum is not stored at all. The current sums for all
 * currencies can be printed by the method {@link #print(Writer)}.</p>
 *
 * <p>In addition, USD exchange rates can be set by calling {@link #setUSDRate(USDRate)}. These rates are optional. They
 * need not be defined for all currencies and they need not be defined at all. If such exchange rate is defined for
 * a particular currency, the payment sum is supplemented by the equal value in USD in scope of print. The converted
 * value is printed with precision of two decimal digits.</p>
 *
 * <p>An instance of this class is thread safe.</p>
 */
public class PaymentSummarizer {
    private final Object lock = new Object();
    private final Map<Currency, USDRate> usdRates = new HashMap<>();
    private final Map<Currency, BigDecimal> sums = new LinkedHashMap<>();

    public void setUSDRate(final USDRate usdRate) {
        synchronized(lock) {
            usdRates.put(usdRate.getCurrency(), usdRate);
        }
    }

    public void addPayment(final Payment payment) {
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
                    final Currency currency = sumEntry.getKey();
                    final BigDecimal sum = sumEntry.getValue();
                    final USDRate usdRate = usdRates.get(currency);
                    writer.write(currency.getCode() + " " + sum.toPlainString());
                    if (usdRate != null) {
                        final BigDecimal usdAmount
                                = sum.multiply(usdRate.getUsdAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        writer.write(" (USD " + usdAmount.toPlainString() + ")");
                    }
                    writer.write("\n");
                }
                writer.write("\n");
            }
        }
    }
}
