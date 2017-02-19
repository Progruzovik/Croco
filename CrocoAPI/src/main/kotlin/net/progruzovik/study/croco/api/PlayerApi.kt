package net.progruzovik.study.croco.api

import net.progruzovik.study.croco.enum.Status
import net.progruzovik.study.croco.model.Player
import net.progruzovik.study.croco.service.QueueService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/api/player")
class PlayerApi(val queueService: QueueService) {

    @GetMapping("/status")
    fun getStatus(@SessionAttribute player: Player): Any {
        return object {
            val statusCode = player.statusCode
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
    }

    @PostMapping("/queue")
    fun postQueue(response: HttpServletResponse, @SessionAttribute player: Player) {
        val isOk: Boolean = (player.status == Status.IDLE || player.status == Status.WIN) && queueService.add(player)
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