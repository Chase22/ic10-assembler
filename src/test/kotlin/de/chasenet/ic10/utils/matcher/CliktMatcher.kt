package de.chasenet.ic10.utils.matcher

import com.github.ajalt.clikt.testing.CliktCommandTestResult
import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import io.kotest.matchers.should

fun isErrorWithMessage(error: String) = Matcher.all(isError(), haveErrorMessage(error))

fun CliktCommandTestResult.shouldBeErrorWithMessage(error: String) =
    this should isErrorWithMessage(error)