package net.progruzovik.study.croco.game

interface Queue {

    val queuedPlayer: Player?

    fun addPlayer(player: Player): Boolean
    fun removePlayer(player: Player): Boolean
}