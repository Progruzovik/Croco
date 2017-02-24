package net.progruzovik.study.croco.api

import net.progruzovik.study.croco.enum.Role
import net.progruzovik.study.croco.game.Lobby
import net.progruzovik.study.croco.game.Player
import net.progruzovik.study.croco.game.Quad
import net.progruzovik.study.croco.game.SessionPlayer
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/lobby")
class LobbyApi(
        val player: Player) {

    @GetMapping("/players")
    fun getPlayers(response: HttpServletResponse): Any? {
        return object {
            val players = player.lobby.players
        }
    }

    @GetMapping("/game")
    fun getGame(response: HttpServletResponse): Lobby? {
        return player.lobby
    }

    @GetMapping("/keyword")
    fun getKeyword(response: HttpServletResponse): Any? {
        if (player.role == Role.PLAYER) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return null
        }
        return object {
            val keyword = player.lobby.keyword
        }
    }

    @PostMapping("/message")
    fun postMessage(response: HttpServletResponse, text: String) {
        if (player.role == Role.PLAYER) {
            player.lobby.addMessage(player, text)
        } else {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @PostMapping("/quad")
    fun postQuad(response: HttpServletResponse, value: Quad) {
        if (player.role == Role.PAINTER) {
            player.lobby.addQuad(value)
        } else {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }
}