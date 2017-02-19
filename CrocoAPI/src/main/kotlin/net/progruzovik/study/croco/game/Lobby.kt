package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

class Lobby(
        @JsonIgnore val players: List<Player>) {

    companion object {
        const val SIZE = 1
    }

    private val messages = LinkedList<Message>()
    private val quads = LinkedList<Quad>()

    fun addMessage(message: Message) {
        messages.add(message)
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