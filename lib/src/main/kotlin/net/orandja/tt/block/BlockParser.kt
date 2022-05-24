package net.orandja.tt.block

import net.orandja.tt.block.BlockParser.Anchor.*
import net.orandja.tt.templates.Delimiters

object BlockParser {

    @JvmStatic
    fun parseBlock(content: CharSequence, delimiters: Delimiters = Delimiters.DEFAULT_BLOCK): Block {
        val anchors = getAnchors(content, delimiters)
        assertValidity(anchors, delimiters)
        val tree = createTree(content, anchors, delimiters)
        return blockFromTree(tree)
    }

    private enum class Anchor { UP, DOWN }
    private data class PreTemplate(
        val content: StringBuilder = StringBuilder(),
        val children: MutableList<PreTemplate> = mutableListOf(),
    ) {
        override fun toString(): String = "C'${content}'${if (children.isNotEmpty()) "[${children.joinToString { it.toString() }}]" else ""}"
    }

    private fun assertValidity(anchors: List<Pair<Int, Anchor>>, delimiters: Delimiters) {
        val isValid = anchors.fold(0) { acc, (_, anchor) ->
            when (anchor) {
                UP -> acc + 1
                DOWN -> acc - 1
            }
        }
        when {
            isValid < 0 -> throw IllegalStateException("Block template is malformed. Missing opening delimiter. (${delimiters.start})")
            isValid > 0 -> throw IllegalStateException("Block template is malformed. Missing closing delimiter. (${delimiters.end})")
        }
    }

    private fun getAnchors(content: CharSequence, delimiters: Delimiters): List<Pair<Int, Anchor>> {
        val anchors = mutableListOf<Pair<Int, Anchor>>()
        val (start, stop) = delimiters
        var upCount = 0
        var downCount = 0

        for (i in content.indices) {
            if (content[i] == start[upCount]) {
                upCount++
                if (upCount == start.length) {
                    anchors += i + 1 to UP
                    upCount = 0
                    downCount = 0
                }
            } else {
                upCount = 0
            }
            if (content[i] == stop[downCount]) {
                downCount++
                if (downCount == stop.length) {
                    anchors += (i + 1) to DOWN
                    downCount = 0
                    upCount = 0
                }
            } else {
                downCount = 0
            }
        }
        return anchors
    }

    private fun createTree(content: CharSequence, anchors: List<Pair<Int, Anchor>>, delimiters: Delimiters): PreTemplate {
        val root = PreTemplate()
        val stack = mutableListOf(root)
        var last = 0 to UP
        for (anchor in anchors) {
            when (anchor.second) {
                UP -> {
                    val parent = stack.last()
                    val next = PreTemplate()
                    parent.children += next
                    stack.add(next)
                    val part = content.substring(last.first, anchor.first - delimiters.start.length)
                    parent.content.append(part)
                }
                DOWN -> {
                    val current = stack.last()
                    val part = content.substring(last.first, anchor.first - delimiters.end.length)
                    current.content.append(part)
                    stack.removeLast()
                }
            }
            last = anchor
        }
        val part = content.substring(last.first, content.length)
        root.content.append(part)
        return root
    }

    private fun CharSequence.firstWord(): Pair<CharSequence, Int> {
        var startOfFirstWord = -1
        var endOfFirstWord = -1
        var startOfSecondWord = -1
        val separator = System.lineSeparator()
        for (i in indices) {
            if (endOfFirstWord > 0) {
                if (!get(i).isWhitespace() || get(i) == '\n' || get(i) == '\r') {
                    startOfSecondWord = i
                    break
                }
                continue
            }
            if (startOfFirstWord >= 0) {
                if (get(i).isWhitespace()) {
                    endOfFirstWord = i
                    if (get(i) == '\n' || get(i) == '\r') {
                        startOfSecondWord = i
                        break
                    }
                }
                continue
            }
            if (!get(i).isWhitespace()) startOfFirstWord = i
        }
        val firstWord =
            if (startOfFirstWord >= 0 && endOfFirstWord > 0) substring(startOfFirstWord, endOfFirstWord)
            else ""
        return firstWord to startOfSecondWord
    }

    private fun blockFromTree(tree: PreTemplate, cut: Int = 0): Block {
        val templateContent = if (cut > 0) tree.content.substring(cut) else tree.content
        val templateInfo = templateContent.toString().trimIndent()
        if (tree.children.size == 0) return Block(templateInfo)
        val context = tree.children.associate {
            val (name, start) = it.content.firstWord()
            name.toString() to blockFromTree(it, start)
        }
        return Block(templateInfo, context)
    }
}
