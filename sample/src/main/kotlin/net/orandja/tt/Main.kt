package net.orandja.tt

import net.orandja.tt.sample.*
import java.util.*

fun assertEqual(expected: Any?, value: Any?) {
    if (!Objects.equals(expected, value)) throw IllegalStateException("Expected '$expected' - Found '$value'")
    println("'$expected' OK")
}

// Examples are explained with working code and comments.
// You should read examples in the same order as below.

fun main() {
    // https://github.com/L-Briand/TT/blob/feature/block/sample/src/main/kotlin/net/orandja/tt/sample/Basic1.kt#L8
    println("\n--- basic1:")
    basic1()

    // https://github.com/L-Briand/TT/blob/feature/block/sample/src/main/kotlin/net/orandja/tt/sample/Basic2.kt#L7
    println("\n--- basic2:")
    basic2()

    // https://github.com/L-Briand/TT/blob/feature/block/sample/src/main/kotlin/net/orandja/tt/sample/Basic3.kt
    println("\n--- basic3:")
    basic3()

    // https://github.com/L-Briand/TT/blob/feature/block/sample/src/main/kotlin/net/orandja/tt/sample/Basic4.kt
    println("\n--- basic4:")
    basic4()

    // https://github.com/L-Briand/TT/blob/feature/block/sample/src/main/kotlin/net/orandja/tt/sample/List1.kt#L5
    println("\n--- list1:")
    list1()

    // https://github.com/L-Briand/TT/blob/feature/block/sample/src/main/kotlin/net/orandja/tt/sample/Reflection1.kt#L9
    println("\n--- reflection1:")
    reflection1()

    // https://github.com/L-Briand/TT/blob/feature/block/sample/src/main/kotlin/net/orandja/tt/sample/List2.kt#L8
    println("\n--- list2:")
    list2()

    // https://github.com/L-Briand/TT/blob/feature/block/sample/src/main/kotlin/net/orandja/tt/sample/List3.kt#L8
    println("\n--- list3:")
    list3()

    // https://github.com/L-Briand/TT/blob/feature/block/sample/src/main/kotlin/net/orandja/tt/sample/Reflection2.kt
    println("\n--- reflection2:")
    reflection2()

    // https://github.com/L-Briand/TT/blob/feature/block/sample/src/main/kotlin/net/orandja/tt/sample/Merge1.kt
    println("\n--- merge1:")
    merge1()

    // https://github.com/L-Briand/TT/blob/feature/block/sample/src/main/kotlin/net/orandja/tt/sample/Merge2.kt
    println("\n--- merge2:")
    merge2()
}
