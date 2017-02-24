package net.progruzovik.study.croco.api

import net.progruzovik.study.croco.enum.Role
import net.progruzovik.study.croco.game.Player
import net.progruzovik.study.croco.game.QueueService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/api/player")
class PlayerApi(
        val queueService: QueueService) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(PlayerApi::class.java)
    }

    @GetMapping("/role")
    fun getRole(@SessionAttribute player: Player): Any {
        return object {
            val roleCode = player.roleCode
        }
    }

    @GetMapping("/name")
    fun getName(@SessionAttribute player: Player): Any {
        return object {
            val name = player.name
        }
    }

    @PostMapping("/name")
    fun postName(session: HttpSession, value: String) {
        (session.getAttribute("player") as Player).name = value
        logger.debug("POST player name = $value for session with id = ${session.id}")
    }

    @PostMapping("/queue")
    fun postQueue(response: HttpServletResponse, @SessionAttribute player: Player) {
        val isOk: Boolean = (player.role == Role.IDLER || player.role == Role.WINNER) && queueService.add(player)
        if (!isOk) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @DeleteMapping("/queue")
    fun deleteQueue(response: HttpServletResponse, @SessionAttribute player: Player) {
        if (!queueService.remove(player)) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }
}