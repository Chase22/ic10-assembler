package de.chasenet.ic10.utils.matcher

import com.github.ajalt.clikt.testing.CliktCommandTestResult
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

fun isSuccess() = haveErrorCode(0)

fun isError() = isSuccess().invert()

fun haveErrorCode(code: Int) = Matcher.Companion<CliktCommandTestResult> { value ->
    MatcherResult.Companion(
        value.statusCode == code,
        { "Expected exit code to be $code but was ${value.statusCode}" },
        { "Expected exit code to not be $code but was ${value.statusCode}" }
    )
}

fun CliktCommandTestResult.shouldBeSuccess() =
    this should isSuccess()

fun CliktCommandTestResult.shouldBeError() =
    this should isError()

fun CliktCommandTestResult.shouldHaveErrorCode(code: Int) =
    this should haveErrorCode(code)