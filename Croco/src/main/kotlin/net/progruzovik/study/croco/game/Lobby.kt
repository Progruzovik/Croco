package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import java.lang.Math.abs
import java.time.Duration.between
import java.time.LocalTime
import java.util.*

class Lobby(guesser: Player,
            val painter: Player,
            private val keyword: String) {

    val guessers = ArrayList<Player>()
    var winner: Player? = null
        private set

    @JsonIgnore val messages = ArrayList<Message>()
    @JsonIgnore val quads = LinkedList<Quad>()

    private val startTime = LocalTime.now()

    companion object {
        private const val SIZE = 5
        private const val QUADS_NUMBER = 1600
        private const val COLORS_NUMBER = 13
    }

    init {
        painter.role = Role.PAINTER
        painter.isQuadsRemoved = true
        painter.lobby = this
        addGuesser(guesser)
    }

    fun addGuesser(guesser: Player): Boolean {
        if (winner == null && painter != guesser && !guessers.contains(guesser) && guessers.size < SIZE
                && abs(between(startTime, LocalTime.now()).toMinutes()) < 1) {
            guesser.role = Role.GUESSER
            guesser.isQuadsRemoved = true
            guesser.lobby = this
            guessers.add(guesser)
            return true
        }
        return false
    }

    fun addMessage(player: Player, text: String): Boolean {
        if (guessers.contains(player) && winner == null) {
            messages.add(Message(messages.size, player.name, text))
            if (text.toLowerCase() == keyword) {
                painter.role = Role.IDLER
                guessers.forEach {
                    it.role = if (it == player) Role.WINNER else Role.IDLER
                }
                winner = player
            }
            return true
        }
        return false
    }

    fun markMessage(player: Player, number: Int, isMarked: Boolean?): Boolean {
        if (player == painter && winner == null && number < messages.size) {
            messages[number].isMarked = isMarked
            return true
        }
        return false
    }

    fun addQuad(player: Player, number: Int, color: Int): Boolean {
        if (player == painter && winner == null
                && number > -1 && number < QUADS_NUMBER && color > -1 && color < COLORS_NUMBER) {
            val existingQuad: Quad? = quads.find { it.number == number }
            if (existingQuad == null) {
                quads.add(Quad(number, color))
            } else {
                existingQuad.color = color
            }
            return true
        }
        return false
    }

    fun removeQuad(player: Player, number: Int): Boolean {
        if (player == painter && winner == null && number > -1 && number < QUADS_NUMBER) {
            val iterator: MutableIterator<Quad> = quads.iterator()
            var isRemoved: Boolean = false
            while (iterator.hasNext() && !isRemoved) {
                if (iterator.next().number == number) {
                    iterator.remove()
                    isRemoved = true
                }
            }
            painter.isQuadsRemoved = true
            guessers.forEach { it.isQuadsRemoved = true }
            return true
        }
        return false
    }

    fun removeQuads(player: Player): Boolean {
        if (player == painter && winner == null) {
            quads.clear()
            painter.isQuadsRemoved = true
            guessers.forEach { it.isQuadsRemoved = true }
            return true
        }
        return false
    }

    fun requestKeyword(player: Player): String? {
        return if (player == painter || winner != null) keyword else null
    }
}