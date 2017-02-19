package net.progruzovik.study.croco.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

data class Lobby(
        @JsonIgnore val players: List<Player>) {

    val messages: MutableList<Message> = ArrayList()
    val quads: MutableList<Quad> = ArrayList()
}