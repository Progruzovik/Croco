package net.progruzovik.study.croco.api

import net.progruzovik.study.croco.game.Player
import net.progruzovik.study.croco.game.Quad
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

    @GetMapping("/players") fun getPlayers(response: HttpServletResponse): Any {
        return hashMapOf("players".to(player.lobby.players))
    }

    @GetMapping("/game") fun getGame(response: HttpServletResponse): Any {
        return player.lobby
    }

    @GetMapping("/keyword") fun getKeyword(response: HttpServletResponse): Any? {
        val keyword: String? = player.keyword
        if (keyword == null) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return null
        }
        return hashMapOf("keyword".to(player.keyword))
    }

    @PostMapping("/message") fun postMessage(response: HttpServletResponse, text: String) {
        if (!player.say(text)) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @PostMapping("/quad") fun postQuad(response: HttpServletResponse, value: Quad) {
        if (!player.paint(value)) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }
}