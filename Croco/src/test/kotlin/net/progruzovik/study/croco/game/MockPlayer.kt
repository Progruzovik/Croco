package net.progruzovik.study.croco.game

class MockPlayer(
        override val id: String,
        override var name: String) : Player {

    override lateinit var role: Role
    override var lobby: Lobby? = null
    override var isQuadsRedrawn: Boolean = false

    override fun addToQueue() {}

    override fun removeFromQueue(): Boolean = false

    override fun say(text: String): Boolean {
        return lobby?.addMessage(this, text) ?: false
    }

    override fun markMessage(number: Int, isMarked: Boolean?): Boolean {
        return lobby?.markMessage(this, number, isMarked) ?: false
    }

    override fun paint(number: Int, color: Int): Boolean {
        return lobby?.addQuad(this, number, color) ?: false
    }

    override fun clearCanvas(): Boolean {
        return lobby?.removeQuads(this) ?: false
    }

    override fun requestKeyword(): String? = lobby?.getKeyword(this)
}