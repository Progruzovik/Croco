package net.progruzovik.study.croco.service

import net.progruzovik.study.croco.enum.Status
import net.progruzovik.study.croco.model.Lobby
import net.progruzovik.study.croco.model.Player
import org.springframework.stereotype.Service
import java.util.*

@Service
class QueueService {

    private val LOBBY_SIZE: Int = 1

    private var queuedPlayers: MutableSet<Player> = LinkedHashSet()

    fun add(player: Player): Boolean {
        if (queuedPlayers.add(player)) {
            player.status = Status.QUEUED
            if (queuedPlayers.size >= LOBBY_SIZE) {
                val players: List<Player> = queuedPlayers.take(LOBBY_SIZE)
                val lobby = Lobby(players)
                players.forEach {
                    it.status = Status.PLAY
                    it.lobby = lobby
                }
                queuedPlayers = queuedPlayers.drop(LOBBY_SIZE).toMutableSet()
            }
            return true
        }
        return false
    }

    fun remove(player: Player): Boolean {
        if (queuedPlayers.remove(player)) {
            player.status = Status.IDLE
            return true
        }
        return false
    }
}