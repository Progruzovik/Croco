package net.progruzovik.study.croco.session

import net.progruzovik.study.croco.game.Player
import org.springframework.stereotype.Component
import javax.servlet.http.HttpSession
import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

@Component
open class GameSessionListener : HttpSessionListener {

    override fun sessionCreated(event: HttpSessionEvent) {
        val session: HttpSession = event.session
        session.setAttribute("player", Player(session.id))
    }

    override fun sessionDestroyed(event: HttpSessionEvent) {}
}