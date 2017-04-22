package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import net.progruzovik.study.croco.dao.KeywordDao
import net.progruzovik.study.croco.data.Message
import net.progruzovik.study.croco.data.Quad
import net.progruzovik.study.croco.data.Role
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.lang.Math.abs
import java.time.Duration.between
import java.time.LocalTime
import java.util.*

@Service
@Scope("prototype")
class LobbyService(queueService: QueueService, keywordDao: KeywordDao) : Lobby {

    override var painter: Player? = queueService.queuedPlayer
    override val guessers = ArrayList<Player>()
    override var winner: Player? = null
        private set

    @JsonIgnore override val messages = ArrayList<Message>()
    @JsonIgnore override val quads = LinkedList<Quad>()

    private val isRunning: Boolean
        get() = painter != null && winner == null

    private val keyword: String = keywordDao.getRandomKeyword()
    private val startTime = LocalTime.now()

    companion object {
        private const val SIZE = 5
        private const val QUADS_NUMBER = 400
        private const val COLORS_NUMBER = 13
    }

    init {
        painter?.role = Role.PAINTER
        painter?.isQuadsRemoved = true
        painter?.lobby = this
    }

    override fun addGuesser(guesser: Player): Boolean {
        if (painter != guesser && !guessers.contains(guesser) && guessers.size < SIZE
                && isRunning && abs(between(startTime, LocalTime.now()).toMinutes()) < 1) {
            guesser.role = Role.GUESSER
            guesser.isQuadsRemoved = true
            guesser.lobby = this
            guessers.add(guesser)
            return true
        }
        return false
    }

    override fun addMessage(player: Player, text: String): Boolean {
        if (guessers.contains(player) && isRunning) {
            messages.add(Message(messages.size, player.name, text))
            if (text.toLowerCase().replace('ё', 'е') == keyword) {
                painter?.role = Role.IDLER
                guessers.forEach {
                    it.role = if (it == player) Role.WINNER else Role.IDLER
                }
                winner = player
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

    override fun addQuad(player: Player, number: Int, color: Int): Boolean {
        if (player == painter && isRunning && number > -1 && number < QUADS_NUMBER
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

    override fun removeQuad(player: Player, number: Int): Boolean {
        if (player == painter && isRunning && number > -1 && number < QUADS_NUMBER) {
            val iterator: MutableIterator<Quad> = quads.iterator()
            var isRemoved: Boolean = false
            while (iterator.hasNext() && !isRemoved) {
                if (iterator.next().number == number) {
                    iterator.remove()
                    isRemoved = true
                }
            }
            painter?.isQuadsRemoved = true
            guessers.forEach { it.isQuadsRemoved = true }
            return true
        }
        return false
    }

    override fun removeQuads(player: Player): Boolean {
        if (player == painter && isRunning) {
            quads.clear()
            painter?.isQuadsRemoved = true
            guessers.forEach { it.isQuadsRemoved = true }
            return true
        }
        return false
    }

    override fun requestKeyword(player: Player): String? {
        return if (player == painter || !isRunning) keyword else null
    }

    override fun close(player: Player) {
        if (player == painter && isRunning) {
            painter?.role = Role.IDLER
            painter = null
            guessers.forEach { it.role = Role.IDLER }
        }
    }
}