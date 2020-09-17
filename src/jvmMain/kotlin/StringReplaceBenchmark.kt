package org.test.benchmarks.string_replace

import kotlinx.benchmark.*
import org.openjdk.jmh.annotations.Level


@State(Scope.Benchmark)
open class StringReplaceBenchmark {
    @Param(">>back", "unique")
    var needle = ""

    val replacement = "unique-replacement"

    @Param("false", "true")
    var ignoreCase = false
    @Param("100", "100000")
    var totalLength = 5000
    @Param("0", "1", "10")
    var occurrences = 10

    var testString: String = ""

    @Setup(Level.Iteration)
    fun setup() {
        testString = generateTestString(totalLength, needle, occurrences)
    }

    @Benchmark
    fun replaceStdlib() = testString.replace(needle, replacement, ignoreCase)

    @Benchmark
    fun replaceManual() = testString.replaceManual(needle, replacement, ignoreCase)

    @Benchmark
    fun replaceRegex() = testString.replaceRegex(needle, replacement, ignoreCase)

    @Benchmark
    fun replaceRegex1() = testString.replaceRegex1(needle, replacement, ignoreCase)

    @Benchmark
    fun replacePlatform() = testString.replacePlatform(needle, replacement, ignoreCase)
}



