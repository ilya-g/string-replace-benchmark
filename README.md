# String.replace benchmark

`String.replace` JVM implementations comparison based on benchmarks

4 implementations of `String.replace` were compared:

- 'manual': a manual implementation  using `indexOf` and `StringBuilder.append` 
- 'platform': an implementation delegating to `java.lang.String.replace`
- 'regex': an implementation based on literal regex pattern
- 'stdlib': the current implementation in the kotlin-stdlib 1.4.10, just `split(oldValue) + join(newValue)` 

Varying parameters:

- JVM: 1.8.0_191, 11.0.1
- `ignoreCase`: false for case-sensitive search, true for case-insensitive
- `needle`: the string being searched in a test string
  - "unique", a string that can be easily found just by its first letter
  - ">>back", a string that shares its first two chars with a lot of other substrings in the test string
- `occurrences`: the number of searched substrings actually found in a test string
- `totalLength`: the length of a test string

## Results



## Conclusions

### case-sensitive replacement (`ignoreCase=false`)

With JDK 8:
- 'platform' performs the same as 'regex' 
- 'regex' outperforms 'manual' (~x3) on long strings when the string being searched requires a lot of backtracking
- 'regex' takes time to compile the regular expression, thus is slower than 'manual' on short strings


With JDK 11: 
- String operations are generally faster than with JDK 8
- 'platform' is roughly the same as 'manual'
- 'regex' is generally slower than 'manual'
- 'stdlib' (split+join) is faster! (~x1.14) than 'platform' on long strings


### case-insensitive replacement (`ignoreCase=true`)

- no platform method available for case-insensitive replacement

With JDK 8:
- 'regex' outperforms 'manual' (~x1.5-2) even on short strings (except one case: many occurrences in a short string, x2.5 slower)

With JDK 11:
- 'regex' outperforms 'manual' (~x1.1-2) except in the case of many occurrences in a long string (x1.2 slower) and in a short string (x2.5 slower)
 