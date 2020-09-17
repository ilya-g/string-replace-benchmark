package org.test.benchmarks.string_replace

import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.random.Random


fun generateTestString(totalLength: Int, needle: String, occurrences: Int): String {
    require(needle.length > 0 && needle.length * occurrences <= totalLength)
    val randomChars = charArrayOf('<', '>')
    return buildString(totalLength) {
        var totalRuns = (totalLength - (needle.length * occurrences))
        val avgRun = totalRuns / (occurrences + 1)

        repeat(occurrences) {
            val runLength = Random.nextInt(0, (avgRun * 2).coerceAtMost(totalRuns) + 1)
            repeat(runLength) { append(randomChars.random()) }
            append(needle)
            totalRuns -= runLength
        }
        repeat(totalRuns) { append(randomChars.random()) }
        check(this.length == totalLength)
        println(this.take(80))
    }
}


fun String.replaceRegex(oldValue: String, newValue: String, ignoreCase: Boolean = false): String {
    return Pattern.compile(oldValue, Pattern.LITERAL or if(ignoreCase) Pattern.CASE_INSENSITIVE else 0)
            .matcher(this)
            .replaceAll(Matcher.quoteReplacement(newValue))
}

fun String.replacePlatform(oldValue: String, newValue: String, ignoreCase: Boolean = false): String {
    require(ignoreCase == false) { "case insensitive replacement is not supported" }
    return (this as java.lang.String).replace(oldValue, newValue)
}


fun String.replaceManual(oldValue: String, newValue: String, ignoreCase: Boolean = false): String {
    var occurrenceIndex: Int = indexOf(oldValue, 0, ignoreCase)
    // FAST PATH: no match
    if (occurrenceIndex < 0) return this

    val oldValueLength = oldValue.length
    val searchStep = oldValueLength.coerceAtLeast(1)
    val newLengthHint = length - oldValueLength + newValue.length
    if (newLengthHint < 0) throw OutOfMemoryError()
    val stringBuilder = StringBuilder(newLengthHint)

    var i = 0
    do {
        stringBuilder.append(this, i, occurrenceIndex).append(newValue)
        i = occurrenceIndex + oldValueLength
        if (occurrenceIndex >= length) break
        occurrenceIndex = indexOf(oldValue, occurrenceIndex + searchStep, ignoreCase)
    } while (occurrenceIndex > 0)
    return stringBuilder.append(this, i, length).toString()
}
