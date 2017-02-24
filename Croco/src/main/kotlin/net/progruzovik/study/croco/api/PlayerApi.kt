package net.progruzovik.study.croco.api

import net.progruzovik.study.croco.enum.Role
import net.progruzovik.study.croco.game.Player
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/player")
class PlayerApi(
        val player: Player) {

    @GetMapping("/role")
    fun getRole(): Any {
        return hashMapOf("roleCode".to(player.roleCode))
    }

    @GetMapping("/name")
    fun getName(): Any {
        return hashMapOf("name".to(player.name))
    }

    @PostMapping("/name")
    fun postName(value: String) {
        player.name = value
    }

    @PostMapping("/queue")
    fun postQueue(response: HttpServletResponse) {
        val isOk: Boolean = (player.role == Role.IDLER || player.role == Role.WINNER) && player.toQueue()
        if (!isOk) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }

    @DeleteMapping("/queue")
    fun deleteQueue(response: HttpServletResponse) {
        if (!player.fromQueue()) {
            response.status = HttpStatus.BAD_REQUEST.value()
        }
    }
}