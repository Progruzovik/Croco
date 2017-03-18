package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.enum.GameStatus
import net.progruzovik.study.croco.enum.Role
import java.util.*

class Lobby(
        val players: List<Player>,
        private val keyword: String) {

    val messages = LinkedList<Message>()
    val quads = LinkedList<Quad>()

    companion object {
        const val SIZE = 2
        const val QUADS_NUMBER = 1600
        const val COLORS_NUMBER = 10
    }

    fun addMessage(player: Player, text: String): Boolean {
        if (player.role == Role.GUESSER) {
            messages.add(Message(player.name, text))
            if (text.toLowerCase() == keyword) {
                players.forEach {
                    it.role = if (it == player) Role.WINNER else Role.IDLER
                }
            }
            players.forEach { it.gameStatus = GameStatus.MODIFIED }
            return true
        }
        return false
    }

    fun addQuad(playerRole: Role, number: Int, color: Int): Boolean {
        if (playerRole == Role.PAINTER && number > -1 && number < QUADS_NUMBER
                && color > -1 && color < COLORS_NUMBER) {
            val existingQuad: Quad? = quads.find { it.number == number }
            if (existingQuad == null) {
                quads.add(Quad(number, color))
            } else {
                existingQuad.color = color
            }
            players.forEach { it.gameStatus = GameStatus.MODIFIED }
            return true
        }
        return false
    }

    fun removeQuads(playerRole: Role): Boolean {
        if (playerRole == Role.PAINTER) {
            quads.clear()
            players.forEach { it.gameStatus = GameStatus.REDRAWN }
            return true
        }
        return false
    }

    fun getKeyword(playerRole: Role): String? {
        return if (playerRole == Role.GUESSER) null else keyword
    }
}