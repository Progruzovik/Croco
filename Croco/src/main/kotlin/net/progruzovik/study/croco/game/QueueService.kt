package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.enum.Role
import org.springframework.stereotype.Service
import java.util.*

@Service
class QueueService {

    private val queuedPlayers: MutableSet<Player> = LinkedHashSet()

    fun add(player: Player): Boolean {
        if (queuedPlayers.add(player)) {
            player.role = Role.QUEUED
            if (queuedPlayers.size >= Lobby.SIZE) {
                val players: List<Player> = queuedPlayers.take(Lobby.SIZE)
                queuedPlayers.removeAll(players)
                val lobby = Lobby(players, "куб")
                players.forEach {
                    it.role = Role.PLAYER
                    it.lobby = lobby
                }
            }
            return true
        }
        return false
    }

    fun remove(player: Player): Boolean {
        if (queuedPlayers.remove(player)) {
            player.role = Role.IDLER
            return true
        }
        return false
    }
}