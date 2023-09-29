package revxrsal.args

import kotlin.random.Random
import kotlin.reflect.KParameter

fun getIndex() = Random.nextInt()

fun main() {
    var mask = 0;
    mask = mask or (1 shl (getIndex() % Integer.SIZE))

}

object Barb {
    @JvmStatic
    fun testBarb(
//    req: Int,
        a: String = "A",
        b: String = "B",
        c: String = "C"
    ) {
        val param: KParameter = ::testBarb.parameters[1]
        val getDescriptor = param.javaClass.getDeclaredMethod("getDescriptor")
            .also { it.isAccessible = true }
        val descriptor = getDescriptor(param)
        println(descriptor.javaClass)
        println(param)
    }
}

class Test {

    object A {

    }

    companion object Zz {

        @JvmStatic
        val A: Test.A = Test.A

        fun test(a: String, b: String = "", c: String) {

        }

        @JvmStatic
        fun testStatic(a: String, b: String = "xc", c: String) {
            println("a: $a")
            println("b: $b")
            println("c: $c")
        }
    }
}

