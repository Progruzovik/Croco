package net.progruzovik.study.croco.game

interface Player {

    val id: String
    var name: String

    var role: Role
    var lobby: Lobby?
    var wasRedrawn: Boolean

    fun addToQueue(): Boolean

    fun removeFromQueue(): Boolean

    fun say(text: String): Boolean?

    fun paint(number: Int, color: Int): Boolean?

    fun clearCanvas(): Boolean?

    fun requestKeyword(): String?
}