package net.progruzovik.study.croco.api

import net.progruzovik.study.croco.model.Player
import net.progruzovik.study.croco.model.Quad
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/lobby")
@SessionAttributes("player")
class LobbyApi {

    @GetMapping("/roles")
    fun getRoles(@SessionAttribute player: Player): Any {
        return object {
            val painter = player
            val players = ArrayList<Player>()
        }
    }

    @PostMapping("/message")
    fun postMessage(value: String) {
        println(value)
    }

    @PostMapping("/quad")
    fun postQuad(value: Quad) {
        println(value.number)
        println(value.color)
    }
}