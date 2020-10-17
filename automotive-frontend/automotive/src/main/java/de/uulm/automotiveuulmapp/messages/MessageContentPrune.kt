package de.uulm.automotiveuulmapp.messages

/**
 * Removes all instances of [img] tags and [link description](link#) tags, keeping the link description.
 */
fun String.pruneMessageTags(): String {
    return this.replace("[img]", "", ignoreCase = true).replace("""(?i)\[([^()\[\]]*?)]\(link\d+\)""".toRegex(), "$1")
}