import java.security.MessageDigest
import java.util.*

private val ZERO = byteArrayOf(0)
private val ONE = byteArrayOf(1)
private val TWO = byteArrayOf(2)
private val NULL = Base64.getDecoder().decode("")

fun main(args: Array<String>) {
    val decoder = Base64.getDecoder()
    val h = readLine()!!.toInt()
    val rootHash = decoder.decode(readLine())
    val q = readLine()!!.toInt()
    val blocks = hashMapOf<Int, ByteArray>()
    for (i in 0 until q) {
        val string = readLine()!!.split(" ")
        var id = string[0].toInt()
        var hash = NULL
        if (string[1] != "null") {
            hash = decoder.decode(string[1])
        }
        blocks[id] = hash
        if (!hash.contentEquals(NULL)) {
            hash = sha256(ZERO.plus(hash))
        }
        for (j in 0 until h) {
            val kek = readLine()
            var neighbour = NULL
            if (kek != "null"){
                neighbour = decoder.decode(kek)
            }
            hash = countHash(id, hash, neighbour)
            id /= 2
        }
        if (hash.contentEquals(rootHash)){
            println("YES")
        } else {
            println("NO")
        }
    }
}

private fun countHash(id: Int, hash: ByteArray, neighbour: ByteArray): ByteArray {
    if (hash.contentEquals(NULL) && neighbour.contentEquals(NULL)) {
        return NULL
    }
    if (id % 2 == 0) {
        return sha256(ONE.plus(hash).plus(TWO).plus(neighbour))
    } else {
        return sha256(ONE.plus(neighbour).plus(TWO).plus(hash))
    }
}


private fun sha256(bytes: ByteArray): ByteArray {
    return MessageDigest
            .getInstance("SHA-256")
            .digest(bytes)
}