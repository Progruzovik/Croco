package net.progruzovik.study.croco.data

data class Message(
        val number: Int, val sender: String, val text: String) {
    var isMarked: Boolean? = null
}