package cz.jaroslavciml.paymenttracker;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

public class USDRateParserTest {

    @Test
    public void testParse() throws USDRateParseException {
        final USDRateParser usdRateParser = new USDRateParser();

        ReflectionAssert.assertReflectionEquals(
                new USDRate(new Currency("NZD"), new BigDecimal("0.71")),
                usdRateParser.parse(" NZD   0.71  "));
    }

    @Test(expected = USDRateParseException.class)
    public void testParseSpaceException() throws USDRateParseException {
        new USDRateParser().parse("NZD0.71");
    }

    @Test(expected = USDRateParseException.class)
    public void testParseCurrencyException() throws USDRateParseException {
        new USDRateParser().parse("NZ 0.71");
    }

    @Test(expected = USDRateParseException.class)
    public void testParseUSDAmountException() throws USDRateParseException {
        new USDRateParser().parse("NZD x");
    }

    @Test(expected = USDRateParseException.class)
    public void testParseNegativeException() throws USDRateParseException {
        new USDRateParser().parse("NZD -0.3");
    }

    @Test
    public void testParseFile() throws IOException, USDRateParseException {
        final USDRateParser usdRateParser = new USDRateParser();

        ReflectionAssert.assertReflectionEquals(
                Arrays.asList(
                        new USDRate(new Currency("GBP"), new BigDecimal("1.47")),
                        new USDRate(new Currency("NZD"), new BigDecimal("0.71"))),
                usdRateParser.parseFile(getClass().getResource("exchange_rate_test.txt").getFile()));
    }

    @Test(expected = IOException.class)
    public void testParseFileException() throws IOException, USDRateParseException {
        new USDRateParser().parseFile("__non_existing_file.txt");
    }
}
