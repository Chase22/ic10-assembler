package de.chasenet.ic10.utils

fun String.nonEmptyLines() = lines().filterNot { it.isBlank() }
