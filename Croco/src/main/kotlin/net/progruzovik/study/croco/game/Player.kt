package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.enum.Role
import net.progruzovik.study.croco.game.Lobby
import net.progruzovik.study.croco.game.Quad

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

    fun paint(quad: Quad): Boolean
}