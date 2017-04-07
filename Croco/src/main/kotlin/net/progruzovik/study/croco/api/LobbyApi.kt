package net.progruzovik.study.croco.api

import net.progruzovik.study.croco.game.Lobby
import net.progruzovik.study.croco.game.Player
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/lobby")
class LobbyApi(
        private val player: Player) {

    @GetMapping("/players") fun getPlayers(response: HttpServletResponse): Any? {
        if (player.lobby == null) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
        return player.lobby
    }

    @GetMapping("/game") fun getGame(response: HttpServletResponse): Any? {
        val lobby: Lobby? = player.lobby
        if (lobby == null) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return null
        }
        val isQuadsRemoved = player.isQuadsRemoved
        player.isQuadsRemoved = false
        return hashMapOf(
                "messages".to(lobby.messages),
                "quads".to(lobby.quads),
                "quadsRemoved".to(isQuadsRemoved))
    }

    @GetMapping("/messages") fun getMessages(response: HttpServletResponse): Any? {
        val lobby: Lobby? = player.lobby
        if (lobby == null) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return null
        }
        return hashMapOf("messages".to(lobby.messages))
    }

    @PostMapping("/message") fun postMessage(@RequestParam("text") text: String, response: HttpServletResponse) {
        if (!player.addMessage(text)) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @PostMapping("/mark/{number}") fun postMark(@PathVariable number: Int,
                                                @RequestParam(value = "marked", required = false) isMarked: Boolean?,
                                                response: HttpServletResponse) {
        if (!player.markMessage(number, isMarked)) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @PostMapping("/quad/{number}") fun postQuad(@PathVariable number: Int,
                                                @RequestParam("color") color: Int,
                                                response: HttpServletResponse) {
        if (!player.addQuad(number, color)) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @DeleteMapping("/quad/{number}") fun deleteQuad(@PathVariable number: Int, response: HttpServletResponse) {
        if (!player.removeQuad(number)) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @DeleteMapping("/quads") fun deleteQuads(response: HttpServletResponse) {
        if (!player.removeQuads()) {
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