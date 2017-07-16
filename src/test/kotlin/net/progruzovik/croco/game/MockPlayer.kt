package net.progruzovik.croco.game

import net.progruzovik.croco.data.Role

class MockPlayer(
        override val id: String,
        override var name: String) : Player {

    override var role: Role = Role.IDLER
    override var lobby: Lobby? = null
    override var isQuadsRemoved: Boolean = false

    override fun addToQueue(): Boolean = false

    override fun removeFromQueue(): Boolean = false

    override fun addMessage(text: String): Boolean {
        return lobby?.addMessage(this, text) ?: false
    }

    override fun markMessage(number: Int, isMarked: Boolean?): Boolean {
        return lobby?.markMessage(this, number, isMarked) ?: false
    }

    override fun addQuad(number: Int, color: Int): Boolean {
        return lobby?.addQuad(this, number, color) ?: false
    }

    override fun removeQuad(number: Int): Boolean {
        return lobby?.removeQuad(this, number) ?: false
    }

    override fun removeQuads(): Boolean {
        return lobby?.removeQuads(this) ?: false
    }

    override fun requestKeyword(): String? = lobby?.requestKeyword(this)
}