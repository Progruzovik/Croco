package net.progruzovik.study.croco.rest

import net.progruzovik.study.croco.enum.GameStatus
import net.progruzovik.study.croco.enum.Role
import net.progruzovik.study.croco.game.Player
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/lobby")
class LobbyRest(
        private val player: Player) {

    @GetMapping("/players") fun getPlayers(response: HttpServletResponse): Any? {
        try {
            return hashMapOf(
                    "painter".to(player.lobby.players.first { it.role == Role.PAINTER }),
                    "guessers".to(player.lobby.players.filter { it.role == Role.GUESSER }))
        } catch (e: UninitializedPropertyAccessException) {
            response.status = HttpStatus.BAD_REQUEST.value()
            return null
        }
    }

    @GetMapping("/game") fun getGame(response: HttpServletResponse): Any? {
        if (player.gameStatus == GameStatus.ACTUAL) {
            response.status = HttpStatus.NOT_MODIFIED.value()
            return null
        }
        val redrawnCode = if (player.gameStatus == GameStatus.REDRAWN) 1 else 0
        player.gameStatus = GameStatus.ACTUAL
        return hashMapOf(
                "messages".to(player.lobby.messages),
                "quads".to(player.lobby.quads),
                "redrawnCode".to(redrawnCode))
    }

    @PostMapping("/message") fun postMessage(@RequestParam("text") text: String, response: HttpServletResponse) {
        if (!player.say(text)) {
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