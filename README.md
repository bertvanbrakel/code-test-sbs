
# Requirements

Assumes 
  - openjdk 11 (or any jdk supporting java 11)
  - apache maven 3+ is installed

# Running

```
mvn compile exec:java
```

# Testing

```
mvn compile test
```

# Notes

Using arrays for the card code as the question request the format returned be arrays. In a production system I'd be
using Collections/Lists/Streams (depending on need) and not arrays.



