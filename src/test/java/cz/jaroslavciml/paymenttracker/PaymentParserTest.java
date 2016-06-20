package cz.jaroslavciml.paymenttracker;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

public class PaymentParserTest {

    @Test
    public void testParse() throws PaymentParseException {
        final PaymentParser paymentParser = new PaymentParser();

        ReflectionAssert.assertReflectionEquals(
                new Payment(new Currency("NZD"), new BigDecimal("-150.5")),
                paymentParser.parse("   NZD     -150.5"));
    }

    @Test(expected = PaymentParseException.class)
    public void testParseSpaceException() throws PaymentParseException {
        new PaymentParser().parse("USD100");
    }

    @Test(expected = PaymentParseException.class)
    public void testParseCurrencyException() throws PaymentParseException {
        new PaymentParser().parse("US 100");
    }

    @Test(expected = PaymentParseException.class)
    public void testParseAmountException() throws PaymentParseException {
        new PaymentParser().parse("US 123-14");
    }

    @Test
    public void testParseFile() throws IOException, PaymentParseException {
        final PaymentParser paymentParser = new PaymentParser();

        ReflectionAssert.assertReflectionEquals(
                Arrays.asList(
                        new Payment(new Currency("USD"), new BigDecimal("200")),
                        new Payment(new Currency("GBP"), new BigDecimal("100")),
                        new Payment(new Currency("NZD"), new BigDecimal("-25"))),
                paymentParser.parseFile(getClass().getResource("payments_test.txt").getFile()));
    }

    @Test(expected = IOException.class)
    public void testParseFileException() throws IOException, PaymentParseException {
        new PaymentParser().parseFile("__non_existing_file.txt");
    }
}
