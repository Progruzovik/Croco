package net.progruzovik.study.croco.game

class MockPlayer(
        override val id: String,
        override var name: String) : Player {

    override lateinit var role: Role
    override var lobby: Lobby? = null
    override var wasRedrawn: Boolean = false

    override fun addToQueue() {}

    override fun removeFromQueue(): Boolean = false

    override fun say(text: String): Boolean? = lobby?.addMessage(this, text)

    override fun paint(number: Int, color: Int): Boolean? = lobby?.addQuad(this, number, color)

    override fun clearCanvas(): Boolean? = lobby?.removeQuads(this)

    override fun requestKeyword(): String? = lobby?.getKeyword(this)
}