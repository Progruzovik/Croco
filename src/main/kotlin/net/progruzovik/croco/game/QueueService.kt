package net.progruzovik.croco.game

import net.progruzovik.croco.data.Role
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Service

@Service
class QueueService(private val lobbyFactory: ObjectFactory<Lobby>) : Queue {

    override var queuedPlayer: Player? = null
        private set

    private var lastLobby: Lobby? = null

    @Synchronized override fun addPlayer(player: Player): Boolean {
        if (queuedPlayer == player) return false
        if (lastLobby?.addGuesser(player) != true) {
            if (queuedPlayer == null) {
                queuedPlayer = player
                lastLobby = null
                player.role = Role.QUEUED
            } else {
                val lobby: Lobby = lobbyFactory.`object`
                lobby.addGuesser(player)
                queuedPlayer = null
                lastLobby = lobby
            }
        }
        return true
    }

    @Synchronized override fun removePlayer(player: Player): Boolean {
        if (queuedPlayer == player) {
            queuedPlayer = null
            player.role = Role.IDLER
            return true
        }
        return false
    }
}
