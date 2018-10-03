import kotlin.math.abs

const val INDEX_COINCIDENCE = 0.644

val letterFrequency = hashMapOf<Char, Double>(
        'a' to 0.08167,
        'b' to 0.01492,
        'c' to 0.02782,
        'd' to 0.04253,
        'e' to 0.12702,
        'f' to 0.02228,
        'g' to 0.02015,
        'h' to 0.06094,
        'i' to 0.06966,
        'j' to 0.00153,
        'k' to 0.00772,
        'l' to 0.04025,
        'm' to 0.02406,
        'n' to 0.06749,
        'o' to 0.07507,
        'p' to 0.01929,
        'q' to 0.00095,
        'r' to 0.05987,
        's' to 0.06327,
        't' to 0.09056,
        'u' to 0.02758,
        'v' to 0.00978,
        'w' to 0.0236,
        'x' to 0.0015,
        'y' to 0.01975,
        'z' to 0.00074
).toList().sortedWith(Comparator<Pair<Char, Double>> { o1, o2 -> ((o2.second - o1.second) * 10000).toInt() })

fun main(args: Array<String>) {
    val text = args[0]
    var eps = Double.MAX_VALUE
    var keyLength = -1
    var key = mutableListOf<Int>()
    for (n in 6..10) {
        val shifted = shift(text, n)
        var equal = 0
        for (i in text.indices) {
            if (text[i] == shifted[i])
                equal++
        }
        val index = equal.toDouble() / text.length.toDouble()
        if (abs(index - INDEX_COINCIDENCE) < eps) {
            eps = abs(index - INDEX_COINCIDENCE)
            keyLength = n
        }
    }
    var list = Array(keyLength, { "" })
    for ((i, c) in text.withIndex()) {
        list[i % keyLength] = list[i % keyLength] + c
    }
    for (string in list) {
        key.add(keyChar(string))
    }
    var answer = ""
    for ((i, c) in text.withIndex()) {
        answer += 'a' + ((c - key[i % keyLength] - 'a' + letterFrequency.size) % letterFrequency.size)
    }
    print(answer)
}

fun keyChar(string: String): Int {
    val countLetters = mutableMapOf<Char, Int>()
    val keys = mutableMapOf<Int, Int>()
    for (c in string) {
        countLetters[c] = countLetters.getOrPut(c, { 0 }) + 1
    }
    for ((i, letter) in countLetters.toList().sortedWith(Comparator<Pair<Char, Int>> { o1, o2 -> o2.second - o1.second }).withIndex()) {
        val key = ((letter.first - letterFrequency[i].first) + letterFrequency.size) % letterFrequency.size
        keys[key] = keys.getOrPut(key, { 0 }) + 1
    }
    return keys.toList().sortedWith(Comparator<Pair<Int, Int>> { o1, o2 -> o2.second - o1.second })[0].first
}


fun shift(string: String, number: Int): String {
    return string.takeLast(number) + string.dropLast(number)
}