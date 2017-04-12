package net.progruzovik.study.croco.api

import net.progruzovik.study.croco.game.Player
import net.progruzovik.study.croco.getLogger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/player")
class PlayerApi(
        private val player: Player) {

    companion object {
        private val logger = getLogger<PlayerApi>()
    }

    @GetMapping("/id") fun getId(): Any {
        return hashMapOf("id".to(player.id))
    }

    @GetMapping("/name") fun getName(): Any {
        return hashMapOf("name".to(player.name))
    }

    @PostMapping("/name") fun postName(@RequestParam("value") value: String) {
        player.name = value
    }

    @GetMapping("/role") fun getRole(): Any {
        return hashMapOf("roleCode".to(player.role.ordinal))
    }

    @PostMapping("/queue") fun postQueue(response: HttpServletResponse) {
        if (!player.addToQueue()) {
            response.status = HttpStatus.BAD_REQUEST.value()
            logger.debug("Bad Request on POST /player/queue")
        }
    }

    @DeleteMapping("/queue") fun deleteQueue(response: HttpServletResponse) {
        if (!player.removeFromQueue()) {
            response.status = HttpStatus.BAD_REQUEST.value()
            logger.debug("Bad Request on DELETE /player/queue")
        }
    }
}