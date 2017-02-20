package net.progruzovik.study.croco.api

import net.progruzovik.study.croco.enum.Role
import net.progruzovik.study.croco.game.Lobby
import net.progruzovik.study.croco.game.Player
import net.progruzovik.study.croco.game.Quad
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

    @GetMapping("/keyword")
    fun getKeyword(response: HttpServletResponse, @SessionAttribute player: Player): Any? {
        if (player.role == Role.PLAYER || player.lobby == null) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return null
        }
        return object {
            val keyword = player.lobby?.keyword
        }
    }

    @PostMapping("/message")
    fun postMessage(response: HttpServletResponse, @SessionAttribute player: Player, text: String) {
        if (player.role == Role.PLAYER) {
            player.lobby?.addMessage(player, text)
        } else {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @PostMapping("/quad")
    fun postQuad(response: HttpServletResponse, @SessionAttribute player: Player, value: Quad) {
        if (player.role == Role.PAINTER) {
            player.lobby?.addQuad(value)
        } else {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }
}