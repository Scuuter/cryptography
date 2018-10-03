
fun main(args: Array<String>) {
    val N = 10000
    val X = 1000
    var k1 = 0
    var k0 = 0
    val k: Int
    for (i in 1 .. X) {
        val c = readLine()!!.toInt()
        println(0)
        val ans = readLine()
        if (ans == "YES" && c == 1 || ans == "NO" && c == 0) {
            k1++
        } else {
            k0++
        }
    }
    if (k0 > k1) {
        k = 0
    } else {
        k = 1
    }
    for (i in X + 1 .. N) {
        val c = readLine()!!.toInt()
        println(c xor k)
        val ans = readLine()
    }
}