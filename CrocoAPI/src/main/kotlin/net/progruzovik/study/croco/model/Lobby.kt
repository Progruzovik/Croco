package net.progruzovik.study.croco.model

import net.progruzovik.study.croco.enum.Status

class Lobby {

    var players: List<Player>? = null
    set(value) {
        field = value
        value?.forEach {
            it.status = Status.PLAY
            it.lobby = this
        }
    }
}