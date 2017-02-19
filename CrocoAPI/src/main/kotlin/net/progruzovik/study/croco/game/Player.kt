package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import net.progruzovik.study.croco.enum.Role

data class Player(
        val id: String) {

    lateinit var name: String

    @JsonIgnore var role: Role = Role.IDLE
    val roleCode: Int
        get() {
            return role.number
        }

    @JsonIgnore var lobby: Lobby? = null
}