import java.security.MessageDigest
import java.util.*
import kotlin.collections.HashMap

private val ZERO = byteArrayOf(0)
private val ONE = byteArrayOf(1)
private val TWO = byteArrayOf(2)
private val NULL = Base64.getDecoder().decode("")

fun main(args: Array<String>) {
    val decoder = Base64.getDecoder()
    val encoder = Base64.getEncoder()
    val h = readLine()!!.toInt()
    val n = readLine()!!.toInt()
    var max = 0
    val blocks = hashMapOf<Int, ByteArray>()
    for (i in 0 until n) {
        val string = readLine()!!.split(" ")
        val id = string[0].toInt()
        blocks[id] = decoder.decode(string[1])
        max = Math.max(max, id)
    }
    val size = Math.pow(2.0, h.toDouble()).toInt()
    val tree = HashMap<Int, ByteArray>()
    val queue = ArrayDeque<Int>()
    for (i in blocks.keys) {
        tree[size - 1 + i] = sha256(ZERO.plus(blocks[i]!!))
        queue.add(size - 1 + i)
    }
    while (!queue.isEmpty()) {
        val id = queue.poll()
        val parent = (id - 1) / 2
        if (!tree.containsKey(parent)) {
            tree[parent] = countHash(id, get(tree, id), get(tree, findNeighbour(id)))
            if (parent != 0) {
                queue.add(parent)
            }
        }
    }
    val q = readLine()!!.toInt()
    val ids = readLine()!!.split(" ")

    for (i in 0 until q) {
        var id = size - 1 + ids[i].toInt()
        println(ids[i] + " " + toPrint(encoder, blocks, ids[i].toInt()))
        for (j in 0 until h) {
            println(toPrint(encoder, tree, findNeighbour(id)))
            id = (id - 1) / 2
        }

    }
}

private fun toPrint(encoder: Base64.Encoder, tree: HashMap<Int, ByteArray>, id: Int): String {
    if (!tree.containsKey(id) || get(tree, id).contentEquals(NULL)) {
        return "null"
    }
    return encoder.encodeToString(tree[id])
}

private fun get(tree: HashMap<Int, ByteArray>, id: Int): ByteArray {
    if (!tree.containsKey(id)){
        return NULL
    }
    return tree[id]!!
}

private fun findNeighbour(id: Int): Int {
    return if (id % 2 == 0) {
        id - 1
    } else {
        id + 1
    }
}

private fun countHash(id: Int, hash: ByteArray, neighbour: ByteArray): ByteArray {
    if (hash.contentEquals(NULL) && neighbour.contentEquals(NULL)) {
        return NULL
    }
    return if (id % 2 != 0) {
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