package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.enum.GameStatus
import net.progruzovik.study.croco.enum.Role

interface Player {

    val id: String
    var name: String

    var role: Role
    var gameStatus: GameStatus
    var lobby: Lobby

    fun addToQueue(): Boolean

    fun removeFromQueue(): Boolean

    fun say(text: String): Boolean

    fun paint(number: Int, color: Int): Boolean

    fun clearCanvas(): Boolean

    fun requestKeyword(): String?
}