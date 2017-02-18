package net.progruzovik.study.croco.model

import com.fasterxml.jackson.annotation.JsonIgnore

class Player(val id: String) {
    var name: String = ""
    @JsonIgnore var lobby: Lobby? = null

    val status: Int
    get() = 0
}