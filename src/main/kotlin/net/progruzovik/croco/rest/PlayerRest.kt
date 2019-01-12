package net.progruzovik.croco.rest

import net.progruzovik.croco.game.Player
import net.progruzovik.croco.getLogger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/player")
class PlayerRest(private val player: Player) {

    companion object {
        private val logger = getLogger<PlayerRest>()
    }

    @GetMapping("/id")
    fun getId(): Any {
        return hashMapOf("id" to player.id)
    }

    @GetMapping("/name")
    fun getName(): Any {
        return hashMapOf("name" to player.name)
    }

    @PostMapping("/name")
    fun postName(@RequestParam("value") value: String) {
        player.name = value
    }

    @GetMapping("/role")
    fun getRole(): Any {
        return hashMapOf("roleCode" to player.role.ordinal)
    }

    @PostMapping("/queue")
    fun postQueue(): ResponseEntity<Unit?> {
        if (!player.addToQueue()) {
            logger.debug("Bad Request on POST /player/queue")
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/queue")
    fun deleteQueue(): ResponseEntity<Unit?> {
        if (!player.removeFromQueue()) {
            logger.debug("Bad Request on DELETE /player/queue")
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok().build()
    }
}
