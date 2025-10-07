package de.chasenet.ic10

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText

data class AssemblyResult(val content: String, val parameters: Map<String, String>)

class Assembler {
    private val parameters: MutableMap<String, String> = mutableMapOf()

    fun assemble(file: Path): AssemblyResult {

        val content = file.readText()
        return AssemblyResult(content.lines().flatMap { line ->
            if (!line.trim().startsWith(".")) {
                return@flatMap listOf(line)
            }
            val parts = line.trim().removePrefix(".").split(" ")

            val key = parts[0]
            val params = parts.drop(1)

            return@flatMap when (key) {
                "COMMENT" -> emptyList()
                "DEFINE" -> processDefine(params)
                "INCLUDE" -> handleInclude(file, params)

                else -> throw IllegalArgumentException("Unknown directive: $key")
            }
        }.joinToString("\n"), parameters)
    }

    private fun processDefine(params: List<String>): List<String> {
        if (params.size != 2) {
            throw IllegalArgumentException("DEFINE requires exactly 2 parameters: key and value")
        }
        val defineKey = params[0]
        val defineValue = params[1]
        parameters[defineKey] = defineValue
        return emptyList()
    }

    private fun handleInclude(file: Path, params: List<String>): List<String> {
        if (params.size != 1) {
            throw IllegalArgumentException("INCLUDE requires exactly 1 parameter: filename")
        }
        val includeFile = params[0]
        val includePath = file.parent.resolve(Path(includeFile))
        if (!includePath.exists()) {
            throw IllegalArgumentException("Included file does not exist: $includePath")
        }
        val (assembledContent, newParameters) = assemble(includePath)
        parameters.putAll(newParameters)
        return assembledContent.lines()
    }

    companion object {
        fun assemble(file: Path): AssemblyResult {
            return Assembler().assemble(file)
        }
    }
}