package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import net.progruzovik.study.croco.enum.Role
import java.util.*

class Lobby(
        @JsonIgnore val players: List<Player>,
        @JsonIgnore val keyword: String) {

    companion object {
        const val SIZE = 1
    }

    private val messages = LinkedList<Message>()
    private val quads = LinkedList<Quad>()

    fun addMessage(sender: Player, text: String) {
        messages.add(Message(sender.name, text))
        if (text.toLowerCase() == keyword) {
            players.forEach {
                it.role = if (it == sender) Role.WINNER else Role.IDLER
            }
        }
    }

    fun addQuad(quad: Quad) {
        val existingQuad: Quad? = quads.find { it.number == quad.number }
        if (existingQuad == null) {
            quads.add(quad)
        } else {
            existingQuad.color = quad.color
        }
    }
}