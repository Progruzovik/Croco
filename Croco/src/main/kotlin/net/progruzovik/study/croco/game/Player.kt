package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.enum.Role

interface Player {

    val id: String
    var name: String
    val roleCode: Int

    var role: Role
    val lobby: Lobby

    fun addToQueue(): Boolean

    fun removeFromQueue(): Boolean

    fun requestKeyword(): String?

    fun say(text: String): Boolean

    fun paint(number: Int, color: Int): Boolean
}