package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import net.progruzovik.study.croco.enum.Role
import net.progruzovik.study.croco.game.Player
import java.util.*

class Lobby(
        @JsonIgnore val players: List<Player>,
        private val keyword: String) {

    companion object {
        const val SIZE = 1
    }

    val messages = LinkedList<Message>()
    val quads = LinkedList<Quad>()

    fun requestKeyword(playerRole: Role): String? {
        return if (playerRole == Role.PAINTER) keyword else null
    }

    fun addMessage(sender: Player, text: String): Boolean {
        if (sender.role != Role.PLAYER) {
            return false
        }
        messages.add(Message(sender.name, text))
        if (text.toLowerCase() == keyword) {
            players.forEach {
                it.role = if (it == sender) Role.WINNER else Role.IDLER
            }
        }
        return true
    }

    fun addQuad(playerRole: Role, quad: Quad): Boolean {
        if (playerRole != Role.PAINTER) {
            return false
        }
        val existingQuad: Quad? = quads.find { it.number == quad.number }
        if (existingQuad == null) {
            quads.add(quad)
        } else {
            existingQuad.color = quad.color
        }
        return true
    }
}