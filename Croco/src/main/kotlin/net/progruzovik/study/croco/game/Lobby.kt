package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Duration
import java.time.LocalTime
import java.util.*

class Lobby(guesser: Player,
        val painter: Player) {

    val guessers = ArrayList<Player>()
    var winner: Player? = null
        private set

    @JsonIgnore val messages = ArrayList<Message>()
    @JsonIgnore val quads = ArrayList<Quad>()

    private val keyword: String = keywords[random.nextInt(keywords.size)]
    private val startTime = LocalTime.now()

    companion object {
        private const val SIZE = 5
        private const val QUADS_NUMBER = 1600
        private const val COLORS_NUMBER = 10

        private val random = Random()
        private val keywords = listOf("куб", "стюардесса", "краб", "дровосек", "химик",
                "динозавр", "паук", "наковальня", "шахтёр", "лампочка", "бокал", "певец")
    }

    init {
        painter.role = Role.PAINTER
        painter.wasRedrawn = true
        painter.lobby = this
        addGuesser(guesser)
    }

    fun addGuesser(guesser: Player): Boolean {
        if (winner == null && painter != guesser && !guessers.contains(guesser) && guessers.size <= SIZE
                && Duration.between(startTime, LocalTime.now()).toMinutes() < 1) {
            guesser.role = Role.GUESSER
            guesser.wasRedrawn = true
            guesser.lobby = this
            guessers.add(guesser)
            return true
        }
        return false
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
            return true
        }
        return false
    }

    fun removeQuads(player: Player): Boolean {
        if (player == painter && winner == null) {
            quads.clear()
            painter.wasRedrawn = true
            guessers.forEach { it.wasRedrawn = true }
            return true
        }
        return false
    }

    fun getKeyword(player: Player): String? {
        return if (player == painter) keyword else null
    }
}