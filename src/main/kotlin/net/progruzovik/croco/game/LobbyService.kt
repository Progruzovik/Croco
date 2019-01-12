package net.progruzovik.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import net.progruzovik.croco.dao.KeywordDao
import net.progruzovik.croco.data.Message
import net.progruzovik.croco.data.Quad
import net.progruzovik.croco.data.Role
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.lang.Math.abs
import java.time.Duration.between
import java.time.LocalTime
import java.util.*

@Service
@Scope("prototype")
class LobbyService(queueService: QueueService, keywordDao: KeywordDao) : Lobby {

    override val painter: Player = queueService.queuedPlayer!!
    override val guessers = ArrayList<Player>()

    override var winner: Player? = null
        private set

    @JsonIgnore override val quads = LinkedList<Quad>()
    @JsonIgnore override val messages = ArrayList<Message>()

    private var isRunning = true

    private val keyword: String = keywordDao.getRandomKeyword()
    private val startTime = LocalTime.now()

    companion object {
        private const val GUESSERS_COUNT = 5
        private const val QUADS_COUNT = 400
        private const val COLORS_COUNT = 13
    }

    init {
        painter.role = Role.PAINTER
        painter.isQuadsRemoved = true
        painter.lobby = this
    }

    override fun addGuesser(guesser: Player): Boolean {
        if (painter != guesser && !guessers.contains(guesser) && guessers.size < GUESSERS_COUNT
            && isRunning && abs(between(startTime, LocalTime.now()).toMinutes()) < 1
        ) {
            guesser.role = Role.GUESSER
            guesser.isQuadsRemoved = true
            guesser.lobby = this
            guessers.add(guesser)
            return true
        }
        return false
    }

    override fun addQuad(player: Player, number: Int, color: Int): Boolean {
        if (player == painter && isRunning
            && number > -1 && number < QUADS_COUNT
            && color > -1 && color < COLORS_COUNT
        ) {
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

    override fun removeQuad(player: Player, number: Int): Boolean {
        if (player == painter && isRunning && number > -1 && number < QUADS_COUNT) {
            quads.removeIf { it.number == number }
            painter.isQuadsRemoved = true
            guessers.forEach { it.isQuadsRemoved = true }
            return true
        }
        return false
    }

    override fun removeQuads(player: Player): Boolean {
        if (player == painter && isRunning) {
            quads.clear()
            painter.isQuadsRemoved = true
            guessers.forEach { it.isQuadsRemoved = true }
            return true
        }
        return false
    }

    override fun addMessage(player: Player, text: String): Boolean {
        if (guessers.contains(player) && isRunning) {
            messages.add(Message(messages.size, player.name, text))
            if (text.toLowerCase().replace('ё', 'е') == keyword) {
                painter.role = Role.IDLER
                guessers.forEach { it.role = if (it == player) Role.WINNER else Role.IDLER }
                winner = player
                isRunning = false
            }
            return true
        }
        return false
    }

    override fun markMessage(player: Player, number: Int, isMarked: Boolean?): Boolean {
        if (player == painter && isRunning && number < messages.size) {
            messages[number].isMarked = isMarked
            return true
        }
        return false
    }

    override fun requestKeyword(player: Player): String? = if (player == painter || !isRunning) keyword else null

    override fun close(player: Player) {
        if (player == painter && isRunning) {
            painter.role = Role.IDLER
            guessers.forEach { it.role = Role.IDLER }
            isRunning = false
        }
    }
}
