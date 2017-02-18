package net.progruzovik.study.croco.service

import net.progruzovik.study.croco.model.Lobby
import net.progruzovik.study.croco.model.Player
import org.springframework.stereotype.Service
import java.util.*

@Service
class QueueService {
    private var queuedPlayers: MutableList<Player> = ArrayList()

    fun add(player: Player) {
        if (!queuedPlayers.contains(player)) {
            queuedPlayers.add(player)
        }
    }

    fun getLobby(): Lobby {
        val result = Lobby()
        result.players = queuedPlayers.take(3)
        queuedPlayers = queuedPlayers.drop(3).toMutableList()
        return result
    }
}