package net.orandja.tt

import net.orandja.tt.sample.basic1
import net.orandja.tt.sample.basic2
import net.orandja.tt.sample.basic3
import net.orandja.tt.sample.list1
import net.orandja.tt.sample.list2
import net.orandja.tt.sample.reflection1
import net.orandja.tt.sample.reflection2
import java.util.*

fun assertEqual(expected: Any?, value: Any?) {
    if (!Objects.equals(expected, value)) throw IllegalStateException("Expected '$expected' - Found '$value'")
    println("'$expected' OK")
}

fun main() {
    println("\n--- basic1:"); basic1()
    println("\n--- basic2:"); basic2()
    println("\n--- basic2:"); basic3()
    println("\n--- list1:"); list1()
    println("\n--- list2:"); list2()
    println("\n--- reflection1:"); reflection1()
    println("\n--- reflection2:"); reflection2()
}
