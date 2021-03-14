
For Questions 1.1 to 1.4, run the tests (see below)

For Questions 2+, see the *Answers.md* file

# Requirements

Tested with
  - openjdk 11
  - apache maven 3.6.3

Likely to run on
  - any jdk supporting java 11
  - apache maven 3+

# Running random poker hand

The following will generate a random poker hand and test if it's a straight (or straight flush)

```
mvn compile exec:java
```

See src/main/java/com/acme/Program.java 

# Testing

This runs all the tests:

```
mvn test
```

Tests are under src/test/java

# Notes

Using arrays for the card code as the question request the format returned be arrays. In a production system I'd be
using Collections/Lists/Streams (depending on need) and not arrays.

I'd probably also use a 'Card' class to contain the suite, rank, rank order instead of parsing everything.



