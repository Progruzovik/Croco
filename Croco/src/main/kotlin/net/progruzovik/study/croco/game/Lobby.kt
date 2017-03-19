package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import net.progruzovik.study.croco.enum.GameStatus
import net.progruzovik.study.croco.enum.Role
import java.util.*

class Lobby(players: List<Player>) {

    val painter: Player = players.first()
    val guessers: List<Player> = players.minus(painter)
    var winner: Player? = null
        private set

    @JsonIgnore val messages = ArrayList<Message>()
    @JsonIgnore val quads = ArrayList<Quad>()

    private val keyword: String = "куб"

    companion object {
        const val SIZE = 2
        const val QUADS_NUMBER = 1600
        const val COLORS_NUMBER = 10
    }

    init {
        painter.role = Role.PAINTER
        painter.gameStatus = GameStatus.ACTUAL
        painter.lobby = this
        guessers.forEach {
            it.role = Role.GUESSER
            it.gameStatus = GameStatus.ACTUAL
            it.lobby = this
        }
    }

    fun addMessage(player: Player, text: String): Boolean {
        if (guessers.contains(player) && winner == null) {
            messages.add(Message(player.name, text))
            if (text.toLowerCase() == keyword) {
                painter.role = Role.IDLER
                guessers.forEach {
                    it.role = if (it == player) Role.WINNER else Role.IDLER
                }
                winner = player
            }

            if (painter.gameStatus == GameStatus.ACTUAL) {
                painter.gameStatus = GameStatus.MODIFIED
            }
            guessers.forEach {
                if (it.gameStatus == GameStatus.ACTUAL) {
                    it.gameStatus = GameStatus.MODIFIED
                }
            }
            return true
        }
        return false
    }

    fun addQuad(player: Player, number: Int, color: Int): Boolean {
        if (player == painter && winner == null
                && number > -1 && number < QUADS_NUMBER
                && color > -1 && color < COLORS_NUMBER) {
            val existingQuad: Quad? = quads.find { it.number == number }
            if (existingQuad == null) {
                quads.add(Quad(number, color))
            } else {
                existingQuad.color = color
            }

            if (painter.gameStatus == GameStatus.ACTUAL) {
                painter.gameStatus = GameStatus.MODIFIED
            }
            guessers.forEach {
                if (it.gameStatus == GameStatus.ACTUAL) {
                    it.gameStatus = GameStatus.MODIFIED
                }
            }
            return true
        }
        return false
    }

    fun removeQuads(player: Player): Boolean {
        if (player == painter && winner == null) {
            quads.clear()
            painter.gameStatus = GameStatus.REDRAWN
            guessers.forEach { it.gameStatus = GameStatus.REDRAWN }
            return true
        }
        return false
    }

    fun getKeyword(player: Player): String? {
        return if (player == painter) keyword else null
    }
}