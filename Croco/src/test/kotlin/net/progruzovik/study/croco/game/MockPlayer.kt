package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.enum.GameStatus
import net.progruzovik.study.croco.enum.Role

class MockPlayer(
        override val id: String,
        override var name: String) : Player {

    override lateinit var role: Role
    override lateinit var gameStatus: GameStatus
    override lateinit var lobby: Lobby

    override fun addToQueue(): Boolean = false

    override fun removeFromQueue(): Boolean = false

    override fun say(text: String): Boolean = lobby.addMessage(this, text)

    override fun paint(number: Int, color: Int): Boolean = lobby.addQuad(this, number, color)

    override fun clearCanvas(): Boolean = lobby.removeQuads(this)

    override fun requestKeyword(): String? = lobby.getKeyword(this)
}