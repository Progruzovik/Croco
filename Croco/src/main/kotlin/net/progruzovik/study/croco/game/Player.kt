package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.enum.Role

interface Player {

    val id: String
    var name: String
    val roleCode: Int

    var role: Role
    var lobby: Lobby

    fun toQueue(): Boolean
    fun fromQueue(): Boolean
}