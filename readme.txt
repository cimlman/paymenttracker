Payment Tracker
===============

The application keeps a record of payments. Each payment includes a currency (three uppercase letter code, e.g. USD) and
an amount (number with arbitrary precision, possibly negative, e.g. 20 or -15.5). Use '.' (dot character) to separate
the integer part and the fraction part of the amount.

The input consists from a list of payment on standard input. In addition, other payments can be specified in an input
file. Each payment is expected on an extra line. Such line consists of a currency code and an amount separated by one or
more arbitrary whitespace (e.g. USD 20.5).

Optionally, a file with exchange rates compared to USD can be specified. This file can possibly contain exchange rates
only for a subset of currencies provided in the payment input file and on standard input. The format of this exchange
rate file is similar to the format of the file with payments. Just the amount must be positive in this case.
Example: NZD 0.71 - corresponds to exchange rate 1 NZD = 0.71 USD.

Once a minute, the application prints payment sums on the standard output. One line is printed for each particular
currency used in input, except currencies where the amount sum is equal to zero (these are not printed at all).
If an exchange is available for a particular currency, the equal amount (rounded to two decimal digits) in USD is
appended to te appropriate line.

If "quit" is typed on standard input or if the standard input is closed, the application exits.

Sample payment input:
USD 22
USD -12
NZD 300
GBP -5.12

Sample exchange rate input:
NZD 0.71
HKD 0.13

Sample output:
USD 10
NZD 300 (USD 213.00)
GBP -5.12



Requirements
============

The following tools must be installed on your machine to download and build the application:
- JDK 7
- Maven (developed with version 3.3.9 but older version should work too)
- Git



Dependencies
============

The application is dependent on:
- Apache Commons Lang3, version 3.4
- JUnit, version 4.12
- Unitils Core, version 3.4.2

The dependencies will be downloaded automatically from Maven Central Repository when building with Maven.



Building
========

1. Clone remote repository from GitHub to your local machine.
git clone https://github.com/cimlman/paymenttracker.git

2. Change working directory to Git repository root.
cd paymenttracker

3. Build application using Maven.
mvn package

The last step builds the application including running the automated tests. To skip the tests, use
mvn package -Dmaven.test.skip=true

The following files will be created:
target/paymenttracker-1.0.jar
target/paymenttracker-1.0-jar-with-dependencies.jar



Launching
=========

To start the application without any input files (just standard input will be read), type
java -jar target/paymenttracker-1.0-jar-with-dependencies.jar

If you wish to provide an input payment file, type
java -jar target/paymenttracker-1.0-jar-with-dependencies.jar <payment_filename>

If you wish to provide an exchange rate file, type
java -jar target/paymenttracker-1.0-jar-with-dependencies.jar -e <exchange_rate_filename>

Or combine both
java -jar target/paymenttracker-1.0-jar-with-dependencies.jar -e <exchange_rate_filename> <payment_filename>

The formats of a payment file and an exchange rate file are described above.



Error Handling
==============

If an I/O error occurs when reading from a payment file, an exchange rate file or the standard input, the application
writes an error message to the standard error output and exits.

If a payment file or an exchange rate file have an invalid format, the application writes an error message to the
standard error output and exits.

If a line on the standard input has an invalid format, the application writes an error message to the standard error
output and continues.
