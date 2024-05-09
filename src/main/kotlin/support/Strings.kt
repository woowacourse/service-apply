package support

/**
 * Inside [trimMargin], "\n" is used so there is no need to distinguish between operating systems.
 */
fun String.flattenByMargin(marginPrefix: String = "|"): String = trimMargin(marginPrefix).replace("\n", "")
