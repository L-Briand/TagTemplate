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

    println("\n--- basic1:"); basic1()
    println("\n--- basic2:"); basic2()
    println("\n--- basic3:"); basic3()
    println("\n--- list1:"); list1()
    println("\n--- reflection1:"); reflection1()
    println("\n--- list2:"); list2()
    println("\n--- list3:"); list3()
    println("\n--- reflection2:"); reflection2()
    println("\n--- block1:"); block1()
    println("\n--- block2:"); block2()
    println("\n--- block3:"); block3()
    println("\n--- merge1:"); merge1()
    //println("\n--- merge2:"); merge2()
    println("\n--- example"); example()
}
