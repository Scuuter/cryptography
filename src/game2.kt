
fun main(args: Array<String>) {
    val N = 10000
    val X = 2000
    val keys = Array(X, {-1})
    val k: Int
    var per = -1
    for (i in 0 until X) {
        val c = readLine()!!.toInt()
        println(c xor 0)
        val ans = readLine()
        if (ans == "YES") {
            keys[i] = 0
        } else {
            keys[i] = 1
        }
    }
    for (j in 1 .. 1000) {
        var flag = true
        for ((i, a) in keys.withIndex()) {
            if (i + j >= X)
                break
            if (a != keys[i + j] ) {
                flag = false
                break
            }
        }
        if (flag) {
            per = j
            break
        }
    }
    for (i in X until N) {
        val c = readLine()!!.toInt()
        println(c xor keys[i % per])
        val ans = readLine()
    }
}