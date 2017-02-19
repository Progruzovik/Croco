package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.enum.Role
import org.springframework.stereotype.Service
import java.util.*

@Service
class QueueService {

    private var queuedPlayers: MutableSet<Player> = LinkedHashSet()

    fun add(player: Player): Boolean {
        if (queuedPlayers.add(player)) {
            player.role = Role.QUEUED
            if (queuedPlayers.size >= Lobby.SIZE) {
                val players: List<Player> = queuedPlayers.take(Lobby.SIZE)
                val lobby = Lobby(players)
                players.forEach {
                    it.role = Role.PLAY
                    it.lobby = lobby
                }
                queuedPlayers = queuedPlayers.drop(Lobby.SIZE).toMutableSet()
            }
            return true
        }
        return false
    }

    fun remove(player: Player): Boolean {
        if (queuedPlayers.remove(player)) {
            player.role = Role.IDLE
            return true
        }
        return false
    }
}