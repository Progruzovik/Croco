package net.progruzovik.croco.rest

import net.progruzovik.croco.game.Lobby
import net.progruzovik.croco.game.Player
import net.progruzovik.croco.getLogger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/lobby")
class LobbyRest(private val player: Player) {

    companion object {
        private val log = getLogger<LobbyRest>()
    }

    @GetMapping("/players")
    fun getPlayers(): ResponseEntity<Lobby?> {
        if (player.lobby == null) {
            log.debug("Bad Request on GET /lobby/players")
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok(player.lobby)
    }

    @GetMapping("/game")
    fun getGame(): ResponseEntity<Any?> {
        val lobby: Lobby? = player.lobby
        if (lobby == null) {
            log.debug("Bad Request on GET /lobby/game")
            return ResponseEntity.badRequest().build()
        }
        val isQuadsRemoved = player.isQuadsRemoved
        player.isQuadsRemoved = false
        val game = hashMapOf(
            "quadsRemoved" to isQuadsRemoved,
            "quads" to lobby.quads,
            "messages" to lobby.messages
        )
        return ResponseEntity.ok(game)
    }

    @PostMapping("/quad/{number}")
    fun postQuad(@PathVariable number: Int, @RequestParam("color") color: Int): ResponseEntity<Unit?> {
        if (!player.addQuad(number, color)) {
            log.debug("Bad Request on POST /lobby/quad")
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/quad/{number}")
    fun deleteQuad(@PathVariable number: Int): ResponseEntity<Unit?> {
        if (!player.removeQuad(number)) {
            log.debug("Bad Request on DELETE /lobby/quad")
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/quads")
    fun deleteQuads(): ResponseEntity<Unit?> {
        if (!player.removeQuads()) {
            log.debug("Bad Request on DELETE /lobby/quads")
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok().build()
    }

    @GetMapping("/messages")
    fun getMessages(): ResponseEntity<Any?> {
        val lobby: Lobby? = player.lobby
        if (lobby == null) {
            log.debug("Bad Request on GET /lobby/messages")
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok(hashMapOf("messages" to lobby.messages))
    }

    @PostMapping("/message")
    fun postMessage(@RequestParam("text") text: String): ResponseEntity<Unit?> {
        if (!player.addMessage(text)) {
            log.debug("Bad Request on POST /lobby/message")
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok().build()
    }

    @PostMapping("/mark/{number}")
    fun postMark(@PathVariable number: Int,
                 @RequestParam(value = "marked", required = false) isMarked: Boolean?
    ): ResponseEntity<Unit?> {
        if (!player.markMessage(number, isMarked)) {
            log.debug("Bad Request on POST /lobby/mark")
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok().build()
    }

    @GetMapping("/keyword")
    fun getKeyword(): ResponseEntity<Any?> {
        val keyword: String? = player.requestKeyword()
        if (keyword == null) {
            log.debug("Bad Request on GET /lobby/keyword")
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok(hashMapOf("keyword" to keyword))
    }
}
