package net.progruzovik.study.croco.api

import net.progruzovik.study.croco.enum.Status
import net.progruzovik.study.croco.model.Lobby
import net.progruzovik.study.croco.model.Message
import net.progruzovik.study.croco.model.Player
import net.progruzovik.study.croco.model.Quad
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/lobby")
class LobbyApi {

    @GetMapping("/players")
    fun getPlayers(response: HttpServletResponse, @SessionAttribute player: Player): Any? {
        if (player.lobby == null) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return null
        }
        return object {
            val players = player.lobby?.players
        }
    }

    @GetMapping("/game")
    fun getGame(response: HttpServletResponse, @SessionAttribute player: Player): Lobby? {
        if (player.lobby == null) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return null
        }
        return player.lobby
    }

    @PostMapping("/message")
    fun postMessage(response: HttpServletResponse, @SessionAttribute player: Player, value: String) {
        if (player.status == Status.PLAY) {
            player.lobby!!.messages.add(Message(player.name, value))
        } else {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @PostMapping("/quad")
    fun postQuad(response: HttpServletResponse, @SessionAttribute player: Player, value: Quad) {
        if (player.status == Status.DRAW) {
            player.lobby!!.quads.add(value)
        } else {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }
}