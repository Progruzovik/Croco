package net.progruzovik.study.croco.model

import com.fasterxml.jackson.annotation.JsonIgnore
import net.progruzovik.study.croco.enum.Status

data class Player(val id: String) {

    lateinit var name: String

    @JsonIgnore var status: Status = Status.IDLE
    @JsonIgnore var lobby: Lobby? = null
}