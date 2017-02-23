package net.progruzovik.study.croco.session

import net.progruzovik.study.croco.enum.Role
import net.progruzovik.study.croco.game.Player
import net.progruzovik.study.croco.game.QueueService
import org.springframework.stereotype.Component
import javax.servlet.http.HttpSession
import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

@Component
open class GameSessionListener(val queueService: QueueService) : HttpSessionListener {

    override fun sessionCreated(event: HttpSessionEvent) {
        val session: HttpSession = event.session
        session.setAttribute("player", Player(session.id))
    }

    override fun sessionDestroyed(event: HttpSessionEvent) {
        val player = event.session.getAttribute("player") as Player
        if (player.role == Role.QUEUED) {
            queueService.remove(player)
        }
    }
}