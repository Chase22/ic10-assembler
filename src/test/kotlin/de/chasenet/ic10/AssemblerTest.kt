package de.chasenet.ic10

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.writeText

class AssemblerTest : ShouldSpec({
    fun String.asFile(): Path {
        return Files.createTempFile("assembler-test", ".ic10")
            .also { it.writeText(this) }
    }

    context("The Assembler") {

        should("passthrough an empty string") {
            val (result, _) = Assembler.assemble("".asFile())
            result shouldBe ""
        }

        should("passthrough a string without directives") {
            val input = """
                mov r0 0
                add r0 r1 r2
            """.trimIndent()
            val (result, _) = Assembler.assemble(input.asFile())
            result shouldBe input
        }

        should("remove comments") {
            val input = """
                .COMMENT This is a comment
                mov r0 0
                .COMMENT Another comment
                add r0 r1 r2
            """.trimIndent()
            val expected = """
                mov r0 0
                add r0 r1 r2
            """.trimIndent()
            val (result, _) = Assembler.assemble(input.asFile())
            result shouldBe expected
        }

        should("handle DEFINE directives") {
            val input = """
                .DEFINE CONST 42
                mov r0 0
                """.trimIndent()
            val expected = """
                mov r0 0
            """.trimIndent()
            val (content, parameters) = Assembler.assemble(input.asFile())
            content shouldBe expected
            parameters["CONST"] shouldBe "42"
        }

        should("include extra code") {
            val tempFolder = Files.createTempDirectory("assembler-include-test")

            val includeFile = tempFolder.resolve("include.ic10").absolute()
            val inputFile = tempFolder.resolve("input.ic10").absolute()

            val include = """
                mov r1 1
                add r0 r0 r1
            """.trimIndent()
            includeFile.writeText(include)

            val input = """
                mov r0 0
                .INCLUDE ${includeFile.fileName}
                sub r2 r0 r1
            """.trimIndent()
            inputFile.writeText(input)

            val expected = """
                mov r0 0
                mov r1 1
                add r0 r0 r1
                sub r2 r0 r1
            """.trimIndent()

            val (result, _) = Assembler.assemble(inputFile)
            result shouldBe expected
        }
    }
})