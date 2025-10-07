package de.chasenet.ic10

object Templater {
    val regex = Regex("\\$\\{(\\w+)}")

    fun template(input: String, parameters: Map<String, String>): String {
        val keys = regex.findAll(input).map { it.groupValues[1] }
        val missingKeys = keys.filter { it !in parameters }.toSet()
        if (missingKeys.isNotEmpty()) {
            throw IllegalArgumentException("Missing parameters for keys: $missingKeys")
        }

        return parameters.entries.fold(input) { acc, (key, value) ->
            acc.replace("\${$key}", value)
        }
    }
}