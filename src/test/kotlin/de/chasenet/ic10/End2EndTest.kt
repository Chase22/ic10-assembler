package de.chasenet.ic10

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.testing.test
import de.chasenet.ic10.utils.matcher.shouldBeErrorWithMessage
import de.chasenet.ic10.utils.matcher.shouldBeSuccess
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.paths.shouldExist
import io.kotest.matchers.shouldBe
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory
import kotlin.io.path.createTempFile
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.io.path.writeText

class End2EndTest : ShouldSpec({
    context("the CLI") {
        should("fail if no input-file is passed") {
            val result = Ic10Assembler().test()
            result.shouldBeErrorWithMessage("Error: missing argument <input-file>")
        }

        should("fail if the input-file does not exist") {
            val result = Ic10Assembler().test(listOf("nonexistent.ic10t"))
            result.shouldBeErrorWithMessage(
                """Error: invalid value for <input-file>: path "nonexistent.ic10t" does not exist."""
            )
        }

        should("fail if the output dir is a file") {
            val testFile = createTempFile("test", ".ic10t").toFile()
            val result = Ic10Assembler().test(listOf(testFile.absolutePath, "--output", testFile.absolutePath))
            result.shouldBeErrorWithMessage(
                """Error: invalid value for --output: directory "${testFile.absolutePath}" is a file."""
            )
        }

        should("fully process a given file") {
            // Define files
            val testDir = createTempDirectory("asmtest")
            val inputFile = testDir.resolve("test.ic10t")
            val includedFile = testDir.resolve("included.ic10t")

            val outputDir = testDir.resolve("out")
            val assembledFile = outputDir.resolve("test.ic10a")
            val finishedFile = outputDir.resolve("test.ic10")

            // Write test data
            includedFile.writeText(
                """
                    add r1 r3
                    sub r4 ${'$'}{secondRegister}
                
                """.trimIndent()
            )

            inputFile.writeText(
                """
                .DEFINE secondRegister r5
                mov r1 r2
                add r1 r3
                sub r4 r5
                .INCLUDE ${includedFile.fileName}
                .COMMENT This is a comment
            """.trimIndent()
            )

            // Run assembler
            val result = Ic10Assembler().test(listOf(inputFile.absolutePathString()))


            // Assertions
            assembledFile.shouldExist()
            assembledFile.readLines().shouldBe(
                listOf(
                    "mov r1 r2",
                    "add r1 r3",
                    "sub r4 r5",
                    "add r1 r3",
                    "sub r4 \${secondRegister}"
                )
            )

            finishedFile.shouldExist()
            finishedFile.readLines().shouldBe(
                listOf(
                    "mov r1 r2",
                    "add r1 r3",
                    "sub r4 r5",
                    "add r1 r3",
                    "sub r4 r5"
                )
            )

            result.shouldBeSuccess()
        }
    }
})
