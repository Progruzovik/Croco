package net.progruzovik.study.croco.model

class Lobby {

    var players: List<Player>? = null
    set(value) {
        field = value
        value?.forEach { it.lobby = this }
    }
}