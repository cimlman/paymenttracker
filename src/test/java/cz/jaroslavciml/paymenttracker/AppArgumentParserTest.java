package cz.jaroslavciml.paymenttracker;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

public class AppArgumentParserTest {

    @Test
    public void testParse() {
        final AppArgumentParser appArgumentParser = new AppArgumentParser();

        ReflectionAssert.assertReflectionEquals(
                new AppArguments(null, null),
                appArgumentParser.parse(new String[]{}));

        ReflectionAssert.assertReflectionEquals(
                new AppArguments(null, "input.txt"),
                appArgumentParser.parse(new String[]{"input.txt"}));

        ReflectionAssert.assertReflectionEquals(
                new AppArguments("rates.txt", null),
                appArgumentParser.parse(new String[]{"-e", "rates.txt"}));

        ReflectionAssert.assertReflectionEquals(
                new AppArguments("rates.txt", "input.txt"),
                appArgumentParser.parse(new String[]{"-e", "rates.txt", "input.txt"}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseException() {
        new AppArgumentParser().parse(new String[]{"-e"});
    }
}
