package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.data.Message
import net.progruzovik.study.croco.data.Quad

interface Lobby {

    val painter: Player
    val guessers: List<Player>
    val winner: Player?

    val messages: List<Message>
    val quads: List<Quad>

    fun addGuesser(guesser: Player): Boolean

    fun addMessage(player: Player, text: String): Boolean
    fun markMessage(player: Player, number: Int, isMarked: Boolean?): Boolean

    fun addQuad(player: Player, number: Int, color: Int): Boolean
    fun removeQuad(player: Player, number: Int): Boolean
    fun removeQuads(player: Player): Boolean

    fun requestKeyword(player: Player): String?
}