package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.data.Role

interface Player {

    val id: String
    var name: String

    var role: Role
    var lobby: Lobby?
    var isQuadsRemoved: Boolean

    fun addMessage(text: String): Boolean
    fun markMessage(number: Int, isMarked: Boolean?): Boolean

    fun addQuad(number: Int, color: Int): Boolean
    fun removeQuad(number: Int): Boolean
    fun removeQuads(): Boolean

    fun requestKeyword(): String?
}