package de.chasenet.ic10

import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.writeText

fun assemble(
    inputFile: Path,
    outputDir: Path,
    parameters: Map<String, String>
): Path {
    val assemblyFile = outputDir.resolve("${inputFile.nameWithoutExtension}.ic10a")
    val outputFile = outputDir.resolve("${inputFile.nameWithoutExtension}.ic10")

    val (assembledString, assemblyParameters) = Assembler.assemble(inputFile)
    assemblyFile.writeText(assembledString)

    val allParameters = parameters + assemblyParameters

    outputFile.writeText(Templater.template(assembledString, allParameters))

    return outputFile
}