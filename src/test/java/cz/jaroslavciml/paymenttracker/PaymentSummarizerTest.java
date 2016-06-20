package cz.jaroslavciml.paymenttracker;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;

public class PaymentSummarizerTest {

    @Test
    public void test() throws IOException {
        final PaymentSummarizer paymentSummarizer = new PaymentSummarizer();

        paymentSummarizer.setUSDRate(new USDRate(new Currency("GBP"), new BigDecimal("1.47")));
        paymentSummarizer.setUSDRate(new USDRate(new Currency("NZD"), new BigDecimal("0.71")));

        paymentSummarizer.addPayment(new Payment(new Currency("USD"), new BigDecimal("123")));
        paymentSummarizer.addPayment(new Payment(new Currency("NZD"), new BigDecimal("30")));
        paymentSummarizer.addPayment(new Payment(new Currency("USD"), new BigDecimal("-15.4")));
        paymentSummarizer.addPayment(new Payment(new Currency("GBP"), new BigDecimal("-5")));
        paymentSummarizer.addPayment(new Payment(new Currency("HKD"), new BigDecimal("200")));

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Writer writer = new OutputStreamWriter(outputStream);
        paymentSummarizer.print(writer);
        writer.flush();
        Assert.assertEquals(
                "\nUSD 107.6\nNZD 30 (USD 21.30)\nGBP -5 (USD -7.35)\nHKD 200\n\n",
                outputStream.toString("UTF-8"));
    }
}
