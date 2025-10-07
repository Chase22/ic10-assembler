package de.chasenet.ic10

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.associate
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.file
import kotlin.io.path.absolutePathString

class Ic10Assembler : CliktCommand() {
    init {
        versionOption(
            version = this::class.java.`package`.implementationVersion ?: "development",
            message = { it }
        )
    }

    val inputFile by argument("input-file")
        .file(true)
        .help("Input file to process")


    val outputDir by option("-o", "--output").file(
        mustExist = false,
        canBeFile = false,
        canBeDir = true
    ).help("Output directory")

    val parameters by option("-p", "--parameter")
        .help("Parameter in the form key=value similar to the .DEFINE assembly instruction")
        .associate()

    override fun run() {
        val outputDirectory = outputDir?.toPath() ?: inputFile.parentFile.resolve("out").toPath()
        outputDirectory.toFile().mkdirs()

        val output = assemble(inputFile.toPath(), outputDirectory, parameters)
        println("Assembled file to ${output.absolutePathString()}")
    }
}

class AssembleCommand : CliktCommand() {
    val inputFile by argument("input-file")
        .file(true)
        .help("Input file to process")


    val outputDir by option("-o", "--output").file(
        mustExist = false,
        canBeFile = false,
        canBeDir = true
    ).help("Output directory")

    val parameters by option("-p", "--parameter")
        .help("Parameter in the form key=value similar to the .DEFINE assembly instruction")
        .associate()

    override fun run() {
        val outputDirectory = outputDir?.toPath() ?: inputFile.parentFile.resolve("out").toPath()
        outputDirectory.toFile().mkdirs()

        val output = assemble(inputFile.toPath(), outputDirectory, parameters)
        println("Assembled file to ${output.absolutePathString()}")
    }
}

fun main(args: Array<String>) = Ic10Assembler().main(args)
