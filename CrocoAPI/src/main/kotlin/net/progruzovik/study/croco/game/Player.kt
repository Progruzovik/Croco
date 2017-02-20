package net.progruzovik.study.croco.game

import com.fasterxml.jackson.annotation.JsonIgnore
import net.progruzovik.study.croco.enum.Role

data class Player(
        val id: String) {

    lateinit var name: String

    @JsonIgnore var role: Role = Role.IDLER
    val roleCode: Int
        get() {
            return role.ordinal
        }

    @JsonIgnore var lobby: Lobby? = null
}