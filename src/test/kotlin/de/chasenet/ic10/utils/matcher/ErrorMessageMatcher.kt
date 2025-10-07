package de.chasenet.ic10.utils.matcher

import com.github.ajalt.clikt.testing.CliktCommandTestResult
import de.chasenet.ic10.utils.nonEmptyLines
import io.kotest.assertions.eq.EqMatcher
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

class ErrorMessageMatcher(private val error: String): Matcher<CliktCommandTestResult> {
    override fun test(value: CliktCommandTestResult): MatcherResult =
        EqMatcher(error).test(value.output.nonEmptyLines().last())
}

fun haveErrorMessage(error: String) = ErrorMessageMatcher(error)

fun CliktCommandTestResult.shouldHaveErrorMessage(error: String) =
    this should haveErrorMessage(error)