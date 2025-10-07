package de.chasenet.ic10

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class TemplaterTest: ShouldSpec({
    context("The Templater") {
        should("passthrough an empty string") {
            val result = Templater.template("", emptyMap())
            result shouldBe ""
        }
        should("passthrough a string without parameters") {
            val input = "hello world"
            val result = Templater.template(input, emptyMap())
            result shouldBe input
        }
        should("replace parameters") {
            val input = "hello \${world}"
            val result = Templater.template(input, mapOf("world" to "universe"))
            result shouldBe "hello universe"
        }

        should("replace multiple parameters") {
            val input = "hello \${who}, welcome to \${where}"
            val result = Templater.template(input, mapOf("who" to "Alice", "where" to "Wonderland"))
            result shouldBe "hello Alice, welcome to Wonderland"
        }
        should("throw on missing parameters") {
            val input = "hello \${world}"
            shouldThrow<IllegalArgumentException> {
                Templater.template(input, emptyMap())
            }
        }
    }
})