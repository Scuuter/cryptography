import java.security.MessageDigest
import java.util.*

private const val ROUNDS = 1000
private const val HEIGHT = 8
private val ZERO256 = StringBuilder().append("0").repeat(256).toString()
private val FULL256 = StringBuilder().append("1").repeat(256).toString()

private val ZERO = byteArrayOf(0)
private val ONE = byteArrayOf(1)
private val TWO = byteArrayOf(2)
private val NULL = Base64.getDecoder().decode("")

fun main(args: Array<String>) {
    val decoder = Base64.getDecoder()
    val encoder = Base64.getEncoder()
    val rootHash = decoder.decode(readLine())
    val keys = hashMapOf<Int, Key>()
    for (i in 1..ROUNDS) {
        val num = readLine()!!.toInt()
        var message: String
        var key = Key()
        message = if (!keys.containsKey(num)) {
            ZERO256
        } else {
            key = keys[num]!!
            FULL256
        }

        println(message)
        val sign = decoder.decode(readLine())
        val concatKey = decoder.decode(readLine()!!)
        key.setPublic(concatKey)
        if (!checkSign(message, sign, key)) {
            println("NO")
            println("NO")
            for (j in 1..HEIGHT + 1) {
                readLine()
            }
            continue
        }

        var id = num
        var hash = sha256(ZERO.plus(concatKey))
        for (j in 1..HEIGHT) {
            val neighbour = decoder.decode(readLine()!!)
            hash = countHash(id, hash, neighbour)
            id /= 2
        }
        if (hash.contentEquals(rootHash)) {
            println("YES")
            if (!keys.containsKey(num)) {
                key.setPrivateFirst(sign)
            } else {
                key.setPrivateSecond(sign)
            }
            keys[num] = key
        } else {
            println("NO")
        }
        val document = readLine()!!
        if (!key.complete) {
            println("NO")
        } else {
            println("YES")
            println(encoder.encodeToString(sign(document, key)))
            break
        }
    }
}

fun checkSign(message: String, sign: ByteArray, key: Key): Boolean {
    for ((i, c) in message.withIndex()) {
        val partSign = sha256(sign.copyOfRange(i * 32, i * 32 + 32))
        if (c == '0') {
            if (!partSign.contentEquals(key.public[i].first))
                return false
        } else {
            if (!partSign.contentEquals(key.public[i].second))
                return false
        }
    }
    return true
}

fun sign(message: String, key: Key): ByteArray {
    var ans = byteArrayOf()
    for ((i, c) in message.withIndex()) {
        ans = if (c == '0') {
            ans.plus(key.private[i].first)
        } else {
            ans.plus(key.private[i].second)
        }
    }
    return ans
}

class Key(var public: ArrayList<Pair<ByteArray, ByteArray>> = arrayListOf(), var private: ArrayList<Pair<ByteArray, ByteArray>> = arrayListOf()) {
    var complete = false
    fun setPublic(byte: ByteArray) { // 512 x 256
        for (i in 0..255) {
            public.add(Pair(byte.copyOfRange(i * 32, i * 32 + 32), byteArrayOf()))
        }
        for (i in 256..511) {
            public[i - 256] = public[i - 256].copy(second = byte.copyOfRange(i * 32, i * 32 + 32))
        }
    }

    fun setPrivateFirst(byte: ByteArray) { // 256 x 256
        for (i in 0..255) {
            private.add(Pair(byte.copyOfRange(i * 32, i * 32 + 32), byteArrayOf()))
        }
    }

    fun setPrivateSecond(byte: ByteArray) { // 256 x 256
        for (i in 0..255) {
            private[i] = private[i].copy(second = byte.copyOfRange(i * 32, i * 32 + 32))
        }
        complete = true
    }
}

private fun countHash(id: Int, hash: ByteArray, neighbour: ByteArray): ByteArray {
    if (hash.contentEquals(NULL) && neighbour.contentEquals(NULL)) {
        return NULL
    }
    return if (id % 2 == 0) {
        sha256(ONE.plus(hash).plus(TWO).plus(neighbour))
    } else {
        sha256(ONE.plus(neighbour).plus(TWO).plus(hash))
    }
}


private fun sha256(bytes: ByteArray): ByteArray {
    return MessageDigest
            .getInstance("SHA-256")
            .digest(bytes)
}
