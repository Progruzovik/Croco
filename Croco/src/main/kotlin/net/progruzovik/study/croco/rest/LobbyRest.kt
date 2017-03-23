package net.progruzovik.study.croco.rest

import net.progruzovik.study.croco.game.Player
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/lobby")
class LobbyRest(
        private val player: Player) {

    @GetMapping("/players") fun getPlayers(response: HttpServletResponse): Any? {
        if (player.lobby == null) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return null
        }
        return player.lobby
    }

    @GetMapping("/game") fun getGame(response: HttpServletResponse): Any? {
        if (player.lobby == null) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return null
        }
        val isQuadsRedrawn = player.isQuadsRedrawn
        player.isQuadsRedrawn = false
        return hashMapOf(
                "messages".to(player.lobby?.messages),
                "quads".to(player.lobby?.quads),
                "quadsRedrawn".to(isQuadsRedrawn))
    }

    @PostMapping("/message") fun postMessage(@RequestParam("text") text: String, response: HttpServletResponse) {
        if (!player.say(text)) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @PostMapping("/mark") fun postMark(@RequestParam("number") number: Int,
                                       @RequestParam(value = "marked", required = false) isMarked: Boolean?,
                                       response: HttpServletResponse) {
        if (!player.markMessage(number, isMarked)) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @PostMapping("/quad") fun postQuad(@RequestParam("number") number: Int,
                                       @RequestParam("color") color: Int,
                                       response: HttpServletResponse) {
        if (!player.paint(number, color)) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @DeleteMapping("/quads") fun deleteQuads(response: HttpServletResponse) {
        if (!player.clearCanvas()) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @GetMapping("/keyword") fun getKeyword(response: HttpServletResponse): Any? {
        val keyword: String? = player.requestKeyword()
        if (keyword == null) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return null
        }
        return hashMapOf("keyword".to(keyword))
    }
}