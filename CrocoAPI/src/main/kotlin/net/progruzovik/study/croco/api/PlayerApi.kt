package net.progruzovik.study.croco.api

import net.progruzovik.study.croco.model.Player
import net.progruzovik.study.croco.service.QueueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/api/player")
class PlayerApi {

    @Autowired lateinit var queueService: QueueService

    @GetMapping("/status")
    fun getStatus(@SessionAttribute player: Player): Any {
        return object {
            val status = player.status
        }
    }

    @GetMapping("/name")
    fun getName(@SessionAttribute player: Player): Any {
        return object {
            val name = player.name
        }
    }

    @PostMapping("/name")
    fun postName(name: String, session: HttpSession) {
        (session.getAttribute("player") as Player).name = name
    }

    @PostMapping("/queue")
    fun postQueue(@SessionAttribute player: Player) {
        queueService.add(player)
    }
}